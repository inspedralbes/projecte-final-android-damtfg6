package com.example.recuerdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutoritzarNouUsuari extends AppCompatActivity {

    private apiService apiService;
    private static final String URL = "http://192.168.205.57:3672/";

    TextView textViewOK;
    TextView textViewnNoOK;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoritzar_nou_usuari);

        EditText editText = findViewById(R.id.editTextTextNovaTutoritzacio);

        textViewOK = findViewById(R.id.textViewTutoritzacioRegistrada);
        textViewnNoOK = findViewById(R.id.textViewTutoritzacioError);
        textViewOK.setVisibility(View.GONE);
        textViewnNoOK.setVisibility(View.GONE);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Abre el teclado solo cuando se toca el área del EditText, pero no el icono de ayuda
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showImageHelpDialog();
                        return true;
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void showImageHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_help, null);

        // Configura la imagen en el diálogo (asumiendo que tienes una imagen llamada "telefono" en tu carpeta "drawable")
        ImageView imageView = dialogView.findViewById(R.id.imageViewHelp);
        imageView.setImageResource(R.drawable.telefono);

        // Configura el botón "Tancar"
        Button closeButton = dialogView.findViewById(R.id.Tancar);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el diálogo cuando se hace clic en el botón "Tancar"
                AlertDialog dialog = (AlertDialog) v.getTag();
                dialog.dismiss();
            }
        });

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        closeButton.setTag(alertDialog);

        alertDialog.show();
    }

    public void registraNovaTutoritzacio(View view) {

        Log.d("click", "hola");
        SessionManagment sessionManagment = new SessionManagment(this);

        int idFamiliar = sessionManagment.getUserData().getUserId();

        EditText identificadorEditText = findViewById(R.id.editTextTextNovaTutoritzacio);
        String identificadorInput = identificadorEditText.getText().toString().trim();
        int Identificador;

        if (identificadorInput.isEmpty()) {
                identificadorEditText.setError(getString(R.string.value_required));
            } else {

                Identificador = Integer.parseInt(identificadorInput);


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                apiService = retrofit.create(apiService.class);

                Log.d("Button Click", "idFamiliar: " + idFamiliar +
                    ", Identificador: " + Identificador);

                Call<RespostaTutoritzacio> call = apiService.EnviarTutoritzacio(idFamiliar, Identificador);



                call.enqueue(new Callback<RespostaTutoritzacio>() {
                    @Override
                    public void onResponse(Call<RespostaTutoritzacio> call, Response<RespostaTutoritzacio> response) {
                        if (response.isSuccessful()) {
                            Log.d("CONEXION", "CONEXION SERVIDOR CONECTADO");
                            RespostaTutoritzacio r = response.body();
                            Log.d("error", "onFailure: "+ response.body());
                            System.out.println(r.isAutoritzacio());
                            if (r.isAutoritzacio()) {
                                textViewOK = findViewById(R.id.textViewTutoritzacioRegistrada);
                                textViewnNoOK.setVisibility(View.GONE);
                                textViewOK.setVisibility(View.VISIBLE);

                                Usuari usuariTutoritzatData = r.getUsuariTutoritzatData();
                                //Log.d("dades", String.valueOf(usuariTutoritzatData));


                                sessionManagment.saveSession(sessionManagment.getDni(), sessionManagment.getRol(), sessionManagment.getUserData(), usuariTutoritzatData);
                                Log.d("session", String.valueOf(sessionManagment));

                                int assignarIdentificador = sessionManagment.getUsuariTutoritzatData().getUsuariIdentificador();
                                sessionManagment.getUserData().setUsuariIdentificador(assignarIdentificador);
                            }
                            else{
                                textViewOK.setVisibility(View.GONE);
                                textViewnNoOK.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("ERROR", "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<RespostaTutoritzacio> call, Throwable t) {
                        Log.d("error", "onFailure: " + t.getMessage());
                    }
                });

            }
        }

    public void TornaBotoTutor(View view) {
        Intent intent = new Intent(TutoritzarNouUsuari.this, MainActivityTutor.class);
        startActivity(intent);
    }

}