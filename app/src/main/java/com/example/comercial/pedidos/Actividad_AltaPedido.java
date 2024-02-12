package com.example.comercial.pedidos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.CabPedidos;
import com.example.comercial.BBDD.LineasPedido;
import com.example.comercial.BBDD.Catalogo;
import com.example.comercial.Catalogo.CatalogoAdapter;
import com.example.comercial.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Actividad_AltaPedido extends AppCompatActivity {

    TextView FechaPedido, NombrePartner;
    RecyclerView catalogoPedido;
    private DbHelper db;

    private List<Catalogo> catalogoList; // Lista de ítems del catálogo

    private CatalogoAdapter catalogoAdapter;
    int idPartner, idComercial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapedido);
        db = new DbHelper(this);
        FechaPedido = findViewById(R.id.textFechaPedido);
        NombrePartner = findViewById(R.id.textNombrePartner);
        catalogoPedido = findViewById(R.id.rCatalogoPedido);
        Intent intent = getIntent();
        idPartner = intent.getIntExtra("partnerId", -1); // El ID del partner seleccionado

        // Inicializar la instancia de la base de datos
        db = new DbHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasComerciales", Context.MODE_PRIVATE);
        int comercial = sharedPreferences.getInt("IdComercial", 1);
        idComercial = comercial;
        String nombreRelacionado = db.buscarNombrePorIdPartnerEnCabPedido(idPartner);

        // Mostrar la fecha actual en el TextView
        mostrarFechaActual(FechaPedido);
        NombrePartner.setText(nombreRelacionado != null ? nombreRelacionado : "No encontrado");

        // Cargar los datos del catálogo (esto dependerá de cómo almacenes tus datos)
        catalogoList = db.getAllArticulos(); // Actualizar el método para obtener todos los artículos de la base de datos
        initRecyclerView(catalogoList);

        // Inicializa el botón de Crear Pedido
        Button btnCrearPedido = findViewById(R.id.bRealizarPedido); // Asegúrate de que el ID del botón es correcto.
        btnCrearPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para crear el pedido.
                crearPedido();
                finish();
            }
        });
    }
    private void initRecyclerView(List<Catalogo> catalogoList) {
        catalogoAdapter = new CatalogoAdapter(this, catalogoList);
        catalogoPedido.setLayoutManager(new LinearLayoutManager(this));
        catalogoPedido.setAdapter(catalogoAdapter);
    }


    private void crearPedido() {
        List<Catalogo> itemsSeleccionados = getItemsSeleccionados();
        if (itemsSeleccionados.isEmpty()) {
            Toast.makeText(this, "No se han seleccionado artículos", Toast.LENGTH_SHORT).show();
            return;
        }
        CabPedidos nuevoPedido = new CabPedidos();
        nuevoPedido.setIdPartner(idPartner);
        nuevoPedido.setIdComercial(idComercial);
        nuevoPedido.setFechaPedido(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        long idPedido = db.addCabPedido(nuevoPedido);
        if (idPedido == -1) {
            Toast.makeText(this, "Error al crear el pedido", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Catalogo catalogo : itemsSeleccionados) {
            LineasPedido lineaPedido = new LineasPedido();
            lineaPedido.setIdPedido((int) idPedido);
            lineaPedido.setIdArticulo(catalogo.getIdArticulo());
            lineaPedido.setCantidad(catalogo.getQuantity());
            lineaPedido.setDescuento(0);
            lineaPedido.setPrecio(catalogo.getPvVent());

            long idLinea = db.addLineaPedido(lineaPedido);
            if (idLinea == -1) {
                Toast.makeText(this, "Error al insertar la línea de pedido para el artículo: " + catalogo.getNombre(), Toast.LENGTH_SHORT).show();
            }
        }

        Toast.makeText(this, "Pedido creado con éxito", Toast.LENGTH_SHORT).show();
    }

    private List<Catalogo> getItemsSeleccionados() {
        List<Catalogo> seleccionados = new ArrayList<>();
        for (Catalogo articulo : catalogoList) {
            if (articulo.isSelected() && articulo.getQuantity() > 0) {
                seleccionados.add(articulo);
            }
        }
        return seleccionados;
    }

    private void mostrarFechaActual(TextView textViewFecha) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        textViewFecha.setText(formattedDate);
    }

}
