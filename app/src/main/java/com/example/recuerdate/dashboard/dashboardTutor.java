package com.example.recuerdate.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.recuerdate.ChatFamFragment;
import com.example.recuerdate.FamiliarsV2.FamiliarsV2;
import com.example.recuerdate.JocsFragment;
import com.example.recuerdate.LocalitzacioFragment;
import com.example.recuerdate.MainActivity;
import com.example.recuerdate.MainActivityTutor;
import com.example.recuerdate.PerfilActivity;
import com.example.recuerdate.R;
import com.example.recuerdate.calendari.RecordatoriFragment;


public class dashboardTutor extends Fragment {

    private MainActivityTutor mainActivityTutor;

    public dashboardTutor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_tutor, container, false);

        // Obtener referencia al LinearLayout
        LinearLayout linearLayoutPerfil = rootView.findViewById(R.id.linear_layout_perfil_tutor);
        LinearLayout linearLayoutChat = rootView.findViewById(R.id.linear_layout_chat_tutor);
        LinearLayout linearLayoutUbicacio = rootView.findViewById(R.id.linear_layout_ubi_tutor);
        LinearLayout linearLayoutFamiliars = rootView.findViewById(R.id.linear_layout_familiars_tutor);
        LinearLayout linearLayoutCalendari = rootView.findViewById(R.id.linear_layout_calendari_tutor);
        LinearLayout linearLayoutAlertes = rootView.findViewById(R.id.linear_layout_alertes_tutor);
        LinearLayout linearLayoutProgres = rootView.findViewById(R.id.linear_layout_progres_tutor);


        // Agregar un OnClickListener al LinearLayout
        linearLayoutPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                PerfilClick(v);
            }
        });

        //Listener Chat
        linearLayoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                ChatClick(v);
            }
        });

        //Listener Ubicacio

        linearLayoutUbicacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                UbiClick(v);
            }
        });

        //Listener Familiars

        linearLayoutFamiliars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                FamiliarsClick(v);
            }
        });

        //Listener Calendari
        linearLayoutCalendari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                CalendariClick(v);
            }
        });

        //Listener Alertes
        linearLayoutAlertes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                AlertesClick(v);
            }
        });

        //Listener Progres
        linearLayoutProgres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                ProgresClick(v);
            }
        });

        return rootView;
    }

    // Método para manejar el clic en el LinearLayout
    public void PerfilClick(View view) {
        // Lógica para manejar el clic en el LinearLayout
        // Por ejemplo, abrir una nueva actividad
        Intent intent = new Intent(getActivity(), PerfilActivity.class);
        startActivity(intent);
    }

    public void ChatClick(View view) {
        mainActivityTutor.replaceFragment(new ChatFamFragment());
    }

    public void UbiClick(View view) {
        mainActivityTutor.replaceFragment(new LocalitzacioFragment());;
    }

    public void FamiliarsClick(View view) {
        mainActivityTutor.replaceFragment(new FamiliarsV2());
    }

    public void CalendariClick(View view) {
        mainActivityTutor.replaceFragment(new RecordatoriFragment());
    }

    public void AlertesClick(View view) {
        mainActivityTutor.replaceFragment(new LocalitzacioFragment());
    }

    public void ProgresClick(View view) {
        System.out.println("Click a progres");
        //mainActivity.replaceFragment(new ProgresFragment());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityTutor) {
            mainActivityTutor = (MainActivityTutor) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }
}