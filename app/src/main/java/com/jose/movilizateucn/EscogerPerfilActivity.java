package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jose.movilizateucn.Consultas.Consulta;
import com.jose.movilizateucn.DiagramaClases.Login;
import com.jose.movilizateucn.DiagramaClases.Usuario;

public class EscogerPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_perfil);
        configureButtons();
    }

    @Override
    public void onBackPressed(){
        Intent main = new Intent(this, MainActivity.class);
        Login.desconectarse();
        startActivity(main);
    }

    private void configureButtons(){
        final AppCompatActivity thisActivity = this;
        Button btnChofer = (Button) findViewById(R.id.btnChofer);
        Button btnPasajero = (Button) findViewById(R.id.btnPasajero);

        btnChofer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Transforma a Chofer y luego se va a ChoferActivity
                Login.transformToChofer(thisActivity);
            }
        });

        btnPasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Transforma a Pasajero y luego se va a PasajeroActivity
                Login.transformToPasajero(thisActivity);
            }
        });
    }
}
