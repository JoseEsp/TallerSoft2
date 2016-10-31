package com.jose.movilizateucn.Consultas;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.jose.movilizateucn.DiagramaClases.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//Todas las consultas.
public class Consulta {

    //Variable auxiliar. Es el objeto retornado en cada 'obtener'
    private static JSONObject objeto = null;

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
        Consulta.insertarDatos("Usuario", Constantes.INSERTUSUARIO, activity, jobject);
        // Depurando objeto Json...
        Log.d("Usuario: ", jobject.toString());
    }

    /**
     * Obtiene un Usuario del sistema dado un rut.
     * Si el rut no está en el sistema, entonces retorna null.
     * @return Usuario en el sistema.
     */
    public static Usuario obtenerUsuario(String rut, final AppCompatActivity activity){
        String url = Constantes.GETUSUARIOBYRUT + "?rut=" + rut;
        Consulta.obtenerDato("Usuario", url, activity);
        //Parsear objeto
        Gson gson = new Gson();
        Usuario usuario = null;
        if (objeto != null) {
            usuario = gson.fromJson(objeto.toString(), Usuario.class);
        }
        return usuario;
    }






    /**
     * Función auxiliar para la inserción
     * @param tag Tag para el Toast
     * @param insertaDato Tipo de inserción definido en Constantes. Ejemeplo: INSERTUSUARIO
     * @param activity Activity actual
     * @param jobject Objeto Json con los datos.
     */
    private static void insertarDatos(final String tag, String insertaDato,
                                      final AppCompatActivity activity, JSONObject jobject){
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
                                Consulta.procesarRespuesta(response, activity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(tag, "Error Volley: " + error.getMessage());
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
     * Función auxiliar para la obtención.
     * @param tag
     * @param url
     * @param activity
     */
    private static void obtenerDato(final String tag, String url,
                                    final AppCompatActivity activity){
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuestaDato(response, activity);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(tag, "Error Volley: " + error.getMessage());
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
            String mensaje = response.getString("estado");

            switch (mensaje) {
                case "1":
                    // Obtener objeto
                    objeto = response.getJSONObject("meta");
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
    private static void procesarRespuesta(JSONObject response, final AppCompatActivity activity) {

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
