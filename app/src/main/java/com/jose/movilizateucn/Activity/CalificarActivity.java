package com.jose.movilizateucn.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jose.movilizateucn.R;

public class CalificarActivity extends AppCompatActivity {

    private int codViaje;
    private String[] ruts;
    private String[] nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);
        //Se envían los rut, los nombres y el codViaje
        codViaje = getIntent().getIntExtra("codViaje", -1);
        ruts = getIntent().getStringArrayExtra("ruts");
        nombres = getIntent().getStringArrayExtra("nombres");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //muestra cada cuadrito
        mostrarCalificaciones();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    private void mostrarCalificaciones(){

    }


}
