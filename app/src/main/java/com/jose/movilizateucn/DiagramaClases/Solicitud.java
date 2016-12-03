package com.jose.movilizateucn.DiagramaClases;

import org.json.JSONException;
import org.json.JSONObject;

public class Solicitud {
    private int codSolicitud;
    private String fechaSalida;
    private int codEstado;
    private double latitud;
    private double longitud;
    private Pasajero pasajero;

    public Solicitud(int codSolicitud, String fechaSalida, int codEstado, double latitud, double longitud) {
        this.codSolicitud = codSolicitud;
        this.fechaSalida = fechaSalida;
        this.codEstado = codEstado;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pasajero = null;
    }

    public static Solicitud JSonObject_to_Solicitud(JSONObject response){
        if (response != null){
            Solicitud solicitud = null;
            try{
                solicitud = new Solicitud(
                        Integer.parseInt(response.getString("codSolicitud")),
                        response.getString("fechaSalida"),
                        Integer.parseInt(response.getString("codEstado")),
                        Double.parseDouble(response.getString("lat")),
                        Double.parseDouble(response.getString("lon")));
                solicitud.setPasajero(new Pasajero(response.getString("rut"), response.getString("nombre"), "", "", "", ""));
                return solicitud;
            }catch(JSONException e){
                return solicitud;
            }
        }else{
            return null;
        }
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

    public int getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(int codEstado) {
        this.codEstado = codEstado;
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
