package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jose.movilizateucn.DiagramaClases.Sesion;

public class EscogerPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_perfil);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Detiene toda actualización de Fecha Fin Conexión.
        Sesion.stopUpdate();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Sesion.deleteUser();
    }

    public void PasajeroButton(View view){
        if (Sesion.exists()){
            Intent pasajeroActivity = new Intent(this, PasajeroActivity.class);
            this.startActivity(pasajeroActivity);
        }
    }

    public void ChoferButton(View view){
        if (Sesion.exists()){
            final Intent choferActivity = new Intent(this, ChoferActivity.class);
            this.startActivity(choferActivity);
        }
    }

    public void FeedBack(View view){
        Intent ifeedback = new Intent(EscogerPerfilActivity.this, FeedBack.class);
        startActivity(ifeedback);
    }

}
