package com.example.recuerdate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recuerdate.dashboard.Dashboard;
import com.example.recuerdate.dashboard.DashboardTutor;
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

        // Configuración del ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayoutTutor, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_viewTutor);
        binding.drawerLayoutTutor.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tutorMenu:
                        ObrirScreenTutoritzar();
                        return true;
                    case R.id.perfilmenuTutor:
                        obrirPerfil();
                        return true;

                    case R.id.sesiomenuTutor:
                        showLogoutConfirmationDialog();
                        return true;
                }
                return true;
            }
        });

        // Configuración del BottomNavigationView
        binding.bottomNavigationViewTutor.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeTutor:
                    replaceFragment(new DashboardTutor());
                    binding.bottomNavigationViewTutor.setVisibility(View.GONE); // Oculta el BottomNavigationView
                    break;
                case R.id.chatTutor:
                    replaceFragment(new ChatFamFragment());
                    break;
                case R.id.mapaTutor:
                    replaceFragment(new LocalitzacioFragment());
                    break;
            }
            return true;
        });

        // Asegúrate de que el InfoFragment esté abierto al inicio
        replaceFragment(new DashboardTutor());
        binding.bottomNavigationViewTutor.setVisibility(View.GONE);
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

    private void ObrirScreenTutoritzar() {
        Intent intent = new Intent(MainActivityTutor.this, TutoritzarNouUsuari.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_tutor, fragment);
        fragmentTransaction.commit();

        if (!(fragment instanceof Dashboard)) {
            binding.bottomNavigationViewTutor.setVisibility(View.VISIBLE); // Muestra el BottomNavigationView
        }
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
    public void openDrawer(View view) {
        binding.drawerLayoutTutor.openDrawer(GravityCompat.START);
    }
}