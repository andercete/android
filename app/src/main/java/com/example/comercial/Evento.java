package com.example.comercial;

// Evento.java
public class Evento {
    private String titulo;
    private String fecha;
    private String ubicacion;
    private String descripcion;

    public Evento(String titulo, String fecha, String ubicacion, String descripcion) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}