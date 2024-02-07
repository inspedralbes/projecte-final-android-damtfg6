package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Obtener referencia al TextView
        TextView textViewNomPerfil = findViewById(R.id.textViewNomPerfil);
        TextView textViewCorreuPerfil = findViewById(R.id.textViewCorreuPerfil);
        TextView textViewTelefonPerfil = findViewById(R.id.textViewTelefonPerfil2);
        TextView textViewDniPerfil = findViewById(R.id.textViewDniPerfil2);
        TextView textViewIdentificadorPerfil = findViewById(R.id.textViewIdentificadorPerfil2);

        SessionManagment sessionManagment = new SessionManagment(this);

        String nombreUsuario = sessionManagment.getUserData().getNomCognoms();//nom Cognoms
        String CorreuUsuario = sessionManagment.getUserData().getCorreu();//correu
        //Contrasenya
        String ContrasenyaUsuario = sessionManagment.getUserData().getContrasenya();
        String dniUsuario = sessionManagment.getUserData().getDni();//Dni
        String telefonUsuarioString = String.valueOf(sessionManagment.getUserData().getTelefon());//Telefon
        String IdentificadorUsuarioString = String.valueOf(sessionManagment.getUserData().getUsuariIdentificador());//Identificador

        // Asignar el nombre del usuario al TextView
        textViewNomPerfil.setText(nombreUsuario);
        textViewCorreuPerfil.setText(CorreuUsuario);
        textViewDniPerfil.setText(dniUsuario);
        textViewTelefonPerfil.setText(telefonUsuarioString);
        textViewIdentificadorPerfil.setText(IdentificadorUsuarioString);
    }

    public void TornaBoto(View view){
        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(intent);
    }
}