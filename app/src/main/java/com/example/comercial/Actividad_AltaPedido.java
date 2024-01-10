package com.example.comercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.res.Configuration;
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

        actualizarColorBoton();
        setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

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
                eIdPartner.getText().toString(),
                eIdArticulo.getText().toString(),
                Integer.parseInt(eCantidad.getText().toString()),
                ePoblacion.getText().toString(),
                Float.parseFloat(eDescuento.getText().toString())
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
            pedidoElement.appendChild(createElementWithText(doc, "idPartner", nuevoPedido.getIdPartner()));
            pedidoElement.appendChild(createElementWithText(doc, "idArticulo", nuevoPedido.getIdArticulo()));
            pedidoElement.appendChild(createElementWithText(doc, "cantidad", String.valueOf(nuevoPedido.getCantidad())));
            pedidoElement.appendChild(createElementWithText(doc, "poblacion", nuevoPedido.getPoblacion()));
            pedidoElement.appendChild(createElementWithText(doc, "descuento", String.valueOf(nuevoPedido.getDescuento())));
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
    private void actualizarColorBoton() {
        // Obtén el tema actual de la aplicación
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Determina si el tema actual es oscuro
        boolean isDarkTheme = currentTheme == Configuration.UI_MODE_NIGHT_YES;

        // Configura el color de fondo del botón según el tema
        if (isDarkTheme) {
            bRealizarPedido.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonOscuro));
            bLimpiar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonOscuro));
        } else {
            bRealizarPedido.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonClaro));
            bLimpiar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonClaro));
        }
    }

    private void copiarXMLaAlmacenamientoInterno() {
        // Crear la carpeta 'pedidos' dentro de 'files'
        File directorioPedidos = new File(getFilesDir(), "pedidos");
        if (!directorioPedidos.exists()) {
            directorioPedidos.mkdirs();
        }

        // Crear el archivo en la carpeta 'pedidos'
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
        if (!isValidCampo(eIdPartner, "ID de Partner", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(eIdArticulo, "ID de Artículo", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(eCantidad, "Cantidad", FieldType.INTEGER)) {
            return false;
        }
        if (!isValidCampo(ePoblacion, "Poblacion", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(eDescuento, "Descuento", FieldType.PERCENTAGE)) {
            return false;
        }

        // Puedes agregar más validaciones según sea necesario
        return true;
    }

    private enum FieldType {
        STRING,
        INTEGER,
        FLOAT,
        PERCENTAGE
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private boolean isValidCampo(EditText editText, String campoName, FieldType fieldType) {
        String input = editText.getText().toString().trim();

        if (input.length() == 0) {
            mostrarError("Por favor, ingrese un " + campoName, editText);
            return false;
        }

        switch (fieldType) {
            case STRING:
                if (input.length() <= 1) {
                    mostrarError(campoName + " debe tener más de un carácter", editText);
                    return false;
                }if (isNumeric(input)) {
                    mostrarError("El campo " + campoName + " no puede contener solo números", editText);
                    return false;
                }
                break;
            case INTEGER:
                try {
                    int value = Integer.parseInt(input);
                    if (value <= 0) {
                        mostrarError(campoName + " debe ser mayor que cero", editText);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Por favor, ingrese un " + campoName + " válido", editText);
                    return false;
                }
                break;
            case FLOAT:
                try {
                    float value = Float.parseFloat(input);
                    if (value <= 0) {
                        mostrarError(campoName + " debe ser mayor que cero", editText);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Por favor, ingrese un " + campoName + " válido", editText);
                    return false;
                }
                break;
            case PERCENTAGE:
                try {
                    float value = Float.parseFloat(input);
                    if (value < 0 || value > 100) {
                        mostrarError(campoName + " debe estar entre 0 y 100", editText);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Por favor, ingrese un " + campoName + " válido", editText);
                    return false;
                }
                break;
        }

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
        eIdPartner.setText("");
        eIdArticulo.setText("");
        eCantidad.setText("");
        ePoblacion.setText("");
        eDescuento.setText("");
    }
}
