    package com.example.comercial.partners;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.app.Activity;
    import android.content.Intent;
    import android.database.Cursor;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.util.Xml;
    import android.view.View;
    import android.widget.Button;
    import android.widget.Toast;

    import com.example.comercial.DBHelper;
    import com.example.comercial.Metodos;
    import com.example.comercial.R;

    import org.xmlpull.v1.XmlPullParser;
    import org.xmlpull.v1.XmlPullParserFactory;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.OutputStream;
    import java.util.ArrayList;
    import java.util.List;

    public class Actividad_Partners extends AppCompatActivity {

        //PUEDES ACCEDER A LA ACTIVIDAD PARA TESTEARLA YENDO A APP>EDIT CONFIGURATIONS>Launch(Specified Activity)>actividad_partners.

        PartnerListAdapter mAdapter;
        RecyclerView recyclerView;
        Button bAgregar;
        Button bBorrar;
        Button bImportar;

        private static final int SELECCIONAR_ARCHIVO_REQUEST_CODE = 1;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_partners);
            bAgregar = findViewById(R.id.bPartnerAgregar);
            bBorrar = findViewById(R.id.bBorrar);
            bImportar = findViewById(R.id.bImportar);
            bAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Actividad_Partners.this, Actividad_AltaPartner.class);
                    startActivity(intent);
                }
            });
            bBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarRegistros();
                }
            });
            bImportar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/xml");  // Puedes ajustar el tipo de archivo según tus necesidades

                    // Inicia la actividad esperando un resultado
                    startActivityForResult(intent, SELECCIONAR_ARCHIVO_REQUEST_CODE);
                }
            });

            // Copiar el contenido de assets/partners.xml (los partners que nos envian de delegación) en el almacenamiento interno
            copiarXMLaAlmacenamientoInterno();

            // Suponiendo que ya tienes una lista de partners parseada
            List<Partner> partnersList = parsePartnersXML();

            // Inicializa el RecyclerView con la lista de partners
            initRecyclerView(partnersList);
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == SELECCIONAR_ARCHIVO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                // Obtiene la URI del archivo seleccionado
                Uri uri = data.getData();

                // Puedes trabajar con la URI para leer el contenido del archivo y realizar las operaciones necesarias
                // Por ejemplo, puedes pasar la URI a un método para procesar el archivo
                procesarArchivo(uri);
            }
        }
        private void procesarArchivo(Uri uri) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser parser = xmlFactoryObject.newPullParser();
                parser.setInput(inputStream, null);

                List<Partner> partners = parseXMLFile(uriToFile(uri));

                // Llama a un método para insertar los datos en la base de datos
             //   for (Partner partner : partners) {
            //        DBHelper.insertarPartner(partner);
            //    }

                inputStream.close();

                Toast.makeText(this, "Importación completada", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error en la importación", Toast.LENGTH_SHORT).show();
            }
        }
        private File uriToFile(Uri uri) {
            String path;
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Si el URI es de tipo "content", entonces obtén el path a partir de la base de datos
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    path = cursor.getString(columnIndex);
                    cursor.close();
                } else {
                    path = uri.getPath();
                }
            } else {
                // Si el URI es de tipo "file", entonces obtén el path directamente
                path = uri.getPath();
            }

            return new File(path);
        }



        private void copiarXMLaAlmacenamientoInterno() {
            // Crear la carpeta 'partners' dentro de 'files'
            File directorioPartners = new File(getFilesDir(), "partners");
            if (!directorioPartners.exists()) {
                directorioPartners.mkdirs();
            }

            // Crear el archivo en la carpeta 'partners'
            File file = new File(directorioPartners, "PARTNERS.xml");

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


        private void initRecyclerView(List<Partner> partners) {
            PartnerListAdapter partnerListAdapter = new PartnerListAdapter(partners, this);
            recyclerView = findViewById(R.id.listRecyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(partnerListAdapter);
        }

        private List<Partner> parsePartnersXML() {
            List<Partner> partners = new ArrayList<>();
            File directory = new File(getFilesDir(), "partners");

            File partnersDelegacion = new File("/data/user/0/com.example.comercial/files/partners/PARTNERS.xml");

            if (partnersDelegacion.isFile() && partnersDelegacion.getName().endsWith(".xml")) {
                    partners.addAll(parseXMLFile(partnersDelegacion));
            } else
            {
                Metodos.mostrarAlerta("Error","Ha habido un error al intentar importar los partners enviados desde delegación",Actividad_Partners.this);
            }

            // Verificar si el directorio existe y contiene archivos
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".xml")) {
                            if (file.getName().equalsIgnoreCase(Metodos.getNombreArchivoPartners())) {
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
            File file = new File(new File(getFilesDir(), "partners"), Metodos.getNombreArchivoPartners());
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

