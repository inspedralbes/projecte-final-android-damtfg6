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
import android.widget.LinearLayout;

import com.example.recuerdate.FamiliarsV2.FamiliarsV2;
import com.example.recuerdate.JocsFragment;
import com.example.recuerdate.LocalitzacioFragment;
import com.example.recuerdate.MainActivity;
import com.example.recuerdate.PerfilActivity;
import com.example.recuerdate.R;
import com.example.recuerdate.activities.TokenActivity;
import com.example.recuerdate.calendari.RecordatoriFragment;
import com.example.recuerdate.databinding.FragmentDashboardBinding;
import com.example.recuerdate.infoApp.infoAppFragment;
import com.example.recuerdate.jocMemoria.WelcomeScreen;
import com.example.recuerdate.stats.statsFragment;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;

public class Dashboard extends Fragment {

    private MainActivity mainActivity;
    private FragmentDashboardBinding binding;
    private PreferenceManager preferenceManager;

    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(getContext());
        String nameTutor = preferenceManager.getString(Constants.KEY_NAME);

        // Agregar un OnClickListener al LinearLayout
        binding.linearLayoutPerfil.setOnClickListener(v -> PerfilClick(v));
        binding.linearLayoutChat.setOnClickListener(v -> ChatClick(v));
        binding.linearLayoutUbi.setOnClickListener(v -> UbiClick(v));
        binding.linearLayoutFamiliars.setOnClickListener(v -> FamiliarsClick(v));
        binding.linearLayoutCalendari.setOnClickListener(v -> CalendariClick(v));
        binding.linearLayoutAlertes.setOnClickListener(v -> AlertesClick(v));
        binding.linearLayoutJocs.setOnClickListener(v -> JocsClick(v));
        binding.linearLayoutProgres.setOnClickListener(v -> ProgresClick(v));
        binding.linearLayoutAppInfo.setOnClickListener(v -> appInfoClick(v));
        binding.textView3.setText(nameTutor);
        loadUserDetails();
        return binding.getRoot();
    }
    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageView14.setImageBitmap(bitmap);
    }
    // Métodos para manejar el clic en el LinearLayout
    public void PerfilClick(View view) {
        Intent intent = new Intent(getActivity(), PerfilActivity.class);
        startActivity(intent);
    }

    public void ChatClick(View view) {
        Intent intent = new Intent(getActivity(), TokenActivity.class);
        startActivity(intent);
    }

    public void UbiClick(View view) {
        mainActivity.replaceFragment(new LocalitzacioFragment());
    }

    public void FamiliarsClick(View view) {
        mainActivity.replaceFragment(new FamiliarsV2());
    }

    public void CalendariClick(View view) {
        mainActivity.replaceFragment(new RecordatoriFragment());
    }

    public void AlertesClick(View view) {
        mainActivity.replaceFragment(new LocalitzacioFragment());
    }

    public void JocsClick(View view) {
        Intent intent = new Intent(getContext(), WelcomeScreen.class);
        startActivity(intent);
    }

    public void ProgresClick(View view) {
        System.out.println("Click a progres");
        mainActivity.replaceFragment(new statsFragment());
    }

    public void appInfoClick(View view) {
        System.out.println("Click a progres");
        mainActivity.replaceFragment(new infoAppFragment());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }
}
