package com.example.recuerdate.FamiliarsV2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recuerdate.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FamiliarsV2 extends Fragment {

    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_familiars_v2, container, false);

        RecyclerView rvItem = view.findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemList = new ArrayList<>(); // Lista vacía al principio
        itemAdapter = new ItemAdapter(requireContext(), itemList);
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);

        Button btnAddItem = view.findViewById(R.id.btn_add_item);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        return view;
    }

    private void addItem() {
        int newItemIndex = itemList.size();
        Item newItem = new Item("New Item " + newItemIndex, new ArrayList<>());
        itemList.add(newItem);
        itemAdapter.notifyItemInserted(newItemIndex);
    }
}

