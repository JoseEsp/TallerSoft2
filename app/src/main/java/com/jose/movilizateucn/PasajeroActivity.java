package com.jose.movilizateucn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jose.movilizateucn.DiagramaClases.Login;
import com.jose.movilizateucn.DiagramaClases.Usuario;

public class PasajeroActivity extends AppCompatActivity {

    private TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        configureNameText();
        Login.updateFechaFinConexion(this);
    }

    private void configureNameText(){
        nameText = (TextView) findViewById(R.id.lblNombrePasajero);
        Usuario user = Login.getUsuario();
        if (user != null){
            nameText.setText(user.getNombre());
        }
    }
}
