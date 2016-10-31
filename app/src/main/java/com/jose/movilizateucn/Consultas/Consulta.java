package com.jose.movilizateucn.Consultas;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        ConsultasGenerales.insertarDatos(Constantes.INSERTUSUARIO, activity, jobject);
    }

    /**
     * Obtiene un Usuario del sistema dado un rut.
     * Si el rut no está en el sistema, entonces retorna null.
     * @return Usuario en el sistema.
     */
    public static Usuario obtenerUsuario(String rut, final AppCompatActivity activity){
        String url = Constantes.GETUSUARIOBYRUT + "?rut=" + rut;
        ConsultasGenerales.obtenerDato(url, activity);
        Usuario usuario = null;
        //Objeto almacenado luego de una obtención.
        JSONObject objeto = ConsultasGenerales.getJSONObject();
        if (objeto != null) {
            try {
                usuario = new Usuario(objeto.getString("rut"),
                                      objeto.getString("nombre"),
                                      objeto.getString("email"),
                                      objeto.getString("contra"));
            }catch(JSONException e){
                Log.e("ERROR:", "Obtencion de registro Usuario");
            }
        }
        return usuario;
    }


}
