package com.jose.movilizateucn.DiagramaClases;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jose.movilizateucn.ChoferActivity;
import com.jose.movilizateucn.Consultas.Consulta;
import com.jose.movilizateucn.Consultas.ConsultasGenerales;
import com.jose.movilizateucn.EscogerPerfilActivity;
import com.jose.movilizateucn.PasajeroActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Un login para todos (para saber de conexiones)
 */

public class Login {
    //Usuario del login, inicialmente no hay nada
    private static Usuario usuario = null;

    //Como la conexión a la página no es tan rápida. Hay 3 estados
    //NADA: No hay usuario logueado. (o Fallo)
    //ESPERANDO: Está esperando respuesta.
    //EXITO: Se logró conectar usuario.
    public static enum ESTADO{NADA, ESPERANDO, EXITO};

    //Actualmente no hay nadie conectado.
    private static ESTADO estadoActual = ESTADO.NADA;
    //Tiempo de conexión (cuando presiona botón para loguearse)
    private static int actualTime = 0;

    //Variable auxiliar para que evite actualizarse la fecha Fin de Conexión
    private static boolean stopFinConexion = true;


    /**
     * Se loguea
     * Intenta checkarlo cada 100 ms (ya que la respuesta no es intanstánea
     *
     */
    public static void conectarse(String rut, String contra, final AppCompatActivity activity){
        final Handler handler = new Handler();
        final int ms_time = 100;
        final int max_ms_time = 3000; //queda esperando máximo 3 seg.
        actualTime = 0;
        //Ahora queda esperando.
        estadoActual = ESTADO.ESPERANDO;
        Consulta.obtenerUsuarioLogin(rut, contra, activity);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Intenta obtener el objeto usuario
                JSONObject userObject = ConsultasGenerales.getJSONObject();
                //Si recibió al usuario.
                if (userObject != null && estadoActual == ESTADO.ESPERANDO){
                    try {
                        usuario = new Usuario(userObject.getString("rut"),
                                userObject.getString("nombre"),
                                userObject.getString("email"),
                                userObject.getString("contra"));
                        //Actualiza inicio conexión
                        Consulta.actualizarFechaInicioConexion(usuario, activity);
                    }catch(JSONException e){
                        Log.e("ERROR:", "Obtencion de registro Usuario");
                    }
                    estadoActual = ESTADO.EXITO;
                    actualTime = 0;
                    //Cambia al activity del perfil
                    final Intent perfilActivity = new Intent(activity, EscogerPerfilActivity.class);
                    activity.startActivity(perfilActivity);
                }else if (actualTime >= max_ms_time && estadoActual == ESTADO.ESPERANDO){ //Si sobrepasó los 3 segundos.
                    estadoActual = ESTADO.NADA;
                    usuario = null;
                    Toast.makeText(
                            activity,
                            "Logueo Fallido",
                            Toast.LENGTH_LONG).show();
                }else{ //Si no pasa vuelve a checkear
                    actualTime += ms_time;
                    handler.postDelayed(this, ms_time);
                }
            }
        };
        handler.postDelayed(runnable, ms_time);
    }

    //Convierte al usuario en un chofer (Cuando presiona el botón chofer)
    public static void transformToChofer(final AppCompatActivity activity){
        final Handler handler = new Handler();
        final int ms_time = 100;
        final int max_ms_time = 1000; //queda esperando máximo 1 seg.
        actualTime = 0;
        //Ahora queda esperando.
        if (usuario == null){
            return;
        }
        Consulta.obtenerChofer(usuario.getRut(), activity);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Intenta obtener el objeto usuario
                JSONObject choferObject = ConsultasGenerales.getJSONObject();
                //Si recibió al usuario.
                if (choferObject != null){
                    try {
                        usuario = new Chofer(choferObject.getString("rut"),
                                choferObject.getString("nombre"),
                                choferObject.getString("email"),
                                choferObject.getString("contra"),
                                Integer.parseInt(choferObject.getString("cantAsientos")),
                                choferObject.getString("colorAuto"));
                    }catch(JSONException e){
                        Log.e("ERROR:", "Obtencion de registro Chofer");
                    }
                    actualTime = 0;
                    //Cambia al activity del perfil
                    final Intent choferActivity = new Intent(activity, ChoferActivity.class);
                    activity.startActivity(choferActivity);
                }else if (actualTime >= max_ms_time ){
                    Toast.makeText(
                            activity,
                            "Error al ser Chofer.",
                            Toast.LENGTH_LONG).show();
                }else{ //Si no pasa vuelve a checkear
                    actualTime += ms_time;
                    handler.postDelayed(this, ms_time);
                }
            }
        };
        handler.postDelayed(runnable, ms_time);
    }

    //Convierte al usuario en un pasajero
    public static void transformToPasajero(final AppCompatActivity activity){
        final Handler handler = new Handler();
        final int ms_time = 100;
        final int max_ms_time = 1000; //queda esperando máximo 1 seg.
        actualTime = 0;
        //Ahora queda esperando.
        if (usuario == null){
            return;
        }
        Consulta.obtenerPasajero(usuario.getRut(), activity);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Intenta obtener el objeto usuario
                JSONObject pasajeroObject = ConsultasGenerales.getJSONObject();
                //Si recibió al usuario.
                if (pasajeroObject != null){
                    try {
                        usuario = new Pasajero(pasajeroObject.getString("rut"),
                                pasajeroObject.getString("nombre"),
                                pasajeroObject.getString("email"),
                                pasajeroObject.getString("contra"));
                    }catch(JSONException e){
                        Log.e("ERROR:", "Obtencion de registro Pasajero");
                    }
                    actualTime = 0;
                    //Cambia al activity del perfil
                    final Intent pasajeroActivity = new Intent(activity, PasajeroActivity.class);
                    activity.startActivity(pasajeroActivity);
                }else if (actualTime >= max_ms_time ){
                    Toast.makeText(
                            activity,
                            "Error al ser Pasajero.",
                            Toast.LENGTH_LONG).show();
                }else{ //Si no pasa vuelve a checkear
                    actualTime += ms_time;
                    handler.postDelayed(this, ms_time);
                }
            }
        };
        handler.postDelayed(runnable, ms_time);
    }

    //Actualiza la fecha de fin de conexión cada 1 minuto
    public static void updateFechaFinConexion(final AppCompatActivity activity){
        final int ms_time = 60000;
        final Handler handler = new Handler();
        stopFinConexion = false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (usuario != null && !stopFinConexion){
                    Consulta.actualizarFechaFinConexion(usuario, activity);
                }
                if (!stopFinConexion){
                    handler.postDelayed(this, ms_time);
                }
            }
        };
        handler.postDelayed(runnable, 100);
    }

    public static void desconectarse(){
        estadoActual = ESTADO.NADA;
        usuario = null;
        stopFinConexion = true;
    }

    public static ESTADO getEstadoActual(){
        return estadoActual;
    }

    public static Usuario getUsuario(){
        return usuario;
    }
}
