package com.jose.movilizateucn;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jose.movilizateucn.Consultas.Consulta;
import com.jose.movilizateucn.DiagramaClases.Usuario;

public class RegistrarseActivity extends AppCompatActivity {

    private Button btnRegistrar;
    private TextView txtRut;
    private TextView txtNombre;
    private TextView txtContra;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        configureTextViews();
        configureBtnRegistrarReg();
    }

    private void configureTextViews(){
        txtRut    = (TextView) findViewById(R.id.txtRutReg);
        txtNombre = (TextView) findViewById(R.id.txtNombreReg);
        txtContra = (TextView) findViewById(R.id.txtContralReg);
        txtEmail  = (TextView) findViewById(R.id.txtEmailReg);
    }

    private void configureBtnRegistrarReg(){
        final AppCompatActivity thisActivity = this;
        final Intent loginActivity = new Intent(this, MainActivity.class);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarReg);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si ha ingresado algo al menos
                if (checkTextViews()){
                    //Crea un usuario.
                    Usuario usuario = new Usuario(txtRut.getText().toString(),
                                                  txtNombre.getText().toString(),
                                                  txtEmail.getText().toString(),
                                                  txtContra.getText().toString());
                    //Inserta el usuario (Acá se debería checkear si el usuario existe)
                    //V1.0
                    Consulta.insertarUsuario(usuario, thisActivity);
                }
            }
        });
    }

    /**
     * Checkea si ha ingresado algo en cada textView.
     * v1.0
     * @return Si los CheckViews están en el formato correcto.
     */
    private boolean checkTextViews(){
        if (txtRut.getText().length() < 1){
            Toast.makeText(this, "Rut inválido",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtNombre.getText().length() < 1){
            Toast.makeText(this, "Ingrese Nombre de Usuario",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtEmail.getText().length() < 1){
            Toast.makeText(this, "Ingrese un Email",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtContra.getText().length() < 1){
            Toast.makeText(this, "Ingrese una Contraseña",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
