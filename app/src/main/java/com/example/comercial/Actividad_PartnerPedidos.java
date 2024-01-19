package com.example.comercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Actividad_PartnerPedidos extends AppCompatActivity {

    TextView tNombre,tDireccion,tPoblacion,tCif,tTelefono,tEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_partnerpedidos);

        tNombre = findViewById(R.id.tPartnerNombre);
        tDireccion = findViewById(R.id.tDireccion);
        tPoblacion = findViewById(R.id.tPoblacion);
        tCif = findViewById(R.id.tCif);
        tTelefono = findViewById(R.id.tTelefono);
        tEmail = findViewById(R.id.tEmail);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("partnerNombre");
        String direccion = intent.getStringExtra("partnerDireccion");
        String poblacion = intent.getStringExtra("partnerPoblacion");
        String cif = intent.getStringExtra("partnerCif");
        // Note: Assuming telefono is an integer. If it's a String, use getStringExtra.
        int telefono = intent.getIntExtra("partnerTelefono", 0);
        String email = intent.getStringExtra("partnerEmail");

        tNombre.setText(nombre);
        tDireccion.setText(direccion);
        tPoblacion.setText(poblacion);
        tCif.setText(cif);
        tTelefono.setText(telefono);
        tEmail.setText(email);
    }
}