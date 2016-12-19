package com.jose.movilizateucn.Volley;

import com.google.firebase.database.DataSnapshot;

public class SolicitudFireBase {
    private String codSolicitud;
    private String nombre;
    private String codEstado;
    private String fechaSalida;
    private String lat;
    private String lon;
    private String calificacion;

    public SolicitudFireBase(String codSolicitud, String nombre, String calificacion, String codEstado, String fechaSalida, String lat, String lon) {
        this.codSolicitud = codSolicitud;
        this.nombre = nombre;
        this.calificacion = calificacion;
        this.codEstado = codEstado;
        this.fechaSalida = fechaSalida;
        this.lat = lat;
        this.lon = lon;
    }

    public SolicitudFireBase(DataSnapshot dataSnapshot, String rut){
        this(
                dataSnapshot.child("codSolicitud").getValue(String.class),
                dataSnapshot.child("nombre").getValue(String.class),
                dataSnapshot.child("calificacion").getValue(String.class),
                dataSnapshot.child("codEstado").getValue(String.class),
                dataSnapshot.child("fechaSalida").getValue(String.class),
                dataSnapshot.child("lat").getValue(String.class),
                dataSnapshot.child("lon").getValue(String.class)
        );
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(String codSolicitud) {
        this.codSolicitud = codSolicitud;
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

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }
}
