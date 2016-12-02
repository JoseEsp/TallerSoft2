package com.jose.movilizateucn.Volley;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.DiagramaClases.Solicitud;
import com.jose.movilizateucn.GenerarSolicitudMap;
import com.jose.movilizateucn.R;

import org.json.JSONArray;
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
    //Cuando se obtiene más de un objeto se debe usar este:
    private static JSONArray arrayObject = null;
    //devuelve el último ID agregado (de algún AUTO_INCREMENT), debe estar especificado en el
    //json de respuesta del php!
    private static int ultimoID = -1;

    public static void insertarSolicitudMapa(String insertaDato,
                                     final FragmentActivity activity,
                                     JSONObject jobject,
                                     final String msjError,
                                     final Solicitud solicitud){
        // inserta datos en el servidor
        ultimoID = -1; //resetea el último ID
        VolleySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        insertaDato,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                ConsultasGenerales.procesarRespuestaSolicitudMapa(response, solicitud, activity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (ConsultasGenerales.isNetworkAvailable(activity)) {
                                    Snackbar.make(activity.findViewById(R.id.parentLayout), msjError, Snackbar.LENGTH_SHORT).show();
                                    if (activity instanceof GenerarSolicitudMap){
                                        ((GenerarSolicitudMap)activity).setSolicitud(null);
                                    }
                                }else{
                                    Snackbar.make(activity.findViewById(R.id.parentLayout), "Error de conexión.", Snackbar.LENGTH_SHORT).show();
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

    public static void actualizarDato(String url, final Activity activity, JSONObject jobject){
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                ConsultasGenerales.procesarRespuestaActualizacion(response, activity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //NADA
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

    private static void procesarRespuestaSolicitudMapa(JSONObject response, final Solicitud solicitud, final FragmentActivity activity) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            Log.d("RESPONSE", estado + ", " + mensaje);
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    activity.setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    activity.finish();
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText( activity, mensaje, Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    activity.setResult(Activity.RESULT_CANCELED);
                    break;
                //Devuelve id generado para el estado "id"
                case "id":
                    //Obtener ultimo ID
                    String lastID = response.getString("lastID");
                    ultimoID = Integer.parseInt(lastID);
                    solicitud.setCodSolicitud(ultimoID);
                    Snackbar.make(activity.findViewById(R.id.parentLayout), mensaje, Snackbar.LENGTH_SHORT).show();
                    activity.setResult(Activity.RESULT_OK);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void procesarRespuestaActualizacion(JSONObject response, final Activity activity) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            Log.d("RESPONSE", estado + ", " + mensaje);
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(activity, mensaje, Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    activity.setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    activity.finish();
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText( activity, mensaje, Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    activity.setResult(Activity.RESULT_CANCELED);
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
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
