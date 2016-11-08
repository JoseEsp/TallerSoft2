package com.jose.movilizateucn.DiagramaClases;

public class Viaje {
    private int codViaje;
    private String fechaViaje;
    private int notaChofer;
    private String comChofer;
    private int notaPasajero;
    private String comPasajero;
    private Solicitud solicitud;

    public Viaje(int codViaje, String fechaViaje, int notaChofer, String comChofer, int notaPasajero, String comPasajero) {
        this.codViaje = codViaje;
        this.fechaViaje = fechaViaje;
        this.notaChofer = notaChofer;
        this.comChofer = comChofer;
        this.notaPasajero = notaPasajero;
        this.comPasajero = comPasajero;
        this.solicitud = null;
    }

    public int getCodViaje() {
        return codViaje;
    }

    public void setCodViaje(int codViaje) {
        this.codViaje = codViaje;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
    }

    public int getNotaChofer() {
        return notaChofer;
    }

    public void setNotaChofer(int notaChofer) {
        this.notaChofer = notaChofer;
    }

    public String getComChofer() {
        return comChofer;
    }

    public void setComChofer(String comChofer) {
        this.comChofer = comChofer;
    }

    public String getComPasajero() {
        return comPasajero;
    }

    public void setComPasajero(String comPasajero) {
        this.comPasajero = comPasajero;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public int getNotaPasajero() {
        return notaPasajero;
    }

    public void setNotaPasajero(int notaPasajero) {
        this.notaPasajero = notaPasajero;
    }
}
