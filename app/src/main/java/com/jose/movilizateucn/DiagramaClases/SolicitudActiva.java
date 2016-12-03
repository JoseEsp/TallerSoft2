package com.jose.movilizateucn.DiagramaClases;

//Solicitud recogida por el Chofer

public class SolicitudActiva {
    private Solicitud solicitud;
    private float calificacion;

    public SolicitudActiva(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.calificacion = 0.0f;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }
}
