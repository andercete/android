package com.example.comercial.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.comercial.calendario.Evento;
import com.example.comercial.partners.Partner;

import java.util.ArrayList;
import java.util.List;

public class AnderBD extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "comercial.db";
    private static final int DATABASE_VERSION = 1;

    // Creación de tablas
    private static final String CREATE_TABLE_ZONAS = "CREATE TABLE ZONAS (" +
            "IdZona INTEGER PRIMARY KEY," +
            "Descripcion TEXT" +
            ")";

    private static final String CREATE_TABLE_COMERCIALES = "CREATE TABLE COMERCIALES (" +
            "IdComercial INTEGER PRIMARY KEY," +
            "IdZona INTEGER," +
            "Nombre TEXT," +
            "Apellidos TEXT," +
            "Contraseña TEXT," +
            "Correo TEXT," +
            "Direccion TEXT," +
            "DNI TEXT," +
            "Telefono TEXT," +
            "FOREIGN KEY(IdZona) REFERENCES ZONAS(IdZona)" +
            ")";

    private static final String CREATE_TABLE_PARTNERS = "CREATE TABLE PARTNERS (" +
            "IdPartner INTEGER PRIMARY KEY," +
            "IdZona INTEGER," +
            "Nombre TEXT," +
            "CIF TEXT," +
            "Direccion TEXT," +
            "Telefono TEXT," +
            "Correo TEXT," +
            "FechaRegistro TEXT," +
            "FOREIGN KEY(IdZona) REFERENCES ZONAS(IdZona)" +
            ")";

    private static final String CREATE_TABLE_ARTICULOS = "CREATE TABLE ARTICULOS (" +
            "IdArticulo INTEGER PRIMARY KEY," +
            "Nombre TEXT," +
            "Descripcion TEXT," +
            "Categoria TEXT," +
            "Proveedor TEXT," +
            "PvVent REAL," +
            "PvCost REAL," +
            "Existencias INTEGER," +
            "Direccion_Imagen TEXT" +
            ")";

    private static final String CREATE_TABLE_CAB_PEDIDOS = "CREATE TABLE CAB_PEDIDOS (" +
            "IdPedido INTEGER PRIMARY KEY," +
            "IdPartner INTEGER," +
            "IdComercial INTEGER," +
            "FechaPedido TEXT," +
            "FOREIGN KEY(IdPartner) REFERENCES PARTNERS(IdPartner)," +
            "FOREIGN KEY(IdComercial) REFERENCES COMERCIALES(IdComercial)" +
            ")";

    private static final String CREATE_TABLE_LINEAS_PEDIDO = "CREATE TABLE LINEAS_PEDIDO (" +
            "IdLinea INTEGER PRIMARY KEY," +
            "IdArticulo INTEGER," +
            "IdPedido INTEGER," +
            "Cantidad INTEGER," +
            "Descuento REAL," +
            "Precio REAL," +
            "FOREIGN KEY(IdArticulo) REFERENCES ARTICULOS(IdArticulo)," +
            "FOREIGN KEY(IdPedido) REFERENCES CAB_PEDIDOS(IdPedido)" +
            ")";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE EVENTOS (" +
            "IdEvento INTEGER PRIMARY KEY," +
            "Title TEXT," +
            "Location TEXT," +
            "Date DATE," +
            "Time TEXT," +
            "Description TEXT" +
            ")";

    public AnderBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ZONAS);
        db.execSQL(CREATE_TABLE_COMERCIALES);
        db.execSQL(CREATE_TABLE_PARTNERS);
        db.execSQL(CREATE_TABLE_ARTICULOS);
        db.execSQL(CREATE_TABLE_CAB_PEDIDOS);
        db.execSQL(CREATE_TABLE_LINEAS_PEDIDO);
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas existentes
        db.execSQL("DROP TABLE IF EXISTS LINEAS_PEDIDO");
        db.execSQL("DROP TABLE IF EXISTS CAB_PEDIDOS");
        db.execSQL("DROP TABLE IF EXISTS ARTICULOS");
        db.execSQL("DROP TABLE IF EXISTS PARTNERS");
        db.execSQL("DROP TABLE IF EXISTS COMERCIALES");
        db.execSQL("DROP TABLE IF EXISTS ZONAS");
        db.execSQL("DROP TABLE IF EXISTS EVENTS");
        // Crear nuevas tablas
        onCreate(db);
    }

    public List<Evento> getAllEventos() {
        List<Evento> eventosList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EVENTOS", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("IdEvento"));
                String title = cursor.getString(cursor.getColumnIndex("Title"));
                String location = cursor.getString(cursor.getColumnIndex("Location"));
                String date = cursor.getString(cursor.getColumnIndex("Date"));
                String time = cursor.getString(cursor.getColumnIndex("Time"));
                String description = cursor.getString(cursor.getColumnIndex("Description"));

                Evento evento = new Evento(id, title, location, date, time, description);
                eventosList.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eventosList;
    }

    // Método para agregar un nuevo evento
    public long addEvento(Evento evento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", evento.getTitle());
        values.put("Location", evento.getLocation());
        values.put("Date", evento.getDate());
        values.put("Time", evento.getTime());
        values.put("Description", evento.getDescription());

        // Inserta el nuevo evento y obtén el ID
        long id = db.insert("EVENTOS", null, values);

        // Cierra la conexión a la base de datos
        db.close();

        return id;
    }

    // Método para eliminar un evento por su ID
    public void eliminarEvento(int eventoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("EVENTOS", "IdEvento = ?", new String[]{String.valueOf(eventoId)});
        db.close();
    }


    // Métodos CRUD para la tabla PARTNERS
