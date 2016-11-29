package com.jose.movilizateucn;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jose.movilizateucn.Consultas.Constantes;
import com.jose.movilizateucn.Consultas.Consulta;
import com.jose.movilizateucn.Consultas.ConsultasGenerales;
import com.jose.movilizateucn.Consultas.ListenerPosicionActual;
import com.jose.movilizateucn.Consultas.Login;
import com.jose.movilizateucn.DiagramaClases.Pasajero;
import com.jose.movilizateucn.DiagramaClases.Solicitud;
import com.jose.movilizateucn.POJO.Example;
import com.jose.movilizateucn.POJO.RetrofitMaps;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class GenerarSolicitudMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker origen;
    private Marker destino;
    private Polyline line;
    private ProgressBar spinner;
    private final float MAX_RADIUS = 500; //500 a la redonda de la ucn no se puede generar solicitud
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
        this.line = null;
        zoomUnaVez = true;
        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Cargado inicial.
        spinner.setVisibility(View.VISIBLE);

        // Marcador en la UCN
        LatLng ucn = new LatLng(-29.9659721, -71.3476425);
        destino = mMap.addMarker(new MarkerOptions().position(ucn).title("UCN").snippet("Universidad Católica del Norte"));
        destino.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ucn, 12f));
        //Donde no se puede generar solicitud:
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(ucn)
                .radius(MAX_RADIUS)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(128, 255, 0, 0)));

        //Posicion Actual:
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Checkear GPS
        if (locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            //Listener
            LocationListener locationListener = new ListenerPosicionActual(this);
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
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

        String url = Constantes.RUTAOPTIMAGENERARSOLICITUD;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        LatLng origin = origen.getPosition();
        LatLng dest = destino.getPosition();
        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, "driving");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {
                if (line != null) {
                    line.remove();
                }
                for (int i = 0; i < response.body().getRoutes().size(); i++) {
                    String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                    String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                    tvDistancia.setText("~" + distance);
                    tvTiempo.setText("~" + time);
                    String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                    List<LatLng> list = decodePoly(encodedString);
                    line = mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(10)
                            .color(Color.RED)
                            .geodesic(true)
                    );
                }
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    //Decodifica el Polyline (No tocar esta función)
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public void setOrigen(Location loc){
        if (origen != null){
            origen.remove();
        }
        LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
        origen = mMap.addMarker(new MarkerOptions().position(pos).title("Inicio").snippet("Punto de Partida")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        origen.showInfoWindow();
        if (zoomUnaVez) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f));
            zoomUnaVez = false;
        }
        mostrarInfo();
    }

    public void GenerarSolicitudButton(View view){
        if (ConsultasGenerales.isNetworkAvailable(this)){
            if (origen != null){
                Snackbar.make(view, "Generando...", Snackbar.LENGTH_SHORT).show();
                LatLng latLng = origen.getPosition();
                LatLng ucn = destino.getPosition();
                float[] distancia = new float[1];
                Location.distanceBetween(ucn.latitude, ucn.longitude, latLng.latitude, latLng.longitude, distancia);
                if ( distancia[0] > MAX_RADIUS) {
                    Solicitud solicitud = new Solicitud(0, "", 1, latLng.latitude, latLng.longitude);
                    solicitud.setPasajero((Pasajero) Login.getUsuario());
                    Login.setSolicitud(solicitud);
                    Consulta.insertarSolicitud(solicitud, this);
                }else{
                    Snackbar.make(view, "Estás muy cerca de la UCN!", Snackbar.LENGTH_SHORT).show();
                }
            }else{
                Snackbar.make(view, "No hay punto de origen...", Snackbar.LENGTH_SHORT).show();
            }
        }else{
            Snackbar.make(view, "No hay conexión a Internet", Snackbar.LENGTH_SHORT).show();
        }
    }
}
