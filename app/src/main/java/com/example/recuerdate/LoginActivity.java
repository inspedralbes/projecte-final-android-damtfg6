package com.example.recuerdate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String URL = "http://192.168.205.57:3672/";
    public static apiService apiService;
    private String dni;
    private String contrasenya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
        String dniSaved = sessionManagment.getDni();

        if (dniSaved != null) {
            if (sessionManagment.getRol().equals("usuari")){
                moveToMainActivity();
            }
            else{
                moveToMainTutorActivity();
            }
        } else {
            // No hagas nada
        }
    }

    public void iniciSessio(View view) {

        EditText dniuser = findViewById(R.id.editTextTextLogDNI);
        EditText contrasenyaUser = findViewById(R.id.editTextTextLogContrasenya);

        dni = dniuser.getText().toString();
        contrasenya = contrasenyaUser.getText().toString();


        if (dni.isEmpty() || contrasenya.isEmpty()) {
            if (dni.isEmpty()) {
                dniuser.setError(getString(R.string.value_required));
            }
            if (contrasenya.isEmpty()) {
                contrasenyaUser.setError(getString(R.string.value_required));
            }
        } else {
            VerificarUsuari();
        }
        /*Pas 1
        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
        //sessionManagment.saveSession(usuariTrobat);

        //Pas 2
        moveToMainActivity();*/
    }

    private void VerificarUsuari() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(apiService.class);

        UsuariLocalitzat datosLogin = new UsuariLocalitzat(dni, contrasenya);

        Call<RespostaLogin> call = apiService.EnviarUsuari(datosLogin);

        call.enqueue(new Callback<RespostaLogin>() {
            @Override
            public void onResponse(Call<RespostaLogin> call, Response<RespostaLogin> response) {
                if (response.isSuccessful()) {
                    RespostaLogin r = response.body();
                    if (r.isAutoritzacio()) {
                        Log.d("Usuari", "Has ");

                        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
                        Usuari userData = r.getUserData();
                        Usuari usuariTutoritzatData = r.getUsuariTutoritzatData();
                        sessionManagment.saveSession(dni, r.getRol(), userData, usuariTutoritzatData);

                        // Verificar el rol y actuar en consecuencia
                        if ("tutor".equals(r.getRol())) {
                            moveToMainTutorActivity();
                        } else {
                            moveToMainActivity();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "L'usuari no existeix", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<RespostaLogin> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void launchRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
        startActivity(intent);
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void moveToMainTutorActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivityTutor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}