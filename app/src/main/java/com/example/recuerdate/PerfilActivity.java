package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.example.recuerdate.databinding.ActivityPerfilBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        String nombreUsuario = preferenceManager.getString(Constants.KEY_NAME);
        String dniUsuario = preferenceManager.getString(Constants.KEY_EMAIL);
        String telefonoUsuario = preferenceManager.getString(Constants.KEY_PHONE);
        String rolUsuario = preferenceManager.getString(Constants.KEY_ROLE);
        String usuari_identificador = preferenceManager.getString(Constants.KEY_USER_IDENTIFIER);
        String passwordUsuario = preferenceManager.getString(Constants.KEY_PASSWORD);
        String dniTutoritzat = preferenceManager.getString(Constants.KEY_SUPERVISED_USER_DNI);

        binding.textViewNomPerfil.setText(nombreUsuario);
        binding.textViewDniPerfil2.setText(dniUsuario);
        binding.textViewAdreAPerfil2.setText(rolUsuario);
        binding.textViewTelefonPerfil2.setText(telefonoUsuario);
        binding.textViewIdentificadorPerfil2.setText(usuari_identificador);
        binding.textViewModificarPerfil.setText(passwordUsuario);
        binding.textViewCorreuPerfil.setText(dniTutoritzat);


        loadUserDetails();
    }
    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageViewPerfil.setImageBitmap(bitmap);
    }

    public void TornaBoto(View view){
        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(intent);
    }
}