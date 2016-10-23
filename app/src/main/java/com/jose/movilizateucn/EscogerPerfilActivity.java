package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EscogerPerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_perfil);
        configureButtons();
    }

    private void configureButtons(){
        Button btnChofer = (Button) findViewById(R.id.btnChofer);
        Button btnPasajero = (Button) findViewById(R.id.btnPasajero);

        final Intent choferActivity = new Intent(this, ChoferActivity.class);
        final Intent pasajeroActivity = new Intent(this, PasajeroActivity.class);

        btnChofer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(choferActivity);
            }
        });

        btnPasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(pasajeroActivity);
            }
        });
    }
}
