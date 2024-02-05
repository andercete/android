package com.example.comercial;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.comercial.BBDD.AnderBD;
import android.database.sqlite.SQLiteDatabase;



public class Actividad_Login extends AppCompatActivity {

    private AnderBD helper;
    private EditText eUsuario, eContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login); // Asegúrate de que este es el nombre correcto de tu layout de login.

        helper = new AnderBD(this);
        eUsuario = findViewById(R.id.eUsuario);
        eContra = findViewById(R.id.eContra);

        findViewById(R.id.bLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String usuario = eUsuario.getText().toString();
        String contraseña = eContra.getText().toString();
        if (validateLogin(usuario, contraseña)) {
            // Si el login es correcto, puedes iniciar una nueva actividad o lo que necesites hacer después del login
            Intent i = new Intent(Actividad_Login.this, Actividad_Inicio.class);
            startActivity(i);
            finish();
        } else {
            // Si el login es incorrecto, muestra un mensaje
            Toast.makeText(Actividad_Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateLogin(String usuario, String contraseña) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"IdComercial", "Contraseña"};
        String selection = "Nombre=? AND Contraseña=?";
        String[] selectionArgs = {usuario, contraseña};

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
