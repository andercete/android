package com.example.comercial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Actividad_Pedidos extends AppCompatActivity {

    PedidoListAdapter mAdapter;
    RecyclerView recyclerView;
    Button bAgregarPedido;
    Button bBorrarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pedidos);

        bAgregarPedido = findViewById(R.id.bPedidoAgregar);
        bAgregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad_Pedidos.this, Actividad_AltaPedido.class);
                startActivity(intent);
            }
        });

        bBorrarPedido = findViewById(R.id.bBorrar2);
        bBorrarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarRegistros();
            }
        });

        List<Pedido> pedidosList = parsePedidosXML();

        initRecyclerView(pedidosList);
    }

    private void initRecyclerView(List<Pedido> pedidos) {
        PedidoListAdapter PedidoListAdapter = new PedidoListAdapter(pedidos, this);
        recyclerView = findViewById(R.id.list_pedidos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(PedidoListAdapter);
    }

    private String getNombreArchivoFecha() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + "_pedidos.xml";
        return nombrearchivo;
    }

    private List<Pedido> parsePedidosXML() {
        List<Pedido> pedidos = new ArrayList<>();
        File directory = new File(getFilesDir(), "pedidos");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith("_pedidos.xml")) {
                        if (file.getName() == getNombreArchivoFecha())
                        {
                            pedidos.addAll(parseXMLFile(file));
                        }
                    }
                }
            }
        }
        return pedidos;
    }

    private List<Pedido> parseXMLFile(File file) {
        List<Pedido> pedidosInFile = new ArrayList<>();
        Pedido currentPedido = null;
        String currentTag = null;

        try {
            InputStream inputStream = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("pedido".equals(tagName)) {
                            currentPedido = new Pedido();
                        }
                        currentTag = tagName;
                        break;

                    case XmlPullParser.TEXT:
                        if (currentPedido != null && currentTag != null) {
                            switch (currentTag) {
                                case "idPartner":
                                    currentPedido.setIdPartner(parser.getText());
                                    break;
                                case "idArticulo":
                                    currentPedido.setIdArticulo(parser.getText());
                                    break;
                                case "cantidad":
                                    currentPedido.setCantidad(Integer.parseInt(parser.getText()));
                                    break;
                                case "descuento":
                                    currentPedido.setDescuento(Float.parseFloat(parser.getText()));
                                    break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ("pedido".equals(tagName) && currentPedido != null) {
                            pedidosInFile.add(currentPedido);
                            currentPedido = null;
                        }
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pedidosInFile;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Pedido> updatedPedidosList = parsePedidosXML();
        if (mAdapter != null) {
            mAdapter.setItems(updatedPedidosList);
            mAdapter.notifyDataSetChanged();
        } else {
            initRecyclerView(updatedPedidosList);
        }
    }

    private void borrarRegistros() {
        File file = new File(new File(getFilesDir(), "pedidos"), getNombreArchivoFecha());
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(this, "Archivo borrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al borrar el archivo", Toast.LENGTH_SHORT).show();
            }
        }
        recreate();
    }
}