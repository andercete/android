package com.example.comercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
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

public class Actividad_Altasocio extends AppCompatActivity {

    EditText eNombre;
    EditText eDireccion;
    EditText ePoblacion;
    EditText eCif;
    EditText eTelefono;
    EditText eEmail;
    Button bAlta;
    Button bLimpiar;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_altasocio);

        eNombre = findViewById(R.id.eAltaNombre);
        eDireccion = findViewById(R.id.eAltaDireccion);
        ePoblacion = findViewById(R.id.eAltaPoblacion);
        eCif = findViewById(R.id.eAltaCif);
        eTelefono = findViewById(R.id.eAltaTelefono);
        eEmail = findViewById(R.id.eAltaEmail);
        bAlta = findViewById(R.id.bAlta);
        bLimpiar = findViewById(R.id.bAltaLimpiar);

        bLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar_vistas();
            }
        });

        bAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    copiarXMLaAlmacenamientoInterno();
                    altaSocio();
                    finish();
                }
            }
        });
    }

    private void copiarXMLaAlmacenamientoInterno() {
        // Crear la carpeta 'partners' dentro de 'files'
        File directorioPartners = new File(getFilesDir(), "partners");
        if (!directorioPartners.exists()) {
            directorioPartners.mkdirs();
        }

        // Crear el archivo en la carpeta 'partners'
        File file = new File(directorioPartners, getNombreArchivoFecha());
        if (!file.exists()) {
            try {
                // Obtener el nombre del archivo desde los recursos
                String nombreArchivo = "partners.xml";  // Reemplazar con el nombre correcto si es diferente

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
    }

    private boolean validarCampos() {
        if (!isValidCampo(eNombre, "Nombre", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(eDireccion, "Dirección", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(ePoblacion, "Población", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(eCif, "CIF", FieldType.STRING)) {
            return false;
        }
        if (!isValidCampo(eTelefono, "Teléfono", FieldType.TELEPHONE)) {
            return false;
        }
        if (!isValidCampo(eEmail, "Email", FieldType.EMAIL)) {
            return false;
        }

        // Puedes agregar más validaciones según sea necesario
        return true;
    }

    private void altaSocio() {
        Partner nuevoSocio = new Partner(
                eNombre.getText().toString(),
                eDireccion.getText().toString(),
                ePoblacion.getText().toString(),
                eCif.getText().toString(),
                Integer.parseInt(eTelefono.getText().toString()),
                eEmail.getText().toString()
        );

        try {
            File file = new File(new File(getFilesDir(), "partners"), getNombreArchivoFecha());
            Document doc;
            Element root;

            if (!file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.newDocument();
                root = doc.createElement("partners");
                doc.appendChild(root);
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(file);
                root = doc.getDocumentElement();
            }

            Element partnerElement = doc.createElement("partner");
            partnerElement.appendChild(createElementWithText(doc, "nombre", nuevoSocio.getNombre()));
            partnerElement.appendChild(createElementWithText(doc, "direccion", nuevoSocio.getDireccion()));
            partnerElement.appendChild(createElementWithText(doc, "poblacion", nuevoSocio.getPoblacion()));
            partnerElement.appendChild(createElementWithText(doc, "cif", nuevoSocio.getCif()));
            partnerElement.appendChild(createElementWithText(doc, "telefono", String.valueOf(nuevoSocio.getTelefono())));
            partnerElement.appendChild(createElementWithText(doc, "email", nuevoSocio.getEmail()));
            root.appendChild(partnerElement);

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date()) + ".xml";
    }

    public void limpiar_vistas() {
        eNombre.setText("");
        eDireccion.setText("");
        ePoblacion.setText("");
        eCif.setText("");
        eTelefono.setText("");
        eEmail.setText("");
    }

    private enum FieldType {
        STRING,
        INTEGER,
        EMAIL,
        TELEPHONE
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
                }
                // Verificar que no haya solo números en el campo de texto
                if (isNumeric(input)) {
                    mostrarError("El campo " + campoName + " no puede contener solo números", editText);
                    return false;
                }
                // Puedes agregar más lógica específica para cadenas si es necesario
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
            case EMAIL:
                if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    mostrarError("Por favor, ingrese un " + campoName + " válido", editText);
                    return false;
                }
                break;
            case TELEPHONE:
                if (!Patterns.PHONE.matcher(input).matches()) {
                    mostrarError("Por favor, ingrese un " + campoName + " válido", editText);
                    return false;
                }
                break;
        }

        return true;
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void mostrarError(String mensaje, final EditText editText) {
        dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
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
}