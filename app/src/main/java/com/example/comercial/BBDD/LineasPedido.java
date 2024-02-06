package com.example.comercial.BBDD;

public class LineasPedido {
    private int idLinea;
    private int idArticulo;
    private int idPedido;
    private int cantidad;
    private double descuento;
    private double precio;

    // Constructor
    public LineasPedido(int idLinea, int idArticulo, int idPedido,
                        int cantidad, double descuento, double precio) {
        this.idLinea = idLinea;
        this.idArticulo = idArticulo;
        this.idPedido = idPedido;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.precio = precio;
    }
    public LineasPedido() {

    }

    // Getter y Setter para idLinea
    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    // Getter y Setter para idArticulo
    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    // Getter y Setter para idPedido
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    // Getter y Setter para cantidad
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Getter y Setter para descuento
    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    // Getter y Setter para precio
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
