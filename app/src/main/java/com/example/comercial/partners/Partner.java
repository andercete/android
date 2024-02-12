package com.example.comercial.partners;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Partner {
    private int idPartner;
    private int idZona;
    private String nombre;
    private String cif;
    private String direccion;
    private String telefono;
    private String correo;
    private String fechaRegistro;

    // Constructor

    public Partner(int idPartner, int idZona, String nombre, String cif,
                   String direccion, String telefono, String correo,
                   String fechaRegistro) {
        this.idPartner = idPartner;
        this.idZona = idZona;
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
    }
    public Partner(int idZona, String nombre, String cif,
                   String direccion, String telefono, String correo,
                   String fechaRegistro) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.cif = cif;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
    }
    public Partner() {
    }
    // Getter y Setter para idPartner
    public int getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(int idPartner) {
        this.idPartner = idPartner;
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

    // Getter y Setter para cif
    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    // Getter y Setter para direccion
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Getter y Setter para telefono
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Getter y Setter para correo
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Getter y Setter para fechaRegistro
    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
