package com.jose.movilizateucn.Util;

import com.google.android.gms.maps.model.LatLng;

public class Distancia {
    public static double distanciaCoord(LatLng l1, LatLng l2) {
        double radioTierra = 6371;//en kilómetros
        double dLat = Math.toRadians(l2.latitude - l1.latitude);
        double dLng = Math.toRadians(l2.longitude - l1.longitude);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(l1.latitude)) * Math.cos(Math.toRadians(l2.latitude));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
    }

}
