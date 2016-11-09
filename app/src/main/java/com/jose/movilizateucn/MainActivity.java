package com.jose.movilizateucn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }

    public void LoginButton(View view){
        rutTxt = (EditText) findViewById(R.id.txtRut);
        passTxt = (EditText) findViewById(R.id.txtPassword);

        if (rutTxt.getText().toString().equals("") || passTxt.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this,"Debe completar todos los campos",Toast.LENGTH_SHORT).show();
        }
        else{
            //Login log = new Login();
            Login.conectarse(rutTxt.getText().toString(), passTxt.getText().toString(), this );
        }
    }

    public void RegistrarButton(View view){
        Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        Intent registrarse = new Intent(MainActivity.this, RegistrarseActivity.class);
        startActivity(registrarse);
    }
}
