package com.example.recuerdate;

public class Familiar {
    private String nombre;

    private int imagen;
    public Familiar(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }



    public int getImagen() {
        return imagen;
    }
}