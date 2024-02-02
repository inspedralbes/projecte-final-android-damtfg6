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

    public UsuariLocalitzat(String nomCognoms, String dni, int telefon, String correu, String contrasenya) {
        this.nomCognoms = nomCognoms;
        this.dni = dni;
        this.telefon = telefon;
        this.correu = correu;
        this.contrasenya = contrasenya;

    }
}