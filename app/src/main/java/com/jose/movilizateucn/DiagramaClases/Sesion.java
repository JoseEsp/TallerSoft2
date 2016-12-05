package com.jose.movilizateucn.DiagramaClases;

import android.app.Activity;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;

/**
 * Simula la 'sesion' de PHP
 * Solo un usuario por sesión
 */

public class Sesion {
    private static Usuario usuario;
    private static double calificacionPasajero;
    private static boolean stopUpdate;

    public static void setUsuario(Usuario user){
        usuario = user;
    }

    public static boolean exists(){
        return usuario != null;
    }

    public static Usuario getUser(){
        return usuario;
    }

    public static void deleteUser(){
        usuario = null;
    }

    public static void stopUpdate(){
        stopUpdate = true;
    }

    public static void updateFechaFinConexion(final Activity activity, final String tipo){
        final int ms_time = 1000; //1 segundo
        final Handler handler = new Handler();
        stopUpdate = false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (Sesion.exists()) {
                    String url;
                    if (tipo.equals("chofer")) {
                        url = Url.UPDATEFECHAFIN + "?tipo=chofer&rut=" + usuario.getRut();
                    } else if (tipo.equals("pasajero")) {
                        url = Url.UPDATEFECHAFIN + "?tipo=pasajero&rut=" + usuario.getRut();
                    } else {
                        return;
                    }
                    VolleySingleton.getInstance(activity).addToRequestQueue(
                            new JsonObjectRequest(Request.Method.GET, url, null, null));
                    if (!stopUpdate) {
                        handler.postDelayed(this, ms_time);
                    }
                }
            }
        };
        handler.postDelayed(runnable, ms_time);
    }

    public static double getCalificacionPasajero(){return calificacionPasajero;}

    public static void setCalificacionPasajero(double cal){calificacionPasajero = cal;}

}
