package com.example.recuerdate.familiars;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.recuerdate.R;
import java.util.ArrayList;



public class FamiliarsFragmentUsuari extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Model> arrayList = new ArrayList<>();

    ModelRecyclerView modelRecyclerView;

    private static final int REQUEST_SELECT_IMAGE = 100;

    ImageView imageViewFoto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_familiars_usuari, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewFamiliar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Button btnAgregarItem = rootView.findViewById(R.id.btnAgregarItem);

        // Establecer OnClickListener para el botón btnAgregarItem
        btnAgregarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarNuevoItem();
            }
        });


        modelRecyclerView = new ModelRecyclerView(getActivity(),arrayList);
        recyclerView.setAdapter(modelRecyclerView);




        return rootView;
    }


    public void agregarNuevoItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agregar_item, null);
        builder.setView(dialogView);

        final EditText etNombre = dialogView.findViewById(R.id.etNombre);
        final EditText etNumero = dialogView.findViewById(R.id.etNumero);
        imageViewFoto = dialogView.findViewById(R.id.imageViewFoto);
        Button btnSeleccionarFoto = dialogView.findViewById(R.id.btnSeleccionarFoto);

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un intent para abrir el selector de imágenes del dispositivo
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_SELECT_IMAGE);
            }
        });

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombre.getText().toString();
                String numero = etNumero.getText().toString();

                ImageView etImagen = imageViewFoto;
                // Aquí deberías guardar la foto seleccionada por el usuario (si hay alguna)

                arrayList.add(new Model(etImagen, nombre, numero));
                modelRecyclerView.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // La imagen ha sido seleccionada por el usuario
            Uri selectedImageUri = data.getData();

            // Aquí puedes cargar la imagen seleccionada en el ImageView para mostrarla al usuario
            imageViewFoto.setImageURI(selectedImageUri);
        }
    }


}


