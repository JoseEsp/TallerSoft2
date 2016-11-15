package com.jose.movilizateucn.DiagramaClases;

//Clase Viaje pero por un lado.
//La nota, comentario y nombre son los que otro usuario ha dicho o puesto de ti
public class Viaje {
    private int codViaje;
    private String fechaViaje;
    private float nota; //La nota que alguien más te ha puesto
    private String comentario; //Comentario que alguien más te ha puesto
    private String nombre; //El otro usuario que puso la nota

    public Viaje(int codViaje, String fechaViaje, float nota, String comentario, String nombre) {
        this.codViaje = codViaje;
        this.fechaViaje = fechaViaje;
        this.nota = nota;
        this.comentario = comentario;
        this.nombre = nombre;
    }

    public int getCodViaje() {
        return codViaje;
    }

    public void setCodViaje(int codViaje) {
        this.codViaje = codViaje;
    }

    public String getFechaViaje() {
        return fechaViaje;
    }

    public void setFechaViaje(String fechaViaje) {
        this.fechaViaje = fechaViaje;
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
