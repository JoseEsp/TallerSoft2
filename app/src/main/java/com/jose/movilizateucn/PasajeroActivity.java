package com.jose.movilizateucn;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jose.movilizateucn.Consultas.Login;
import com.jose.movilizateucn.DiagramaClases.Calificacion;
import com.jose.movilizateucn.DiagramaClases.Usuario;

public class PasajeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        configureNameText();
        Login.updateFechaFinConexion(this);
        mostrarCalificacion();
    }

    public void GenerarSolicitudButton(View view){
        if (Login.getSolicitud() == null){
            Intent generarSolicitudMap = new Intent(PasajeroActivity.this, GenerarSolicitudMap.class);
            startActivity(generarSolicitudMap);
        }else{
            Snackbar.make(view, "Solicitud ya generada.", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void VerSolicitudButton(View view){
       if (Login.getSolicitud() == null){
            Snackbar.make(view, "No has generado ninguna solicitud.", Snackbar.LENGTH_SHORT).show();
        }else{
            //Abrir info aquí

       }
    }

    public void HistorialViajesButton(View view){
        Intent histViajes = new Intent(PasajeroActivity.this, HistorialViajesActivity.class);
        startActivity(histViajes);
    }

    private void configureNameText(){
        TextView nameText = (TextView) findViewById(R.id.lblNombrePasajero);
        Usuario user = Login.getUsuario();
        if (user != null){
            nameText.setText(user.getNombre().substring(0, 1).toUpperCase() + user.getNombre().substring(1));
        }
    }

    private void mostrarCalificacion(){
        RatingBar starBar = (RatingBar) findViewById(R.id.ratingBarPasajero);
        TextView lblScore = (TextView)  findViewById(R.id.lblStarScorePasajero);
        //Obtiene prefencia de calificación
        SharedPreferences pref = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (Login.getUsuario() != null) {
            String calPasajero = pref.getString("calificacionPasajero[" + Login.getUsuario().getRut() + "]", "2.5");
            starBar.setRating(Float.parseFloat(calPasajero));
            Calificacion.updateScore(starBar, lblScore);
        }
        Login.mostrarCalificacion(starBar, lblScore, this);
    }
}
