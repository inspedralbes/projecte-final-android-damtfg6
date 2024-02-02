package com.example.recuerdate;

import com.google.gson.annotations.SerializedName;

public class UsuariLocalitzat {
    @SerializedName("id_usuari")
    private int id_usuari;

    @SerializedName("nomCognoms")
    private String nomCognoms;

    @SerializedName("dni")
    private String dni;

    @SerializedName("telefon")
    private int telefon;
    @SerializedName("correu")
    private String correu;
    @SerializedName("contrasenya")
    private String contrasenya;



    public UsuariLocalitzat(String dni, String contrasenya) {
        this.dni = dni;
        this.contrasenya = contrasenya;
    }

    public int getId_usuari() {
        return id_usuari;
    }

    public void setId_usuari(int id_usuari) {
        this.id_usuari = id_usuari;
    }

    public String getNomCognoms() {
        return nomCognoms;
    }

    public void setNomCognoms(String nomCognoms) {
        this.nomCognoms = nomCognoms;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getTelefon() {
        return telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }

    public String getCorreu() {
        return correu;
    }

    public void setCorreu(String correu) {
        this.correu = correu;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public UsuariLocalitzat(String nomCognoms, String dni, int telefon, String correu, String contrasenya) {
        this.nomCognoms = nomCognoms;
        this.dni = dni;
        this.telefon = telefon;
        this.correu = correu;
        this.contrasenya = contrasenya;

    }
}