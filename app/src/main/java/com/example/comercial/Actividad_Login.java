package com.example.comercial;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Actividad_Login extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        dbHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.eUsuario);
        passwordEditText = findViewById(R.id.eContra);

        Button loginButton = findViewById(R.id.bLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Consultar la base de datos para verificar el login
        if (checkLogin(username, password)) {
            // Acceso concedido
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
            // Aquí puedes redirigir a la siguiente actividad o realizar otras acciones
        } else {
            // Acceso denegado
            Toast.makeText(this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLogin(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("comerciales", null, "username=? AND password=?", new String[]{username, password}, null, null, null);

        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();

        return result;
    }
}
