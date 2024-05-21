package com.example.recuerdate.infoApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recuerdate.R;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.example.recuerdate.R;

public class infoAppFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_app, container, false);

        ImageView imageViewFamiliars = view.findViewById(R.id.imageViewFamiliars);
        ImageView imageViewCalendari = view.findViewById(R.id.imageViewCalendari);
        ImageView imageViewUbicacio = view.findViewById(R.id.imageViewUbicacio);
        ImageView imageViewJocs = view.findViewById(R.id.imageViewJocs);
        ImageView imageViewProgres = view.findViewById(R.id.imageViewProgres);
        ImageView imageViewChat = view.findViewById(R.id.imageViewChat);

        imageViewFamiliars.setOnClickListener(v -> showExplanationDialog("Familiars", "Llista de familiars, amics, coneguts que pots afegir perque l'usuari pugui recordarlos."));
        imageViewCalendari.setOnClickListener(v -> showExplanationDialog("Calendari", "Calendari per afegir recordatoris o events diaris."));
        imageViewUbicacio.setOnClickListener(v -> showExplanationDialog("Ubicació", "Localització de l'usuari i boto per guiarte a tornar a casa."));
        imageViewJocs.setOnClickListener(v -> showExplanationDialog("Jocs", "Joc de parelles per entrenar la memòria."));
        imageViewProgres.setOnClickListener(v -> showExplanationDialog("Progres", "Descobreix el teu progrés de les partides realitzades en el joc."));
        imageViewChat.setOnClickListener(v -> showExplanationDialog("Chat", "Chat amb el que pots comunicarte amb els teus tutors que tinguis enllaçats."));

        return view;
    }

    private void showExplanationDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}

