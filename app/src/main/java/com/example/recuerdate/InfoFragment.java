package com.example.recuerdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Familiar> listaFamiliares;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        listaFamiliares = new ArrayList<>();

        adapter = new MyAdapter(listaFamiliares);
        recyclerView.setAdapter(adapter);

        LinearLayout customButtonLayout = view.findViewById(R.id.customButtonLayout);
        Button customButton = view.findViewById(R.id.customButton);

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogFragment dialog = new MyDialogFragment(InfoFragment.this);
                dialog.show(getFragmentManager(), "MyDialogFragment");
            }
        });

        return view;
    }

    public void addFamiliar(Familiar familiar) {
        listaFamiliares.add(familiar);
        adapter.notifyItemInserted(listaFamiliares.size() - 1);
        TextView textTitol = getView().findViewById(R.id.Nom_seccio);
        textTitol.setText(familiar.getNombre());
    }
}
