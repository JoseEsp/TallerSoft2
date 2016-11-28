package com.jose.movilizateucn.DiagramaClases;

public class Solicitud {
    private int codSolicitud;
    private String fechaSalida;
    private int estado;
    private double latitud;
    private double longitud;
    private Pasajero pasajero;

    public Solicitud(int codSolicitud, String fechaSalida, int estado, double latitud, double longitud) {
        this.codSolicitud = codSolicitud;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pasajero = null;
    }

    public int getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(int codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

}
