package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jose.movilizateucn.Consultas.Login;

public class EscogerPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_perfil);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Login.desconectarse();
    }

    public void PasajeroButton(View view){
        Login.transformToPasajero(EscogerPerfilActivity.this);
    }
    public void ChoferButton(View view){
        Login.transformToChofer(EscogerPerfilActivity.this);
    }

    public void FeedBack(View view){
        Intent ifeedback = new Intent(EscogerPerfilActivity.this, FeedBack.class);
        startActivity(ifeedback);
    }

}
