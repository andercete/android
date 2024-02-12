package com.example.comercial.BBDD;

import java.util.List;

public class CabPedidos {
    private int IdPedido;
    private int IdPartner;
    private int IdComercial;
    private String FechaPedido;
    private List<LineasPedido> lineasPedido; // Añade esta línea

    public CabPedidos() {

    }
    // Constructor
    public CabPedidos(int idPedido, int idPartner, int idComercial, String fechaPedido) {
        IdPedido = idPedido;
        IdPartner = idPartner;
        IdComercial = idComercial;
        FechaPedido = fechaPedido;
    }

    // Getters y Setters
    public int getIdPedido() {
        return IdPedido;
    }

    public void setIdPedido(int idPedido) {
        IdPedido = idPedido;
    }
    public List<LineasPedido> getLineasPedido() {
        return lineasPedido;
    }

    public void setLineasPedido(List<LineasPedido> lineasPedido) {
        this.lineasPedido = lineasPedido;
    }

    public int getIdPartner() {
        return IdPartner;
    }

    public void setIdPartner(int idPartner) {
        IdPartner = idPartner;
    }

    public int getIdComercial() {
        return IdComercial;
    }

    public void setIdComercial(int idComercial) {
        IdComercial = idComercial;
    }

    public String getFechaPedido() {
        return FechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        FechaPedido = fechaPedido;
    }

    // Método toString() para imprimir los detalles del CabPedido
    @Override
    public String toString() {
        return "CabPedido{" +
                "IdPedido=" + IdPedido +
                ", IdPartner=" + IdPartner +
                ", IdComercial=" + IdComercial +
                ", FechaPedido='" + FechaPedido + '\'' +
                '}';
    }
}

