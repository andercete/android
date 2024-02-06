package com.example.comercial.BBDD;

public class Zonas {
    private int idZona;
    private String descripcion;

    public Zonas() {}

    public Zonas(int idZona, String descripcion) {
        this.idZona = idZona;
        this.descripcion = descripcion;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
