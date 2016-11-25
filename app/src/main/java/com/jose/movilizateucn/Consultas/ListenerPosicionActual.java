package com.jose.movilizateucn.Consultas;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.jose.movilizateucn.GenerarSolicitudMap;

public class ListenerPosicionActual implements LocationListener {

    GenerarSolicitudMap gsm;

    public ListenerPosicionActual(GenerarSolicitudMap gsm){
        this.gsm = gsm;
    }

    @Override
    public void onLocationChanged(Location location) {
        gsm.setOrigen(location);
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
