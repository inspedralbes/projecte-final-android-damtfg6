package com.example.recuerdate;

public class Situacio {

    private String situacio;

    public Situacio(String situacio) {
        this.situacio = situacio;
    }

    public String getSituacio() {
        return situacio;
    }

    public void setSituacio(String situacio) {
        this.situacio = situacio;
    }

    @Override
    public String toString() {
        return situacio;
    }
}
