package com.example.recuerdate.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.recuerdate.MainActivity;
import com.example.recuerdate.databinding.ActivitySignUpBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }
    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String role = binding.spinnerRole.getSelectedItem().toString();
        String collection = role.equals("Usuari") ? Constants.KEY_COLLECTION_USERS : Constants.KEY_COLLECTION_RELATIVES;

        // Primero, verifica si el DNI ya existe en la base de datos
        database.collection(collection)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // El DNI no existe en la base de datos, por lo que puedes proceder a registrar al usuario
                            HashMap<String, Object> user = new HashMap<>();
                            user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
                            user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
                            user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                            user.put(Constants.KEY_IMAGE, encodedImage);
                            user.put(Constants.KEY_PHONE, binding.inputConfirmPassword.getText().toString());
                            user.put(Constants.KEY_ROLE, role); // Añade el rol al registro
                            if (role.equals("Tutor")) {
                                user.put(Constants.KEY_USER_IDENTIFIER, "00000");
                                user.put(Constants.KEY_SUPERVISED_USER_NAME, "Nombre del usuario supervisado");// Añade el campo usuari_identificador vacío si el rol es Tutor
                            }

                            database.collection(collection)
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        // Obtén el usuari_identificador del documento que acabas de crear
                                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                                            String usuari_identificador = documentSnapshot.getString("usuari_identificador");
                                            String supervisedUserName = documentSnapshot.getString(Constants.KEY_SUPERVISED_USER_NAME);
                                            preferenceManager.putString(Constants.KEY_USER_IDENTIFIER, usuari_identificador);
                                            preferenceManager.putString(Constants.KEY_SUPERVISED_USER_NAME, supervisedUserName);
                                        });

                                        loading(false);
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                                        preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                                        preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                                        preferenceManager.putString(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
                                        preferenceManager.putString(Constants.KEY_PHONE, binding.inputConfirmPassword.getText().toString());
                                        preferenceManager.putString(Constants.KEY_ROLE, role);
                                        preferenceManager.putString(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(exception -> {
                                        loading(false);
                                        showToast(exception.getMessage());
                                    });
                        } else {
                            // El DNI ya existe en la base de datos, muestra un mensaje al usuario
                            loading(false);
                            showToast("El DNI ya está en uso. Por favor, introduce un DNI diferente.");
                        }
                    } else {
                        loading(false);
                        showToast("Error al verificar el DNI: " + task.getException().getMessage());
                    }
                });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitMap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitMap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if(result.getData()!=null){
                    Uri imageUri = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.imageProfile.setImageBitmap(bitmap);
                        binding.textAddImage.setVisibility(View.GONE);
                        encodedImage = encodeImage(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
         }
    );
    private Boolean isValidSignUpDetails(){
        if (encodedImage == null){
            showToast("Selecciona una imatge de perfil");
            return false;
        } else if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Introdueix el teu nom i cognoms");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Introdueix el teu DNI");
            return false;
        } else if (!esDniValido(binding.inputEmail.getText().toString())) {
            showToast("Introdueix un DNI vàlid");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Introdueix una contrasenya");
            return false;
        } else if (!esTelefonoValido(binding.inputConfirmPassword.getText().toString())) {
            showToast("Introdueix un número de teléfon vàlid");
            return false;
        } else{
            return true;
        }
    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
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
    public boolean esTelefonoValido(String numero) {
        String REGEX_TELEFONO = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(REGEX_TELEFONO);
        Matcher matcher = pattern.matcher(numero);

        return matcher.matches();
    }
}