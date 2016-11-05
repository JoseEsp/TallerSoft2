package com.jose.movilizateucn.DiagramaClases;

public class Chofer extends Usuario{
    private int cantAsientos;
    private String colorAuto;

    public Chofer(String rut, String nombre, String email, String contra, int cantAsientos, String colorAuto){
        super(rut, nombre, email, contra);
        this.cantAsientos = cantAsientos;
        this.colorAuto = colorAuto;
    }

    public int getCantAsientos() {
        return cantAsientos;
    }

    public void setCantAsientos(int cantAsientos) {
        this.cantAsientos = cantAsientos;
    }

    public String getColorAuto() {
        return colorAuto;
    }

    public void setColorAuto(String colorAuto) {
        this.colorAuto = colorAuto;
    }
}
