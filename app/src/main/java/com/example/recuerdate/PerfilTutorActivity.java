package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PerfilTutorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_tutor);

        TextView textViewNomPerfil = findViewById(R.id.textViewNomPerfilTutor);
        TextView textViewCorreuPerfil = findViewById(R.id.textViewCorreuPerfilTutor);
        TextView textViewTelefonPerfil = findViewById(R.id.textViewTelefonPerfilTutor2);
        TextView textViewDniPerfil = findViewById(R.id.textViewDniPerfilTutor2);
        TextView textViewNomTutoritzatPerfil = findViewById(R.id.textViewNomCognomsPerfilTutor2);
        TextView textViewIdentificadorPerfil = findViewById(R.id.textViewIdentificadorPerfilTutor2);


        SessionManagment sessionManagment = new SessionManagment(this);

        String nombreTutor = sessionManagment.getUserData().getNomCognoms();//nom Cognoms
        String CorreuTutor = sessionManagment.getUserData().getCorreu();//correu
        //Contrasenya
        String ContrasenyaTutor = sessionManagment.getUserData().getContrasenya();
        String dniTutor = sessionManagment.getUserData().getDni();//Dni
        String telefonTutorString = String.valueOf(sessionManagment.getUserData().getTelefon());//Telefon
        String IdentificadorTutorString;//Identificador
        String nomUsuariTutoritzat;



        if (sessionManagment.getUsuariTutoritzatData() != null) {
            Log.d("userData", String.valueOf(sessionManagment.getUsuariTutoritzatData()));
            nomUsuariTutoritzat = sessionManagment.getUsuariTutoritzatData().getNomCognoms();
        } else {
            nomUsuariTutoritzat = "Null";
        }

        if (sessionManagment.getUsuariTutoritzatData() != null) {
            Log.d("userData", String.valueOf(sessionManagment.getUsuariTutoritzatData()));
            IdentificadorTutorString = String.valueOf(sessionManagment.getUsuariTutoritzatData().getUsuariIdentificador());
        } else {
            IdentificadorTutorString = "Null";
        }


        // Asignar el nombre del usuario al TextView
        textViewNomPerfil.setText(nombreTutor);
        textViewCorreuPerfil.setText(CorreuTutor);
        textViewDniPerfil.setText(dniTutor);
        textViewTelefonPerfil.setText(telefonTutorString);
        textViewIdentificadorPerfil.setText(IdentificadorTutorString);
        textViewNomTutoritzatPerfil.setText(nomUsuariTutoritzat);
    }



    public void TornaBotoTutor(View view){
        Intent intent = new Intent(PerfilTutorActivity.this, MainActivityTutor.class);
        startActivity(intent);
    }
}