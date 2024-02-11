package com.example.comercial.BBDD;

public class Catalogo {
    private int idArticulo;
    private String nombre;
    private String descripcion;
    private String proveedor;
    private double pvVent;
    private double pvCost;
    private int existencias;
    private String direccionImagen;
    private boolean isSelected = false;
    private int quantity = 1; // Valor inicial


    // Constructor
    public Catalogo(int idArticulo, String nombre, String descripcion,
                      String proveedor, double pvVent,
                     double pvCost, int existencias, String direccionImagen) {
        this.idArticulo = idArticulo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.pvVent = pvVent;
        this.pvCost = pvCost;
        this.existencias = existencias;
        this.direccionImagen = direccionImagen;
    }
    public Catalogo() {

    }

    // Getter y Setter para idArticulo
    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    // Getter y Setter para nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter para descripcion
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter y Setter para proveedor
    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    // Getter y Setter para pvVent
    public double getPvVent() {
        return pvVent;
    }

    public void setPvVent(double pvVent) {
        this.pvVent = pvVent;
    }

    // Getter y Setter para pvCost
    public double getPvCost() {
        return pvCost;
    }

    public void setPvCost(double pvCost) {
        this.pvCost = pvCost;
    }

    // Getter y Setter para existencias
    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    // Getter y Setter para direccionImagen
    public String getDireccionImagen() {
        return direccionImagen;
    }

    public void setDireccionImagen(String direccionImagen) {
        this.direccionImagen = direccionImagen;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
}