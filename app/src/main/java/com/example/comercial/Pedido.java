package com.example.comercial;

public class Pedido {

    private String idPartner;
    private String idArticulo;
    private int cantidad;
    private  String poblacion;
    private float descuento;

    // Constructor vacío
    public Pedido() {
    }

    // Constructor con todos los campos
    public Pedido( String idPartner, String idArticulo, int cantidad, String poblacion, float descuento) {
        this.idPartner = idPartner;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.poblacion = poblacion;
        this.descuento = descuento;
    }

    // Getters y Setters



    public String getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(String idPartner) {
        this.idPartner = idPartner;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String  poblacion) {
        this.poblacion = poblacion;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

}
