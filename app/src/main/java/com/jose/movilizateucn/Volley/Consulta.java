package com.jose.movilizateucn.Volley;

import android.support.v4.app.FragmentActivity;

import com.jose.movilizateucn.DiagramaClases.*;

import org.json.JSONObject;

import java.util.HashMap;

public class Consulta {

    public static void insertarSolicitud(Solicitud solicitud, final FragmentActivity activity){
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("codEstado", solicitud.getEstado() + "");
        map.put("lat", solicitud.getLatitud() + "");
        map.put("lon", solicitud.getLongitud() + "");
        map.put("rutPasajero", solicitud.getPasajero().getRut());

        JSONObject jobject = new JSONObject(map);
        ConsultasGenerales.insertarSolicitudMapa(Url.INSERTARSOLICITUD,
                activity,
                jobject,
                "Error al generar solicitud!",
                solicitud);
    }

    public static void desactivarSolicitud(Solicitud solicitud, FragmentActivity activity){
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("codSolicitud", solicitud.getCodSolicitud() + "");
        JSONObject jobject = new JSONObject(map);
        ConsultasGenerales.actualizarDato(Url.DESACTIVARSOLICITUD,
                activity,
                jobject);
    }

    public static void activarSolicitud(Solicitud solicitud, FragmentActivity activity){
        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("codSolicitud", solicitud.getCodSolicitud() + "");
        JSONObject jobject = new JSONObject(map);
        ConsultasGenerales.actualizarDato(Url.ACTIVARSOLICITUD,
                activity,
                jobject);
    }

}
