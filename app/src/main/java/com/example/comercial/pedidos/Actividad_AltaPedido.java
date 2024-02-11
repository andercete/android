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
import com.example.comercial.Catalogo.Catalogo;
import com.example.comercial.Catalogo.CatalogoAdapter;
import com.example.comercial.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    int idPartner,idComercial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapedido);

        FechaPedido = findViewById(R.id.textFechaPedido);
        NombrePartner = findViewById(R.id.textNombrePartner);
        catalogoPedido = findViewById(R.id.rCatalogoPedido);
        Intent intent = getIntent();
        idPartner = intent.getIntExtra("partnerId", -1); // El ID del partner seleccionado
        // Inicializar la instancia de la base de datos
        db = new AnderBD(this);
        SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasComerciales", Context.MODE_PRIVATE);
        int comercial = sharedPreferences.getInt("idComercial", 1);
        idComercial = comercial;
        String nombreRelacionado = db.buscarNombrePorIdPartnerEnCabPedido(idPartner);

        // Mostrar la fecha actual en el TextView
        mostrarFechaActual(FechaPedido);
        NombrePartner.setText(nombreRelacionado != null ? nombreRelacionado : "No encontrado");

// Cargar los datos del catálogo (esto dependerá de cómo almacenes tus datos)
        catalogoList = cargarDatosCatalogo(); // Implementa este método según tus necesidades

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
        catalogoPedido = findViewById(R.id.rCatalogoPedido); // Asegúrate de que este ID está correcto
        catalogoAdapter = new CatalogoAdapter(this, catalogoList);
        catalogoPedido.setLayoutManager(new LinearLayoutManager(this));
        catalogoPedido.setAdapter(catalogoAdapter);
    }
    private List<Catalogo> cargarDatosCatalogo() {
        List<Catalogo> catalogoList = new ArrayList<>();
        // Suponiendo que tienes un método parseCatalogoXML que retorna una lista de objetos Catalogo
        try {
            InputStream in = getAssets().open("catalogo.xml");
            catalogoList = parseCatalogoXML();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar datos del catálogo", Toast.LENGTH_SHORT).show();
        }
        return catalogoList;
    }

    private List<Catalogo> parseCatalogoXML() {
        List<Catalogo> catalogoList = new ArrayList<>();
        File file = new File(getFilesDir(), "catalogo/CATALOGO.xml");

        if (file.exists()) {
            catalogoList.addAll(parseXMLFile(file));
        } else {
            Toast.makeText(this, "El archivo CATALOGO.xml no existe", Toast.LENGTH_SHORT).show();
        }
        return catalogoList;
    }

    private List<Catalogo> parseXMLFile(File file) {
        List<Catalogo> catalogoList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(fis, null);

            Catalogo currentCatalogo = null;
            String currentTag = null; // Temporary variable to hold the current tag name
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if ("articulo".equals(currentTag)) {
                            currentCatalogo = new Catalogo();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (currentCatalogo != null && currentTag != null) {
                            switch (currentTag) {
                                case "idArticulo":
                                    currentCatalogo.setIdArticulo(text);
                                    break;
                                case "nombre":
                                    currentCatalogo.setNombre(text);
                                    break;
                                case "descripcion":
                                    currentCatalogo.setDescripcion(text);
                                    break;
                                case "proveedor":
                                    currentCatalogo.setProveedor(text);
                                    break;
                                case "prVenta":
                                    currentCatalogo.setPrVent(Float.parseFloat(text));
                                    break;
                                case "prCoste":
                                    currentCatalogo.setPrCost(Float.parseFloat(text));
                                    break;
                                case "existencias":
                                    currentCatalogo.setExistencias(Integer.parseInt(text));
                                    break;
                                case "Direccion_Imagen":
                                    currentCatalogo.setImageName(text);
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("articulo".equals(parser.getName()) && currentCatalogo != null) {
                            catalogoList.add(currentCatalogo);
                            currentCatalogo = null; // Reset currentCatalogo for the next item
                        }
                        currentTag = null; // Reset currentTag after processing end tag
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Assuming this method is in an Activity. If not, you'll need to modify how you show the toast.
            Toast.makeText(this, "Error al parsear CATALOGO.xml", Toast.LENGTH_SHORT).show();
        }

        return catalogoList;
    }
    private void crearPedido() {
        List<Catalogo> itemsSeleccionados = getItemsSeleccionados();
        if (itemsSeleccionados.isEmpty()) {
            Toast.makeText(this, "No se han seleccionado artículos", Toast.LENGTH_SHORT).show();
            return;
        }
        CabPedidos nuevoPedido = new CabPedidos();
        nuevoPedido.setIdPartner(idPartner); // Asegúrate de que idPartner sea el ID correcto del partner
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
            lineaPedido.setDescuento(0); // Por defecto 0, ajustar si es necesario
            lineaPedido.setPrecio(catalogo.getPrVent());

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
