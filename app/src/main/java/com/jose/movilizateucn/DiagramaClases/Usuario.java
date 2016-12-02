package com.jose.movilizateucn.DiagramaClases;

import org.json.JSONException;
import org.json.JSONObject;

public class Usuario {
    private String rut;
    private String nombre;
    private String email;
    private String contra;
    private String enableS_N;
    private String fechaInicioConexion;

    public Usuario(String rut, String nombre, String email, String contra, String enableS_N, String fechaInicioConexion){
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.contra = contra;
        this.enableS_N = enableS_N;
        this.fechaInicioConexion = fechaInicioConexion;
    }

    public static Usuario JSonObject_to_Usuario(JSONObject response){
        if (response != null){
            try {
                return new Usuario(response.getString("rut"), response.getString("nombre"),
                        response.getString("email"), response.getString("contra"),
                        response.getString("enableS_N"), response.getString("fechaInicioConexion"));
            }catch(JSONException e){
                return null;
            }
        }else{
            return null;
        }
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

    public String getEnableS_N() {
        return enableS_N;
    }

    public void setEnableS_N(String enableS_N) {
        this.enableS_N = enableS_N;
    }

    public String getFechaInicioConexion() {
        return fechaInicioConexion;
    }

    public void setFechaInicioConexion(String fechaInicioConexion) {
        this.fechaInicioConexion = fechaInicioConexion;
    }
}
