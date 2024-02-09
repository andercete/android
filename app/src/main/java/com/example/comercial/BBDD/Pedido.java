package com.example.comercial.BBDD;

public class Pedido {
    private String idPartner;
    private String idArticulo;
    private int cantidad;
    private String direccion;
    private float descuento;


    private String imageUrl;

    // Constructor vacío
    public Pedido() {
    }

    // Constructor con todos los campos
    public Pedido(String idPartner, String idArticulo, int cantidad, String direccion, float descuento) {
        this.idPartner = idPartner;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.direccion = direccion;
        this.descuento = descuento;
    }

    // Getter y Setter para idPartner
    public String getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(String idPartner) {
        this.idPartner = idPartner;
    }

    // Getter y Setter para idArticulo
    public String getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    // Getter y Setter para cantidad
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Getter y Setter para poblacion
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Getter y Setter para descuento
    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }
}
