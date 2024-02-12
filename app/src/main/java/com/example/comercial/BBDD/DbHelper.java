package com.example.comercial.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.comercial.calendario.Evento;
import com.example.comercial.partners.Partner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "comercial.db";
    private static final int DATABASE_VERSION = 1;

    // Creación de tablas
    private static final String CREATE_TABLE_COMERCIALES = "CREATE TABLE COMERCIALES (" +
            "IdComercial INTEGER PRIMARY KEY," +

            "Nombre TEXT," +
            "Apellidos TEXT," +
            "Contraseña TEXT," +
            "Correo TEXT," +
            "Direccion TEXT," +
            "DNI TEXT," +
            "Telefono TEXT)";

    private static final String CREATE_TABLE_PARTNERS = "CREATE TABLE PARTNERS (" +
            "IdPartner INTEGER PRIMARY KEY," +
            "Nombre TEXT," +
            "CIF TEXT," +
            "Direccion TEXT," +
            "Telefono TEXT," +
            "Correo TEXT," +
            "FechaRegistro TEXT)";


    private static final String CREATE_TABLE_ARTICULOS = "CREATE TABLE ARTICULOS (" +
            "IdArticulo INTEGER PRIMARY KEY," +
            "Nombre TEXT," +
            "Descripcion TEXT," +
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

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

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
        db.execSQL("DROP TABLE IF EXISTS EVENTS");
        // Crear nuevas tablas
        onCreate(db);
    }

    public List<Evento> getAllEventos() {
        List<Evento> eventosList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EVENTOS", null);

        int idIndex = cursor.getColumnIndex("IdEvento");
        int titleIndex = cursor.getColumnIndex("Title");
        int locationIndex = cursor.getColumnIndex("Location");
        int dateIndex = cursor.getColumnIndex("Date");
        int timeIndex = cursor.getColumnIndex("Time");
        int descriptionIndex = cursor.getColumnIndex("Description");

        if (idIndex != -1 && titleIndex != -1 && locationIndex != -1 && dateIndex != -1 && timeIndex != -1 && descriptionIndex != -1) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String location = cursor.getString(locationIndex);
                    String date = cursor.getString(dateIndex);
                    String time = cursor.getString(timeIndex);
                    String description = cursor.getString(descriptionIndex);

                    Evento evento = new Evento(id, title, location, date, time, description);
                    eventosList.add(evento);
                } while (cursor.moveToNext());
            }
        } else {
            // Manejar el caso donde alguna columna no exista
            Log.e("getAllEventos", "Una o más columnas no existen en la tabla EVENTOS.");
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

    public List<Partner> getPartnersOfToday() {
        List<Partner> partners = new ArrayList<>();
        // Obtén la fecha actual en formato YYYY-MM-DD
        String fechaHoy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Abre la base de datos en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para seleccionar partners basados en la fecha de hoy
        String selection = "FechaRegistro = ?";
        String[] selectionArgs = { fechaHoy };

        // Ejecuta la consulta
        Cursor cursor = db.query("PARTNERS", null, selection, selectionArgs, null, null, null);

        // Obtiene los índices de las columnas
        int idIndex = cursor.getColumnIndex("IdPartner");

        int nombreIndex = cursor.getColumnIndex("Nombre");
        int cifIndex = cursor.getColumnIndex("CIF");
        int direccionIndex = cursor.getColumnIndex("Direccion");
        int telefonoIndex = cursor.getColumnIndex("Telefono");
        int correoIndex = cursor.getColumnIndex("Correo");
        int fechaRegistroIndex = cursor.getColumnIndex("FechaRegistro");

        // Verifica que todos los índices sean válidos
        if (idIndex != -1  && nombreIndex != -1 && cifIndex != -1 &&
                direccionIndex != -1 && telefonoIndex != -1 && correoIndex != -1 && fechaRegistroIndex != -1) {

            // Itera sobre los resultados y añade cada partner a la lista
            if (cursor.moveToFirst()) {
                do {
                    Partner partner = new Partner();
                    partner.setIdPartner(cursor.getInt(idIndex));

                    partner.setNombre(cursor.getString(nombreIndex));
                    partner.setCif(cursor.getString(cifIndex));
                    partner.setDireccion(cursor.getString(direccionIndex));
                    partner.setTelefono(cursor.getString(telefonoIndex));
                    partner.setCorreo(cursor.getString(correoIndex));
                    partner.setFechaRegistro(cursor.getString(fechaRegistroIndex));
                    partners.add(partner);
                } while (cursor.moveToNext());
            }
        } else {
            // Log o manejo de error si alguna columna no existe
            Log.e("getPartnersOfToday", "Una o más columnas no existen en la tabla PARTNERS.");
        }

        cursor.close();
        db.close();
        return partners;
    }


    public Comerciales obtenerComercialPorDNI(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        Comerciales comercial = null;

        String[] columns = {
                "IdComercial", "Nombre", "Apellidos",
                "Contraseña", "Correo", "Direccion", "DNI", "Telefono"
        };
        String selection = "DNI=?";
        String[] selectionArgs = { dni };

        Cursor cursor = db.query("COMERCIALES", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            // Obtiene los índices de columna de manera segura
            int idComercialIndex = cursor.getColumnIndexOrThrow("IdComercial");
            int nombreIndex = cursor.getColumnIndexOrThrow("Nombre");
            int apellidosIndex = cursor.getColumnIndexOrThrow("Apellidos");
            int contraseñaIndex = cursor.getColumnIndexOrThrow("Contraseña");
            int correoIndex = cursor.getColumnIndexOrThrow("Correo");
            int direccionIndex = cursor.getColumnIndexOrThrow("Direccion");
            // El DNI ya se proporciona como parámetro, no es necesario obtenerlo del cursor
            int telefonoIndex = cursor.getColumnIndexOrThrow("Telefono");

            // Extrae los datos usando los índices de columna
            int idComercial = cursor.getInt(idComercialIndex);
            String nombre = cursor.getString(nombreIndex);
            String apellidos = cursor.getString(apellidosIndex);
            String contraseña = cursor.getString(contraseñaIndex);
            String correo = cursor.getString(correoIndex);
            String direccion = cursor.getString(direccionIndex);
            String telefono = cursor.getString(telefonoIndex);

            // Crea una instancia de Comerciales con los datos extraídos
            comercial = new Comerciales(
                    idComercial, nombre, apellidos,
                    contraseña, correo, direccion, dni, telefono
            );
        }

        cursor.close();
        db.close();

        return comercial;
    }


    // Método para borrar un partner
    public void deletePartner(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PARTNERS", "IdPartner = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // Método para vaciar el catalogo
    public void vaciarCatalogo() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ARTICULOS", null, null);
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


    // Método para agregar un nuevo comercial
    public long addComercial(Comerciales comercial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
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

    // Método para agregar un nuevo artículo
    //ALBERTO
    public long addArticulo(Catalogo catalogo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Nombre", catalogo.getNombre());
        values.put("Descripcion", catalogo.getDescripcion());
        values.put("Proveedor", catalogo.getProveedor());
        values.put("PvVent", catalogo.getPvVent());
        values.put("PvCost", catalogo.getPvCost());
        values.put("Existencias", catalogo.getExistencias());
        values.put("Direccion_Imagen", catalogo.getImagen());

        long id = db.insert("ARTICULOS", null, values);
        db.close();
        return id;
    }
    public boolean existeArticulo(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { "IdArticulo" };
        String selection = "Nombre = ?";
        String[] selectionArgs = { nombre };
        Cursor cursor = db.query("ARTICULOS", columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    // Método para obtener todos los artículos
    public List<Catalogo> getAllArticulos() {
        List<Catalogo> listaArticulos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ARTICULOS", null);

        int idIndex = cursor.getColumnIndex("IdArticulo");
        int nombreIndex = cursor.getColumnIndex("Nombre");
        int descripcionIndex = cursor.getColumnIndex("Descripcion");
        int proveedorIndex = cursor.getColumnIndex("Proveedor");
        int pvVentIndex = cursor.getColumnIndex("PvVent");
        int pvCostIndex = cursor.getColumnIndex("PvCost");
        int existenciasIndex = cursor.getColumnIndex("Existencias");
        int direccionImagenIndex = cursor.getColumnIndex("Direccion_Imagen");

        if (cursor.moveToFirst()) {
            do {
                Catalogo articulo = new Catalogo();
                if(idIndex != -1) articulo.setIdArticulo(cursor.getInt(idIndex));
                if(nombreIndex != -1) articulo.setNombre(cursor.getString(nombreIndex));
                if(descripcionIndex != -1) articulo.setDescripcion(cursor.getString(descripcionIndex));
                if(proveedorIndex != -1) articulo.setProveedor(cursor.getString(proveedorIndex));
                if(pvVentIndex != -1) articulo.setPvVent(cursor.getDouble(pvVentIndex));
                if(pvCostIndex != -1) articulo.setPvCost(cursor.getDouble(pvCostIndex));
                if(existenciasIndex != -1) articulo.setExistencias(cursor.getInt(existenciasIndex));
                if(direccionImagenIndex != -1) articulo.setImagen(cursor.getString(direccionImagenIndex));

                listaArticulos.add(articulo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaArticulos;
    }



    // Método para buscar el nombre en la TablaPadre basado en el número en la TablaHija
    public String buscarNombrePorIdPartnerEnCabPedido(int idPartner) {
        String nombre = null;

        // Obtener una instancia de la base de datos en modo lectura
        SQLiteDatabase db = getReadableDatabase();

        // Realizar la consulta
        Cursor cursor = db.rawQuery("SELECT nombre FROM PARTNERS WHERE IdPartner = " + idPartner, null);

        // Verificar si se encontró algún resultado
        if (cursor.moveToFirst()) {
            nombre = cursor.getString(0);
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        db.close();

        return nombre;
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
