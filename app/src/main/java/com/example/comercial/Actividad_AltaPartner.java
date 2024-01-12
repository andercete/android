package com.example.comercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Actividad_AltaPartner extends AppCompatActivity {

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
        setContentView(R.layout.layout_altapartner);

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
                    altaSocio();
                    finish();
                }
            }
        });
    }

    private boolean validarCampos() {
        if (!Metodos.isValidCampo(eNombre, "Nombre", Metodos.FieldType.STRING,Actividad_AltaPartner.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eDireccion, "Dirección", Metodos.FieldType.STRING,Actividad_AltaPartner.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(ePoblacion, "Población", Metodos.FieldType.STRING,Actividad_AltaPartner.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eCif, "CIF", Metodos.FieldType.STRING,Actividad_AltaPartner.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eTelefono, "Teléfono", Metodos.FieldType.TELEPHONE,Actividad_AltaPartner.this)) {
            return false;
        }
        if (!Metodos.isValidCampo(eEmail, "Email", Metodos.FieldType.EMAIL,Actividad_AltaPartner.this)) {
            return false;
        }
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
            File file = new File(new File(getFilesDir(), "partners"), Metodos.getNombreArchivoPartners());
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
            partnerElement.appendChild(Metodos.createElementWithText(doc, "nombre", nuevoSocio.getNombre()));
            partnerElement.appendChild(Metodos.createElementWithText(doc, "direccion", nuevoSocio.getDireccion()));
            partnerElement.appendChild(Metodos.createElementWithText(doc, "poblacion", nuevoSocio.getPoblacion()));
            partnerElement.appendChild(Metodos.createElementWithText(doc, "cif", nuevoSocio.getCif()));
            partnerElement.appendChild(Metodos.createElementWithText(doc, "telefono", String.valueOf(nuevoSocio.getTelefono())));
            partnerElement.appendChild(Metodos.createElementWithText(doc, "email", nuevoSocio.getEmail()));
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

    public void limpiar_vistas() {
        eNombre.setText("");
        eDireccion.setText("");
        ePoblacion.setText("");
        eCif.setText("");
        eTelefono.setText("");
        eEmail.setText("");
    }
}