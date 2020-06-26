package com.example.mapsact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pratos {
    String prato;
    ArrayList<String> ingredientes;
    Boolean havelactose;
    Boolean havegluten;
    Double ID;

    public Pratos() {
    }

    public Pratos(String prato, ArrayList<String> ingredientes, Boolean havelactose, Boolean havegluten, Double ID) {
        this.prato = prato;
        this.ingredientes = ingredientes;
        this.havelactose = havelactose;
        this.havegluten = havegluten;
        this.ID = ID;
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

    public Double getID() {
        return ID;
    }

    public void setID(Double ID) {
        this.ID = ID;
    }
}
