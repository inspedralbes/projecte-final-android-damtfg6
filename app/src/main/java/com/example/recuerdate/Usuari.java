package com.example.recuerdate;

public class Usuari {
    private int user_id;
    private String nomCognoms;
    private String dni;
    private int telefon;
    private String correu;

    private String contrasenya;
    private int usuari_identificador;

    public Usuari(int user_id, String nomCognoms, String dni, int telefon, String correu, String contrasenya, int usuari_identificador) {
        this.user_id = user_id;
        this.nomCognoms = nomCognoms;
        this.dni = dni;
        this.telefon = telefon;
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.usuari_identificador = usuari_identificador;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
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

    public int getUsuariIdentificador() {
        return usuari_identificador;
    }

    public void setUsuariIdentificador(int usuari_identificador) {
        this.usuari_identificador = usuari_identificador;
    }

    @Override
    public String toString() {
        return "Usuari{" +
                "user_id=" + user_id +
                ", nomCognoms='" + nomCognoms + '\'' +
                ", dni='" + dni + '\'' +
                ", telefon=" + telefon +
                ", correu='" + correu + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", usuari_identificador=" + usuari_identificador +
                '}';
    }
}
