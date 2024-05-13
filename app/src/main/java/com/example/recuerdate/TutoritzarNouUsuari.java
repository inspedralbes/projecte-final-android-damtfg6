package com.example.recuerdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recuerdate.databinding.ActivityTutoritzarNouUsuariBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TutoritzarNouUsuari extends AppCompatActivity {

    private ActivityTutoritzarNouUsuariBinding binding;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutoritzarNouUsuariBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(this);

        binding.textViewTutoritzacioError.setVisibility(View.GONE);
        binding.textViewTutoritzacioRegistrada.setVisibility(View.GONE);
        binding.editTextTextNovaTutoritzacio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Abre el teclado solo cuando se toca el área del EditText, pero no el icono de ayuda
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (binding.editTextTextNovaTutoritzacio.getRight() - binding.editTextTextNovaTutoritzacio.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showImageHelpDialog();
                        return true;
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(binding.editTextTextNovaTutoritzacio, InputMethodManager.SHOW_IMPLICIT);
                        return false;
                    }
                }
                return false;
            }
        });
        binding.botoConfirmarTutoritzacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí va el código que se ejecutará cuando se haga clic en el botón
                String newUsuariIdentificador = binding.editTextTextNovaTutoritzacio.getText().toString().trim();
                checkUsuariIdentificadorAndUpdate(newUsuariIdentificador);
            }
        });
    }


    private void showImageHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_help, null);

        // Configura la imagen en el diálogo (asumiendo que tienes una imagen llamada "telefono" en tu carpeta "drawable")
        ImageView imageView = dialogView.findViewById(R.id.imageViewHelp);
        imageView.setImageResource(R.drawable.header_color);

        // Configura el botón "Tancar"
        Button closeButton = dialogView.findViewById(R.id.Tancar);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el diálogo cuando se hace clic en el botón "Tancar"
                AlertDialog dialog = (AlertDialog) v.getTag();
                dialog.dismiss();
            }
        });

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        closeButton.setTag(alertDialog);

        alertDialog.show();
    }


    private void checkUsuariIdentificadorAndUpdate(String newUsuariIdentificador) {
        // Verifica si el usuari_identificador existe en la colección 'users'
        db.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_IDENTIFIER, newUsuariIdentificador)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // El usuari_identificador existe en la colección 'users', puedes proceder a cambiar el usuari_identificador del usuario
                            DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                            userDocument.getReference().update(Constants.KEY_USER_IDENTIFIER, newUsuariIdentificador)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firebase", "DocumentSnapshot successfully updated!");

                                        // Guarda el nuevo usuari_identificador en las preferencias compartidas
                                        preferenceManager.putString(Constants.KEY_USER_IDENTIFIER, newUsuariIdentificador);

                                        // Muestra el mensaje de éxito
                                        binding.textViewTutoritzacioRegistrada.setVisibility(View.VISIBLE);
                                        binding.textViewTutoritzacioError.setVisibility(View.GONE);

                                        // Actualiza el usuari_identificador en la colección 'relatives'
                                        updateRelativeUsuariIdentificador(newUsuariIdentificador);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Firebase", "Error updating document", e);

                                        // Muestra el mensaje de error
                                        binding.textViewTutoritzacioError.setVisibility(View.VISIBLE);
                                        binding.textViewTutoritzacioRegistrada.setVisibility(View.GONE);
                                    });
                        } else {
                            Log.d("Firebase", "El usuari_identificador no existe en la colección 'users'");

                            // Muestra el mensaje de error
                            binding.textViewTutoritzacioError.setVisibility(View.VISIBLE);
                            binding.textViewTutoritzacioRegistrada.setVisibility(View.GONE);
                        }
                    } else {
                        Log.w("Firebase", "Error getting documents", task.getException());

                        // Muestra el mensaje de error
                        binding.textViewTutoritzacioError.setVisibility(View.VISIBLE);
                        binding.textViewTutoritzacioRegistrada.setVisibility(View.GONE);
                    }
                });
    }

    private void updateRelativeUsuariIdentificador(String newUsuariIdentificador) {
        // Aquí necesitas obtener la referencia al documento del usuario en la colección 'relatives'
        // Esto dependerá de cómo estén estructurados tus documentos en Firestore
        // Por ejemplo, si tienes el ID del usuario, podrías hacer algo como esto:
        String relativeUserId = preferenceManager.getString(Constants.KEY_USER_ID);
        DocumentReference relativeUserRef = db.collection(Constants.KEY_COLLECTION_RELATIVES).document(relativeUserId);

        // Actualiza el usuari_identificador del usuario en la colección 'relatives'
        relativeUserRef.update(Constants.KEY_USER_IDENTIFIER, newUsuariIdentificador)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "DocumentSnapshot in 'relatives' successfully updated!"))
                .addOnFailureListener(e -> Log.w("Firebase", "Error updating document in 'relatives'", e));
    }



    public void TornaBotoTutor(View view) {
        Intent intent = new Intent(TutoritzarNouUsuari.this, MainActivityTutor.class);
        startActivity(intent);
    }

}
