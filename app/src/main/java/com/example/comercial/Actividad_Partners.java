    package com.example.comercial;

    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Xml;
    import android.view.View;
    import android.widget.Button;
    import android.widget.Toast;

    import org.xmlpull.v1.XmlPullParser;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.InputStream;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;

    public class Actividad_Partners extends AppCompatActivity {

        //PUEDES ACCEDER A LA ACTIVIDAD PARA TESTEARLA YENDO A APP>EDIT CONFIGURATIONS>Launch(Specified Activity)>actividad_partners.

        ListAdapter mAdapter;
        RecyclerView recyclerView;
        Button bAgregar;
        Button bBorrar;
        AlertDialog.Builder dialog;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_partners);
            bAgregar = findViewById(R.id.bPartnerAgregar);
            bAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Actividad_Partners.this, Actividad_Altasocio.class);
                    startActivity(intent);
                }
            });
            bBorrar = findViewById(R.id.bBorrar);
            bBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarRegistros();
                }
            });
            // Suponiendo que ya tienes una lista de partners parseada
            List<Partner> partnersList = parsePartnersXML();

            // Inicializa el RecyclerView con la lista de partners
            initRecyclerView(partnersList);
        }

        private void initRecyclerView(List<Partner> partners) {
            ListAdapter listAdapter = new ListAdapter(partners, this);
            recyclerView = findViewById(R.id.listRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        }

        private String getNombreArchivoFecha() {
            String nombrearchivo;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            nombrearchivo = sdf.format(new Date()) + ".xml";
            return nombrearchivo;
        }
        private List<Partner> parsePartnersXML() {
            List<Partner> partners = new ArrayList<>();
            File directory = new File(getFilesDir(), "partners");


            // Verificar si el directorio existe y contiene archivos
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".xml")) {
                            if (file.getName() == getNombreArchivoFecha())
                            {
                                partners.addAll(parseXMLFile(file));
                            }
                        }
                    }
                }
            }
            return partners;
        }
        // Método para parsear tu XML de partners
        private List<Partner> parseXMLFile(File file) {
            List<Partner> partnersInFile = new ArrayList<>();
            Partner currentPartner = null;
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
                            if ("partner".equals(tagName)) {
                                currentPartner = new Partner();
                            }
                            currentTag = tagName;
                            break;

                        case XmlPullParser.TEXT:
                            if (currentPartner != null && currentTag != null) {
                                switch (currentTag) {
                                    case "nombre":
                                        currentPartner.setNombre(parser.getText());
                                        break;
                                    case "direccion":
                                        currentPartner.setDireccion(parser.getText());
                                        break;
                                    case "poblacion":
                                        currentPartner.setPoblacion(parser.getText());
                                        break;
                                    case "cif":
                                        currentPartner.setCif(parser.getText());
                                        break;
                                    case "telefono":
                                        currentPartner.setTelefono(Integer.parseInt(parser.getText()));
                                        break;
                                    case "email":
                                        currentPartner.setEmail(parser.getText());
                                        break;
                                }
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if ("partner".equals(tagName) && currentPartner != null) {
                                partnersInFile.add(currentPartner);
                                currentPartner = null;
                            }
                            currentTag = null;
                            break;
                    }
                    eventType = parser.next();
                }

                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace(); // Manejo de errores
            }

            return partnersInFile;
        }
        @Override
        protected void onResume() {
            super.onResume();
            // Actualiza la lista de socios cada vez que la actividad se reanuda
            List<Partner> updatedPartnersList = parsePartnersXML();
            if (mAdapter != null) {
                mAdapter.setItems(updatedPartnersList);
                mAdapter.notifyDataSetChanged();
            } else {
                // Si el adaptador no se ha inicializado todavía, inicialízalo aquí
                initRecyclerView(updatedPartnersList);
            }
        }
     //  Metodo para borrar registros, se puede asignar a un boton
        // En la linea de File file se pone el nombre del archivo xml a borrar (actualmente esta puesto para borrar archivos de el dia de hoy)
      private void borrarRegistros() {
            File file = new File(new File(getFilesDir(), "partners"), getNombreArchivoFecha());
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




        private void mostrarMensage(String mensaje) {
            dialog = new AlertDialog.Builder(Actividad_Partners.this);
            dialog.setTitle("Advertencia");
            dialog.setMessage(mensaje);
            dialog.setCancelable(false);
            dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogo, int id) {
                    dialogo.cancel();
                }
            });
            dialog.show();
        }
    }

