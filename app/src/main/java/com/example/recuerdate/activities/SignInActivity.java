package com.example.recuerdate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recuerdate.MainActivity;
import com.example.recuerdate.MainActivityTutor;
import com.example.recuerdate.databinding.ActivitySignInBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            String role = preferenceManager.getString(Constants.KEY_ROLE);
            Intent intent;
            if (role.equals("Tutor")) {
                intent = new Intent(getApplicationContext(), MainActivityTutor.class);
            } else {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }
    private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(v ->
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.buttonSignIn.setOnClickListener(v -> {
            if (isValidSignInDetails()) {
                signIn();
            }
        });
    }
    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> taskUsers = database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get();
        Task<QuerySnapshot> taskRelatives = database.collection(Constants.KEY_COLLECTION_RELATIVES)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(taskUsers, taskRelatives);
        allTasks.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<QuerySnapshot> results = task.getResult();
                QuerySnapshot usersResult = results.get(0);
                QuerySnapshot relativesResult = results.get(1);
                if (!usersResult.isEmpty()) {
                    handleSignInSuccess(usersResult.getDocuments().get(0));
                } else if (!relativesResult.isEmpty()) {
                    handleSignInSuccess(relativesResult.getDocuments().get(0));
                } else {
                    loading(false);
                    showToast("No s'ha pogut iniciar sessió");
                }
            } else {
                loading(false);
                showToast("Error al verificar el DNI: " + task.getException().getMessage());
            }
        });
    }

    private void handleSignInSuccess(DocumentSnapshot documentSnapshot) {
        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
        preferenceManager.putString(Constants.KEY_USER_IDENTIFIER, documentSnapshot.getString(Constants.KEY_USER_IDENTIFIER));
        preferenceManager.putString(Constants.KEY_PASSWORD, documentSnapshot.getString(Constants.KEY_PASSWORD));
        preferenceManager.putString(Constants.KEY_PHONE, documentSnapshot.getString(Constants.KEY_PHONE));
        preferenceManager.putString(Constants.KEY_ROLE, documentSnapshot.getString(Constants.KEY_ROLE));

        String role = documentSnapshot.getString(Constants.KEY_ROLE);
        if (role.equals("Tutor")) {
            // Si el usuario es un tutor, busca a través del usuari_identificador del usuario al que supervisa
            String usuari_identificador = documentSnapshot.getString(Constants.KEY_USER_IDENTIFIER);
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .whereEqualTo(Constants.KEY_USER_IDENTIFIER, usuari_identificador)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                            String supervisedUserDni = userDocument.getString(Constants.KEY_EMAIL);
                            // Guarda el DNI del usuario que el tutor supervisa en preferenceManager
                            preferenceManager.putString(Constants.KEY_SUPERVISED_USER_DNI, supervisedUserDni);

                            // Decide a qué actividad redirigir en función del rol del usuario
                            Intent intent;
                            if (role.equals("Tutor")) {
                                intent = new Intent(getApplicationContext(), MainActivityTutor.class);
                            } else {
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            // Maneja el caso en el que no se encuentra el usuari_identificador
                        }
                    });
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }




    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private Boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().isEmpty()) {
            showToast("Escriu el Dni");
            return false;
        } else if (!esDniValido(binding.inputEmail.getText().toString())) {
            showToast("Introdueix un DNI vàlid");
            return false;
        } else if (binding.inputPassword.getText().toString().isEmpty()) {
            showToast("Escriu la contrasenya");
            return false;
        } else {
            return true;
        }
    }

    public boolean esDniValido(String dni) {
        // Comprobar si tiene una longitud de 9 caracteres
        if (dni.length() != 9)
            return false;

        // Obtener los 8 primeros caracteres y convertirlos a número
        String num = dni.substring(0, 8);
        int numeroDNI;
        try {
            numeroDNI = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }

        // Obtener el último carácter
        char letra = Character.toUpperCase(dni.charAt(8));

        // Calcular la letra que debería tener el DNI
        char letraCorrecta = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(numeroDNI % 23);

        // Comprobar si la letra del DNI es correcta
        if (letra != letraCorrecta)
            return false;

        // Si todo es correcto, el DNI es válido
        return true;
    }
}