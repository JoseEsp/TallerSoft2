package com.jose.movilizateucn.Activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Adaptador del recycler view
 */
public class CalificacionAdapter extends RecyclerView.Adapter<CalificacionAdapter.CalificacionViewHolder>{

    //activity en el que está el adapter:
    private final CalificarPasajerosActivity activity;
    private final int codViaje;
    private int[] codSolicitudes;
    private String[] nombres;

    public CalificacionAdapter(CalificarPasajerosActivity activity, int codViaje, int[] cs, String[] noms){
        this.activity = activity;
        this.codViaje = codViaje;
        codSolicitudes = cs;
        nombres = noms;
    }

    public static class CalificacionViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        RatingBar rbStar;
        TextView tvCalificacion;
        TextView tvPasajero;
        EditText etComentario;
        Button btnEnviar;

        CalificacionViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            rbStar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvCalificacion = (TextView) itemView.findViewById(R.id.lblCalificacion);
            tvPasajero = (TextView) itemView.findViewById(R.id.lblNombrePasajero);
            etComentario = (EditText) itemView.findViewById(R.id.etComentario);
            btnEnviar = (Button) itemView.findViewById(R.id.btnEnviar);
        }
    }

    @Override
    public CalificacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calificar_histviaje, viewGroup, false);
        CalificacionViewHolder cvh = new CalificacionViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CalificacionViewHolder cvh, int i) {
        final CalificacionViewHolder innerCvh = cvh;
        cvh.rbStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                innerCvh.tvCalificacion.setText(String.valueOf(v));
            }
        });
        cvh.tvPasajero.setText(nombres[i]);
        final String nombre = nombres[i];
        cvh.tvPasajero.setText("Estimado " + nombre.substring(0, 1).toUpperCase() + nombre.substring(1) + ":");

        final EditText etComentario = cvh.etComentario;
        final int k = i;
        final RatingBar rbStar = cvh.rbStar;
        final View vista = activity.findViewById(R.id.activity_calificar);
        cvh.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressBar spinner = (ProgressBar) activity.findViewById(R.id.spinner);
                spinner.setVisibility(View.VISIBLE);
                spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

                final View vista = view;
                String url = Url.ACTUALIZARCOMENTARIOCHOFERAPASAJERO + "?codViaje=" + codViaje + "&codSolicitud=" + codSolicitudes[k] +
                        "&nota=" + String.format("%.1f", rbStar.getRating()) + "&comentario=" + etComentario.getText();
                url = url.replace(" ", "%20");
                Log.d("url", url);
                VolleySingleton.getInstance(activity).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Snackbar.make(vista, "Has calificado a " + nombres[k] + " correctamente.", Snackbar.LENGTH_SHORT).show();
                                spinner.setVisibility(View.GONE);
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
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount(){
        return nombres.length;
    }

}
