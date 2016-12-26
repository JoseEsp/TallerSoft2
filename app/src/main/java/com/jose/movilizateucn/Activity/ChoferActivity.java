package com.jose.movilizateucn.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;
import com.jose.movilizateucn.Util.Calificacion;
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.DiagramaClases.Usuario;
import com.jose.movilizateucn.Util.Preferencias;

import org.json.JSONObject;

public class ChoferActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    LocationManager locationManager;
    AlertDialog alert = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer);
        if (Sesion.exists()) {
            configureNameText();
            Sesion.updateFechaFinConexion(this, "chofer");
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    Toast.makeText(this, "Se recibieron los permisos", Toast.LENGTH_SHORT).show();
                    Intent generarSolicitud = new Intent(this, GenerarSolicitudActivity.class);
                    startActivity(generarSolicitud);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // permission denied, boo! Disable the
                        Toast.makeText(this, "Es necesario encender el GPS para que funcione correctamente la aplicación", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Debe habilitar los permisos en configuración de aplicaciones ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        if (Sesion.exists()) {
            mostrarCalificacion();
        }
    }

    public void IniciarRutaButton(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (Sesion.exists()) {
                    Intent iniciarRutaActivity = new Intent(ChoferActivity.this, IniciarRutaActivity.class);
                    startActivity(iniciarRutaActivity);
                }
            }
            else{
                // PEDIR PERMISOS ASINCRONOS
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

    }

    public void HistorialViajesButton(View view){
        if (Sesion.exists()) {
            Intent histViajesActivity = new Intent(ChoferActivity.this, HistorialViajesActivity.class);
            histViajesActivity.putExtra("tipo", "chofer");
            startActivity(histViajesActivity);
        }
    }

    private void configureNameText(){
        TextView nameText = (TextView) findViewById(R.id.lblNombreChofer);
        Usuario user = Sesion.getUser();
        nameText.setText(user.getNombre().substring(0, 1).toUpperCase() + user.getNombre().substring(1));
    }

    private void mostrarCalificacion(){
        final RatingBar starBar = (RatingBar) findViewById(R.id.ratingBarChofer);
        final TextView lblScore = (TextView)  findViewById(R.id.lblStarScoreChofer);

        Preferencias.cargarCalificacion(this, starBar, lblScore, "chofer");

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        final Activity activity = this;
        String url = Url.OBTENERCALIFICACIONVIAJES + "?rut=" + Sesion.getUser().getRut() + "&tipo=chofer";
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(
                Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    try {
                        starBar.setRating(Float.parseFloat(response.getString("prom")));
                        Calificacion.updateScore(starBar, lblScore);
                        Preferencias.guardarCalificacion(activity, starBar, "chofer");
                        Sesion.setCalificacionChofer(starBar.getRating());
                    }catch(Exception e){
                        starBar.setRating(4.0f);
                        Calificacion.updateScore(starBar, lblScore);
                        Preferencias.guardarCalificacion(activity, starBar, "chofer");
                        Sesion.setCalificacionChofer(starBar.getRating());
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

    private void AlertNoGps() { //PIDE ATIVAR EL GPS
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Debe activar el GPS para que la aplicación funcione correctamente")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        alert = builder.create();
        alert.show();
    }
}
