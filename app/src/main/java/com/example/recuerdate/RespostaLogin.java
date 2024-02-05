package com.example.recuerdate;

public class RespostaLogin {

    private boolean autoritzacio;
    private String rol; // Agregamos el campo para el rol

    public RespostaLogin(boolean autoritzacio, String rol) {
        this.autoritzacio = autoritzacio;
        this.rol = rol;
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

    @Override
    public String toString() {
        return "RespostaLogin{" +
                "autoritzacio=" + autoritzacio +
                ", rol='" + rol + '\'' +
                '}';
    }
}
