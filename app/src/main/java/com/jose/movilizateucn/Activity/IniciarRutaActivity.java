package com.jose.movilizateucn.Activity;

import android.app.Activity;
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
import android.widget.Toast;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jose.movilizateucn.DiagramaClases.Chofer;
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.DiagramaClases.Viaje;
import com.jose.movilizateucn.ClasesMapa.Example;
import com.jose.movilizateucn.ClasesMapa.RetrofitMaps;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Util.Distancia;
import com.jose.movilizateucn.Util.Internet;
import com.jose.movilizateucn.Volley.SolicitudFireBase;
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

public class IniciarRutaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker origen;
    private Marker destino;
    private ProgressBar spinner;
    private boolean zoomUnaVez;

    //Un viaje, y una lista de SolicitudesActivas.
    private Viaje viaje;
    private HashMap<String, SolicitudFireBase> solicitudes;
    private HashMap<String, SolicitudFireBase> solicitudesAceptadas;
    private HashMap<String, Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_ruta);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.origen = null;
        this.destino = null;
        zoomUnaVez = false;
        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        this.viaje = null;
        this.solicitudes = new HashMap<>();
        this.solicitudesAceptadas = new HashMap<>();
        this.markers = new HashMap<>();
        this.colocarPosicionActual();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Al hacer click aparece el snackbar
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                if (!marker.equals(origen) && !marker.equals(destino)) {
                    clickMarker(marker);
                }
                return true;
            }
        });

        //Cargado inicial.
        spinner.setVisibility(View.VISIBLE);

        // Marcador en la UCN
        LatLng ucn = new LatLng(-29.9655042, -71.3516305);
        destino = mMap.addMarker(new MarkerOptions().position(ucn).title("UCN").snippet("Universidad Católica del Norte"));
        destino.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ucn, 16f));
    }

    private void colocarPosicionActual(){
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
                        origen = mMap.addMarker(new MarkerOptions().position(pos).title("Tú").snippet("Punto de Partida")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        origen.showInfoWindow();
                        if (!zoomUnaVez) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f));
                            zoomUnaVez = true;
                            mostrarInfo();
                            if (Sesion.exists()) {
                                generarViaje();
                            }
                        }
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

    public void generarViaje(){
        HashMap<String, String> map = new HashMap<>();
        if (!Sesion.exists()){
            Toast.makeText(this, "Error inesperado.", Toast.LENGTH_SHORT).show();
            finish();
        }
        map.put("rutChofer", Sesion.getUser().getRut());
        final JSONObject jsonViaje = new JSONObject(map);

        final View view = findViewById(R.id.parentLayout);
        final IniciarRutaActivity activity = this;

        spinner.setVisibility(View.VISIBLE);
        String url = Url.GENERARVIAJE;
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.POST, url, jsonViaje,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Snackbar.make(view, "Viaje Generado.", Snackbar.LENGTH_SHORT).show();
                                try{
                                    activity.setViaje(new Viaje(Integer.parseInt(response.getString("idViaje")), Chofer.usuario_to_Chofer(Sesion.getUser())));
                                    activity.escucharSolicitudesFireBase();
                                }catch(Exception e){

                                }
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

    public void escucharSolicitudesFireBase(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("solicitud");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String rut = dataSnapshot.getKey();
                procesarCambioMarkerFireBase(dataSnapshot, rut);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final String rut = dataSnapshot.getKey();
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("solicitud");
                ref.child(rut).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        procesarCambioMarkerFireBase(dataSnapshot, rut);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String rut = dataSnapshot.getKey();
                if (solicitudes.containsKey(rut)) {
                    solicitudes.remove(rut);
                }
                if (markers.containsKey(rut)) {
                    markers.remove(rut).remove();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void procesarCambioMarkerFireBase(DataSnapshot dataSnapshot, final String rut){
        if (!Sesion.exists()){
            return;
        }
        String codEstado = dataSnapshot.child("codEstado").getValue(String.class);
        if (codEstado != null && codEstado.equals("1") && !rut.equals(Sesion.getUser().getRut())){
            SolicitudFireBase sfb = new SolicitudFireBase(dataSnapshot, rut);
            LatLng pos = new LatLng(Double.parseDouble(sfb.getLat()), Double.parseDouble(sfb.getLon()));
            if (!solicitudes.containsKey(rut)) {
                solicitudes.put(rut, sfb);
            }
            if (!markers.containsKey(rut)) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(pos)
                        .title(sfb.getNombre().substring(0, 1).toUpperCase() + sfb.getNombre().substring(1))
                        .snippet("Calificación: " + sfb.getCalificacion())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                marker.setTag(rut);
                markers.put(rut, marker);
            }
        }else if (codEstado != null && codEstado.equals("2")){
            if (solicitudes.containsKey(rut)) {
                solicitudes.remove(rut);
            }
            if (markers.containsKey(rut)) {
                markers.remove(rut).remove();
            }
        }
    }

    private void clickMarker(final Marker marker){
        final Activity activity = this;
        View view = (View) findViewById(R.id.parentLayout);
        Snackbar snack = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                .setAction("Llevar a " + marker.getTitle(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        generarAceptacion(String.valueOf(marker.getTag()));
                    }
                });
        snack.setActionTextColor(Color.rgb(0, 128, 0));
        snack.getView().setBackgroundColor(Color.WHITE);
        snack.show();
    }

    private void generarAceptacion(final String rutPasajero){
        final View view = findViewById(R.id.parentLayout);
        final IniciarRutaActivity activity = this;
        spinner.setVisibility(View.VISIBLE);

        //Se desactiva en FireBase la solicitud (para evitar conflictos de click con otro chofer)
        final SolicitudFireBase solicitudFB = solicitudes.get(rutPasajero);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference solicitud = ref.child("solicitud").child(rutPasajero);
        solicitud.child("codEstado").setValue("2");
        String url = Url.GENERARACEPTACION + "?codViaje=" + viaje.getCodViaje() + "&codSolicitud=" + solicitudFB.getCodSolicitud();
        VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, url,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Snackbar.make(view, "Aceptación exitosa.", Snackbar.LENGTH_SHORT).show();
                                solicitudesAceptadas.put(rutPasajero, solicitudFB);
                                //Envía notificación.
                                if (Sesion.exists()) {
                                    LatLng l2 = new LatLng(Double.parseDouble(solicitudFB.getLat()), Double.parseDouble(solicitudFB.getLon()));
                                    String distancia = String.format("~%.1f km", Distancia.distanciaCoord(origen.getPosition(), l2));
                                    String cal = String.format("%.1f", Sesion.getCalificacionChofer());
                                    String mensaje = "El Chofer " + Sesion.getUser().getNombre() + " va por ti-"+ cal + "-" + distancia;
                                    mensaje = mensaje.replace(" ", "%20");
                                    String url = Url.ENVIARNOTIFICACION + "?rut=" + rutPasajero + "&mensaje=" + mensaje;
                                    VolleySingleton.getInstance(activity).addToRequestQueue(
                                            new JsonObjectRequest(Request.Method.GET, url, null, null));
                                }
                                spinner.setVisibility(View.GONE);
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                spinner.setVisibility(View.GONE);
                                //Se activa la solicitud si hubo un error
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference solicitud = ref.child("solicitud").child(rutPasajero);
                                solicitud.child("codEstado").setValue("1");
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

    public void setViaje(Viaje viaje){
        this.viaje = viaje;
    }

    public Viaje getViaje(){
        return this.viaje;
    }

}
