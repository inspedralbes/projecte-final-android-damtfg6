package com.example.recuerdate;


public class Mensaje {
    private String nomCognoms;
    private String message;

    public Mensaje(String nomCognoms, String message) {
        this.nomCognoms = nomCognoms;
        this.message = message;
    }

    public String getNomCognoms() {
        return nomCognoms;
    }

    public void setNomCognoms(String nomCognoms) {
        this.nomCognoms = nomCognoms;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
