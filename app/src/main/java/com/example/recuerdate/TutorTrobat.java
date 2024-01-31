package com.example.recuerdate;

import com.google.gson.annotations.SerializedName;

public class TutorTrobat {
    @SerializedName("id_usuari")
    private int id_usuari;
    @SerializedName("nomCogonoms")
    private String nomCogonoms;

    @SerializedName("dni")
    private String dni;

    @SerializedName("telefon")
    private int telefon;
    @SerializedName("correu")
    private String correu;
    @SerializedName("contrasenya")
    private String contrasenya;

    @SerializedName("identificadorUsuari")
    private int identificadorUsuari;



    public TutorTrobat(String dni, String contrasenya) {
        this.dni = dni;
        this.contrasenya = contrasenya;
    }

    public TutorTrobat(String nomCogonoms, String dni, int telefon, String correu, String contrasenya, int identificadorUsuari) {
        this.nomCogonoms = nomCogonoms;
        this.dni = dni;
        this.telefon = telefon;
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.identificadorUsuari = identificadorUsuari;
    }
}
