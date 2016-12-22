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
import android.widget.Button;
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
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.DiagramaClases.Usuario;
import com.jose.movilizateucn.Util.Calificacion;
import com.jose.movilizateucn.Util.Preferencias;

import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

public class PasajeroActivity extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    Button boton;
    LocationManager locationManager;
    View vista;
    AlertDialog alert = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        boton = (Button) findViewById(R.id.btnGenerarSolicitud);
        if (Sesion.exists()) {
            configureNameText();
            Sesion.updateFechaFinConexion(this, "pasajero");
            mostrarCalificacion();
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(alert != null)
        {
            alert.dismiss ();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
    }

    public void GenerarSolicitudButton(View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Intent generarSolicitud = new Intent(PasajeroActivity.this, GenerarSolicitudActivity.class);
                startActivity(generarSolicitud);
            }
            else{
                // PEDIR PERMISOS ASINCRONOS
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
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
                        Toast.makeText(this, "El permiso de GPS es necesario para usar la App", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Debe habilitar los permisos en configuración de aplicaciones ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
                        Sesion.setCalificacionPasajero(starBar.getRating());
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
