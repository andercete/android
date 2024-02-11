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

import com.example.comercial.BBDD.AnderBD;
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
    private AnderBD db;

    private List<Catalogo> catalogoList; // Lista de ítems del catálogo

    private CatalogoAdapter catalogoAdapter;
    int idPartner, idComercial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapedido);
        db = new AnderBD(this);
        FechaPedido = findViewById(R.id.textFechaPedido);
        NombrePartner = findViewById(R.id.textNombrePartner);
        catalogoPedido = findViewById(R.id.rCatalogoPedido);
        Intent intent = getIntent();
        idPartner = intent.getIntExtra("partnerId", -1); // El ID del partner seleccionado
        // Inicializar la instancia de la base de datos
        db = new AnderBD(this);

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
añadirArticulosPorDefecto();

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
    private void añadirArticulosPorDefecto() {
        // Asumiendo que tienes un método en AnderBD para verificar si un artículo ya existe por nombre
        // y otro para añadir un nuevo artículo si no existe.
        // Ejemplo de cómo añadir un artículo si no existe
        if (!db.existeArticulo("Carne pollo")) {
            db.addArticulo(new Catalogo(0, "Carne pollo", "Muslos", "Euskal Okela", 5.0, 5.0, 5, "carne"));
        }
        if (!db.existeArticulo("Carne Ternera Solomillo")) {
            db.addArticulo(new Catalogo(0, "Carne Ternera Solomillo", "Solomillo de ternera", "Basaxterri", 13.0, 10.0, 10, "solomillo"));
        }
        if (!db.existeArticulo("Carne Ternera Chuletón")) {
            db.addArticulo(new Catalogo(0, "Carne Ternera Chuletón", "Chuletón de ternera",  "Baserriberrí", 15.0, 12.0, 7, "chuleton"));
        }
        if (!db.existeArticulo("Carne Cerdo Lomo")) {
            db.addArticulo(new Catalogo(0, "Carne Cerdo Lomo", "Lomo de cerdo",  "Euskal Txerria", 9.0, 7.0, 15, "lomo"));
        }
        if (!db.existeArticulo("Carne Cordero Chuletas")) {
            db.addArticulo(new Catalogo(0, "Carne Cordero Chuletas", "Chuletas de cordero", "Raza latxa", 12.0, 9.0, 20, "chuletascordero"));
        }
        if (!db.existeArticulo("Pescado Anchoas del Cantábrico")) {
            db.addArticulo(new Catalogo(0, "Pescado Anchoas del Cantábrico", "Anchoas del Cantábrico", "Bizkaiko Ura", 20.0, 16.0, 5, "anchoas"));
        }
        if (!db.existeArticulo("Pescado Bonito del Norte")) {
            db.addArticulo(new Catalogo(0, "Pescado Bonito del Norte", "Bonito del Norte","Bonito del Norte", 18.0, 14.0, 8, "bonito"));
        }
        if (!db.existeArticulo("Bebida Sidra Zapian")) {
            db.addArticulo(new Catalogo(0, "Bebida Sidra Zapian", "Sidra Zapian", "Bilbo Bodegak", 5.0, 3.0, 30, "sidra"));
        }
        if (!db.existeArticulo("Bebida Txakoli Ameztoi")) {
            db.addArticulo(new Catalogo(0, "Bebida Txakoli Ameztoi", "Txakoli Ameztoi", "Txakoli Ameztoi", 7.0, 5.0, 25, "tx"));
        }
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
        for (Catalogo catalogo : catalogoList) {
            if (catalogo.isSelected() && catalogo.getQuantity() > 0) {
                seleccionados.add(catalogo);
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
