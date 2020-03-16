package com.example.mapsact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pratos {
    String prato;
    HashMap<String, Integer> ingredientes= new HashMap<>();
    Boolean havelactose;
    Boolean havegluten;
    String nomerestaurante;


    public Pratos(String prato, String[] ingredientes, Boolean havelactose, Boolean havegluten, String nomerestaurante){
        this.prato=prato;
        this.havegluten=havegluten;
        this.havelactose=havelactose;
        this.nomerestaurante=nomerestaurante;

        for(String c: ingredientes){
            this.ingredientes.put(c,0);
        }
    }

}
