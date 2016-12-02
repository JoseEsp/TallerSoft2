package com.jose.movilizateucn.DiagramaClases;

import org.json.JSONObject;

//Clase HistViaje pero por un lado.
//La nota, comentario y nombre son los que otro usuario ha dicho o puesto de ti
public class HistViaje {
    private int codViaje;
    private String fechaSubida;
    private float nota; //La nota que alguien más te ha puesto
    private String comentario; //Comentario que alguien más te ha puesto
    private String nombre; //El otro usuario que puso la nota

    public HistViaje(int codViaje, String fechaSubida, float nota, String comentario, String nombre) {
        this.codViaje = codViaje;
        this.fechaSubida = fechaSubida;
        this.nota = nota;
        this.comentario = comentario;
        this.nombre = nombre;
    }

    public static HistViaje JSonObject_to_HistViaje(JSONObject jsonHistViaje){
        if (jsonHistViaje != null){
            try {
                return new HistViaje(Integer.parseInt(jsonHistViaje.getString("codViaje")),
                        jsonHistViaje.getString("fechaSubida"),
                        Float.parseFloat(jsonHistViaje.getString("nota")),
                        jsonHistViaje.getString("comentario"),
                        jsonHistViaje.getString("nombre")
                );
            }catch (Exception e){
                return null;
            }
        }else{
            return null;
        }
    }

    public int getCodViaje() {
        return codViaje;
    }

    public void setCodViaje(int codViaje) {
        this.codViaje = codViaje;
    }

    public String getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(String fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
}
