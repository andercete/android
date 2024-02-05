package com.example.comercial.BBDD;

public class Comerciales {
    private int idComercial;
    private int idZona;
    private String nombre;
    private String apellidos;
    private String contra;
    private String correo;
    private String direccion;
    private String dni;
    private String telefono;

    // Constructor
    public Comerciales(int idComercial, int idZona, String nombre, String apellidos,
                       String contra, String correo, String direccion,
                       String dni, String telefono) {
        this.idComercial = idComercial;
        this.idZona = idZona;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contra = contra;
        this.correo = correo;
        this.direccion = direccion;
        this.dni = dni;
        this.telefono = telefono;
    }
    public  Comerciales(){

    }

    // Getter y Setter para idComercial
    public int getIdComercial() {
        return idComercial;
    }

    public void setIdComercial(int idComercial) {
        this.idComercial = idComercial;
    }

    // Getter y Setter para idZona
    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    // Getter y Setter para nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter y Setter para apellidos
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    // Getter y Setter para contraseña
    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    // Getter y Setter para correo
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Getter y Setter para direccion
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Getter y Setter para dni
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    // Getter y Setter para telefono
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
