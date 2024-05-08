package com.example.recuerdate.calendari;

import static com.example.recuerdate.calendari.CalendarUtils.daysInWeekArray;
import static com.example.recuerdate.calendari.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.recuerdate.R;
import com.example.recuerdate.SessionManagment;
import com.example.recuerdate.Settings;
import com.example.recuerdate.calendari.CalendarAdapter;
import com.example.recuerdate.calendari.CalendarUtils;
import com.example.recuerdate.calendari.DailyCalendarActivity;
import com.example.recuerdate.calendari.Event;
import com.example.recuerdate.calendari.EventAdapter;
import com.example.recuerdate.calendari.EventEditActivity;
import com.example.recuerdate.calendari.EventFromServer;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();

        PreferenceManager preferenceManager = new PreferenceManager(this);
        String dniUsuario = preferenceManager.getString(Constants.KEY_EMAIL);

        // Obtener los eventos del servidor
        getEventsFromServer(dniUsuario);
    }

    public void getEventsFromServer(String dniUsuario) {
        Request request = new Request.Builder()
                .url(Settings.SERVER+ ":" + Settings.PORT + "/events?dni=" + dniUsuario)
                .get()
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
                    // Parsear la respuesta JSON
                    String jsonData = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<EventFromServer>>(){}.getType();
                    List<EventFromServer> eventsFromServer = gson.fromJson(jsonData, listType);
                    List<Event> events = new ArrayList<>();
                    for (EventFromServer eventFromServer : eventsFromServer) {
                        // Convertir la fecha y la hora de cadena de texto a LocalDate y LocalTime
                        LocalDate eventDate = LocalDate.parse(eventFromServer.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        LocalTime eventTime = LocalTime.parse(eventFromServer.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));

                        // Crear un nuevo objeto Event y copiar los datos de eventFromServer
                        Event event = new Event(eventFromServer.getName(), eventDate, eventTime);
                        events.add(event);
                    }


                    // Ahora puedes usar la lista de eventos para mostrarlos en tu aplicación
                    // Nota: Esto debe hacerse en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Event.eventsList.clear();
                            Event.eventsList.addAll(events);
                            setEventAdpater();
                        }
                    });
                }
            }
        });
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void dailyAction(View view)
    {
        startActivity(new Intent(this, DailyCalendarActivity.class));
    }
}