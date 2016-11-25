package com.jose.movilizateucn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jose.movilizateucn.Consultas.Login;
import com.jose.movilizateucn.DiagramaClases.Calificacion;
import com.jose.movilizateucn.DiagramaClases.Usuario;

public class ChoferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer);
        configureNameText();
        Login.updateFechaFinConexion(this);
        mostrarCalificacion();
    }

    public void HistorialViajesButton(View view){
        Intent histViajes = new Intent(ChoferActivity.this, HistorialViajesActivity.class);
        startActivity(histViajes);
    }

    private void configureNameText(){
        TextView nameText = (TextView) findViewById(R.id.lblNombreChofer);
        Usuario user = Login.getUsuario();
        if (user != null){
            nameText.setText(user.getNombre().substring(0, 1).toUpperCase() + user.getNombre().substring(1));
        }
    }

    private void mostrarCalificacion(){
        RatingBar starBar = (RatingBar) findViewById(R.id.ratingBarChofer);
        TextView lblScore = (TextView)  findViewById(R.id.lblStarScoreChofer);
        //Obtiene prefencia de calificación
        if (Login.getUsuario() != null) {
            SharedPreferences pref = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            String calChofer = pref.getString("calificacionChofer[" + Login.getUsuario().getRut() + "]", "2.5");
            starBar.setRating(Float.parseFloat(calChofer));
            Calificacion.updateScore(starBar, lblScore);
        }
        Login.mostrarCalificacion(starBar, lblScore, this);
    }

}
