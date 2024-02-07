package com.example.recuerdate;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManagment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY_DNI = "session_dni";
    String SESSION_KEY_ROL = "session_rol";
    String SESSION_KEY_USERDATA = "session_userData";

    public SessionManagment(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String dni, String rol, Usuari userData) {
        // Guardar sesión del usuario cuando el usuario inicia sesión
        Gson gson = new Gson();
        String userDataJson = gson.toJson(userData);

        editor.putString(SESSION_KEY_DNI, dni);
        editor.putString(SESSION_KEY_ROL, rol);
        editor.putString(SESSION_KEY_USERDATA, userDataJson);
        editor.apply();
    }

    public String getDni() {
        // Devuelve el DNI del usuario para el cual se guarda la sesión
        return sharedPreferences.getString(SESSION_KEY_DNI, null);
    }

    public String getRol() {
        // Devuelve el rol del usuario para el cual se guarda la sesión
        return sharedPreferences.getString(SESSION_KEY_ROL, null);
    }

    public Usuari getUserData() {
        // Devuelve los datos del usuario para el cual se guarda la sesión
        Gson gson = new Gson();
        String userDataJson = sharedPreferences.getString(SESSION_KEY_USERDATA, null);
        return gson.fromJson(userDataJson, Usuari.class);
    }

    public void removeSession() {
        // Elimina la sesión guardada al cerrar sesión
        editor.remove(SESSION_KEY_DNI);
        editor.remove(SESSION_KEY_ROL);
        editor.remove(SESSION_KEY_USERDATA);
        editor.apply();
    }
}


