package com.example.recuerdate.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recuerdate.ChatFamFragment;
import com.example.recuerdate.FamiliarsV2.FamiliarsV2;
import com.example.recuerdate.LocalitzacioFragment;
import com.example.recuerdate.MainActivityTutor;
import com.example.recuerdate.PerfilActivity;
import com.example.recuerdate.PerfilTutorActivity;
import com.example.recuerdate.calendari.RecordatoriFragment;
import com.example.recuerdate.databinding.FragmentDashboardTutorBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;


public class DashboardTutor extends Fragment {

    private MainActivityTutor mainActivityTutor;
    private FragmentDashboardTutorBinding binding;

    private PreferenceManager preferenceManager;

    public DashboardTutor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardTutorBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(getContext());
        String nameTutor = preferenceManager.getString(Constants.KEY_NAME);


        // Agregar un OnClickListener al LinearLayout
        binding.linearLayoutPerfilTutor.setOnClickListener(v -> PerfilClick(v));
        binding.linearLayoutChatTutor.setOnClickListener(v -> ChatClick(v));
        binding.linearLayoutUbiTutor.setOnClickListener(v -> UbiClick(v));
        binding.linearLayoutFamiliarsTutor.setOnClickListener(v -> FamiliarsClick(v));
        binding.linearLayoutCalendariTutor.setOnClickListener(v -> CalendariClick(v));
        binding.linearLayoutAlertesTutor.setOnClickListener(v -> AlertesClick(v));
        binding.linearLayoutProgresTutor.setOnClickListener(v -> ProgresClick(v));
        binding.textView30.setText(nameTutor);



        loadUserDetails();
        return binding.getRoot();
    }
    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageView32.setImageBitmap(bitmap);
    }

    // Métodos para manejar el clic en el LinearLayout
    public void PerfilClick(View view) {
        Intent intent = new Intent(getActivity(), PerfilTutorActivity.class);
        startActivity(intent);
    }

    public void ChatClick(View view) {
        mainActivityTutor.replaceFragment(new ChatFamFragment());
    }

    public void UbiClick(View view) {
        mainActivityTutor.replaceFragment(new LocalitzacioFragment());
    }

    public void FamiliarsClick(View view) {
        mainActivityTutor.replaceFragment(new FamiliarsV2());
    }

    public void CalendariClick(View view) {
        mainActivityTutor.replaceFragment(new RecordatoriFragment());
    }

    public void AlertesClick(View view) {
        mainActivityTutor.replaceFragment(new LocalitzacioFragment());
    }

    public void ProgresClick(View view) {
        System.out.println("Click a progres");
        //mainActivity.replaceFragment(new ProgresFragment());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityTutor) {
            mainActivityTutor = (MainActivityTutor) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }
}
