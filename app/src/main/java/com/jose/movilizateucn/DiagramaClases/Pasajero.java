package com.jose.movilizateucn.DiagramaClases;

public class Pasajero extends Usuario {

    public Pasajero(String rut, String nombre, String email, String contra, String enableS_N, String fechaInicioConexion){
        super(rut, nombre, email, contra, enableS_N, fechaInicioConexion);
    }

    //Útil para la solicitud activa:
    public Pasajero(Usuario u){
        this(u.getRut(), u.getNombre(), u.getEmail(), u.getContra(), u.getEnableS_N(), u.getFechaInicioConexion());
    }

}
