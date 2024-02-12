package com.example.comercial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.Comerciales;

public class Actividad_Login extends AppCompatActivity {

    private DbHelper helper;
    private EditText eDNI, eContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        helper = new DbHelper(this);
        eDNI = findViewById(R.id.eUsuario);
        eContra = findViewById(R.id.eContra);

        añadirComercialPorDefecto();

        findViewById(R.id.bLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void añadirComercialPorDefecto() {
        if (!helper.existeComercial("72538203K")) {
            helper.addComercial(new Comerciales(0, 1, "Nombre Por Defecto", "Apellido Por Defecto", "12345", "correo@ejemplo.com", "Direccion Por Defecto", "72538203K", "000000000"));
        }

        if (!helper.existeComercial("72538684L")) {
            helper.addComercial(new Comerciales(0, 1, "Iñigo", "Valdivia", "12345", "correo1@ejemplo.com", "Direccion1 Por Defecto", "72538684L", "000000001"));
        }

        if (!helper.existeComercial("73034420J")) {
            helper.addComercial(new Comerciales(0, 1, "Julen", "Izeta", "12345", "correo2@ejemplo.com", "Direccion2 Por Defecto", "73034420J", "000000002"));
        }

        if (!helper.existeComercial("44570590V")) {
            helper.addComercial(new Comerciales(0, 1, "Alberto", "", "12345", "correo3@ejemplo.com", "Direccion3 Por Defecto", "44570590V", "000000003"));
        }
        if (!helper.existeComercial("")) {
            helper.addComercial(new Comerciales(0, 1, "Alberto", "", "", "correo3@ejemplo.com", "Direccion3 Por Defecto", "", "000000003"));
        }

    }

    private void login() {
        String dni = eDNI.getText().toString().toUpperCase();
        String contraseña = eContra.getText().toString();

        if (validateLogin(dni, contraseña)) {
            guardarDatosComercialLogueado(dni);
            iniciarActividadInicio();
        } else {
            Toast.makeText(Actividad_Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarDatosComercialLogueado(String dni) {
        Comerciales comercial = helper.obtenerComercialPorDNI(dni);

        if (comercial != null) {
            // Guardar datos del comercial en Preferencias Compartidas
            SharedPreferences sharedPref = getSharedPreferences("PreferenciasComerciales", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("IdZona", comercial.getIdZona());
            editor.putString("NombreComercial", comercial.getNombre());
            editor.putInt("IdComercial", comercial.getIdComercial());
            editor.apply();
        }
    }



    private boolean validateLogin(String dni, String contraseña) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"DNI", "Contraseña"};
        String selection = "DNI=? AND Contraseña=?";
        String[] selectionArgs = {dni, contraseña};

        Cursor cursor = db.query("COMERCIALES", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    private void iniciarActividadInicio() {
        Intent i = new Intent(Actividad_Login.this, Actividad_Inicio.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
