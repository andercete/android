package com.example.comercial.pedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.CabPedidos;
import com.example.comercial.BBDD.LineasPedido;
import com.example.comercial.R;

import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Actividad_VerPedido extends AppCompatActivity {

    private TextView textViewIdPedido, textViewIdPartner, textViewIdComercial, textViewFechaPedido;
    private RecyclerView recyclerViewLineasPedido;
    private Button btnEditar;
    int idPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ver_pedido);

        // Obtener referencias a los elementos de la interfaz
        textViewIdPedido = findViewById(R.id.textViewIdPedido);
        textViewIdPartner = findViewById(R.id.textViewIdPartner);
        textViewIdComercial = findViewById(R.id.textViewIdComercial);
        textViewFechaPedido = findViewById(R.id.textViewFechaPedido);
        recyclerViewLineasPedido = findViewById(R.id.recyclerViewLineasPedido);

        // Obtener información del pedido según el ID
        int codPedido = getIntent().getIntExtra("idPedido", -1);
        idPedido = codPedido;

        // Inicializar la base de datos
        DbHelper dbHelper = new DbHelper(this);

        // Obtener la cabecera del pedido
        CabPedidos cabeceraPedido = obtenerCabeceraPedido(dbHelper, idPedido);

        // Obtener las líneas de pedido
        List<LineasPedido> lineasPedido = obtenerLineasPedido(dbHelper, idPedido);

        // Mostrar información en los TextViews
        textViewIdPedido.setText(String.valueOf(cabeceraPedido.getIdPedido()));

        // Obtener el nombre del Partner
        String nombrePartner = obtenerNombrePartner(dbHelper, cabeceraPedido.getIdPartner());
        textViewIdPartner.setText(nombrePartner);

        // Obtener el nombre del Comercial
        String nombreComercial = obtenerNombreComercial(dbHelper, cabeceraPedido.getIdComercial());
        textViewIdComercial.setText(nombreComercial);

        textViewFechaPedido.setText(cabeceraPedido.getFechaPedido());

        // Configurar el RecyclerView
        LineasPedidoAdapter adapter = new LineasPedidoAdapter(lineasPedido);
        recyclerViewLineasPedido.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLineasPedido.setAdapter(adapter);
    }

    private CabPedidos obtenerCabeceraPedido(DbHelper dbHelper, int idPedido) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "CAB_PEDIDOS",
                new String[]{"IdPedido", "IdPartner", "IdComercial", "FechaPedido"},
                "IdPedido = ?",
                new String[]{String.valueOf(idPedido)},
                null,
                null,
                null
        );

        CabPedidos cabeceraPedido = new CabPedidos();

        if (cursor.moveToFirst()) {
            cabeceraPedido.setIdPedido(cursor.getInt(cursor.getColumnIndex("IdPedido")));
            cabeceraPedido.setIdPartner(cursor.getInt(cursor.getColumnIndex("IdPartner")));
            cabeceraPedido.setIdComercial(cursor.getInt(cursor.getColumnIndex("IdComercial")));
            cabeceraPedido.setFechaPedido(cursor.getString(cursor.getColumnIndex("FechaPedido")));
        }

        cursor.close();
        db.close();

        return cabeceraPedido;
    }

    private List<LineasPedido> obtenerLineasPedido(DbHelper dbHelper, int idPedido) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "LINEAS_PEDIDO",
                new String[]{"IdLinea", "IdArticulo", "IdPedido", "Cantidad", "Descuento", "Precio"},
                "IdPedido = ?",
                new String[]{String.valueOf(idPedido)},
                null,
                null,
                null
        );

        List<LineasPedido> lineasPedidoList = new ArrayList<>();

        while (cursor.moveToNext()) {
            LineasPedido lineaPedido = new LineasPedido();
            lineaPedido.setIdLinea(cursor.getInt(cursor.getColumnIndex("IdLinea")));
            lineaPedido.setIdArticulo(cursor.getInt(cursor.getColumnIndex("IdArticulo")));
            lineaPedido.setIdPedido(cursor.getInt(cursor.getColumnIndex("IdPedido")));
            lineaPedido.setCantidad(cursor.getInt(cursor.getColumnIndex("Cantidad")));
            lineaPedido.setDescuento(cursor.getDouble(cursor.getColumnIndex("Descuento")));
            lineaPedido.setPrecio(cursor.getDouble(cursor.getColumnIndex("Precio")));

            lineasPedidoList.add(lineaPedido);
        }

        cursor.close();
        db.close();

        return lineasPedidoList;
    }

    private String obtenerNombrePartner(DbHelper dbHelper, int idPartner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "PARTNERS",
                new String[]{"Nombre"},
                "IdPartner = ?",
                new String[]{String.valueOf(idPartner)},
                null,
                null,
                null
        );

        String nombre = "";

        if (cursor.moveToFirst()) {
            nombre = cursor.getString(cursor.getColumnIndex("Nombre"));
        }

        cursor.close();
        db.close();

        return nombre;
    }

    private String obtenerNombreComercial(DbHelper dbHelper, int idComercial) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "COMERCIALES",
                new String[]{"Nombre"},
                "IdComercial = ?",
                new String[]{String.valueOf(idComercial)},
                null,
                null,
                null
        );

        String nombre = "";

        if (cursor.moveToFirst()) {
            nombre = cursor.getString(cursor.getColumnIndex("Nombre"));
        }

        cursor.close();
        db.close();

        return nombre;
    }
}
