package com.jose.movilizateucn.Consultas;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jose.movilizateucn.DiagramaClases.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//Todas las consultas.
public class Consulta {

    /**
     * Inserta un usuario en la base de datos
     * @param usuario Usuario de la aplicación
     */
    public static void insertarUsuario(Usuario usuario, final AppCompatActivity activity){
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("rut", usuario.getRut());
        map.put("nombre", usuario.getNombre());
        map.put("contra", usuario.getContra());
        map.put("email", usuario.getEmail());

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        ConsultasGenerales.insertarDatos(Constantes.INSERTUSUARIO,
                                         activity,
                                         jobject,
                                         "Rut ya existe.");
    }

    /**
     * Obtiene un Usuario del sistema dado un rut.
     * El usuario queda almacenado como u JSON en ConsultasGenerales
     */
    public static void obtenerUsuario(String rut, final AppCompatActivity activity){
        String url = Constantes.GETUSUARIOBYRUT + "?rut=" + rut;
        ConsultasGenerales.obtenerDato(url, activity);
    }

    /**
     * Obtiene un Chofer del sistema dado un rut.
     * El usuario queda almacenado como u JSON en ConsultasGenerales
     */
    public static void obtenerChofer(String rut, final AppCompatActivity activity){
        String url = Constantes.GETCHOFERBYRUT+ "?rut=" + rut;
        ConsultasGenerales.obtenerDato(url, activity);
    }

    /**
     * Obtiene un Pasajero del sistema dado un rut.
     * El usuario queda almacenado como u JSON en ConsultasGenerales
     */
    public static void obtenerPasajero(String rut, final AppCompatActivity activity){
        String url = Constantes.GETPASAJEROBYRUT + "?rut=" + rut;
        ConsultasGenerales.obtenerDato(url, activity);
    }

    /**
     * Obtiene un Usuario del sistema dado un rut y contraseña.
     * Si el rut no está en el sistema, entonces retorna null.
     * @return Usuario en el sistema.
     */
    public static void obtenerUsuarioLogin(String rut, String contra, final AppCompatActivity activity){
        Toast.makeText(
                activity,
                "Conectando...",
                Toast.LENGTH_LONG).show();
        String url = Constantes.GETLOGIN + "?rut=" + rut + "&contra=" + contra;
        ConsultasGenerales.obtenerDato(url, activity);
    }

    /**
     * Actualiza la fecha de inicio de conexión del usuario
     * @param usuario
     * @param activity Activity actual
     */
    public static void actualizarFechaInicioConexion(Usuario usuario, final AppCompatActivity activity){
        String url = Constantes.UPDATEFECHAINICIO + "?rut=" + usuario.getRut();
        ConsultasGenerales.actualizarFecha(usuario, url, activity);
    }

    public static void actualizarFechaFinConexion(Usuario usuario, final AppCompatActivity activity){
        String url;
        if (usuario instanceof Chofer) {
            url = Constantes.UPDATEFECHAFIN + "?tipo=chofer&rut=" + usuario.getRut();
        }else if (usuario instanceof Pasajero){
            url = Constantes.UPDATEFECHAFIN + "?tipo=pasajero&rut=" + usuario.getRut();
        }else{
            return;
        }
        ConsultasGenerales.actualizarFecha(usuario, url, activity);
    }


}
