package com.example.comercial;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "partners_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "partners";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_DIRECCION = "direccion";
    private static final String COLUMN_POBLACION = "poblacion";
    private static final String COLUMN_CIF = "cif";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_EMAIL = "email";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NOMBRE + " TEXT," +
                    COLUMN_DIRECCION + " TEXT," +
                    COLUMN_POBLACION + " TEXT," +
                    COLUMN_CIF + " TEXT," +
                    COLUMN_TELEFONO + " INTEGER," +
                    COLUMN_EMAIL + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertarPartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, partner.getNombre());
        values.put(COLUMN_DIRECCION, partner.getDireccion());
        values.put(COLUMN_POBLACION, partner.getPoblacion());
        values.put(COLUMN_CIF, partner.getCif());
        values.put(COLUMN_TELEFONO, partner.getTelefono());
        values.put(COLUMN_EMAIL, partner.getEmail());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Otros métodos útiles, como consultar partners, actualizar, borrar, etc., pueden agregarse aquí.

}

