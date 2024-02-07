package com.example.recuerdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.recuerdate.databinding.ActivityMainBinding;
import com.example.recuerdate.databinding.ActivityMainTutor2Binding;
import com.google.android.material.navigation.NavigationView;

public class MainActivityTutor extends AppCompatActivity {

    ActivityMainTutor2Binding binding;
    ActionBarDrawerToggle drawerToggle;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainTutor2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(findViewById(R.id.toolbar));

        // Configuración del ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayoutTutor, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        binding.drawerLayoutTutor.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.perfilmenu:
                        obrirPerfil();
                        return true;

                    case R.id.sesiomenu:
                        showLogoutConfirmationDialog();
                        return true;
                }
                return true;
            }
        });

        // Configuración del BottomNavigationView
        binding.bottomNavigationViewTutor.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.infoTutor:
                    replaceFragment(new InfoTutorFragment());
                    break;
                case R.id.chatTutor:
                    replaceFragment(new ChatTutorFragment());
                    break;
                case R.id.localitzacioTutor:
                    replaceFragment(new localitzaTutorFragment());
                    break;
            }
            return true;
        });

        // Asegúrate de que el InfoFragment esté abierto al inicio
        replaceFragment(new InfoTutorFragment());
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.confirm_logout))
                .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void logout(){
        SessionManagment sessionManagment = new SessionManagment(MainActivityTutor.this);
        sessionManagment.removeSession();
        movetoLogin();
    }

    private void movetoLogin() {
        Intent intent = new Intent(MainActivityTutor.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void obrirPerfil() {
        Intent intent = new Intent(MainActivityTutor.this, PerfilTutorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
}