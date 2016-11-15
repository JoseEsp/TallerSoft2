package com.jose.movilizateucn;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jose.movilizateucn.DiagramaClases.Viaje;

/**
 * Adaptador del recycler view
 */
public class ViajeAdapter extends RecyclerView.Adapter<ViajeAdapter.ViajeViewHolder>{

    private Viaje[] viajes;

    public ViajeAdapter(Viaje[] viajes){
        this.viajes = viajes;
    }

    public static class ViajeViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView ivStar;
        TextView tvCalificacion;
        ImageView ivCalendario;
        TextView tvFecha;
        TextView tvComentador;
        TextView tvComentario;

        ViajeViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            ivStar = (ImageView) itemView.findViewById(R.id.idStar);
            tvCalificacion = (TextView) itemView.findViewById(R.id.lblCalificacion);
            ivCalendario = (ImageView) itemView.findViewById(R.id.idCalendar);
            tvFecha = (TextView) itemView.findViewById(R.id.lblFechaViaje);
            tvComentador = (TextView) itemView.findViewById(R.id.lblNomComentador);
            tvComentario = (TextView) itemView.findViewById(R.id.lblComentario);
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
        viajeViewHolder.tvCalificacion.setText(viajes[i].getNota() + "");
        viajeViewHolder.tvFecha.setText(viajes[i].getFechaViaje());
        String nombre = viajes[i].getNombre();
        viajeViewHolder.tvComentador.setText(nombre.substring(0, 1).toUpperCase() +
                                             nombre.substring(1) + ":");
        String comentario = viajes[i].getComentario();
        viajeViewHolder.tvComentario.setText('\"' + comentario.substring(0, 1).toUpperCase() +
                                             comentario.substring(1) + '\"');
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount(){
        return viajes.length;
    }

}
