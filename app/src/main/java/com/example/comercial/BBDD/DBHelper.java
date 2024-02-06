package com.example.comercial.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.comercial.calendario.Evento;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

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

    public DBHelper(Context context) {
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

}

