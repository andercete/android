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
    Spinner sAlta;
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

        //CREO CARPETA, ESTO MEJOR IMPLEMENTAR EN ACTIVIDAD PARTNERS, MAÑANA LO MIRO MEJOR
        File directorioPartners = new File(getFilesDir(), "partners");
        if (!directorioPartners.exists()) {
            directorioPartners.mkdir(); // Crear la carpeta si no existe
        }

        sAlta = findViewById(R.id.sAlta);
        List<String> opciones = new ArrayList<>();
        opciones.add("Gourmet Euskadi Market"); // Única opción real

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sAlta.setAdapter(adapter);

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
                                        if (sAlta.getSelectedItemPosition() != 0) {
                                            dialog = new AlertDialog.Builder(Actividad_Altasocio.this);
                                            dialog.setTitle("Error");
                                            dialog.setMessage("Porfavor elige un comercial");
                                            dialog.setCancelable(false);
                                            dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogo, int id) {
                                                    dialogo.cancel();
                                                    sAlta.requestFocus();
                                                }
                                            });
                                            dialog.show();
                                        } else {
                                            copiarXMLaAlmacenamientoInterno();
                                            altaSocio();
                                            finish();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private String getNombreArchivoFecha() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + ".xml";
        return nombrearchivo;
    }

    //metodo usado para crear copia del XML en almacenamiento interno de assets > partners.xml
    // (solo los archivos en almacenamiento interno permiten actualizarse cuando el programa esta en ejecucion)
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
    private void altaSocio() {
        String comercialSeleccionado = sAlta.getSelectedItem().toString();
        Partner nuevoSocio = new Partner(
                eNombre.getText().toString(),
                eDireccion.getText().toString(),
                ePoblacion.getText().toString(),
                eCif.getText().toString(),
                Integer.parseInt(eTelefono.getText().toString()),
                eEmail.getText().toString(),comercialSeleccionado
        );

        try {
            File file = new File(new File(getFilesDir(), "partners"), getNombreArchivoFecha());
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
            partnerElement.appendChild(createElementWithText(doc, "telefono", String.valueOf(nuevoSocio.getTelefono())));
            partnerElement.appendChild(createElementWithText(doc, "email", nuevoSocio.getEmail()));
            partnerElement.appendChild(createElementWithText(doc, "comercial", nuevoSocio.getComercial()));
            root.appendChild(partnerElement);

            // Guardar el documento modificado de nuevo en el archivo
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // Ajusta el número para controlar el nivel de indentación

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

    public void limpiar_vistas() {
        eNombre.setText("");
        eDireccion.setText("");
        ePoblacion.setText("");
        eCif.setText("");
        eTelefono.setText("");
        eEmail.setText("");
    }

}