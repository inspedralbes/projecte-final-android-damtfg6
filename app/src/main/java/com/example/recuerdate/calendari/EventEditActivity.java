package com.example.recuerdate.calendari;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.recuerdate.R;
import com.example.recuerdate.SessionManagment;
import com.example.recuerdate.Settings;
import com.google.gson.Gson;



public class EventEditActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private LocalTime time;
    private OkHttpClient client;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        client = new OkHttpClient();
        gson = new Gson();
        time = LocalTime.now();
        eventDateTV.setText("Data seleccionada: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Selecciona l'hora: " + CalendarUtils.formattedTime(time));

        // Agrega un OnClickListener al TextView de la hora
        eventTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un nuevo TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Actualiza la hora seleccionada
                                time = LocalTime.of(hourOfDay, minute);
                                eventTimeTV.setText("Hora: " + CalendarUtils.formattedTime(time));
                            }
                        }, time.getHour(), time.getMinute(), true);
                timePickerDialog.show();
            }
        });
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);
        postEventToServer(newEvent);
        finish();
    }



    public void postEventToServer(Event event) {
        // Obtener el DNI del usuario
        SessionManagment sessionManagment = new SessionManagment(this);
        String dniUsuario;
        if (sessionManagment.getRol().equals("tutor")) {
            dniUsuario = sessionManagment.getUsuariTutoritzatData().getDni();
        } else {
            dniUsuario = sessionManagment.getUserData().getDni();
        }

        // Crear un mapa para almacenar los datos del evento
        Map<String, String> eventMap = new LinkedHashMap<>();
        eventMap.put("dni", dniUsuario);
        eventMap.put("name", event.getName());
        eventMap.put("date", event.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        eventMap.put("time", event.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        String json = gson.toJson(eventMap);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Settings.SERVER+ ":" + Settings.PORT)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    // Haz algo con la respuesta
                }
            }
        });
    }
}
