package com.jose.movilizateucn.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;
import com.jose.movilizateucn.Util.Internet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void RegistrarButtonReg(View view){
        final TextView txtRut    = (TextView) findViewById(R.id.txtRutReg);
        final TextView txtNombre = (TextView) findViewById(R.id.txtNombreReg);
        final TextView txtContra = (TextView) findViewById(R.id.txtContralReg);
        final TextView txtEmail  = (TextView) findViewById(R.id.txtEmailReg);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);

        String rut = txtRut.getText().toString();
        String nombre = txtNombre.getText().toString();
        String contra = txtContra.getText().toString();
        String email = txtEmail.getText().toString();

        //Si ha ingresado algo al menos
        if (rut.equals("") || nombre.equals("") || contra.equals("") || email.equals("")) {
            Snackbar.make(findViewById(R.id.activity_registrarse), "Complete todos los campos.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("rut", rut);
        map.put("nombre", nombre);
        map.put("contra", contra);
        map.put("email", email);
        JSONObject jsonRegistro = new JSONObject(map);

        final Activity activity = this;
        final View vista = view;
        String url = Url.REGISTRARUSUARIO;
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonObjectRequest(
                Request.Method.POST, url, jsonRegistro,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            switch(response.getString("estado")){
                                case "1":
                                    Toast.makeText(activity, response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                    spinner.setVisibility(View.GONE);
                                    activity.finish();
                                    break;
                                case "2":
                                    Snackbar.make(vista, response.getString("mensaje"), Snackbar.LENGTH_SHORT).show();
                                    spinner.setVisibility(View.GONE);
                                    break;
                            }

                        }catch(JSONException e){
                            spinner.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!Internet.hayConexion(activity)) {
                            Snackbar.make(vista, "Conéctese a Internet y vuelva a intentarlo.", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(vista, "Error inesperado.", Snackbar.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.GONE);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8" + getParamsEncoding();
            }
        });
    }
}
