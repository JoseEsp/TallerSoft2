package com.jose.movilizateucn.DiagramaClases;

public class Chofer extends Usuario{
    public Chofer(String rut, String nombre, String email, String contra, String enableS_N, String fechaInicioConexion){
        super(rut, nombre, email, contra, enableS_N, fechaInicioConexion);
    }

    public static Chofer usuario_to_Chofer(Usuario user) {
        if (user != null) {
            return new Chofer(user.getRut(), user.getNombre(), user.getEmail(), user.getContra(), user.getEnableS_N(), user.getFechaInicioConexion());
        }else{
            return null;
        }
    }
}
