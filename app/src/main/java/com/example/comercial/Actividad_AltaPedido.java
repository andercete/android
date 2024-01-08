package com.example.comercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
public class Actividad_AltaPedido extends AppCompatActivity {
    EditText eIdPedido;
    EditText eIdPartner;
    EditText eIdLinea;
    EditText eIdArticulo;
    EditText eCantidad;
    EditText eDescuento;
    EditText ePrecio;
    Button bRealizarPedido;
    Button bLimpiar;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altapedido); // Asegúrate de tener un layout_altapedido.xml válido

        eIdPedido = findViewById(R.id.eIdPedido);
        eIdPartner = findViewById(R.id.eIdPartner);
        eIdLinea = findViewById(R.id.eIdLinea);
        eIdArticulo = findViewById(R.id.eIdArticulo);
        eCantidad = findViewById(R.id.eCantidad);
        eDescuento = findViewById(R.id.eDescuento);
        ePrecio = findViewById(R.id.ePrecio);
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
                    realizarPedido();
                    limpiar_vistas();
                    // Puedes agregar aquí cualquier otra lógica después de realizar el pedido
                }
            }
        });
    }

    private void realizarPedido() {
        String idPedido = eIdPedido.getText().toString();
        String idPartner = eIdPartner.getText().toString();
        String idLinea = eIdLinea.getText().toString();
        String idArticulo = eIdArticulo.getText().toString();
        String cantidad = eCantidad.getText().toString();
        String descuento = eDescuento.getText().toString();
        String precio = ePrecio.getText().toString();

        try {
            File file = new File(new File(getFilesDir(), "pedidos"), getNombreArchivoFecha());
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
            pedidoElement.appendChild(createElementWithText(doc, "idPedido", idPedido));
            pedidoElement.appendChild(createElementWithText(doc, "idPartner", idPartner));
            pedidoElement.appendChild(createElementWithText(doc, "idLinea", idLinea));
            pedidoElement.appendChild(createElementWithText(doc, "idArticulo", idArticulo));
            pedidoElement.appendChild(createElementWithText(doc, "cantidad", cantidad));
            pedidoElement.appendChild(createElementWithText(doc, "descuento", descuento));
            pedidoElement.appendChild(createElementWithText(doc, "precio", precio));
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

    private Element createElementWithText(Document doc, String tagName, String text) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(text));
        return element;
    }

    private String getNombreArchivoFecha() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + "_pedidos.xml";
        return nombrearchivo;
    }

    private boolean validarCampos() {
        if (eIdPedido.length() == 0 || eIdPartner.length() == 0 || eIdLinea.length() == 0 ||
                eIdArticulo.length() == 0 || eCantidad.length() == 0 || eDescuento.length() == 0 || ePrecio.length() == 0) {

            dialog = new AlertDialog.Builder(Actividad_AltaPedido.this);
            dialog.setTitle("Error");
            dialog.setMessage("Por favor, complete todos los campos");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogo, int id) {
                    dialogo.cancel();
                }
            });
            dialog.show();
            return false;
        }
        // Aquí puedes agregar más validaciones según tus requisitos, como verificar si los campos numéricos son válidos, etc.
        return true;
    }

    public void limpiar_vistas() {
        eIdPedido.setText("");
        eIdPartner.setText("");
        eIdLinea.setText("");
        eIdArticulo.setText("");
        eCantidad.setText("");
        eDescuento.setText("");
        ePrecio.setText("");
    }
}
