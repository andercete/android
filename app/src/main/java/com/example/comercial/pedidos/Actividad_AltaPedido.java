package com.example.comercial.pedidos;


import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Actividad_AltaPedido extends AppCompatActivity {

    TextView FechaPedido, NombrePartner;
    private AnderBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapedido);

        FechaPedido = findViewById(R.id.textFechaPedido);
        NombrePartner = findViewById(R.id.textNombrePartner);

        // Inicializar la instancia de la base de datos
        db = new AnderBD(this);

        Integer idPartner = 1;
        String nombreRelacionado = db.buscarNombrePorIdPartnerEnCabPedido(idPartner);

        // Mostrar la fecha actual en el TextView
        mostrarFechaActual(FechaPedido);
        NombrePartner.setText(nombreRelacionado != null ? nombreRelacionado : "No encontrado");



    }

    private void mostrarFechaActual(TextView textViewFecha) {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Formatear la fecha sin la hora en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Establecer la fecha en el TextView
        textViewFecha.setText(formattedDate);
    }



}
