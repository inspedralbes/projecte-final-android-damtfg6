package com.example.recuerdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TutoritzarNouUsuari extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoritzar_nou_usuari);

        EditText editText = findViewById(R.id.editTextTextNovaTutoritzacio);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Abre el teclado solo cuando se toca el área del EditText, pero no el icono de ayuda
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showImageHelpDialog();
                        return true;
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void showImageHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_help, null);

        // Configura la imagen en el diálogo (asumiendo que tienes una imagen llamada "telefono" en tu carpeta "drawable")
        ImageView imageView = dialogView.findViewById(R.id.imageViewHelp);
        imageView.setImageResource(R.drawable.telefono);

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


}