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
                    copiarXMLaAlmacenamientoInterno();
                    realizarPedido();
                    limpiar_vistas();
                    // Puedes agregar aquí cualquier otra lógica después de realizar el pedido
                }
            }
        });
    }

    private void realizarPedido() {
        Pedido nuevoPedido = new Pedido(
                eIdPedido.getText().toString(),
                eIdPartner.getText().toString(),
                eIdLinea.getText().toString(),
                eIdArticulo.getText().toString(),
                Integer.parseInt(eCantidad.getText().toString()),
                Float.parseFloat(eDescuento.getText().toString()),
                Float.parseFloat(ePrecio.getText().toString())
        );

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
            pedidoElement.appendChild(createElementWithText(doc, "idPedido", nuevoPedido.getIdPedido()));
            pedidoElement.appendChild(createElementWithText(doc, "idPartner", nuevoPedido.getIdPartner()));
            pedidoElement.appendChild(createElementWithText(doc, "idLinea", nuevoPedido.getIdLinea()));
            pedidoElement.appendChild(createElementWithText(doc, "idArticulo", nuevoPedido.getIdArticulo()));
            pedidoElement.appendChild(createElementWithText(doc, "cantidad", String.valueOf(nuevoPedido.getCantidad())));
            pedidoElement.appendChild(createElementWithText(doc, "descuento", String.valueOf(nuevoPedido.getDescuento())));
            pedidoElement.appendChild(createElementWithText(doc, "precio", String.valueOf(nuevoPedido.getPrecio())));
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
    private void copiarXMLaAlmacenamientoInterno() {
        // Crear la carpeta 'partners' dentro de 'files'
        File directorioPedidos = new File(getFilesDir(), "pedidos");
        if (!directorioPedidos.exists()) {
            directorioPedidos.mkdirs();
        }


        // Crear el archivo en la carpeta 'partners'
        File file = new File(directorioPedidos, getNombreArchivoFecha());
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

    private String getNombreArchivoFecha() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + "_pedidos.xml";
        return nombrearchivo;
    }

    private boolean validarCampos() {
        if (eIdPedido.length() == 0) {
            mostrarError("Por favor, ingrese un ID de Pedido", eIdPedido);
            return false;
        } else if (eIdPartner.length() == 0) {
            mostrarError("Por favor, ingrese un ID de Partner", eIdPartner);
            return false;
        } else if (eIdLinea.length() == 0) {
            mostrarError("Por favor, ingrese un ID de Línea", eIdLinea);
            return false;
        } else if (eIdArticulo.length() == 0) {
            mostrarError("Por favor, ingrese un ID de Artículo", eIdArticulo);
            return false;
        } else if (eCantidad.length() == 0) {
            mostrarError("Por favor, ingrese una Cantidad", eCantidad);
            return false;
        } else if (eDescuento.length() == 0) {
            mostrarError("Por favor, ingrese un Descuento", eDescuento);
            return false;
        } else if (ePrecio.length() == 0) {
            mostrarError("Por favor, ingrese un Precio", ePrecio);
            return false;
        }

        // Puedes agregar más validaciones según sea necesario
        return true;
    }
    private void mostrarError(String mensaje, final EditText editText) {
        dialog = new AlertDialog.Builder(Actividad_AltaPedido.this);
        dialog.setTitle("Error");
        dialog.setMessage(mensaje);
        dialog.setCancelable(false);
        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogo, int id) {
                dialogo.cancel();
                editText.requestFocus();
            }
        });
        dialog.show();
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
