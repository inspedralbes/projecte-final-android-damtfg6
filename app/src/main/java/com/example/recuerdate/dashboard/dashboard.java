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
import com.example.recuerdate.PerfilActivity;
import com.example.recuerdate.R;
import com.example.recuerdate.RecordatoriFragment;

public class dashboard extends Fragment {
    // Constructor y otros métodos si es necesario

    private MainActivity mainActivity;

    public dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Obtener referencia al LinearLayout
        LinearLayout linearLayoutPerfil = rootView.findViewById(R.id.linear_layout_perfil);
        LinearLayout linearLayoutChat = rootView.findViewById(R.id.linear_layout_chat);
        LinearLayout linearLayoutUbicacio = rootView.findViewById(R.id.linear_layout_ubi);
        LinearLayout linearLayoutFamiliars = rootView.findViewById(R.id.linear_layout_familiars);
        LinearLayout linearLayoutCalendari = rootView.findViewById(R.id.linear_layout_calendari);
        LinearLayout linearLayoutAlertes = rootView.findViewById(R.id.linear_layout_alertes);
        LinearLayout linearLayoutJocs = rootView.findViewById(R.id.linear_layout_jocs);
        LinearLayout linearLayoutProgres = rootView.findViewById(R.id.linear_layout_progres);



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

        //listener Jocs
        linearLayoutJocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar el clic en el LinearLayout
                JocsClick(v);
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
        mainActivity.replaceFragment(new ChatFamFragment());
    }

    public void UbiClick(View view) {
        mainActivity.replaceFragment(new LocalitzacioFragment());;
    }

    public void FamiliarsClick(View view) {
        mainActivity.replaceFragment(new FamiliarsV2());
    }

    public void CalendariClick(View view) {
        mainActivity.replaceFragment(new RecordatoriFragment());
    }

    public void AlertesClick(View view) {
        mainActivity.replaceFragment(new LocalitzacioFragment());
    }

    public void JocsClick(View view) {
        mainActivity.replaceFragment(new JocsFragment());
    }

    public void ProgresClick(View view) {
        System.out.println("Click a progres");
        //mainActivity.replaceFragment(new ProgresFragment());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }
}
