package com.example.aplicacion_notas;

public class Notas {


    private int id;
    private String titulo;
    private String contenido;
    private String fechacreacion;
    private String categoria;

    public Notas(int id, String titulo , String contenido, String fechacreacion , String categoria){

        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechacreacion = fechacreacion;
        this.categoria = categoria;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(String fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }



}
