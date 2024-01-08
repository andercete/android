package com.example.comercial;

public class Pedido {

    private String idPedido;
    private String idPartner;
    private String idLinea;
    private String idArticulo;
    private int cantidad;
    private float descuento;
    private float precio;

    // Constructor vacío
    public Pedido() {
    }

    // Constructor con todos los campos
    public Pedido(String idPedido, String idPartner, String idLinea, String idArticulo, int cantidad, float descuento, float precio) {
        this.idPedido = idPedido;
        this.idPartner = idPartner;
        this.idLinea = idLinea;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.precio = precio;
    }

    // Getters y Setters

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(String idPartner) {
        this.idPartner = idPartner;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(String idLinea) {
        this.idLinea = idLinea;
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

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
