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

    /*@Override
    protected void onStart() {
        super.onStart();
        checkSession();
    }

    private void checkSession() {
        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
        int userID = sessionManagment.getSession();

        if (userID != -1){
            //user id esta loggejat i mourel a la mainActivity
            moveToMainActivity();
        }
        else {
            //No fes res
        }
    }*/

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
        }
        else{
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

        UsuariLocalitzat datosLogin = new UsuariLocalitzat(dni,contrasenya);

        Call<Resposta> call = apiService.EnviarUsuari(datosLogin);

        call.enqueue(new Callback<Resposta>() {
            @Override
            public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                if (response.isSuccessful()) {
                    Resposta r = response.body();
                    if (r.isAutoritzacio()) {
                        Log.d("Usuari", "Has ");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "L'usuari no existeix", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Resposta> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    public void launchRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
        startActivity(intent);
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
