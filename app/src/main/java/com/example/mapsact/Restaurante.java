package com.example.mapsact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurante implements Serializable {
    String nome;
    double latitude;
    double longitude;
    String Tipo;
    ArrayList<Pratos> menurestaur= new ArrayList<>();
    Boolean havelactose;
    Boolean havegluten;


    public Restaurante(String nome, double latitude, double longitude, String tipo){
        this.nome= nome;
        this.latitude=latitude;
        this.longitude=longitude;
        this.Tipo=tipo;
    }

    public void addPrato(String prato, String [] ingredientes,Boolean havelactose, Boolean havegluten){
        this.menurestaur.add(new Pratos(prato, ingredientes, havelactose, havegluten, nome));
    }



}
