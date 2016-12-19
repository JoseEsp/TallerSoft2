package com.jose.movilizateucn.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.DiagramaClases.Usuario;
import com.jose.movilizateucn.Util.Internet;
import com.jose.movilizateucn.Util.Preferencias;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText rutTxt;
    private EditText passTxt;
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextViews.
        rutTxt = (EditText) findViewById(R.id.txtRut);
        passTxt = (EditText) findViewById(R.id.txtPassword);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        Preferencias.cargarPreferenciasLogin(this, rutTxt, passTxt, cbRemember);
    }

    public void LoginButton(View view){
        if (rutTxt.getText().toString().equals("") || passTxt.getText().toString().equals("")) {
            Snackbar.make(view,"Debe completar todos los campos",Snackbar.LENGTH_SHORT).show();
        }
        else{
            final View vista = view;
            final Activity activity = this;
            final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);

            final String rut = rutTxt.getText().toString();
            final String contra = passTxt.getText().toString();

            spinner.setVisibility(View.VISIBLE);
            spinner.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

            String url = String.format("%s?rut=%s&contra=%s", Url.OBTENERUSUARIOLOGUEADO, rut, contra);
            VolleySingleton.getInstance(activity).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                    Usuario usuario = Usuario.JSonObject_to_Usuario(response);
                                if (usuario != null){
                                    if (usuario.getEnableS_N().equals("S")){
                                        Preferencias.guardarPreferenciasLogin(activity, cbRemember, rut, contra);
                                        Sesion.setUsuario(usuario);
                                        //Envía el Token
                                        String url = Url.ENVIARTOKEN + "?rut=" + rut + "&token=" + FirebaseInstanceId.getInstance().getToken();
                                        VolleySingleton.getInstance(activity).addToRequestQueue(
                                                new JsonObjectRequest(Request.Method.GET, url, null, null)
                                        );
                                        final Intent perfilActivity = new Intent(activity, EscogerPerfilActivity.class);
                                        activity.startActivity(perfilActivity);
                                    }else{
                                        Snackbar.make(vista, "Usuario Bloqueado.", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                                spinner.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (!Internet.hayConexion(activity)){
                                    Snackbar.make(vista, "Conéctese a Internet y vuelva a intentarlo.", Snackbar.LENGTH_SHORT).show();
                                }else{
                                    Snackbar.make(vista, "Datos inválidos.", Snackbar.LENGTH_SHORT).show();
                                }
                                spinner.setVisibility(View.GONE);
                            }
                        }
                ));
        }
    }

    public void RegistrarButton(View view){
        Button btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        Intent registrarse = new Intent(MainActivity.this, RegistrarseActivity.class);
        startActivity(registrarse);
    }

}
