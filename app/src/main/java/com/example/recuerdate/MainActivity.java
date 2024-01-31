package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner spinner1;
    Button boto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicialitzar components
        spinner1 = findViewById(R.id.spinner);
        boto1 = findViewById(R.id.button2);
        final RelativeLayout layoutUsuario = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_usuari, null);
        final LinearLayout layoutTutor = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_tutor, null);


        boto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        ArrayList<Situacio> situacions = new ArrayList<>();
        situacions.add(new Situacio("Usuari"));
        situacions.add(new Situacio("Tutor"));

        ArrayAdapter<Situacio> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,situacions);

        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener la situación seleccionada
                Situacio selectedSituacio = (Situacio) parentView.getItemAtPosition(position);

                // Mostrar u ocultar layouts según la situación seleccionada
                if (selectedSituacio.getSituacio().equals("Usuari")) {
                    layoutUsuario.setVisibility(View.VISIBLE);
                    layoutTutor.setVisibility(View.GONE);
                } else if (selectedSituacio.getSituacio().equals("Tutor")) {
                    layoutUsuario.setVisibility(View.GONE);
                    layoutTutor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "Fes una Seleccio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}