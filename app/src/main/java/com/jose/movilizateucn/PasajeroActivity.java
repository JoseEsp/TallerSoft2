package com.jose.movilizateucn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.DiagramaClases.Usuario;
import com.jose.movilizateucn.Util.Calificacion;
import com.jose.movilizateucn.Util.Preferencias;

import org.json.JSONObject;

public class PasajeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        if (Sesion.exists()) {
            configureNameText();
            Sesion.updateFechaFinConexion(this, "pasajero");
            mostrarCalificacion();
        }
    }

    public void GenerarSolicitudButton(View view){
        Intent generarSolicitudMap = new Intent(PasajeroActivity.this, GenerarSolicitudMap.class);
        startActivity(generarSolicitudMap);
    }
    public void HistorialViajesButton(View view){
        Intent histViajesActivity = new Intent(PasajeroActivity.this, HistorialViajesActivity.class);
        histViajesActivity.putExtra("tipo", "pasajero");
        startActivity(histViajesActivity);
    }

    private void configureNameText(){
        TextView nameText = (TextView) findViewById(R.id.lblNombrePasajero);
        Usuario user = Sesion.getUser();
        nameText.setText(user.getNombre().substring(0, 1).toUpperCase() + user.getNombre().substring(1));
    }

    private void mostrarCalificacion(){
        final RatingBar starBar = (RatingBar) findViewById(R.id.ratingBarPasajero);
        final TextView lblScore = (TextView)  findViewById(R.id.lblStarScorePasajero);

        Preferencias.cargarCalificacion(this, starBar, lblScore, "pasajero");

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        final Activity activity = this;
        String url = Url.OBTENERCALIFICACIONVIAJES + "?rut=" + Sesion.getUser().getRut() + "&tipo=pasajero";
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(
                Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    try {
                        starBar.setRating(Float.parseFloat(response.getString("prom")));
                        Calificacion.updateScore(starBar, lblScore);
                        Preferencias.guardarCalificacion(activity, starBar, "pasajero");
                    }catch(Exception e){
                        spinner.setVisibility(View.GONE);
                    }
                    spinner.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                    }
                }
        ));
    }
}
