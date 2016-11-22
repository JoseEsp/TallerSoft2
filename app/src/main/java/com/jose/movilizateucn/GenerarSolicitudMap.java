package com.jose.movilizateucn;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jose.movilizateucn.Consultas.Constantes;
import com.jose.movilizateucn.Consultas.ConsultasGenerales;
import com.jose.movilizateucn.POJO.Example;
import com.jose.movilizateucn.POJO.RetrofitMaps;

import org.json.JSONObject;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final TextView tvInfo = (TextView) findViewById(R.id.info);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (origen != null) {
                    origen.remove();
                }
                origen = mMap.addMarker(new MarkerOptions().position(latLng).title("Inicio"));
                origen.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                tvInfo.setText(String.format("(%f, %f)", latLng.latitude, latLng.longitude));
                mostrarInfo();
            }
        });

        // Marcador en la UCN
        LatLng ucn = new LatLng(-29.9659721, -71.3476425);
        destino = mMap.addMarker(new MarkerOptions().position(ucn).title("UCN").snippet("Universidad Católica del Norte"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ucn, 12f));
    }

    public void mostrarInfo(){
        final TextView tvDistancia = (TextView) findViewById(R.id.distancia);
        final TextView tvTiempo = (TextView) findViewById(R.id.tiempo);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);

        //Cargando...
        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        String url = Constantes.RUTAOPTIMAGENERARSOLICITUD;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        LatLng origin = origen.getPosition();
        LatLng dest = destino.getPosition();
        Call<Example> call = service.getDistanceDuration("metric", origin.latitude + "," + origin.longitude,dest.latitude + "," + dest.longitude, "driving");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(final Response<Example> response, Retrofit retrofit) {
                if (line != null) {
                    line.remove();
                }
                for (int i = 0; i < response.body().getRoutes().size(); i++) {
                    String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                    String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                    tvDistancia.setText(distance);
                    tvTiempo.setText(time);
                    String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                    List<LatLng> list = decodePoly(encodedString);
                    line = mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(20)
                            .color(Color.GREEN)
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
}
