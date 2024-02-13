package com.example.recuerdate;

public class Chat {
    private int imageResource; // ID del recurso de la imagen
    private String name; // Nombre del chat
    private String time; // Hora del último mensaje

    public Chat(int imageResource, String name, String time) {
        this.imageResource = imageResource;
        this.name = name;
        this.time = time;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
