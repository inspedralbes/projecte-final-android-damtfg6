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
    Button boto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        spinner1 = findViewById(R.id.spinner);
        boto1 = findViewById(R.id.button2);

        final View usuariLayout = LayoutInflater.from(this).inflate(R.layout.usuari_layout, null);
        usuariLayout.setVisibility(View.GONE);
        ((ViewGroup) findViewById(android.R.id.content)).addView(usuariLayout);

        final View tutorLayout = LayoutInflater.from(this).inflate(R.layout.tutor_layout, null);
        tutorLayout.setVisibility(View.GONE);
        ((ViewGroup) findViewById(android.R.id.content)).addView(tutorLayout);

        boto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

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
        EditText nomCognoms1 = findViewById(R.id.nom);
        EditText dni1 = findViewById(R.id.dni);
        EditText telefon1 = findViewById(R.id.telefon);
        EditText correu1 = findViewById(R.id.correu);
        EditText contrasenyes = findViewById(R.id.contrasenya);
        EditText IdentificadorUsuari = findViewById(R.id.identificador);


        String nomCognoms = nomCognoms1.getText().toString().trim();
        String dni = dni1.getText().toString().trim();
        String telefon = telefon1.getText().toString().trim();
        String correu = correu1.getText().toString().trim();
        String contrasenya = contrasenyes.getText().toString().trim();
        String id_usuari = IdentificadorUsuari.getText().toString().trim();



        if (nomCognoms.isEmpty() || dni.isEmpty() || telefon.isEmpty()) {
            if (nomCognoms.isEmpty()) {
                nomCognoms1.setError(getString(R.string.value_required));
            }
            if (dni.isEmpty()) {
                dni1.setError(getString(R.string.value_required));
            }
            if (telefon.isEmpty()) {
                contrasenyes.setError(getString(R.string.value_required));
            }
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(apiService.class);

            UsuariLocalitzat usuariTrobat = new UsuariLocalitzat(nomUsuari, nomCogonoms, correu, passwd, rol);

            Call<Resposta> call = apiService.EnviarUsuario(usuariTrobat);

            call.enqueue(new Callback<Resposta>() {
                @Override
                public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                    if (response.isSuccessful()) {
                        Log.d("CONEXION", "CONEXION SERVIDOR CONECTADO");
                        Resposta r = response.body();
                        System.out.println(r.isAutoritzacio());
                        if (r.isAutoritzacio()) {
                            Intent intent = new Intent(Register.this, Login.class);
                            intent.putExtra("user", nomUsuari);
                            startActivity(intent);
                        }
                    } else {
                        Log.e("ERROR", "Gorda");
                    }
                }

                @Override
                public void onFailure(Call<Resposta> call, Throwable t) {
                    Log.d("error", "onFailure: "+t.getMessage());
                }
            });
        }
    }
}