// Método para agregar un nuevo partner
    public long addPartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdZona", partner.getIdZona());
        values.put("Nombre", partner.getNombre());
        values.put("CIF", partner.getCif());
        values.put("Direccion", partner.getDireccion());
        values.put("Telefono", partner.getTelefono());
        values.put("Correo", partner.getCorreo());
        values.put("FechaRegistro", partner.getFechaRegistro());

        // Insertar fila
        long id = db.insert("PARTNERS", null, values);
        db.close();
        return id;
    }

    // Método para obtener un partner
    public Partner getPartner(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "IdPartner", "IdZona", "Nombre", "CIF", "Direccion", "Telefono", "Correo", "FechaRegistro"
        };

        Cursor cursor = db.query("PARTNERS", columns, "IdPartner = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Partner partner = null;
        if (cursor != null && cursor.moveToFirst()) {
            partner = new Partner();

            int idPartnerIndex = cursor.getColumnIndex("IdPartner");
            if (idPartnerIndex != -1) {
                partner.setIdPartner(cursor.getInt(idPartnerIndex));
            }

            int idZonaIndex = cursor.getColumnIndex("IdZona");
            if (idZonaIndex != -1) {
                partner.setIdZona(cursor.getInt(idZonaIndex));
            }

            int nombreIndex = cursor.getColumnIndex("Nombre");
            if (nombreIndex != -1) {
                partner.setNombre(cursor.getString(nombreIndex));
            }

            int cifIndex = cursor.getColumnIndex("CIF");
            if (cifIndex != -1) {
                partner.setCif(cursor.getString(cifIndex));
            }

            int direccionIndex = cursor.getColumnIndex("Direccion");
            if (direccionIndex != -1) {
                partner.setDireccion(cursor.getString(direccionIndex));
            }

            int telefonoIndex = cursor.getColumnIndex("Telefono");
            if (telefonoIndex != -1) {
                partner.setTelefono(cursor.getString(telefonoIndex));
            }

            int correoIndex = cursor.getColumnIndex("Correo");
            if (correoIndex != -1) {
                partner.setCorreo(cursor.getString(correoIndex));
            }

            int fechaRegistroIndex = cursor.getColumnIndex("FechaRegistro");
            if (fechaRegistroIndex != -1) {
                partner.setFechaRegistro(cursor.getString(fechaRegistroIndex));
            }

            cursor.close();
        }
        db.close();
        return partner;
    }

    // Add this method for deleting a partner by object
    public void deletePartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PARTNERS", "IdPartner = ?", new String[]{String.valueOf(partner.getIdPartner())});
        db.close();
    }

    // Método para actualizar un partner
    public int updatePartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdZona", partner.getIdZona());
        values.put("Nombre", partner.getNombre());
        values.put("CIF", partner.getCif());
        values.put("Direccion", partner.getDireccion());
        values.put("Telefono", partner.getTelefono());
        values.put("Correo", partner.getCorreo());
        values.put("FechaRegistro", partner.getFechaRegistro());

        // Actualizar fila
        return db.update("PARTNERS", values, "IdPartner = ?",
                new String[]{String.valueOf(partner.getIdPartner())});
    }

    // Método para borrar un partner
    public void deletePartner(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PARTNERS", "IdPartner = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener todos los partners
    public List<Partner> getAllPartners() {
        List<Partner> partnerList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM PARTNERS";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Partner partner = new Partner();

                int idPartnerIndex = cursor.getColumnIndex("IdPartner");
                if (idPartnerIndex != -1) {
                    partner.setIdPartner(cursor.getInt(idPartnerIndex));
                }

                int idZonaIndex = cursor.getColumnIndex("IdZona");
                if (idZonaIndex != -1) {
                    partner.setIdZona(cursor.getInt(idZonaIndex));
                }

                int nombreIndex = cursor.getColumnIndex("Nombre");
                if (nombreIndex != -1) {
                    partner.setNombre(cursor.getString(nombreIndex));
                }

                int cifIndex = cursor.getColumnIndex("CIF");
                if (cifIndex != -1) {
                    partner.setCif(cursor.getString(cifIndex));
                }

                int direccionIndex = cursor.getColumnIndex("Direccion");
                if (direccionIndex != -1) {
                    partner.setDireccion(cursor.getString(direccionIndex));
                }

                int telefonoIndex = cursor.getColumnIndex("Telefono");
                if (telefonoIndex != -1) {
                    partner.setTelefono(cursor.getString(telefonoIndex));
                }

                int correoIndex = cursor.getColumnIndex("Correo");
                if (correoIndex != -1) {
                    partner.setCorreo(cursor.getString(correoIndex));
                }

                int fechaRegistroIndex = cursor.getColumnIndex("FechaRegistro");
                if (fechaRegistroIndex != -1) {
                    partner.setFechaRegistro(cursor.getString(fechaRegistroIndex));
                }

                partnerList.add(partner);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return partnerList;
    }








    public long addZona(Zonas zona) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Descripcion", zona.getDescripcion());

        // Insertar fila
        long id = db.insert("ZONAS", null, values);
        db.close();
        return id;
    }

    // Método para obtener una zona por su ID
    public Zonas getZona(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "IdZona", "Descripcion"
        };

        Cursor cursor = db.query("ZONAS", columns, "IdZona = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Zonas zona = null;
        if (cursor != null && cursor.moveToFirst()) {
            zona = new Zonas();

            int idZonaIndex = cursor.getColumnIndex("IdZona");
            if (idZonaIndex != -1) {
                zona.setIdZona(cursor.getInt(idZonaIndex));
            }

            int descripcionIndex = cursor.getColumnIndex("Descripcion");
            if (descripcionIndex != -1) {
                zona.setDescripcion(cursor.getString(descripcionIndex));
            }

            cursor.close();
        }
        db.close();
        return zona;
    }

    // Método para actualizar una zona
    public int updateZona(Zonas zona) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Descripcion", zona.getDescripcion());

        // Actualizar fila
        return db.update("ZONAS", values, "IdZona = ?",
                new String[]{String.valueOf(zona.getIdZona())});
    }

    // Método para borrar una zona por su ID
    public void deleteZona(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ZONAS", "IdZona = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener todas las zonas
    public List<Zonas> getAllZonas() {
        List<Zonas> zonaList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM ZONAS";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Zonas zona = new Zonas();

                int idZonaIndex = cursor.getColumnIndex("IdZona");
                if (idZonaIndex != -1) {
                    zona.setIdZona(cursor.getInt(idZonaIndex));
                }

                int descripcionIndex = cursor.getColumnIndex("Descripcion");
                if (descripcionIndex != -1) {
                    zona.setDescripcion(cursor.getString(descripcionIndex));
                }

                zonaList.add(zona);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return zonaList;
    }
    // Método para agregar un nuevo comercial
    public long addComercial(Comerciales comercial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdZona", comercial.getIdZona());
        values.put("Nombre", comercial.getNombre());
        values.put("Apellidos", comercial.getApellidos());
        values.put("Contraseña", comercial.getContra());
        values.put("Correo", comercial.getCorreo());
        values.put("Direccion", comercial.getDireccion());
        values.put("DNI", comercial.getDni());
        values.put("Telefono", comercial.getTelefono());

        // Insertar fila
        long id = db.insert("COMERCIALES", null, values);
        db.close();
        return id;
    }

    // Método para obtener un comercial por su ID
    public Comerciales getComercial(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "IdComercial", "IdZona", "Nombre", "Apellidos", "Contraseña", "Correo", "Direccion", "DNI", "Telefono"
        };

        Cursor cursor = db.query("COMERCIALES", columns, "IdComercial = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Comerciales comercial = null;
        if (cursor != null && cursor.moveToFirst()) {
            comercial = new Comerciales();

            int idComercialIndex = cursor.getColumnIndex("IdComercial");
            if (idComercialIndex != -1) {
                comercial.setIdComercial(cursor.getInt(idComercialIndex));
            }

            int idZonaIndex = cursor.getColumnIndex("IdZona");
            if (idZonaIndex != -1) {
                comercial.setIdZona(cursor.getInt(idZonaIndex));
            }

            int nombreIndex = cursor.getColumnIndex("Nombre");
            if (nombreIndex != -1) {
                comercial.setNombre(cursor.getString(nombreIndex));
            }

            int apellidosIndex = cursor.getColumnIndex("Apellidos");
            if (apellidosIndex != -1) {
                comercial.setApellidos(cursor.getString(apellidosIndex));
            }

            int contraseñaIndex = cursor.getColumnIndex("Contraseña");
            if (contraseñaIndex != -1) {
                comercial.setContra(cursor.getString(contraseñaIndex));
            }

            int correoIndex = cursor.getColumnIndex("Correo");
            if (correoIndex != -1) {
                comercial.setCorreo(cursor.getString(correoIndex));
            }

            int direccionIndex = cursor.getColumnIndex("Direccion");
            if (direccionIndex != -1) {
                comercial.setDireccion(cursor.getString(direccionIndex));
            }

            int dniIndex = cursor.getColumnIndex("DNI");
            if (dniIndex != -1) {
                comercial.setDni(cursor.getString(dniIndex));
            }

            int telefonoIndex = cursor.getColumnIndex("Telefono");
            if (telefonoIndex != -1) {
                comercial.setTelefono(cursor.getString(telefonoIndex));
            }

            cursor.close();
        }
        db.close();
        return comercial;
    }

    // Método para actualizar un comercial
    public int updateComercial(Comerciales comercial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdZona", comercial.getIdZona());
        values.put("Nombre", comercial.getNombre());
        values.put("Apellidos", comercial.getApellidos());
        values.put("Contraseña", comercial.getContra());
        values.put("Correo", comercial.getCorreo());
        values.put("Direccion", comercial.getDireccion());
        values.put("DNI", comercial.getDni());
        values.put("Telefono", comercial.getTelefono());

        // Actualizar fila
        return db.update("COMERCIALES", values, "IdComercial = ?",
                new String[]{String.valueOf(comercial.getIdComercial())});
    }

    // Método para borrar un comercial por su ID
    public void deleteComercial(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("COMERCIALES", "IdComercial = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener todos los comerciales
    public List<Comerciales> getAllComerciales() {
        List<Comerciales> comercialList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM COMERCIALES";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Comerciales comercial = new Comerciales();

                int idComercialIndex = cursor.getColumnIndex("IdComercial");
                if (idComercialIndex != -1) {
                    comercial.setIdComercial(cursor.getInt(idComercialIndex));
                }

                int idZonaIndex = cursor.getColumnIndex("IdZona");
                if (idZonaIndex != -1) {
                    comercial.setIdZona(cursor.getInt(idZonaIndex));
                }

                int nombreIndex = cursor.getColumnIndex("Nombre");
                if (nombreIndex != -1) {
                    comercial.setNombre(cursor.getString(nombreIndex));
                }

                int apellidosIndex = cursor.getColumnIndex("Apellidos");
                if (apellidosIndex != -1) {
                    comercial.setApellidos(cursor.getString(apellidosIndex));
                }

                int contraseñaIndex = cursor.getColumnIndex("Contraseña");
                if (contraseñaIndex != -1) {
                    comercial.setContra(cursor.getString(contraseñaIndex));
                }

                int correoIndex = cursor.getColumnIndex("Correo");
                if (correoIndex != -1) {
                    comercial.setCorreo(cursor.getString(correoIndex));
                }

                int direccionIndex = cursor.getColumnIndex("Direccion");
                if (direccionIndex != -1) {
                    comercial.setDireccion(cursor.getString(direccionIndex));
                }

                int dniIndex = cursor.getColumnIndex("DNI");
                if (dniIndex != -1) {
                    comercial.setDni(cursor.getString(dniIndex));
                }

                int telefonoIndex = cursor.getColumnIndex("Telefono");
                if (telefonoIndex != -1) {
                    comercial.setTelefono(cursor.getString(telefonoIndex));
                }

                comercialList.add(comercial);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return comercialList;
    }
    // Método para agregar un nuevo artículo
    public long addArticulo(Articulos articulo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", articulo.getNombre());
        values.put("Descripcion", articulo.getDescripcion());
        values.put("Categoria", articulo.getCategoria());
        values.put("Proveedor", articulo.getProveedor());
        values.put("PvVent", articulo.getPvVent());
        values.put("PvCost", articulo.getPvCost());
        values.put("Existencias", articulo.getExistencias());
        values.put("Direccion_Imagen", articulo.getDireccionImagen());

        // Insertar fila
        long id = db.insert("ARTICULOS", null, values);
        db.close();
        return id;
    }

    // Método para obtener un artículo por su ID
    public Articulos getArticulo(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "IdArticulo", "Nombre", "Descripcion", "Categoria", "Proveedor", "PvVent", "PvCost", "Existencias", "Direccion_Imagen"
        };

        Cursor cursor = db.query("ARTICULOS", columns, "IdArticulo = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Articulos articulo = null;
        if (cursor != null && cursor.moveToFirst()) {
            articulo = new Articulos();

            int idArticuloIndex = cursor.getColumnIndex("IdArticulo");
            if (idArticuloIndex != -1) {
                articulo.setIdArticulo(cursor.getInt(idArticuloIndex));
            }

            int nombreIndex = cursor.getColumnIndex("Nombre");
            if (nombreIndex != -1) {
                articulo.setNombre(cursor.getString(nombreIndex));
            }

            int descripcionIndex = cursor.getColumnIndex("Descripcion");
            if (descripcionIndex != -1) {
                articulo.setDescripcion(cursor.getString(descripcionIndex));
            }

            int categoriaIndex = cursor.getColumnIndex("Categoria");
            if (categoriaIndex != -1) {
                articulo.setCategoria(cursor.getString(categoriaIndex));
            }

            int proveedorIndex = cursor.getColumnIndex("Proveedor");
            if (proveedorIndex != -1) {
                articulo.setProveedor(cursor.getString(proveedorIndex));
            }

            int pvVentIndex = cursor.getColumnIndex("PvVent");
            if (pvVentIndex != -1) {
                articulo.setPvVent(cursor.getDouble(pvVentIndex));
            }

            int pvCostIndex = cursor.getColumnIndex("PvCost");
            if (pvCostIndex != -1) {
                articulo.setPvCost(cursor.getDouble(pvCostIndex));
            }

            int existenciasIndex = cursor.getColumnIndex("Existencias");
            if (existenciasIndex != -1) {
                articulo.setExistencias(cursor.getInt(existenciasIndex));
            }

            int direccionImagenIndex = cursor.getColumnIndex("Direccion_Imagen");
            if (direccionImagenIndex != -1) {
                articulo.setDireccionImagen(cursor.getString(direccionImagenIndex));
            }

            cursor.close();
        }
        db.close();
        return articulo;
    }

    // Método para actualizar un artículo
    public int updateArticulo(Articulos articulo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", articulo.getNombre());
        values.put("Descripcion", articulo.getDescripcion());
        values.put("Categoria", articulo.getCategoria());
        values.put("Proveedor", articulo.getProveedor());
        values.put("PvVent", articulo.getPvVent());
        values.put("PvCost", articulo.getPvCost());
        values.put("Existencias", articulo.getExistencias());
        values.put("Direccion_Imagen", articulo.getDireccionImagen());

        // Actualizar fila
        return db.update("ARTICULOS", values, "IdArticulo = ?",
                new String[]{String.valueOf(articulo.getIdArticulo())});
    }

    // Método para borrar un artículo por su ID
    public void deleteArticulo(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ARTICULOS", "IdArticulo = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener todos los artículos
    public List<Articulos> getAllArticulos() {
        List<Articulos> articuloList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM ARTICULOS";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Articulos articulo = new Articulos();

                int idArticuloIndex = cursor.getColumnIndex("IdArticulo");
                if (idArticuloIndex != -1) {
                    articulo.setIdArticulo(cursor.getInt(idArticuloIndex));
                }

                int nombreIndex = cursor.getColumnIndex("Nombre");
                if (nombreIndex != -1) {
                    articulo.setNombre(cursor.getString(nombreIndex));
                }

                int descripcionIndex = cursor.getColumnIndex("Descripcion");
                if (descripcionIndex != -1) {
                    articulo.setDescripcion(cursor.getString(descripcionIndex));
                }

                int categoriaIndex = cursor.getColumnIndex("Categoria");
                if (categoriaIndex != -1) {
                    articulo.setCategoria(cursor.getString(categoriaIndex));
                }

                int proveedorIndex = cursor.getColumnIndex("Proveedor");
                if (proveedorIndex != -1) {
                    articulo.setProveedor(cursor.getString(proveedorIndex));
                }

                int pvVentIndex = cursor.getColumnIndex("PvVent");
                if (pvVentIndex != -1) {
                    articulo.setPvVent(cursor.getDouble(pvVentIndex));
                }

                int pvCostIndex = cursor.getColumnIndex("PvCost");
                if (pvCostIndex != -1) {
                    articulo.setPvCost(cursor.getDouble(pvCostIndex));
                }

                int existenciasIndex = cursor.getColumnIndex("Existencias");
                if (existenciasIndex != -1) {
                    articulo.setExistencias(cursor.getInt(existenciasIndex));
                }

                int direccionImagenIndex = cursor.getColumnIndex("Direccion_Imagen");
                if (direccionImagenIndex != -1) {
                    articulo.setDireccionImagen(cursor.getString(direccionImagenIndex));
                }

                articuloList.add(articulo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return articuloList;
    }
    // Método para agregar un nuevo registro en CAB_PEDIDOS
    public long addCabPedido(CabPedidos cabPedido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdPartner", cabPedido.getIdPartner());
        values.put("IdComercial", cabPedido.getIdComercial());
        values.put("FechaPedido", cabPedido.getFechaPedido());

        // Insertar fila
        long id = db.insert("CAB_PEDIDOS", null, values);
        db.close();
        return id;
    }
    public void deleteAllPartners() {
        // Obtiene la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Ejecuta la sentencia SQL para borrar todos los registros
        db.execSQL("DELETE FROM PARTNERS");

        // Opcional: Si también deseas reiniciar el contador del ID autoincremental, puedes descomentar la siguiente línea
        // db.execSQL("DELETE FROM sqlite_sequence WHERE name='PARTNERS'"); // Cuidado al usar, ya que reiniciará los IDs

        db.close(); // Cierra la base de datos para liberar recursos
    }

    // Método para obtener un registro en CAB_PEDIDOS por su ID
    public CabPedidos getCabPedido(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "IdPedido", "IdPartner", "IdComercial", "FechaPedido"
        };

        Cursor cursor = db.query("CAB_PEDIDOS", columns, "IdPedido = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        CabPedidos cabPedido = null;
        if (cursor != null && cursor.moveToFirst()) {
            cabPedido = new CabPedidos();

            int idPedidoIndex = cursor.getColumnIndex("IdPedido");
            if (idPedidoIndex != -1) {
                cabPedido.setIdPedido(cursor.getInt(idPedidoIndex));
            }

            int idPartnerIndex = cursor.getColumnIndex("IdPartner");
            if (idPartnerIndex != -1) {
                cabPedido.setIdPartner(cursor.getInt(idPartnerIndex));
            }

            int idComercialIndex = cursor.getColumnIndex("IdComercial");
            if (idComercialIndex != -1) {
                cabPedido.setIdComercial(cursor.getInt(idComercialIndex));
            }

            int fechaPedidoIndex = cursor.getColumnIndex("FechaPedido");
            if (fechaPedidoIndex != -1) {
                cabPedido.setFechaPedido(cursor.getString(fechaPedidoIndex));
            }

            cursor.close();
        }
        db.close();
        return cabPedido;
    }

    // Método para actualizar un registro en CAB_PEDIDOS
    public int updateCabPedido(CabPedidos cabPedido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdPartner", cabPedido.getIdPartner());
        values.put("IdComercial", cabPedido.getIdComercial());
        values.put("FechaPedido", cabPedido.getFechaPedido());

        // Actualizar fila
        return db.update("CAB_PEDIDOS", values, "IdPedido = ?",
                new String[]{String.valueOf(cabPedido.getIdPedido())});
    }

    // Método para borrar un registro en CAB_PEDIDOS por su ID
    public void deleteCabPedido(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CAB_PEDIDOS", "IdPedido = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener todos los registros en CAB_PEDIDOS
   /* public List<CabPedidos> getAllCabPedidos() {
        List<CabPedidos> cabPedidoList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM CAB_PEDIDOS";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cabPedidoList;
    }*/

    //Metodo para obtener todos los registros en CAB_PEDIDOS cuyo id partner coincida con el partner seleccionado
    public List<CabPedidos> getAllCabPedidos(int idPartner) {
        List<CabPedidos> cabPedidoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM CAB_PEDIDOS WHERE IdPartner = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idPartner)});

        if (cursor.moveToFirst()) {
            do {
                CabPedidos cabPedido = new CabPedidos();

                int idPedidoIndex = cursor.getColumnIndex("IdPedido");
                if (idPedidoIndex != -1) {
                    cabPedido.setIdPedido(cursor.getInt(idPedidoIndex));
                }

                int idPartnerIndex = cursor.getColumnIndex("IdPartner");
                if (idPartnerIndex != -1) {
                    cabPedido.setIdPartner(cursor.getInt(idPartnerIndex));
                }

                int idComercialIndex = cursor.getColumnIndex("IdComercial");
                if (idComercialIndex != -1) {
                    cabPedido.setIdComercial(cursor.getInt(idComercialIndex));
                }

                int fechaPedidoIndex = cursor.getColumnIndex("FechaPedido");
                if (fechaPedidoIndex != -1) {
                    cabPedido.setFechaPedido(cursor.getString(fechaPedidoIndex));
                }

                cabPedidoList.add(cabPedido);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cabPedidoList;
    }



    // Método para agregar una nueva línea de pedido
    public long addLineaPedido(LineasPedido lineaPedido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdArticulo", lineaPedido.getIdArticulo());
        values.put("IdPedido", lineaPedido.getIdPedido());
        values.put("Cantidad", lineaPedido.getCantidad());
        values.put("Descuento", lineaPedido.getDescuento());
        values.put("Precio", lineaPedido.getPrecio());

        // Insertar fila
        long id = db.insert("LINEAS_PEDIDO", null, values);
        db.close();
        return id;
    }

    // Método para obtener una línea de pedido por su ID
    public LineasPedido getLineaPedido(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "IdLinea", "IdArticulo", "IdPedido", "Cantidad", "Descuento", "Precio"
        };

        Cursor cursor = db.query("LINEAS_PEDIDO", columns, "IdLinea = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        LineasPedido lineaPedido = null;
        if (cursor != null && cursor.moveToFirst()) {
            lineaPedido = new LineasPedido();

            int idLineaIndex = cursor.getColumnIndex("IdLinea");
            if (idLineaIndex != -1) {
                lineaPedido.setIdLinea(cursor.getInt(idLineaIndex));
            }

            int idArticuloIndex = cursor.getColumnIndex("IdArticulo");
            if (idArticuloIndex != -1) {
                lineaPedido.setIdArticulo(cursor.getInt(idArticuloIndex));
            }

            int idPedidoIndex = cursor.getColumnIndex("IdPedido");
            if (idPedidoIndex != -1) {
                lineaPedido.setIdPedido(cursor.getInt(idPedidoIndex));
            }

            int cantidadIndex = cursor.getColumnIndex("Cantidad");
            if (cantidadIndex != -1) {
                lineaPedido.setCantidad(cursor.getInt(cantidadIndex));
            }

            int descuentoIndex = cursor.getColumnIndex("Descuento");
            if (descuentoIndex != -1) {
                lineaPedido.setDescuento(cursor.getDouble(descuentoIndex));
            }

            int precioIndex = cursor.getColumnIndex("Precio");
            if (precioIndex != -1) {
                lineaPedido.setPrecio(cursor.getDouble(precioIndex));
            }

            cursor.close();
        }
        db.close();
        return lineaPedido;
    }

    // Método para actualizar una línea de pedido
    public int updateLineaPedido(LineasPedido lineaPedido) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdArticulo", lineaPedido.getIdArticulo());
        values.put("IdPedido", lineaPedido.getIdPedido());
        values.put("Cantidad", lineaPedido.getCantidad());
        values.put("Descuento", lineaPedido.getDescuento());
        values.put("Precio", lineaPedido.getPrecio());

        // Actualizar fila
        return db.update("LINEAS_PEDIDO", values, "IdLinea = ?",
                new String[]{String.valueOf(lineaPedido.getIdLinea())});
    }

    // Método para borrar una línea de pedido por su ID
    public void deleteLineaPedido(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("LINEAS_PEDIDO", "IdLinea = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para obtener todas las líneas de pedido de un pedido específico
    public List<LineasPedido> getLineasPedidoPorPedido(long idPedido) {
        List<LineasPedido> lineaPedidoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM LINEAS_PEDIDO WHERE IdPedido = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idPedido)});

        if (cursor.moveToFirst()) {
            do {
                LineasPedido lineaPedido = new LineasPedido();

                int idLineaIndex = cursor.getColumnIndex("IdLinea");
                if (idLineaIndex != -1) {
                    lineaPedido.setIdLinea(cursor.getInt(idLineaIndex));
                }

                int idArticuloIndex = cursor.getColumnIndex("IdArticulo");
                if (idArticuloIndex != -1) {
                    lineaPedido.setIdArticulo(cursor.getInt(idArticuloIndex));
                }

                int cantidadIndex = cursor.getColumnIndex("Cantidad");
                if (cantidadIndex != -1) {
                    lineaPedido.setCantidad(cursor.getInt(cantidadIndex));
                }

                int descuentoIndex = cursor.getColumnIndex("Descuento");
                if (descuentoIndex != -1) {
                    lineaPedido.setDescuento(cursor.getDouble(descuentoIndex));
                }

                int precioIndex = cursor.getColumnIndex("Precio");
                if (precioIndex != -1) {
                    lineaPedido.setPrecio(cursor.getDouble(precioIndex));
                }

                lineaPedidoList.add(lineaPedido);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lineaPedidoList;
    }
    public boolean existeComercial(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define las columnas que quieres obtener en tu consulta.
        // En este caso, solo necesitas una columna para verificar la existencia, pero debes especificar al menos una.
        String[] columns = {"DNI"};

        // Define la cláusula WHERE de tu consulta.
        String selection = "DNI = ?";

        // Define los argumentos para reemplazar los placeholders (?) en la cláusula WHERE.
        String[] selectionArgs = {dni};

        // Realiza la consulta a la tabla COMERCIALES.
        Cursor cursor = db.query("COMERCIALES", columns, selection, selectionArgs, null, null, null);

        // Verifica si el cursor tiene al menos un resultado.
        boolean exists = cursor.moveToFirst();

        // Cierra el cursor para liberar recursos.
        cursor.close();

        // Cierra la conexión a la base de datos.
        db.close();

        return exists;
    }
}
