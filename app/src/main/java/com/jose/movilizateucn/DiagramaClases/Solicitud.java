package com.jose.movilizateucn.DiagramaClases;

public class Solicitud {
    private int codSolicitud;
    private String fechaSalida;
    private String estado;
    private String latitud;
    private String longitud;
    private Pasajero pasajero;
    private Chofer chofer;

    public Solicitud(int codSolicitud, String fechaSalida, String estado, String latitud, String longitud) {
        this.codSolicitud = codSolicitud;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pasajero = null;
        this.chofer = null;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Chofer getChofer() {
        return chofer;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }
}
