package com.example.recuerdate;

public class RespostaLogin {

    private boolean autoritzacio;
    private String rol;
    private Usuari userData; // Agregar campo para los datos del usuario

    public RespostaLogin(boolean autoritzacio, String rol, Usuari userData) {
        this.autoritzacio = autoritzacio;
        this.rol = rol;
        this.userData = userData;
    }

    public boolean isAutoritzacio() {
        return autoritzacio;
    }

    public void setAutoritzacio(boolean autoritzacio) {
        this.autoritzacio = autoritzacio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Usuari getUserData() {
        return userData;
    }

    public void setUserData(Usuari userData) {
        this.userData = userData;
    }

    @Override
    public String toString() {
        return "RespostaLogin{" +
                "autoritzacio=" + autoritzacio +
                ", rol='" + rol + '\'' +
                ", userData=" + userData +
                '}';
    }
}