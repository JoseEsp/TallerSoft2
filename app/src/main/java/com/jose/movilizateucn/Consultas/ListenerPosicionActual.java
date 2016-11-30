package com.jose.movilizateucn.Consultas;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.jose.movilizateucn.GenerarSolicitudMap;
import com.jose.movilizateucn.IniciarRutaActivity;

public class ListenerPosicionActual implements LocationListener {

    GenerarSolicitudMap gsm;
    IniciarRutaActivity ira;

    public ListenerPosicionActual(GenerarSolicitudMap gsm){
        this.gsm = gsm;
        ira = null;
    }
    public ListenerPosicionActual(IniciarRutaActivity ira){
        this.ira = ira;
        gsm = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (gsm != null){
            gsm.setOrigen(location);
        }else if (ira != null) {
            ira.setOrigen(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
