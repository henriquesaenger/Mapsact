package com.example.mapsact;

import java.util.ArrayList;

public class Pratos {
    String prato;
    ArrayList<String> ingredientes;
    Boolean havelactose;
    Boolean havegluten;
    String ID;
    Double IDR;

    public Pratos() {
    }

    public Pratos(String prato, ArrayList<String> ingredientes, Boolean havelactose, Boolean havegluten, String ID, Double IDR) {
        this.prato = prato;
        this.ingredientes = ingredientes;
        this.havelactose = havelactose;
        this.havegluten = havegluten;
        this.ID = ID;
        this.IDR = IDR;
    }

    public String getPrato() {
        return prato;
    }

    public void setPrato(String prato) {
        this.prato = prato;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Boolean getHavelactose() {
        return havelactose;
    }

    public void setHavelactose(Boolean havelactose) {
        this.havelactose = havelactose;
    }

    public Boolean getHavegluten() {
        return havegluten;
    }

    public void setHavegluten(Boolean havegluten) {
        this.havegluten = havegluten;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Double getIDR() {
        return IDR;
    }

    public void setIDR(Double IDR) {
        this.IDR = IDR;
    }
}
