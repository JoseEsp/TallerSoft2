package com.jose.movilizateucn.Consultas;

import android.util.Log;
import android.widget.Toast;

import com.jose.movilizateucn.DiagramaClases.*;

import java.net.HttpURLConnection;
import java.net.URL;

//Todas las consultas.
public class Consulta {

    //La Ip a conectarse
    private static final String IP = "http://movilizate.webcindario.com";

    /**
     * Inserta un usuario en la base de datos
     * Para actualizar la fecha de inicio de conexión, llámese la función
     * 'actualizarUsuario(Usuario usuario)'
     * @param usuario Usuario de la aplicación
     */
    public static void insertarUsuario(Usuario usuario){
        String strUrl = String.format(
                "%s/consultas/insertarUsuario.php?rut=%s&nombre=%s&contra=%s&email=%s",
                IP, usuario.getRut(), usuario.getNombre(), usuario.getContra(), usuario.getEmail()
        );
        Log.d("url: ", strUrl);
        Consulta.ejecutarUrl(strUrl);
    }



    /**
     * Ejecuta la url dada el strUrl
     * @param strUrl La dirección web
     */
    private static void ejecutarUrl(String strUrl){
        try {

            URL url = new URL(strUrl);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
