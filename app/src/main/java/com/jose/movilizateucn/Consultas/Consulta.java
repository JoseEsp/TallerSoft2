package com.jose.movilizateucn.Consultas;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.DiagramaClases.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//Todas las consultas.
public class Consulta {

    //La Ip a conectarse
    private static final String IP = "http://movilizate.webcindario.com";

    /**
     * Inserta un usuario en la base de datos
     * @param usuario Usuario de la aplicación
     */
    public static void insertarUsuario(Usuario usuario, final Activity activity){
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("rut", usuario.getRut());
        map.put("nombre", usuario.getNombre());
        map.put("contra", usuario.getContra());
        map.put("email", usuario.getEmail());

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);
        Consulta.insertarDatos("Usuario", Constantes.INSERTUSUARIO, activity, jobject);
        // Depurando objeto Json...
        Log.d("Usuario: ", jobject.toString());


    }

    /**
     *
     * @param tag Tag para el Toast
     * @param inserta Tipo de inserción definido en Constantes. Ejemeplo: INSERTUSUARIO
     * @param activity Activity actual
     * @param jobject Objeto Json con los datos.
     */
    private static void insertarDatos(String tag, String inserta, final Activity activity,
                                      JSONObject jobject){
        // inserta datos en el servidor
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERTUSUARIO,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                Consulta.procesarRespuesta(response, activity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Usuario: ", "Error Volley: " + error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
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

    /**
     * Procesa la respuesta obtenida desde el sevidor
     *
     * @param response Objeto Json
     * @param activity Activity actual
     */
    private static void procesarRespuesta(JSONObject response, final Activity activity) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

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

}
