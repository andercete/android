package com.example.comercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.widget.EditText;

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

        copiarXMLaAlmacenamientoInterno();
        bLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiar_vistas();
            }
        });
        bAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eNombre.length() == 0) {
                    dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                    dialog.setTitle("Error");
                    dialog.setMessage("Porfavor ingrese un nombre");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogo, int id) {
                            dialogo.cancel();
                            eNombre.requestFocus();
                        }
                    });
                    dialog.show();
                } else {
                    if (eDireccion.length() == 0) {
                        dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                        dialog.setTitle("Error");
                        dialog.setMessage("Porfavor ingrese una direccion");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogo, int id) {
                                dialogo.cancel();
                                eDireccion.requestFocus();
                            }
                        });
                        dialog.show();
                    } else {
                        if (ePoblacion.length() == 0) {
                            dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                            dialog.setTitle("Error");
                            dialog.setMessage("Porfavor ingrese una poblacion");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogo, int id) {
                                    dialogo.cancel();
                                    ePoblacion.requestFocus();
                                }
                            });
                            dialog.show();
                        } else {
                            if (eCif.length() == 0) {
                                dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                                dialog.setTitle("Error");
                                dialog.setMessage("Porfavor ingrese un cif");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogo, int id) {
                                        dialogo.cancel();
                                        eCif.requestFocus();
                                    }
                                });
                                dialog.show();
                            } else {
                                if (eTelefono.length() == 0) {
                                    dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                                    dialog.setTitle("Error");
                                    dialog.setMessage("Porfavor ingrese un telefono");
                                    dialog.setCancelable(false);
                                    dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogo, int id) {
                                            dialogo.cancel();
                                            eTelefono.requestFocus();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    if (eEmail.length() == 0) {
                                        dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                                        dialog.setTitle("Error");
                                        dialog.setMessage("Porfavor ingrese un email");
                                        dialog.setCancelable(false);
                                        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogo, int id) {
                                                dialogo.cancel();
                                                eEmail.requestFocus();
                                            }
                                        });
                                        dialog.show();
                                    } else {
                                        altaSocio();
                                        finish();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    private void altaSocio() {
        Partner nuevoSocio = new Partner(
                eNombre.getText().toString(),
                eDireccion.getText().toString(),
                ePoblacion.getText().toString(),
                eCif.getText().toString(),
                eTelefono.getText().toString(),
                eEmail.getText().toString()
        );

        try {
            File file = new File(getFilesDir(), "partners.xml");
            Document doc;
            Element root;

            // Si el archivo no existe, inicializamos un nuevo documento XML
            if (!file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.newDocument();
                // Crear el elemento raíz 'partners'
                root = doc.createElement("partners");
                doc.appendChild(root);
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(file);
                root = doc.getDocumentElement();
            }

            // Crear un nuevo elemento partner y añadirlo al documento
            Element partnerElement = doc.createElement("partner");
            partnerElement.appendChild(createElementWithText(doc, "nombre", nuevoSocio.getNombre()));
            partnerElement.appendChild(createElementWithText(doc, "direccion", nuevoSocio.getDireccion()));
            partnerElement.appendChild(createElementWithText(doc, "poblacion", nuevoSocio.getPoblacion()));
            partnerElement.appendChild(createElementWithText(doc, "cif", nuevoSocio.getCif()));
            partnerElement.appendChild(createElementWithText(doc, "telefono", nuevoSocio.getTelefono()));
            partnerElement.appendChild(createElementWithText(doc, "email", nuevoSocio.getEmail()));
            root.appendChild(partnerElement);

            // Guardar el documento modificado de nuevo en el archivo
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Ajusta el número para controlar el nivel de indentación

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


    //metodo usado para crear copia del XML en almacenamiento interno de assets > partners.xml
    // (solo los archivos en almacenamiento interno permiten actualizarse cuando el programa esta en ejecucion)
    private void copiarXMLaAlmacenamientoInterno() {
        File file = new File(getFilesDir(), "partners.xml");
        if (!file.exists()) {
            try {
                InputStream in = getAssets().open("partners.xml");
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
        eNombre.setText("");
        eDireccion.setText("");
        ePoblacion.setText("");
        eCif.setText("");
        eTelefono.setText("");
        eEmail.setText("");
    }

}