package com.jose.movilizateucn.Activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jose.movilizateucn.DiagramaClases.HistViaje;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Volley.SolicitudFireBase;

/**
 * Adaptador del recycler view
 */
public class CalificacionAdapter extends RecyclerView.Adapter<CalificacionAdapter.ViajeViewHolder>{

    private SolicitudFireBase[] sfb;

    public CalificacionAdapter(SolicitudFireBase[] sfb){
        this.sfb = sfb;
    }

    public static class ViajeViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        RatingBar rbStar;
        TextView tvCalificacion;
        TextView tvPasajero;
        EditText tvComentario;
        Button btnEnviar;

        ViajeViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            rbStar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvCalificacion = (TextView) itemView.findViewById(R.id.lblCalificacion);
            tvPasajero = (TextView) itemView.findViewById(R.id.lblNombrePasajero);
            tvComentario = (EditText) itemView.findViewById(R.id.lblComentario);
            btnEnviar = (Button) itemView.findViewById(R.id.btnEnviar);
        }
    }

    @Override
    public ViajeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viaje_list, viewGroup, false);
        ViajeViewHolder vvh = new ViajeViewHolder(v);
        return vvh;
    }

    @Override
    public void onBindViewHolder(ViajeViewHolder viajeViewHolder, int i) {
        //viajeViewHolder.tvCalificacion.setText(sfb[i].getNota() + "");
       // viajeViewHolder.tvFecha.setText(sfb[i].getFechaSubida());
        String nombre = sfb[i].getNombre();
       // viajeViewHolder.tvComentador.setText(nombre.substring(0, 1).toUpperCase() +
        //                                     nombre.substring(1) + ":");
        //String comentario = sfb[i].getComentario();
        //viajeViewHolder.tvComentario.setText('\"' + comentario.substring(0, 1).toUpperCase() +
         //                                    comentario.substring(1) + '\"');
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount(){
        return sfb.length;
    }

}
