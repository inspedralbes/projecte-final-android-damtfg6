package com.example.recuerdate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recuerdate.databinding.ActivityPerfilTutorBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;

public class PerfilTutorActivity extends AppCompatActivity {

    private ActivityPerfilTutorBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilTutorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        String nombreTutor = preferenceManager.getString(Constants.KEY_NAME);
        String CorreuTutor = preferenceManager.getString(Constants.KEY_SUPERVISED_USER_DNI);
        String ContrasenyaTutor = preferenceManager.getString(Constants.KEY_PASSWORD);
        String dniTutor = preferenceManager.getString(Constants.KEY_EMAIL);
        String telefonTutorString = preferenceManager.getString(Constants.KEY_PHONE);
        String IdentificadorTutorString = preferenceManager.getString(Constants.KEY_USER_IDENTIFIER);
        String nomUsuariTutoritzat = preferenceManager.getString(Constants.KEY_SUPERVISED_USER_NAME);
        String rolUsuario = preferenceManager.getString(Constants.KEY_ROLE);

        binding.textViewNomPerfilTutor.setText(nombreTutor);
        binding.textViewCorreuPerfilTutor.setText(CorreuTutor);
        binding.textViewDniPerfilTutor2.setText(dniTutor);
        binding.textViewTelefonPerfilTutor2.setText(telefonTutorString);
        binding.textViewIdentificadorPerfilTutor2.setText(IdentificadorTutorString);
        binding.textViewNomCognomsPerfilTutor2.setText(nomUsuariTutoritzat);
        binding.textViewModificarPerfilTutor.setText(ContrasenyaTutor);
        binding.textViewAdreAPerfil2Tutor.setText(rolUsuario);
        loadUserDetails();

    }
    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageViewPerfilTutor.setImageBitmap(bitmap);
    }

    public void TornaBotoTutor(View view){
        Intent intent = new Intent(PerfilTutorActivity.this, MainActivityTutor.class);
        startActivity(intent);
    }
}
