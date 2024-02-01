package com.example.comercial;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comercial";
    private static final int DATABASE_VERSION = 1;

    // Tabla de eventos
    private static final String TABLE_EVENTS = "eventos";
    private static final String KEY_ID_EVENTS = "id";
    private static final String KEY_TITLE_EVENTS = "title";
    private static final String KEY_DATE_EVENTS = "date";
    private static final String KEY_TIME_EVENTS = "time";
    private static final String KEY_DESCRIPTION_EVENTS = "description";

    // Tabla de partners
    private static final String TABLE_PARTNERS = "partners";
    private static final String KEY_ID_PARTNERS = "id";
    private static final String KEY_NOMBRE_PARTNERS = "nombre";
    private static final String KEY_DIRECCION_PARTNERS = "direccion";
    private static final String KEY_POBLACION_PARTNERS = "poblacion";
    private static final String KEY_CIF_PARTNERS = "cif";
    private static final String KEY_TELEFONO_PARTNERS = "telefono";
    private static final String KEY_EMAIL_PARTNERS = "email";

    // Crear las tablas
    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    KEY_ID_EVENTS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TITLE_EVENTS + " TEXT," +
                    KEY_DATE_EVENTS + " TEXT," +
                    KEY_TIME_EVENTS + " TEXT," +
                    KEY_DESCRIPTION_EVENTS + " TEXT);";

    private static final String CREATE_TABLE_PARTNERS =
            "CREATE TABLE " + TABLE_PARTNERS + " (" +
                    KEY_ID_PARTNERS + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NOMBRE_PARTNERS + " TEXT," +
                    KEY_DIRECCION_PARTNERS + " TEXT," +
                    KEY_POBLACION_PARTNERS + " TEXT," +
                    KEY_CIF_PARTNERS + " TEXT," +
                    KEY_TELEFONO_PARTNERS + " INTEGER," +
                    KEY_EMAIL_PARTNERS + " TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            // Crear las tablas al iniciar la base de datos
            db.execSQL(CREATE_TABLE_EVENTS);
            db.execSQL(CREATE_TABLE_PARTNERS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar las tablas antiguas y recrearlas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNERS);
        onCreate(db);
    }

    // Add the method to get all events
    public List<Evento> getAllEventos() {
        List<Evento> eventosList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Evento evento = new Evento();
            evento.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_EVENTS)));
            evento.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE_EVENTS)));
            evento.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE_EVENTS)));
            evento.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_EVENTS)));
            evento.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION_EVENTS)));

            eventosList.add(evento);
        }

        cursor.close();
        return eventosList;
    }
}

