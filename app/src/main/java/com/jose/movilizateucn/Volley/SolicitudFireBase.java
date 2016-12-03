package com.jose.movilizateucn.Volley;

public class SolicitudFireBase {
    private String codEstado;
    private String fechaSalida;
    private String lat;
    private String lon;
    private String rut;

    public SolicitudFireBase(String codEstado, String fechaSalida, String lat, String lon, String rut) {
        this.codEstado = codEstado;
        this.fechaSalida = fechaSalida;
        this.lat = lat;
        this.lon = lon;
        this.rut = rut;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
}
