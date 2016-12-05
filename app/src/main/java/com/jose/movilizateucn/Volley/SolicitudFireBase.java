package com.jose.movilizateucn.Volley;

public class SolicitudFireBase {
    private int codSolicitud;
    private String nombre;
    private int codEstado;
    private String fechaSalida;
    private double lat;
    private double lon;
    private double calificacion;
    private String token;

    public SolicitudFireBase(int codSolicitud, String nombre, double calificacion, int codEstado, String fechaSalida, double lat, double lon, String token) {
        this.codSolicitud = codSolicitud;
        this.nombre = nombre;
        this.calificacion = calificacion;
        this.codEstado = codEstado;
        this.fechaSalida = fechaSalida;
        this.lat = lat;
        this.lon = lon;
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(int codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public int getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(int codEstado) {
        this.codEstado = codEstado;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}
