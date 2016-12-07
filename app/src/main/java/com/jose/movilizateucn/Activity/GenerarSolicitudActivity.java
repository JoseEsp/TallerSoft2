package com.jose.movilizateucn.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.ClasesMapa.Example;
import com.jose.movilizateucn.ClasesMapa.RetrofitMaps;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Util.Internet;
import com.jose.movilizateucn.DiagramaClases.Solicitud;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenerarSolicitudActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker origen;
    private Marker destino;
    private ProgressBar spinner;
    private Solicitud solicitud;
    private boolean zoomUnaVez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_solicitud_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.origen = null;
        this.destino = null;
        zoomUnaVez = false;
        solicitud = null;
        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Cargado inicial.
        spinner.setVisibility(View.VISIBLE);
        Snackbar.make(findViewById(R.id.parentLayout), "Generando Solicitud...", Snackbar.LENGTH_SHORT).show();

        // Marcador en la UCN
        LatLng ucn = new LatLng(-29.9655042, -71.3516305);
        destino = mMap.addMarker(new MarkerOptions().position(ucn).title("UCN").snippet("Universidad Católica del Norte"));
        destino.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ucn, 16f));

        //Posicion Actual:
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Checkear GPS
        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (origen != null){
                            origen.remove();
                        }
                        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                        origen = mMap.addMarker(new MarkerOptions().position(pos).title("Tú").snippet("Ubicación actual")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        origen.showInfoWindow();
                        if (!zoomUnaVez) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f));
                            zoomUnaVez = true;
                        }
                        mostrarInfo();
                        generarSolicitud();
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
            } catch (SecurityException e) {
                Log.d("Error", "Error de seguridad.");
            }
        }else{
            Snackbar.make(findViewById(R.id.parentLayout), "Encienda el GPS y vuelva a intentarlo.", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (solicitud != null) {
            //Consulta.desactivarSolicitud(solicitud, this);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (solicitud != null) {
           //Consulta.activarSolicitud(solicitud, this);
        }
    }

    public void mostrarInfo(){
        final TextView tvDistancia = (TextView) findViewById(R.id.distancia);
        final TextView tvTiempo = (TextView) findViewById(R.id.tiempo);

        //Cargando...
        spinner.setVisibility(View.VISIBLE);

        String url = Url.RUTAOPTIMA;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        LatLng origin = origen.getPosition();
        LatLng dest = destino.getPosition();
        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, "driving");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                for (int i = 0; i < response.body().getRoutes().size(); i++) {
                    String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                    String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                    tvDistancia.setText("~" + distance);
                    tvTiempo.setText("~" + time);
                }
                spinner.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<Example> call, Throwable t) {}
        });
    }

    public void generarSolicitud(){
        HashMap<String, String> map = new HashMap<>();
        map.put("codEstado", "1");
        map.put("lat", String.valueOf(origen.getPosition().latitude));
        map.put("lon", String.valueOf(origen.getPosition().longitude));
        map.put("rutPasajero", Sesion.getUser().getRut());
        final JSONObject jsonSolicitud = new JSONObject(map);

        final View view = findViewById(R.id.parentLayout);
        final GenerarSolicitudActivity activity = this;

        spinner.setVisibility(View.VISIBLE);

        String url = Url.GENERARSOLICITUD;
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.POST, url, jsonSolicitud,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    int codSolicitud = Integer.parseInt(response.getString("idSolicitud"));
                                    String fechaSalida = response.getString("fechaSalida");
                                    int codEstado = 1;
                                    double lat = origen.getPosition().latitude;
                                    double lon = origen.getPosition().longitude;
                                    Solicitud s = new Solicitud(codSolicitud, fechaSalida, codEstado, lat, lon);
                                    activity.setSolicitud(s);
                                    //FireBase
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference solicitud = ref.child("solicitud").child(Sesion.getUser().getRut());
                                    //Manda Solicitud a Firebase
                                    solicitud.child("codSolicitud").setValue(String.valueOf(codSolicitud));
                                    solicitud.child("nombre").setValue(Sesion.getUser().getNombre());
                                    if (Sesion.getCalificacionPasajero() == -1){
                                        solicitud.child("calificacion").setValue("4.0");
                                    }else {
                                        solicitud.child("calificacion").setValue(String.valueOf(Sesion.getCalificacionPasajero()));
                                    }
                                    solicitud.child("fechaSalida").setValue(fechaSalida);
                                    solicitud.child("codEstado").setValue(String.valueOf(codEstado));
                                    solicitud.child("lat").setValue(String.valueOf(lat));
                                    solicitud.child("lon").setValue(String.valueOf(lon));
                                    solicitud.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                    Snackbar.make(view, "Solicitud Generada.", Snackbar.LENGTH_SHORT).show();
                                }catch(Exception e){
                                    spinner.setVisibility(View.GONE);
                                }
                                spinner.setVisibility(View.GONE);
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (!Internet.hayConexion(activity)){
                                    Snackbar.make(view, "Error de conexión, vuelva a intentarlo.", Snackbar.LENGTH_SHORT).show();
                                }else{
                                    Snackbar.make(view, "Error inesperado. Vuelva a intentarlo.", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }){
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
        });
    }

    public void setSolicitud(Solicitud solicitud){
        this.solicitud = solicitud;
    }

    public Solicitud getSolicitud(){
        return solicitud;
    }

}
