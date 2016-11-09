package com.jose.movilizateucn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jose.movilizateucn.Consultas.Consulta;
import com.jose.movilizateucn.DiagramaClases.Usuario;

public class RegistrarseActivity extends AppCompatActivity {

    private TextView txtRut;
    private TextView txtNombre;
    private TextView txtContra;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void RegistrarButtonReg(View view){
        txtRut    = (TextView) findViewById(R.id.txtRutReg);
        txtNombre = (TextView) findViewById(R.id.txtNombreReg);
        txtContra = (TextView) findViewById(R.id.txtContralReg);
        txtEmail  = (TextView) findViewById(R.id.txtEmailReg);

        //Si ha ingresado algo al menos
        if (txtRut.getText().toString().equals("") || txtNombre.getText().toString().equals("") || txtEmail.getText().toString().equals("") || txtContra.getText().toString().equals("")) {
            Toast.makeText(RegistrarseActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }
        else{
            //Crea un usuario.
            Usuario usuario = new Usuario(txtRut.getText().toString(),
                                          txtNombre.getText().toString(),
                                          txtEmail.getText().toString(),
                                          txtContra.getText().toString());
            Consulta.insertarUsuario(usuario, RegistrarseActivity.this);
        }
    }
}
