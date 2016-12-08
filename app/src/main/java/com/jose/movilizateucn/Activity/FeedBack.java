package com.jose.movilizateucn.Activity;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.R;
import com.jose.movilizateucn.Util.Internet;
import com.jose.movilizateucn.Volley.Url;
import com.jose.movilizateucn.Volley.VolleySingleton;

import org.json.JSONObject;

public class FeedBack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
    }

    public void EnviarFeedBack(View view){
        String msjFeedBack = ((EditText) findViewById(R.id.mensajeFeedBack)).getText().toString();
        if (msjFeedBack.replace(" ", "").equals("")){
            Snackbar.make(view, "Por favor, ingrese un mensaje", Snackbar.LENGTH_SHORT).show();
        }else{
            String url = Url.GENERARFEEDBACK;
            if (Sesion.exists()) {
                url += "?rut=" + Sesion.getUser().getRut() + "&comentario=" + msjFeedBack;
            }
            final View vista = view;
            final Activity activity = this;
            VolleySingleton.getInstance(this).addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(activity, "Mensaje Enviado", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (!Internet.hayConexion(activity)){
                                    Snackbar.make(vista, "Conéctese a Internet y vuelva a intentarlo.", Snackbar.LENGTH_SHORT).show();
                                }else {
                                    Snackbar.make(vista, "Error al enviar mensaje", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                    })
            );
        }
    }


}
