package com.jose.movilizateucn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jose.movilizateucn.Consultas.Login;
import com.jose.movilizateucn.DiagramaClases.Viaje;

public class HistorialViajesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_viajes);
        Login.obtenerHistorialViajes(this);
    }

    //Una vez cargado el historial de viajes, éste método es llamado.
    public void mostrarViajes(Viaje[] viajes){
        ViajeAdapter vAdapter = new ViajeAdapter(viajes);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(vAdapter);
        rv.setHasFixedSize(true);
    }
}
