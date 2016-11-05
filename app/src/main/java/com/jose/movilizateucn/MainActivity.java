package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jose.movilizateucn.DiagramaClases.Login;

public class MainActivity extends AppCompatActivity {

    private EditText rutTxt;
    private EditText passTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Desconecta Logueo previo
        configureEditText();
        configureBtnLogin();
        configureBtnRegistrar();
    }

    private void configureEditText(){
        rutTxt = (EditText) findViewById(R.id.txtRut);
        passTxt = (EditText) findViewById(R.id.txtPassword);
    }

    private void configureBtnLogin(){
        final AppCompatActivity thisActivity = this;
        final Intent perfilActivity = new Intent(this, EscogerPerfilActivity.class);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCampos()) {
                    String rut = rutTxt.getText().toString();
                    String contra = passTxt.getText().toString();
                    Login.conectarse(rut, contra, thisActivity);
                }
            }
        });
    }

    //Checkea si los campos son correctos
    private boolean checkCampos(){
        //Por ahora checkea que no estén vacios
        if (rutTxt.getText().length() < 1){
            Toast.makeText(this, "Ingrese Rut",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (passTxt.getText().length() < 1){
            Toast.makeText(this, "Ingrese Password",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
