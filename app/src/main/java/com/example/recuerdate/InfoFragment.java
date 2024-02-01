package com.example.recuerdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        RecyclerView.LayoutManager layoutManager  = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        listaFamiliares = new ArrayList<>();
        listaFamiliares.add(new Familiar("Familiar 1", R.drawable.ic_launcher_background));
        listaFamiliares.add(new Familiar("Familiar 2", R.drawable.ic_launcher_background));
        listaFamiliares.add(new Familiar("Familiar 3", R.drawable.ic_launcher_background));
        listaFamiliares.add(new Familiar("Familiar 4", R.drawable.ic_launcher_background));
        // Agrega más familiares aquí

        adapter = new MyAdapter(listaFamiliares);
        recyclerView.setAdapter(adapter);

        return view;
    }
}