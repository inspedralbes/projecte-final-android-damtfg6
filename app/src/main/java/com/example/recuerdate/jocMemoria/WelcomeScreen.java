package com.example.recuerdate.jocMemoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recuerdate.ChatFamFragment;
import com.example.recuerdate.JocsFragment;
import com.example.recuerdate.MainActivity;
import com.example.recuerdate.R;
import com.example.recuerdate.jocMemoria.game.InfoBox;

public class WelcomeScreen extends AppCompatActivity {
    ImageButton playButton, highScoreButton;
    ImageView closeButton;

    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        playButton = findViewById(R.id.play_button);
        highScoreButton = findViewById(R.id.high_score_button);
        closeButton = findViewById(R.id.tanca_btn);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeScreen.this, PlayScreen.class);
                startActivity(i);
            }
        });

        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeScreen.this, HighScoreScreen.class);
                startActivity(i);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esto hará que la actividad actual vuelva a la anterior
                onBackPressed();
            }
        });
    }
}