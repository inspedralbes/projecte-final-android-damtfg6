package com.example.recuerdate.familiars;

import android.widget.ImageView;

public class Model {

    ImageView imatge;
    String nomUsuari, numeroTlf;

    public Model(ImageView imatge, String nomUsuari, String numeroTlf) {
        this.imatge = imatge;
        this.nomUsuari = nomUsuari;
        this.numeroTlf = numeroTlf;
    }

    public ImageView getImatge() {
        return imatge;
    }

    public void setImatge(ImageView imatge) {
        this.imatge = imatge;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getNumeroTlf() {
        return numeroTlf;
    }

    public void setNumeroTlf(String numeroTlf) {
        this.numeroTlf = numeroTlf;
    }
}
