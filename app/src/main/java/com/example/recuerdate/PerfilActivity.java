package com.example.recuerdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
    }

    public void TornaBoto(View view){
        Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(intent);
    }
}