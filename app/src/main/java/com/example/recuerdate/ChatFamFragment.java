package com.example.recuerdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


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

    private Socket mSocket;
    private String serverUrl = Settings.SERVER + ":" + Settings.PORT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mSocket = IO.socket(serverUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Connected to server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    mSocket.on("chat message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String message = data.getString("message");
                            String nomCognoms = data.getString("nomCognoms");
                            Mensaje mensaje = new Mensaje(nomCognoms, message);
                            mensajes.add(mensaje);
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(mensajes.size() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mSocket.connect();
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
                String message = editMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    SessionManagment sessionManagment = new SessionManagment(getContext());
                    String nomCognoms = sessionManagment.getUserData().getNomCognoms();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("message", message);
                        data.put("nomCognoms", nomCognoms);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("chat message", data);
                    editMessage.setText("");
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off("chat message");
    }
}


