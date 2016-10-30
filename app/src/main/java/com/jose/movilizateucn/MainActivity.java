package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureBtnLogin();
        configureBtnRegistrar();
    }

    private void configureBtnLogin(){
        final Intent perfilActivity = new Intent(this, EscogerPerfilActivity.class);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(perfilActivity);
            }
        });
    }

    private void configureBtnRegistrar(){
        final Intent registrarseActivity = new Intent(this, RegistrarseActivity.class);
        Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registrarseActivity);
            }
        });
    }


}
