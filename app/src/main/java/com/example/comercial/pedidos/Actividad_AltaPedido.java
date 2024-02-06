package com.example.comercial.pedidos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.comercial.BBDD.Pedido;
import com.example.comercial.Metodos;
import com.example.comercial.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
public class Actividad_AltaPedido extends AppCompatActivity {
    EditText eIdPartner;
    EditText eIdArticulo;
    EditText eCantidad;
    EditText ePoblacion;
    EditText eDescuento;
    Button bRealizarPedido;
    Button bLimpiar;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapedido); // Asegúrate de tener un layout_altapedido.xml válido

        eIdPartner = findViewById(R.id.eIdPartner);
        eIdArticulo = findViewById(R.id.eIdArticulo);
        eCantidad = findViewById(R.id.eCantidad);
        ePoblacion = findViewById(R.id.eAltaPoblacion3);
        eDescuento = findViewById(R.id.eDescuento);
        bRealizarPedido = findViewById(R.id.bRealizarPedido);
        bLimpiar = findViewById(R.id.bLimpiar);

        bLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar_vistas();
            }
        });

        bRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    copiarXMLaAlmacenamientoInterno();
                    realizarPedido();
                    limpiar_vistas();
                }
            }
        });
    }
    private boolean validarCampos() {
        if (!Metodos.isValidCampo(eIdPartner, "ID de Partner", Metodos.FieldType.STRING, Actividad_AltaPedido.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eIdArticulo, "ID de Artículo", Metodos.FieldType.STRING,Actividad_AltaPedido.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eCantidad, "Cantidad", Metodos.FieldType.INTEGER,Actividad_AltaPedido.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(ePoblacion, "Poblacion", Metodos.FieldType.STRING,Actividad_AltaPedido.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eDescuento, "Descuento", Metodos.FieldType.PERCENTAGE,Actividad_AltaPedido.this)) {
            return false;
        }

        // Puedes agregar más validaciones según sea necesario
        return true;
    }

    private void realizarPedido() {
        Pedido nuevoPedido = new Pedido(
                eIdPartner.getText().toString(),
                eIdArticulo.getText().toString(),
                Integer.parseInt(eCantidad.getText().toString()),
                ePoblacion.getText().toString(),
                Float.parseFloat(eDescuento.getText().toString())
        );

        try {
            File file = new File(new File(getFilesDir(), "pedidos"), Metodos.getNombreArchivoPedidos());
            Document doc;
            Element root;

            // Si el archivo no existe, inicializamos un nuevo documento XML
            if (!file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.newDocument();
                // Crear el elemento raíz 'pedidos'
                root = doc.createElement("pedidos");
                doc.appendChild(root);
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(file);
                root = doc.getDocumentElement();
            }

            // Crear un nuevo elemento pedido y añadirlo al documento
            Element pedidoElement = doc.createElement("pedido");
            pedidoElement.appendChild(Metodos.createElementWithText(doc, "idPartner", nuevoPedido.getIdPartner()));
            pedidoElement.appendChild(Metodos.createElementWithText(doc, "idArticulo", nuevoPedido.getIdArticulo()));
            pedidoElement.appendChild(Metodos.createElementWithText(doc, "cantidad", String.valueOf(nuevoPedido.getCantidad())));
            pedidoElement.appendChild(Metodos.createElementWithText(doc, "poblacion", nuevoPedido.getPoblacion()));
            pedidoElement.appendChild(Metodos.createElementWithText(doc, "descuento", String.valueOf(nuevoPedido.getDescuento())));
            root.appendChild(pedidoElement);

            // Guardar el documento modificado de nuevo en el archivo
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void copiarXMLaAlmacenamientoInterno() {
        // Crear la carpeta 'pedidos' dentro de 'files'
        File directorioPedidos = new File(getFilesDir(), "pedidos");
        if (!directorioPedidos.exists()) {
            directorioPedidos.mkdirs();
        }

        // Crear el archivo en la carpeta 'pedidos'
        File file = new File(directorioPedidos, Metodos.getNombreArchivoPedidos());
        if (!file.exists()) {
            try {
                InputStream in = getAssets().open("pedidos.xml");
                OutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void limpiar_vistas() {
        eIdPartner.setText("");
        eIdArticulo.setText("");
        eCantidad.setText("");
        ePoblacion.setText("");
        eDescuento.setText("");
    }
}
