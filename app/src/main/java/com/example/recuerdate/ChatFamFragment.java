package com.example.recuerdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatFamFragment extends Fragment {
    private apiService service;
    private LinearLayout chatArea;
    private RecyclerView recyclerView;
    private LinearLayout contactList;
    private MensajeAdapter adapter;
    private Button expandButton;
    private Button sendButton;
    private EditText editMessage;

    private List<Mensaje> mensajes;

    private String serverUrl = "http://10.0.2.2:3672";
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(serverUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_fam, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mensajes = new ArrayList<>();
        adapter = new MensajeAdapter(mensajes);
        recyclerView.setAdapter(adapter);

        chatArea = view.findViewById(R.id.chat_area);
        contactList = view.findViewById(R.id.contact_list);
        expandButton = view.findViewById(R.id.expand_button);
        sendButton = view.findViewById(R.id.send_button);
        editMessage = view.findViewById(R.id.edit_message);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl) // Asegúrate de que esta es la URL base correcta de tu API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(apiService.class);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams chatParams = (LinearLayout.LayoutParams) chatArea.getLayoutParams();
                LinearLayout.LayoutParams contactParams = (LinearLayout.LayoutParams) contactList.getLayoutParams();

                if (contactParams.weight > 0.0f) {
                    chatParams.weight = 4.0f;
                    contactParams.weight = 0.0f;
                } else {
                    chatParams.weight = 3.0f;
                    contactParams.weight = 1.0f;
                }

                chatArea.setLayoutParams(chatParams);
                contactList.setLayoutParams(contactParams);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("chat message", onNewMessage);
        mSocket.connect();
        Log.d("SocketIO", "Intento connexion  server");

        return view;
    }
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("SocketIO", "Conectado al servidor");
        }
    };
    private void attemptSend() {
        final String message = editMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }
        editMessage.setText("");
        mSocket.emit("chat message", message);
        String nomCognoms= "Coral"; //
        Mensaje mensaje = new Mensaje(nomCognoms, message);
        mensajes.add(mensaje); // Agrega el mensaje a la lista local
        adapter.notifyDataSetChanged(); // Notifica al adaptador sobre el cambio
        recyclerView.scrollToPosition(mensajes.size() - 1); // Desplaza la vista al último mensaje
        Call<Resposta> call = service.EnviarMensaje(mensaje);
        call.enqueue(new Callback<Resposta>() {
            @Override
            public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Mensaje enviado: " + message);
                } else {
                    Log.d("API", "Error al enviar el mensaje: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Resposta> call, Throwable t) {
                Log.d("API", "Error al hacer la llamada a la API", t);
            }
        });
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    handleNewMessage(args);
                }
            });
        }
    };
    private void handleNewMessage(Object... args) {
        if (args.length > 0 && args[0] instanceof JSONObject) {
            JSONObject data = (JSONObject) args[0];
            try {
                String nomCognoms = data.getString("nomCognoms");
                String message = data.getString("message");
                mensajes.add(new Mensaje(nomCognoms, message));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mensajes.size() - 1);
                Log.d("Mensajes", "Tamaño de la lista de mensajes: " + mensajes.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off("chat message", onNewMessage);
    }
}