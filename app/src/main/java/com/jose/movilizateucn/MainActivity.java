package com.jose.movilizateucn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.jose.movilizateucn.Consultas.Login;

public class MainActivity extends AppCompatActivity {
    private EditText rutTxt;
    private EditText passTxt;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPreferences();
        //Si se quieren borrar shared preferences: descomentar y comentar loadPreferences();
        /*SharedPreferences prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE);;
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();*/
    }

    public void LoginButton(View view){
        if (rutTxt.getText().toString().equals("") || passTxt.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this,"Debe completar todos los campos",Toast.LENGTH_SHORT).show();
        }
        else{
            //Se conecta
            Login.conectarse(rutTxt.getText().toString(), passTxt.getText().toString(), cbRemember, this );
        }
    }

    public void RegistrarButton(View view){
        Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        Intent registrarse = new Intent(MainActivity.this, RegistrarseActivity.class);
        startActivity(registrarse);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.this.setTitle("Login");
    }

    public void loadPreferences(){
        //TextViews.
        rutTxt = (EditText) findViewById(R.id.txtRut);
        passTxt = (EditText) findViewById(R.id.txtPassword);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        //Obtiene las preferencia guardada de usuario/contraseña
        SharedPreferences pref = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String cbRememberChecked;
        cbRememberChecked = pref.getString("cbRememberChecked", "false");
        if (cbRememberChecked.equals("true")){
            String rut = pref.getString("rut", "");
            String pass = pref.getString("pass", "");
            rutTxt.setText(rut);
            passTxt.setText(pass);
            cbRemember.setChecked(true);
        }else{ //Aqui es false
            cbRemember.setChecked(false);
        }
    }

}
