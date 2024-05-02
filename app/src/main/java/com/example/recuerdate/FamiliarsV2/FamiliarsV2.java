package com.example.recuerdate.FamiliarsV2;

import static com.example.recuerdate.FamiliarsV2.ItemAdapter.REQUEST_IMAGE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recuerdate.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        itemAdapter = new ItemAdapter(requireContext(), itemList, this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Titol dels familiars");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String itemTitle = input.getText().toString();
                int newItemIndex = itemList.size();
                Item newItem = new Item(itemTitle, new ArrayList<>());
                itemList.add(newItem);
                itemAdapter.notifyItemInserted(newItemIndex);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Handle activity result for image selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            System.out.println(selectedImageUri);
            System.out.println("Hola");
            // Pass the selectedImageUri to your adapter
            if (itemAdapter != null) {
                itemAdapter.setSelectedImageUri(selectedImageUri);
            }
        }
        else{
            System.out.println("Null adapter");
        }
    }
}

