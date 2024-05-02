package com.example.recuerdate;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManagment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String SESSION_KEY_DNI_USUARIO_VINCULADO = "session_dni_usuario_vinculado";
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY_DNI = "session_dni";
    String SESSION_KEY_ROL = "session_rol";
    String SESSION_KEY_USERDATA = "session_userData";
    String SESSION_KEY_USUARI_TUTORITZAT_DATA = "session_usuariTutoritzatData";

    public SessionManagment(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String dni, String rol, Usuari userData, Usuari usuariTutoritzatData) {
        // Guardar sesión del usuario cuando el usuario inicia sesión
        Gson gson = new Gson();
        String userDataJson = gson.toJson(userData);
        String usuariTutoritzatDataJson = gson.toJson(usuariTutoritzatData);

        editor.putString(SESSION_KEY_DNI, dni);
        editor.putString(SESSION_KEY_ROL, rol);
        editor.putString(SESSION_KEY_USERDATA, userDataJson);
        editor.putString(SESSION_KEY_USUARI_TUTORITZAT_DATA, usuariTutoritzatDataJson);
        editor.apply();
    }

    public String getDni() {
        return sharedPreferences.getString(SESSION_KEY_DNI, null);
    }

    public String getRol() {
        return sharedPreferences.getString(SESSION_KEY_ROL, null);
    }

    public Usuari getUserData() {
        Gson gson = new Gson();
        String userDataJson = sharedPreferences.getString(SESSION_KEY_USERDATA, null);
        return gson.fromJson(userDataJson, Usuari.class);
    }

    public Usuari getUsuariTutoritzatData() {
        Gson gson = new Gson();
        String usuariTutoritzatDataJson = sharedPreferences.getString(SESSION_KEY_USUARI_TUTORITZAT_DATA, null);
        return gson.fromJson(usuariTutoritzatDataJson, Usuari.class);
    }

    public void removeSession() {
        editor.remove(SESSION_KEY_DNI);
        editor.remove(SESSION_KEY_ROL);
        editor.remove(SESSION_KEY_USERDATA);
        editor.remove(SESSION_KEY_USUARI_TUTORITZAT_DATA);
        editor.apply();
    }

    public void setDniUsuarioVinculado(String dniUsuarioVinculado) {
        editor.putString(SESSION_KEY_DNI_USUARIO_VINCULADO, dniUsuarioVinculado);
        editor.apply();
    }

    public String getDniUsuarioVinculado() {
        return sharedPreferences.getString(SESSION_KEY_DNI_USUARIO_VINCULADO, null);
    }
}



