package com.example.recuerdate;

public class RespostaLogin {

    private boolean autoritzacio;
    private String rol;
    private Usuari userData; // Campo para los datos del usuario
    private Usuari usuariTutoritzatData; // Campo para los datos del usuario tutorizado

    public RespostaLogin(boolean autoritzacio, String rol, Usuari userData, Usuari usuariTutoritzatData) {
        this.autoritzacio = autoritzacio;
        this.rol = rol;
        this.userData = userData;
        this.usuariTutoritzatData = usuariTutoritzatData;
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

    public Usuari getUsuariTutoritzatData() {
        return usuariTutoritzatData;
    }

    public void setUsuariTutoritzatData(Usuari usuariTutoritzatData) {
        this.usuariTutoritzatData = usuariTutoritzatData;
    }

    @Override
    public String toString() {
        return "RespostaLogin{" +
                "autoritzacio=" + autoritzacio +
                ", rol='" + rol + '\'' +
                ", userData=" + userData +
                ", usuariTutoritzatData=" + usuariTutoritzatData +
                '}';
    }
}
