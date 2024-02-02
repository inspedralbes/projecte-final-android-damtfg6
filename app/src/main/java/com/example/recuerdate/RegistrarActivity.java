package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {

    Spinner spinner1;

    private apiService apiService;
    private static final String URL = "http://192.168.205.57:3672/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        spinner1 = findViewById(R.id.spinner);

        final View usuariLayout = LayoutInflater.from(this).inflate(R.layout.usuari_layout, null);
        usuariLayout.setVisibility(View.GONE);
        ((ViewGroup) findViewById(android.R.id.content)).addView(usuariLayout);

        final View tutorLayout = LayoutInflater.from(this).inflate(R.layout.tutor_layout, null);
        tutorLayout.setVisibility(View.GONE);
        ((ViewGroup) findViewById(android.R.id.content)).addView(tutorLayout);


        ArrayList<Situacio> situacions = new ArrayList<>();
        situacions.add(new Situacio("Usuari"));
        situacions.add(new Situacio("Tutor"));

        ArrayAdapter<Situacio> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, situacions);

        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                   Situacio selectedSituacio = (Situacio) parentView.getItemAtPosition(position);
                                                   if (selectedSituacio.getSituacio().equals("Usuari")) {
                                                       usuariLayout.setVisibility(View.VISIBLE);
                                                       tutorLayout.setVisibility(View.GONE);
                                                   } else if (selectedSituacio.getSituacio().equals("Tutor")) {
                                                       tutorLayout.setVisibility(View.VISIBLE);
                                                       usuariLayout.setVisibility(View.GONE);
                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> adapterView) {
                                                   Toast.makeText(RegistrarActivity.this, "Fes una Seleccio", Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );
    }

    public void btnRegistrar(View view) {
        Log.d("Button Click", "btnRegistrar method called");

        //Usuari
        EditText nomCognoms1 = findViewById(R.id.nom);
        EditText dni1 = findViewById(R.id.dni);
        EditText telefon1 = findViewById(R.id.telefon);
        EditText correu1 = findViewById(R.id.correu);
        EditText contrasenyes = findViewById(R.id.contrasenya);

        String nomCognoms = nomCognoms1.getText().toString().trim();
        String dni = dni1.getText().toString().trim();
        String telefonInput = telefon1.getText().toString().trim();
        String correu = correu1.getText().toString().trim();
        String contrasenya = contrasenyes.getText().toString().trim();
        int telefon;

        //Tutor
        EditText nomCognomsTutor1 = findViewById(R.id.nomTutor);
        EditText dniTutor1 = findViewById(R.id.dniTutor);
        EditText telefonTutor1 = findViewById(R.id.telefonTutor);
        EditText correuTutor1 = findViewById(R.id.correuTutor);
        EditText contrasenyesTutor = findViewById(R.id.contrasenyaTutor);
        EditText id_usuari = findViewById(R.id.identificador);


        String nomCognomsTutor = nomCognomsTutor1.getText().toString().trim();
        String dniTutor = dniTutor1.getText().toString().trim();
        String telefonInputTutor = telefonTutor1.getText().toString().trim();
        String correuTutor = correuTutor1.getText().toString().trim();
        String contrasenyaTutor = contrasenyesTutor.getText().toString().trim();
        String identificadorUsuari1 = id_usuari.getText().toString().trim();
        int telefonTutor;

        // Check the selected item in the Spinner
        Situacio selectedSituacio = (Situacio) spinner1.getSelectedItem();

        if (selectedSituacio.getSituacio().equals("Usuari")) {
            if (nomCognoms.isEmpty() || dni.isEmpty() || telefonInput.isEmpty() || correu.isEmpty() || contrasenya.isEmpty()) {
                if (nomCognoms.isEmpty()) {
                    nomCognoms1.setError(getString(R.string.value_required));
                }
                if (dni.isEmpty()) {
                    dni1.setError(getString(R.string.value_required));
                }
                if (correu.isEmpty()) {
                    correu1.setError(getString(R.string.value_required));
                }
                if (contrasenya.isEmpty()) {
                    contrasenyes.setError(getString(R.string.value_required));
                }
                if (telefonInput.isEmpty()) {
                    telefon1.setError(getString(R.string.value_required));
                }
            } else {
                telefon = Integer.parseInt(telefonInput);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                apiService = retrofit.create(apiService.class);

                // Usuari registration API call
                Log.d("Button Click", "NomCognoms: " + nomCognoms +
                        ", DNI: " + dni +
                        ", Telefon: " + telefon +
                        ", Correu: " + correu +
                        ", Contrasenya: " + contrasenya);
                UsuariLocalitzat usuariTrobat = new UsuariLocalitzat(nomCognoms, dni, telefon, correu, contrasenya);

                Call<Resposta> call = apiService.EnviarUsuario(usuariTrobat);

                call.enqueue(new Callback<Resposta>() {
                    @Override
                    public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                        if (response.isSuccessful()) {
                            Log.d("CONEXION", "CONEXION SERVIDOR CONECTADO");
                            Resposta r = response.body();
                            Log.d("error", "onFailure: "+ response.body());
                            System.out.println(r.isAutoritzacio());
                            if (r.isAutoritzacio()) {
                                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                                intent.putExtra("Usuari", nomCognoms);
                                startActivity(intent);
                            }
                        } else {
                            Log.e("ERROR", "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Resposta> call, Throwable t) {
                        Log.d("error", "onFailure: " + t.getMessage());
                    }
                });
            }
        } else if (selectedSituacio.getSituacio().equals("Tutor")) {
            if (nomCognomsTutor.isEmpty() || dniTutor.isEmpty() || telefonInputTutor.isEmpty() || correuTutor.isEmpty() || contrasenyaTutor.isEmpty()) {
                if (nomCognomsTutor.isEmpty()) {
                    nomCognomsTutor1.setError(getString(R.string.value_required));
                }
                if (dniTutor.isEmpty()) {
                    dniTutor1.setError(getString(R.string.value_required));
                }
                if (correuTutor.isEmpty()) {
                    correuTutor1.setError(getString(R.string.value_required));
                }
                if (contrasenyaTutor.isEmpty()) {
                    contrasenyesTutor.setError(getString(R.string.value_required));
                }
                if (telefonInputTutor.isEmpty()) {
                    telefonTutor1.setError(getString(R.string.value_required));
                }
                if (identificadorUsuari1.isEmpty()) {
                    id_usuari.setError(getString(R.string.value_required));
                }
            } else {
                telefonTutor = Integer.parseInt(telefonInputTutor);
                int usuari_identificador = Integer.parseInt(identificadorUsuari1);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                apiService = retrofit.create(apiService.class);

                // Tutor registration API call
                TutorTrobat tutorTrobat = new TutorTrobat(nomCognomsTutor, dniTutor, telefonTutor, correuTutor, contrasenyaTutor, usuari_identificador);
                Call<Resposta> call = apiService.EnviarTutor(tutorTrobat);

                call.enqueue(new Callback<Resposta>() {
                    @Override
                    public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                        if (response.isSuccessful()) {
                            Log.d("CONEXION", "CONEXION SERVIDOR CONECTADO");
                            Resposta r = response.body();
                            System.out.println(r.isAutoritzacio());
                            if (r.isAutoritzacio()) {
                                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                                intent.putExtra("user", nomCognoms);
                                startActivity(intent);
                            }
                        } else {
                            Log.e("ERROR", "Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Resposta> call, Throwable t) {
                        Log.d("error", "onFailure: " + t.getMessage());
                    }
                });
            }
        }
    }


    public void botto(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

