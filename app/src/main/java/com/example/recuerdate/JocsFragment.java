package com.example.recuerdate;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.recuerdate.jocs.Launcher;
import com.example.recuerdate.jocs.Launcher2;

public class JocsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jocs, container, false);

        // Buscar los ImageButtons por su ID
        ImageButton launchJoc1Button = rootView.findViewById(R.id.LaunchJoc1);
        ImageButton launchJoc2Button = rootView.findViewById(R.id.LaunchJoc2);

        // Establecer OnClickListener para el primer ImageButton
        launchJoc1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para abrir el primer juego
                abrirJoc1();
            }
        });

        // Establecer OnClickListener para el segundo ImageButton
        launchJoc2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para abrir el segundo juego
                abrirJoc2();
            }
        });

        return rootView;
    }

    private void abrirJoc1() {
        // Lógica para abrir el primer juego
        Intent intent = new Intent(getContext(), Launcher.class);
        startActivity(intent);
    }

    private void abrirJoc2() {
        // Lógica para abrir el segundo juego
        Intent intent = new Intent(getContext(), Launcher2.class);
        startActivity(intent);
    }
}