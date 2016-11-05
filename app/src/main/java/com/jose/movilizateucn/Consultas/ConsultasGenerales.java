package com.jose.movilizateucn.Consultas;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.DiagramaClases.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Consultas Generales (Uso de Volley Singleton y despliegue de respuestas.
 */

public class ConsultasGenerales {

    //Atributo necesario, es el 'objeto' luego de una obtención de algún registro en la BD.
    private static JSONObject objeto = null;

    /**
     * Función auxiliar para la inserción
     * @param insertaDato Tipo de inserción definido en Constantes. Ejemeplo: INSERTUSUARIO
     * @param activity Activity actual
     * @param jobject Objeto Json con los datos.
     */
    public static void insertarDatos(String insertaDato,
                                     final AppCompatActivity activity,
                                     JSONObject jobject,
                                     final String msjError){
        // inserta datos en el servidor
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        insertaDato,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                ConsultasGenerales.procesarRespuestaInsercion(response, activity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (ConsultasGenerales.isNetworkAvailable(activity)) {
                                    Toast.makeText(
                                            activity,
                                            msjError,
                                            Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(
                                            activity,
                                            "Error de conexión.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    public static void obtenerDato(String url,
                                   final AppCompatActivity activity){
        objeto = null;
        ConsultasGenerales.fetchData(url, activity, new DataCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                ConsultasGenerales.procesarRespuestaDato(result, activity);
            }
        });
    }

    /**
     * Actualiza fechas de inicio o fin de conexión, según llamado.
     * @param usuario A quién está actualizando
     * @param url Fecha de inicio o de conexión.
     * @param activity La activity actual
     */
    public static void actualizarFecha(Usuario usuario, String url, final AppCompatActivity activity){
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Nada
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d("Error Volley", error.getMessage());
                            }
                        }
                )
        );
    }

    /**
     * Función auxiliar para la obtención.
     * @param url La url
     * @param activity Activity actual
     */
    private static void fetchData(String url,
                                   final AppCompatActivity activity,
                                   final DataCallback callback){
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                callback.onSuccess(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d("Error Volley", error.getMessage());
                            }
                        }
                )
        );
    }

    /**
     * Procesa el dato
     * @param response Objeto Json
     */
    private static void procesarRespuestaDato(JSONObject response,
                                              final AppCompatActivity activity) {
        try {
            // Obtener atributo "mensaje"
            String estado = response.getString("estado");
            switch (estado) {
                case "1":
                    // Obtener objeto
                    objeto = response.getJSONObject("objeto");
                    break;
                case "2":
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            activity,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            activity,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Procesa la respuesta obtenida desde el sevidor
     *
     * @param response Objeto Json
     * @param activity Activity actual
     */
    private static void procesarRespuestaInsercion(JSONObject response, final AppCompatActivity activity) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            Log.d("RESPONSE", estado + ", " + mensaje);
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            activity,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    activity.setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    activity.finish();
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            activity,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    activity.setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    activity.finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * No es 100% segura
     * @return Si hay conexión
     *
     */
    private static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Retorna el objeto JSON generado por alguna obtención de datos.
     */
    public static JSONObject getJSONObject(){
        return objeto;
    }
}
