package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

        final View  tutorLayout =  LayoutInflater.from(this).inflate(R.layout.tutor_layout, null);
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
        boto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí es donde debes guardar el valor 'registered' en las preferencias compartidas
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("registered", true);
                editor.apply();

                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}