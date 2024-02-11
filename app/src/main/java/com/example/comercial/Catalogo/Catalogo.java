package com.example.comercial.Catalogo;

public class Catalogo {
    private int idArticulo; // Cambiado a int para alinearse con la base de datos
    private String nombre;
    private String descripcion;
    private String proveedor;
    private double prVent; // Cambiado a double para consistencia con Articulos
    private double prCost; // Cambiado a double para consistencia con Articulos
    private int existencias;
    private String imageName; // Nombre de la imagen en drawable sin la extensión
    private boolean isSelected = false;
    private int quantity = 1; // Valor inicial

    // Constructor por defecto
    public Catalogo() {
    }

    // Constructor con parámetros
    public Catalogo(int idArticulo, String nombre, String descripcion, String categoria,
                    String proveedor, float prVent, float prCost, int existencias, String imageName) {
        this.idArticulo = idArticulo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.prVent = prVent;
        this.prCost = prCost;
        this.existencias = existencias;
        this.imageName = imageName;
    }

    // Getters y setters
    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = Integer.parseInt(idArticulo);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public float getPrVent() {
        return (float) prVent;
    }

    public void setPrVent(float prVent) {
        this.prVent = prVent;
    }

    public float getPrCost() {
        return (float) prCost;
    }

    public void setPrCost(double prCost) {
        this.prCost = prCost;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    public String getImageName() {
        return imageName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Métodos adicionales para manejar la cantidad de un artículo
    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

}
