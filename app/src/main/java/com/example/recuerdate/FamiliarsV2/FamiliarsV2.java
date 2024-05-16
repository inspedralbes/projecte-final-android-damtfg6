package com.example.recuerdate.FamiliarsV2;

import static com.example.recuerdate.FamiliarsV2.ItemAdapter.REQUEST_IMAGE;

import android.app.Activity;
import android.content.Context;
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

import com.example.recuerdate.MainActivity;
import com.example.recuerdate.MainActivityTutor;
import com.example.recuerdate.R;
import com.example.recuerdate.SessionManagment;
import com.example.recuerdate.Settings;
import com.example.recuerdate.activities.TokenActivity;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FamiliarsV2 extends Fragment {

    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private Context context;

    private PreferenceManager preferenceManager;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
    }
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
        String role = preferenceManager.getString(Constants.KEY_ROLE);
        String dni = preferenceManager.getString(Constants.KEY_EMAIL);
        if (role.equals("Tutor")) {
            dni = preferenceManager.getString(Constants.KEY_SUPERVISED_USER_DNI);
        } else if (role.equals("Usuari")) {
            dni = preferenceManager.getString(Constants.KEY_EMAIL);
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Settings.SERVER+ ":" + Settings.PORT + "/familyItems?dni=" + dni)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String responseData = response.body().string();

                    // Deserializar la respuesta JSON a una lista de Items
                    Gson gson = new Gson();
                    Type itemListType = new TypeToken<ArrayList<Item>>(){}.getType();
                    ArrayList<Item> newItems = gson.fromJson(responseData, itemListType);

                    // Actualizar la interfaz de usuario en el hilo principal
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Aquí puedes usar newItems para actualizar tu RecyclerView
                            itemList.clear();
                            itemList.addAll(newItems);
                            itemAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        return view;
    }

    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nom del grup de familiars: ");

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
            System.out.println("Hola, arriba imatge");
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

