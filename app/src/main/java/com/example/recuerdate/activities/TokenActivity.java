package com.example.recuerdate.activities;


import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import android.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;

import com.example.recuerdate.adapters.RecentConversationsAdapter;
import com.example.recuerdate.databinding.ActivityTokenBinding;
import com.example.recuerdate.listeners.ConversionListener;
import com.example.recuerdate.models.ChatMessage;
import com.example.recuerdate.models.User;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TokenActivity extends BaseActivity implements ConversionListener {
    private ActivityTokenBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTokenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        loadUserDetails();
        getToken();
        setListeners();
        listenConversations();
        // Comprobar si las notificaciones están habilitadas
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (!notificationManagerCompat.areNotificationsEnabled()) {
            // Las notificaciones están deshabilitadas
            // Muestra un diálogo al usuario
            new AlertDialog.Builder(TokenActivity.this)
                    .setTitle("Permisos de notificación")
                    .setMessage("Las notificaciones están deshabilitadas. ¿Te gustaría habilitarlas?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // El usuario ha hecho clic en 'Sí'
                            // Abre la pantalla de configuración de la aplicación
                            Intent settingsIntent = new Intent();
                            settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            settingsIntent.setData(uri);
                            startActivity(settingsIntent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void init(){
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();

    }
    private void setListeners() {
        binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }
    private void loadUserDetails() {
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void listenConversations(){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error)-> {
        if(error != null){
            return;
        }
        if(value!= null){
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType()== DocumentChange.Type.ADDED){
                String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.senderId = senderId;
                chatMessage.receiverId = receiverId;
                if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                    chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                    chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                    chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                } else{
                    chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                    chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                    chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                }
                chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                conversations.add(chatMessage);
            } else if(documentChange.getType()== DocumentChange.Type.MODIFIED){
                for(int i =0; i < conversations.size(); i++){
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                        conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                        conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                        break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        DocumentReference userDocumentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(currentUserId);
        userDocumentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("No se puede actualizar token en la colección de usuarios"));
        DocumentReference relativeDocumentReference =
                database.collection(Constants.KEY_COLLECTION_RELATIVES).document(currentUserId);
        relativeDocumentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("No se puede actualizar token en la colección de parientes"));
    }

    private void signOut(){
        showToast("Signing out...");
        removeTokenFromCollection(Constants.KEY_COLLECTION_USERS);
        removeTokenFromCollection(Constants.KEY_COLLECTION_RELATIVES);
    }

    private void removeTokenFromCollection(String collection){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(collection)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out from " + collection));
    }
    public void onConversionClicked(User user){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
    }
}