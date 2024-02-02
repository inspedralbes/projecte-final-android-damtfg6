package com.example.recuerdate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
    private InfoFragment infoFragment;

    public MyDialogFragment(InfoFragment infoFragment) {
        this.infoFragment = infoFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);

        final EditText sectionName = view.findViewById(R.id.section_name);
        final EditText personCount = view.findViewById(R.id.person_count);

        builder.setView(view)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String name = sectionName.getText().toString();
                        int count = Integer.parseInt(personCount.getText().toString());

                        for (int i = 0; i < count; i++) {
                            Familiar familiar = new Familiar(name, R.drawable.ic_launcher_background);
                            infoFragment.addFamiliar(familiar);
                        }
                        TextView nomSeccio = infoFragment.getView().findViewById(R.id.Nom_seccio);
                        nomSeccio.setText(name);
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MyDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}