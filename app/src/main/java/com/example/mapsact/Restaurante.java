package com.example.mapsact;


public class Restaurante {
    String estabelecimento;
    Double latitude;
    Double longitude;
    String tipo;
    Double ID;
    Boolean havelactoseopt;
    Boolean haveglutenopt;


    public Restaurante(String estabelecimento, Double latitude, Double longitude, String tipo, Double ID, Boolean havelactoseopt, Boolean haveglutenopt) {
        this.estabelecimento = estabelecimento;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipo = tipo;
        this.ID = ID;
        this.havelactoseopt = havelactoseopt;
        this.haveglutenopt = haveglutenopt;
    }

    Restaurante() {
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getID() {
        return ID;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public Boolean getHavelactoseopt() {
        return havelactoseopt;
    }

    public void setHavelactoseopt(Boolean havelactoseopt) {
        this.havelactoseopt = havelactoseopt;
    }

    public Boolean getHaveglutenopt() {
        return haveglutenopt;
    }

    public void setHaveglutenopt(Boolean haveglutenopt) {
        this.haveglutenopt = haveglutenopt;
    }
}
