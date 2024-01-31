package com.example.comercial;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comercial";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_COMERCIANTES = "comerciales";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_COMERCIANTES = "CREATE TABLE " + TABLE_COMERCIANTES +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_COMERCIANTES);
    }

    // Actualizar la base de datos (si es necesario)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMERCIANTES);
        onCreate(db);
    }

}
