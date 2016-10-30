package com.jose.movilizateucn.DiagramaClases;

import java.sql.Date;

public class Usuario {
    private String rut;
    private String nombre;
    private String email;
    private String contra;
    private Date fechaInicioConexion;

    public Usuario(String rut, String nombre, String email, String contra){
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.contra = contra;
        this.fechaInicioConexion = null;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public Date getFechaInicioConexion() {
        return fechaInicioConexion;
    }

    public void setFechaInicioConexion(Date fechaInicioConexion) {
        this.fechaInicioConexion = fechaInicioConexion;
    }
}
