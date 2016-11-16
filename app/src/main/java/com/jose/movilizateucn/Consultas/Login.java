package com.jose.movilizateucn.Consultas;

import android.app.ProgressDialog;
import android.content.Intent;
import java.text.NumberFormat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jose.movilizateucn.ChoferActivity;
import com.jose.movilizateucn.DiagramaClases.Calificacion;
import com.jose.movilizateucn.DiagramaClases.Chofer;
import com.jose.movilizateucn.DiagramaClases.Pasajero;
import com.jose.movilizateucn.DiagramaClases.Usuario;
import com.jose.movilizateucn.DiagramaClases.Viaje;
import com.jose.movilizateucn.EscogerPerfilActivity;
import com.jose.movilizateucn.HistorialViajesActivity;
import com.jose.movilizateucn.PasajeroActivity;
import com.jose.movilizateucn.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

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
        final int max_ms_time = 1500; //queda esperando máximo 1,5 seg.
        actualTime = 0;
        //Ahora queda esperando.
        estadoActual = ESTADO.ESPERANDO;
        Consulta.obtenerUsuarioLogin(rut, contra, activity);
        //Muestra el loading:
        final ProgressBar spinner = (ProgressBar) activity.findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        //No funciona en algunos celus en el xml, por eso está acá en código:
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        //final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        //mProgressDialog.setMessage("Conectando...");
        //mProgressDialog.setCancelable(false);
        //mProgressDialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Intenta obtener el objeto usuario
                JSONObject userObject = ConsultasGenerales.getJSONObject();
                //Si recibió al usuario.
                if (userObject != null && estadoActual == ESTADO.ESPERANDO){
                    try {
                        if (userObject.getString("enableS_N").equals("S")) {
                            usuario = new Usuario(userObject.getString("rut"),
                                    userObject.getString("nombre"),
                                    userObject.getString("email"),
                                    userObject.getString("contra"));
                            //Actualiza inicio conexión
                            Consulta.actualizarFechaInicioConexion(usuario, activity);
                            estadoActual = ESTADO.EXITO;
                            //Cambia al activity del perfil
                            final Intent perfilActivity = new Intent(activity, EscogerPerfilActivity.class);
                            activity.startActivity(perfilActivity);
                        }else{
                            Toast.makeText(activity, "Usuario bloqueado", Toast.LENGTH_SHORT).show();
                        }
                        //Deja de mostrar el mensaje de cargando.
                        //mProgressDialog.cancel();
                        spinner.setVisibility(View.GONE);
                    }catch(JSONException e){
                        Log.e("ERROR:", "Obtencion de registro Usuario");
                    }
                }else if (actualTime >= max_ms_time && estadoActual == ESTADO.ESPERANDO){
                    //mProgressDialog.cancel();
                    spinner.setVisibility(View.GONE);
                    estadoActual = ESTADO.NADA;
                    usuario = null;
                    if (ConsultasGenerales.isNetworkAvailable(activity)){
                        Toast.makeText(activity, "Datos inválidos.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
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
        final int ms_time = 10;
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
                    //Cambia al activity del perfil
                    final Intent choferActivity = new Intent(activity, ChoferActivity.class);
                    activity.startActivity(choferActivity);
                }else if (actualTime >= max_ms_time ){
                    Toast.makeText(activity,"Error al ser Chofer.",Toast.LENGTH_LONG).show();
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
        final int ms_time = 10;
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
                    //Cambia al activity del perfil
                    Intent pasajeroActivity = new Intent(activity, PasajeroActivity.class);
                    activity.startActivity(pasajeroActivity);
                }else if (actualTime >= max_ms_time ){
                    Toast.makeText(activity,"Error al ser Pasajero.",Toast.LENGTH_SHORT).show();
                }else{ //Si no pasa vuelve a checkear
                    actualTime += ms_time;
                    handler.postDelayed(this, ms_time);
                }
            }
        };
        handler.postDelayed(runnable, ms_time);
    }

    //Muestra la calificación en el Rating Bar del pasajero o del chofer
    public static void mostrarCalificacion(final RatingBar ratingBar,
                                           final TextView lblScore,
                                           final AppCompatActivity activity){
        final Handler handler = new Handler();
        final int ms_time = 10;
        final int max_ms_time = 3000; //queda esperando máximo 1 seg.
        actualTime = 0;
        if (usuario == null){
            return;
        }
        //Muestra el loading:
        final ProgressBar spinner = (ProgressBar) activity.findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        //No funciona en algunos celus en el xml, por eso está acá en código:
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        //Realiza la consulta
        Consulta.obtenerCalificacion(usuario, activity);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Intenta obtener Calificación
                JSONObject calObject = ConsultasGenerales.getJSONObject();
                //Si recibió al usuario.
                if (calObject != null){
                    try {
                        ratingBar.setRating(Float.parseFloat(calObject.getString("prom")));
                        Calificacion.updateScore(ratingBar, lblScore);
                    }catch(Exception e){
                        Log.e("Mostrar Calificacion:", "Error al obtener o convertir");
                        Toast.makeText(activity,"No has sido calificado aún.",Toast.LENGTH_SHORT).show();
                    }
                    spinner.setVisibility(View.GONE);
                    return;
                }else if (actualTime >= max_ms_time ){
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(activity,"Calificación no cargada",Toast.LENGTH_SHORT).show();
                }else{ //Si no pasa vuelve a checkear
                    actualTime += ms_time;
                    handler.postDelayed(this, ms_time);
                }
            }
        };
        handler.postDelayed(runnable, ms_time);
    }

    //Carga el historial de viajes
    public static void obtenerHistorialViajes(final AppCompatActivity activity){
        final Handler handler = new Handler();
        final int ms_time = 10;
        final int max_ms_time = 1000; //queda esperando máximo  1 seg.
        actualTime = 0;
        if (usuario == null){
            return;
        }
        Consulta.obtenerHistorialViajes(usuario, activity);
        final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Intenta obtener el objeto usuario
                JSONArray arrObj = ConsultasGenerales.getArrayObject();
                //Si recibió al usuario.
                if (arrObj != null){
                    mProgressDialog.cancel();
                    if(arrObj.length() == 0){
                        Toast.makeText(activity, "No hay viajes aún", Toast.LENGTH_SHORT).show();
                    }else{
                        //Obtiene el arreglo para devolverlo
                        int n = arrObj.length();
                        Viaje[] viajes = new Viaje[n];
                        for(int i = 0; i < n; i++){
                            JSONObject jo;
                            try{
                                jo = arrObj.getJSONObject(i);
                                viajes[i] = new Viaje(Integer.parseInt(jo.getString("codViaje")),
                                                      jo.getString("fechaViaje"),
                                                      Float.parseFloat(jo.getString("nota")),
                                                      jo.getString("comentario"),
                                                      jo.getString("nombre")
                                                     );
                            }catch(Exception e){
                                Log.e("Conversion", "Error al obtener objeto de array");
                            }
                        }
                        if (activity instanceof HistorialViajesActivity) {
                            ((HistorialViajesActivity) activity).mostrarViajes(viajes);
                        }
                        return;
                    }
                }else if (actualTime >= max_ms_time){
                    if (ConsultasGenerales.isNetworkAvailable(activity)){
                        Toast.makeText(activity, "No hay viajes aún", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, "Error conexión", Toast.LENGTH_SHORT).show();
                    }
                    mProgressDialog.cancel();
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
