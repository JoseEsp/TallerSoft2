package com.jose.movilizateucn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;
import com.jose.movilizateucn.DiagramaClases.HistViaje;
import com.jose.movilizateucn.DiagramaClases.Sesion;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistorialViajesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_viajes);
        mostrarViajes(this.getIntent().getStringExtra("tipo"));
    }

    //Una vez cargado el historial de histViajes, éste método es llamado.
    public void mostrarViajes(final String tipo){

        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        String url = "";
        if (tipo.equals("chofer")) {
            url = Url.OBTENERHISTVIAJES + "?rut=" + Sesion.getUser().getRut() + "&tipo=chofer";
        }else if (tipo.equals("pasajero")){
            url = Url.OBTENERHISTVIAJES + "?rut=" + Sesion.getUser().getRut() + "&tipo=pasajero";
        }else{
            return;
        }
        final Activity activity = this;
        VolleySingleton.getInstance(activity).addToRequestQueue(new JsonObjectRequest(
                Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("HistViaje");
                            if (jsonArray.length() == 0){
                                Toast.makeText(activity, "No hay viajes aún.", Toast.LENGTH_SHORT).show();
                            }else {
                                //Obtiene el arreglo para devolverlo
                                int n = jsonArray.length();
                                HistViaje[] histViajes = new HistViaje[n];
                                for (int i = 0; i < n; i++) {
                                    try {
                                        JSONObject jsonHistViaje = jsonArray.getJSONObject(i);
                                        histViajes[i] = HistViaje.JSonObject_to_HistViaje(jsonHistViaje);
                                    } catch (Exception e) {}
                                }
                                ViajeAdapter vAdapter = new ViajeAdapter(histViajes);
                                RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
                                LinearLayoutManager llm = new LinearLayoutManager(activity);
                                rv.setLayoutManager(llm);
                                rv.setAdapter(vAdapter);
                                rv.setHasFixedSize(true);
                            }
                        }catch(Exception e){
                            mProgressDialog.cancel();
                        }
                        mProgressDialog.cancel();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.cancel();
                    }
                }
        ));
    }
}
