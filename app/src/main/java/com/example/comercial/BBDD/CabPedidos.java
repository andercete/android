package com.example.comercial.BBDD;

public class CabPedidos {
    private int idPedido;
    private int idPartner;
    private int idComercial;
    private String fechaPedido;

    // Constructor
    public CabPedidos(int idPedido, int idPartner, int idComercial, String fechaPedido) {
        this.idPedido = idPedido;
        this.idPartner = idPartner;
        this.idComercial = idComercial;
        this.fechaPedido = fechaPedido;
    }
    public CabPedidos() {

    }

    // Getter y Setter para idPedido
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    // Getter y Setter para idPartner
    public int getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(int idPartner) {
        this.idPartner = idPartner;
    }

    // Getter y Setter para idComercial
    public int getIdComercial() {
        return idComercial;
    }

    public void setIdComercial(int idComercial) {
        this.idComercial = idComercial;
    }

    // Getter y Setter para fechaPedido
    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }
}
