package com.example.recuerdate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recuerdate.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ActionBarDrawerToggle drawerToggle;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener el rol del usuario
        SessionManagment sessionManagment = new SessionManagment(this);
        String rol = sessionManagment.getRol();  // Asegúrate de tener un método en SessionManagment para obtener el rol.

        // Inflar el diseño correspondiente según el rol antes de configurar las vistas
        if ("tutor".equals(rol)) {
            setContentView(R.layout.activity_main_tutor);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setSupportActionBar(findViewById(R.id.toolbar));

            // Configuración del ActionBarDrawerToggle
            drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
            navigationView = findViewById(R.id.nav_view);
            binding.drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
            setSupportActionBar(findViewById(R.id.toolbar));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.perfilmenu:
                            return true;

                        case R.id.sesiomenu:
                            showLogoutConfirmationDialog();
                            return true;
                    }
                    return true;
                }
            });
            // Configurar las vistas específicas para el tutor usando findViewById si es necesario
        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
        }

        setSupportActionBar(findViewById(R.id.toolbar));

        // Configuración del ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.perfilmenu:
                        return true;

                    case R.id.sesiomenu:
                        showLogoutConfirmationDialog();
                        return true;
                }
                return true;
            }
        });

    // Configuración del BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
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

        // Asegúrate de que el InfoFragment esté abierto al inicio
        replaceFragment(new InfoFragment());
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
        SessionManagment sessionManagment = new SessionManagment(MainActivity.this);
        sessionManagment.removeSession();
        movetoLogin();
    }

    private void movetoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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

