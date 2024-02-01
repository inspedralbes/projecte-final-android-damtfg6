package com.example.recuerdate;

import com.google.gson.annotations.SerializedName;

public class TutorTrobat {
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

    @SerializedName("usuari_identificador")
    private int usuari_identificador;



    public TutorTrobat(String dni, String contrasenya) {
        this.dni = dni;
        this.contrasenya = contrasenya;
    }

    public TutorTrobat(String nomCognoms, String dni, int telefon, String correu, String contrasenya, int usuari_identificador) {
        this.nomCognoms = nomCognoms;
        this.dni = dni;
        this.telefon = telefon;
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.usuari_identificador = usuari_identificador;
    }
}
