package com.jose.movilizateucn.Activity;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Util.Internet;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CalificarPasajerosActivity extends AppCompatActivity {

    private int codViaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_pasajeros);
        //Se envían los rut, los nombres y el codViaje
        codViaje = getIntent().getIntExtra("codViaje", -1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //muestra cada cuadrito
        if (codViaje != -1) {
            mostrarCalificaciones();
        }else{
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void mostrarCalificaciones(){
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Cargando...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        //ObtenercalificacionesparaChofer.php
        final CalificarPasajerosActivity activity = this;
        final View view = findViewById(R.id.activity_calificar);
        String url = Url.OBTENERCALIFICACIONESPARACHOFER + "?codViaje=" + codViaje;
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("arr");
                            if (jsonArray.length() == 0){
                                Toast.makeText(activity, "No hay viajes aún.", Toast.LENGTH_SHORT).show();
                            }else {
                                //Obtiene el arreglo para devolverlo
                                int n = jsonArray.length();
                                int[] codSolicitudes = new int[n];
                                String[] nombres = new String[n];
                                for (int i = 0; i < n; i++) {
                                    try {
                                        JSONObject json = jsonArray.getJSONObject(i);
                                        nombres[i] = json.getString("nombre");
                                        codSolicitudes[i] = Integer.parseInt(json.getString("codSolicitud"));
                                    } catch (Exception e) {}
                                }
                                CalificacionAdapter cAdapter = new CalificacionAdapter(activity, codViaje, codSolicitudes, nombres);
                                RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
                                LinearLayoutManager llm = new LinearLayoutManager(activity);
                                rv.setLayoutManager(llm);
                                rv.setAdapter(cAdapter);
                                rv.setHasFixedSize(true);
                                TextView tvCantPasajeros = (TextView)activity.findViewById(R.id.idCantPasajeros);
                                tvCantPasajeros.setText("Por favor, califica a estos "+ n + " pasajeros.");
                            }
                        }catch(Exception e){
                            mProgressDialog.cancel();
                        }
                        mProgressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.cancel();
                        Snackbar.make(view, "Nadie a Calificar.", Snackbar.LENGTH_INDEFINITE).show();
                        finish();
                    }
                }){

                }
        );
    }

}
