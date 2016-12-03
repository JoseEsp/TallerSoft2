package com.jose.movilizateucn.DiagramaClases;

public class Viaje {
    private int codViaje;
    private Chofer chofer;

    public Viaje(int codViaje, Chofer chofer) {
        this.codViaje = codViaje;
        this.chofer = chofer;
    }

    public int getCodViaje() {
        return codViaje;
    }

    public void setCodViaje(int codViaje) {
        this.codViaje = codViaje;
    }

    public Chofer getChofer() {
        return chofer;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }
}

