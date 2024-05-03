package com.example.recuerdate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.recuerdate.adapters.UsersAdapter;
import com.example.recuerdate.databinding.ActivityUsersBinding;
import com.example.recuerdate.listeners.UserListener;
import com.example.recuerdate.models.User;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity  implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }
    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        // Obtener el usuari_identificador del usuario actual de ambas colecciones
        Task<DocumentSnapshot> taskUser = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(currentUserId)
                .get();
        Task<DocumentSnapshot> taskRelative = database.collection(Constants.KEY_COLLECTION_RELATIVES)
                .document(currentUserId)
                .get();
        Task<List<DocumentSnapshot>> allTasks = Tasks.whenAllSuccess(taskUser, taskRelative);
        allTasks.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> results = task.getResult();
                String currentUserUsuariIdentificador = null;
                for (DocumentSnapshot documentSnapshot : results) {
                    if (documentSnapshot.exists()) {
                        currentUserUsuariIdentificador = documentSnapshot.getString("usuari_identificador");
                        break;
                    }
                }
                if (currentUserUsuariIdentificador != null) {
                    // Obtener usuarios y parientes con el mismo usuari_identificador
                    getUsersAndRelativesWithSameUsuariIdentificador(currentUserUsuariIdentificador);
                } else {
                    loading(false);
                    showToast("No se pudo obtener usuari_identificador");
                }
            } else {
                loading(false);
                showToast("Error al obtener usuari_identificador: " + task.getException().getMessage());
            }
        });
    }

    private void getUsersAndRelativesWithSameUsuariIdentificador(String usuariIdentificador) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        Task<QuerySnapshot> taskUsers = database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo("usuari_identificador", usuariIdentificador)
                .get();
        Task<QuerySnapshot> taskRelatives = database.collection(Constants.KEY_COLLECTION_RELATIVES)
                .whereEqualTo("usuari_identificador", usuariIdentificador)
                .get();
        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(taskUsers, taskRelatives);
        allTasks.addOnCompleteListener(task -> {
            loading(false);
            if (task.isSuccessful()) {
                List<User> users = new ArrayList<>();
                List<QuerySnapshot> results = task.getResult();
                for (QuerySnapshot querySnapshot : results) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                        if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                            continue;
                        }
                        User user = new User();
                        user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                        user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                        user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                        user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                        user.id = queryDocumentSnapshot.getId();
                        users.add(user);
                    }
                }
                if (users.size() > 0) {
                    UsersAdapter usersAdapter = new UsersAdapter(users,this);
                    binding.usersRecyclerView.setAdapter(usersAdapter);
                    binding.usersRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}