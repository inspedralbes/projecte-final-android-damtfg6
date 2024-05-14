package com.example.recuerdate.jocMemoria.model;

public class GameModel {
    private int score;
    private String nomRonda1; // Nueva variable
    private int acertadesRnd1; // Nueva variable
    private int falladesRnd1; // Nueva variable
    private String nomRonda2; // Nueva variable
    private int acertadesRnd2; // Nueva variable
    private int falladesRnd2; // Nueva variable
    private int totalScore; // Nueva variable

    public GameModel() {
        score = 0;
    }

    public GameModel(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    // Nuevos métodos
    public String getNomRonda1() {
        return nomRonda1;
    }

    public void setNomRonda1(String nomRonda1) {
        this.nomRonda1 = nomRonda1;
    }

    public int getAcertadesRnd1() {
        return acertadesRnd1;
    }

    public void setAcertadesRnd1(int acertadesRnd1) {
        this.acertadesRnd1 = acertadesRnd1;
    }

    public int getFalladesRnd1() {
        return falladesRnd1;
    }

    public void setFalladesRnd1(int falladesRnd1) {
        this.falladesRnd1 = falladesRnd1;
    }

    public String getNomRonda2() {
        return nomRonda2;
    }

    public void setNomRonda2(String nomRonda2) {
        this.nomRonda2 = nomRonda2;
    }

    public int getAcertadesRnd2() {
        return acertadesRnd2;
    }

    public void setAcertadesRnd2(int acertadesRnd2) {
        this.acertadesRnd2 = acertadesRnd2;
    }

    public int getFalladesRnd2() {
        return falladesRnd2;
    }

    public void setFalladesRnd2(int falladesRnd2) {
        this.falladesRnd2 = falladesRnd2;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
