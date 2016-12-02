package com.jose.movilizateucn.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jose.movilizateucn.DiagramaClases.Sesion;
import com.jose.movilizateucn.DiagramaClases.Usuario;

//Solo para probar algunas cosas
public class Preferencias {

    public static void borrarPreferencias(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);;
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    public static void cargarPreferenciasLogin(Activity activity, EditText rutTxt, EditText passTxt, CheckBox cbRemember){
        SharedPreferences pref = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
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

    public static void guardarPreferenciasLogin(Activity activity, CheckBox cbRemember, String rut, String contra){
        //Guarda las preferencias si el checkbox está activado, si no guarda vacío.
        SharedPreferences pref = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (cbRemember.isChecked()) {
            editor.putString("rut", rut);
            editor.putString("pass", contra);
            editor.putString("cbRememberChecked", "true");
        }else{
            editor.putString("rut", "");
            editor.putString("pass", "");
            editor.putString("cbRememberChecked", "false");
        }
        editor.commit();
    }

    public static void cargarCalificacion(Activity activity, RatingBar starBar, TextView lblScore, String tipo){
        //Obtiene prefencia de calificación
        SharedPreferences pref = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if (Sesion.exists()) {
            if (tipo.equals("chofer")){
                String calChofer = pref.getString("calificacionChofer[" + Sesion.getUser().getRut() + "]", "4.0");
                starBar.setRating(Float.parseFloat(calChofer));
            }else if (tipo.equals("pasajero")) {
                String calPasajero = pref.getString("calificacionPasajero[" + Sesion.getUser().getRut() + "]", "4.0");
                starBar.setRating(Float.parseFloat(calPasajero));
            }
            Calificacion.updateScore(starBar, lblScore);
        }
    }

    public static void guardarCalificacion(Activity activity, RatingBar ratingBar, String tipo){
        if (Sesion.exists()) {
            SharedPreferences pref = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            Usuario usuario = Sesion.getUser();
            if (tipo.equals("chofer")) {
                editor.putString("calificacionChofer[" + usuario.getRut() + "]", String.valueOf(ratingBar.getRating()));
            } else if (tipo.equals("pasajero")) {
                editor.putString("calificacionPasajero[" + usuario.getRut() + "]", String.valueOf(ratingBar.getRating()));
            }
            editor.commit();
        }
    }


}
