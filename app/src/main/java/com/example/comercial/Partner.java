package com.example.comercial;

public class Partner {

    private String nombre;
    private String direccion;
    private String poblacion;
    private String cif;
    private int telefono;
    private String email;
    private String comercial;


    // Constructor vacío
    public Partner() {
    }

    // Constructor con todos los campos
    public Partner(String nombre, String direccion, String poblacion, String cif, int telefono, String email, String comercial) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.poblacion = poblacion;
        this.cif = cif;
        this.telefono = telefono;
        this.email = email;
        this.comercial = comercial;
    }

    // Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setComercial(String comercial) {
        this.comercial = comercial;
    }

    public String getComercial() {
        return comercial;
    }
}

