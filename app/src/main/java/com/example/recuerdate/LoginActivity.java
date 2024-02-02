package com.example.recuerdate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editTextTextLogDNI);
        password = findViewById(R.id.editTextTextLogContrasenya);
        loginButton = findViewById(R.id.btnLogin);
    }

    @Override
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
    }

    public void iniciSessio(View view) {

        //Pas 1
        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
        //sessionManagment.saveSession(usuariTrobat);

        //Pas 2
        moveToMainActivity();

    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
