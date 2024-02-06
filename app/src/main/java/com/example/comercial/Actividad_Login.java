package com.example.comercial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.BBDD.Comerciales;

public class Actividad_Login extends AppCompatActivity {

    private AnderBD helper;
    private EditText eDNI, eContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login); // Asegúrate de que este es el nombre correcto de tu layout de login.

        helper = new AnderBD(this);
        eDNI = findViewById(R.id.eUsuario);
        eContra = findViewById(R.id.eContra);

        // Añadir un comercial por defecto al iniciar la aplicación
        añadirComercialPorDefecto();

        findViewById(R.id.bLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void añadirComercialPorDefecto() {
        if (!helper.existeComercial("72538203k")) {
            helper.addComercial(new Comerciales(0, 1, "Nombre Por Defecto", "Apellido Por Defecto", "12345", "correo@ejemplo.com", "Direccion Por Defecto", "72538203k", "000000000"));
        }
    }

    private void login() {
        String dni = eDNI.getText().toString();
        String contraseña = eContra.getText().toString();
        if (validateLogin(dni, contraseña)) {
            Intent i = new Intent(Actividad_Login.this, Actividad_Inicio.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(Actividad_Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
