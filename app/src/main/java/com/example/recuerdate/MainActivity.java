package com.example.recuerdate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recuerdate.activities.BaseActivity;
import com.example.recuerdate.activities.SignInActivity;
import com.example.recuerdate.activities.TokenActivity;
import com.example.recuerdate.dashboard.Dashboard;
import com.example.recuerdate.dashboard.DashboardTutor;
import com.example.recuerdate.databinding.ActivityMainBinding;
import com.example.recuerdate.utilities.Constants;
import com.example.recuerdate.utilities.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    ActionBarDrawerToggle drawerToggle;

    NavigationView navigationView;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView texViewNomHeader = headerView.findViewById(R.id.texViewNomHeader);

        String userName = preferenceManager.getString(Constants.KEY_NAME);
        texViewNomHeader.setText(userName);

        loadUserDetails();
        // Configuración del ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.perfilmenu:
                        obrirPerfil();
                        return true;
                    case R.id.sesiomenu:
                        logout();
                        return true;
                }
                return true;
            }
        });

        // Configuración del BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new Dashboard());
                    binding.bottomNavigationView.setVisibility(View.GONE); // Oculta el BottomNavigationView
                    break;
                case R.id.chat:
                    Intent intent = new Intent(MainActivity.this, TokenActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mapa:
                    replaceFragment(new LocalitzacioFragment());
                    break;
            }
            return true;
        });

        // Asegúrate de que el InfoFragment esté abierto al inicio
        replaceFragment(new Dashboard());
        binding.bottomNavigationView.setVisibility(View.GONE);
    }
    private void loadUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView imageLogoHeader = headerView.findViewById(R.id.imageLogoHeader);
        imageLogoHeader.setImageBitmap(bitmap);
    }
    public void logout() {
        // Muestra un diálogo de confirmación antes de cerrar la sesión
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.confirm_logout))
                .setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cierra la sesión y quita el token de FCM
                        signOut();
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

    private void signOut() {
        showToast("Signing out...");
        removeTokenFromCollection(Constants.KEY_COLLECTION_USERS);
        removeTokenFromCollection(Constants.KEY_COLLECTION_RELATIVES);
    }


    private void obrirPerfil() {
        Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void removeTokenFromCollection(String collection) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(collection)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out from " + collection));
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        if (!(fragment instanceof Dashboard)) {
            binding.bottomNavigationView.setVisibility(View.VISIBLE); // Muestra el BottomNavigationView
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
        binding.drawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public void onBackPressed() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        String role = preferenceManager.getString(Constants.KEY_ROLE);
        if (role.equals("Tutor")) {
            replaceFragment(new DashboardTutor());
        } else {
            replaceFragment(new Dashboard());
        }
    }
}

