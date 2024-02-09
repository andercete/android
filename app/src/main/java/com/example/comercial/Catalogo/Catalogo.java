package com.example.comercial.Catalogo;

public class Catalogo {
    private String idArticulo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String proveedor;
    private float prVenta;
    private float prCoste;
    private int existencias;
    private String imageName; // Nombre de la imagen en drawable sin la extensión
    private boolean isSelected = false;

    private int quantity = 1; // Valor inicial



    public Catalogo() {
    }
    // Constructor
    public Catalogo(String idArticulo, String nombre, String descripcion, String categoria,
                    String proveedor, float prVenta, float prCoste, int existencias, String imageName) {
        this.idArticulo = idArticulo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.prVenta = prVenta;
        this.prCoste = prCoste;
        this.existencias = existencias;
        this.imageName = imageName;
    }

    // Getters
    public String getIdArticulo() {
        return idArticulo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public float getPrVenta() {
        return prVenta;
    }

    public float getPrCoste() {
        return prCoste;
    }

    public int getExistencias() {
        return existencias;
    }

    public String getImageName() {
        return imageName;
    }

    // Setters
    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setPrVenta(float prVenta) {
        this.prVenta = prVenta;
    }

    public void setPrCoste(float prCoste) {
        this.prCoste = prCoste;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    public int getCantidad() {
        return this.quantity;
    }
}

