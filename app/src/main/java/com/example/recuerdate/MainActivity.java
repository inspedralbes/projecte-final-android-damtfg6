package com.example.recuerdate;

import android.icu.text.IDNA;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recuerdate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new InfoFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.info:
                    replaceFragment(new InfoFragment());
                    break;
                case R.id.recordatoris:
                    replaceFragment(new RecordatoriFragment());
                    break;
                case R.id.chat:
                    replaceFragment(new ChatFamFragment());
                    break;
                case R.id.jocs:
                    replaceFragment(new JocsFragment());
                    break;
                case R.id.localitzacio:
                    replaceFragment(new LocalitzacioFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
