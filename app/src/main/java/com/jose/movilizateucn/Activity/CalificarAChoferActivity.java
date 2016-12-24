package com.jose.movilizateucn.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

import org.json.JSONObject;


/**
 * Nota que le coloca el Pasajero al Chofer
 */
public class CalificarAChoferActivity extends AppCompatActivity {

    private RatingBar rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_achofer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        String nombreChofer = extras.getString("nombreChofer");
        ((TextView) findViewById(R.id.lblNombreChofer)).setText("Estimado " + nombreChofer + ":");
        rb = ((RatingBar) findViewById(R.id.ratingBar));
        final TextView tvNota = ((TextView)findViewById(R.id.lblCalificacion));
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tvNota.setText(String.valueOf(v));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void enviar(View view){
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        Bundle extras = getIntent().getExtras();
        int codViaje = extras.getInt("codViaje");
        int codSolicitud = extras.getInt("codSolicitud");

        EditText etComentario = (EditText) findViewById(R.id.etComentario);

        final CalificarAChoferActivity activity = this;
        final View vista = view;
        String url = Url.ACTUALIZARCOMENTARIOPASAJEROACHOFER + "?codViaje=" + codViaje + "&codSolicitud=" + codSolicitud +
               "&nota=" + String.format("%.1f", rb.getRating()) + "&comentario=" + etComentario.getText();
        url = url.replace(" ", "%20");
        VolleySingleton.getInstance(activity).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, "La calificación ha sido enviada con éxito!", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!Internet.hayConexion(activity)){
                            Snackbar.make(vista, "Conéctese a Internet y vuelva a intentarlo.", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(vista, "Error al enviar calificación.", Snackbar.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.GONE);
                    }
                }
        ));
    }
}
