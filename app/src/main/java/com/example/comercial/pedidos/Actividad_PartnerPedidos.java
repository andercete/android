package com.example.comercial.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.comercial.Metodos;
import com.example.comercial.R;

public class Actividad_PartnerPedidos extends AppCompatActivity {
    PedidoListAdapter mAdapter;
    RecyclerView recyclerView;

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
        tTelefono.setText(String.valueOf(telefono));
        tEmail.setText(email);

        copiarXMLaAlmacenamientoInterno();

        List<Pedido> pedidosList = parsePedidosXML();

        initRecyclerView(pedidosList);

    }
    private void copiarXMLaAlmacenamientoInterno() {
        // Crear la carpeta 'partners' dentro de 'files'
        File directorioPedidos = new File(getFilesDir(), "pedidos");
        if (!directorioPedidos.exists()) {
            directorioPedidos.mkdirs();
        }

        // Crear el archivo en la carpeta 'partners'
        File file = new File(directorioPedidos, "PEDIDOS.xml");

        try {
            // Obtener el nombre del archivo desde los recursos
            String nombreArchivo = "pedidos.xml";  // Reemplazar con el nombre correcto si es diferente

            // Abrir el archivo desde los recursos de la aplicación
            InputStream in = getAssets().open(nombreArchivo);

            // Crear un nuevo archivo en la carpeta 'partners'
            OutputStream out = new FileOutputStream(file);

            // Copiar el contenido del archivo desde los recursos al almacenamiento interno
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            // Cerrar los flujos de entrada y salida
            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initRecyclerView(List<Pedido> pedidos) {
        PedidoListAdapter PedidoListAdapter = new PedidoListAdapter(pedidos, this);
        recyclerView = findViewById(R.id.listPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(PedidoListAdapter);
    }
    private List<Pedido> parsePedidosXML() {
        List<Pedido> pedidos = new ArrayList<>();
        File directory = new File(getFilesDir(), "pedidos");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith("_pedidos.xml")) {
                        if (file.getName().equalsIgnoreCase(Metodos.getNombreArchivoPedidos()))
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
        File file = new File(new File(getFilesDir(), "pedidos"), Metodos.getNombreArchivoPedidos());
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