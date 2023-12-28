    package com.example.comercial;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Xml;
    import android.view.View;
    import android.widget.Button;

    import org.xmlpull.v1.XmlPullParser;
    import org.xmlpull.v1.XmlSerializer;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.List;

    public class Actividad_Partners extends AppCompatActivity {

        //PUEDES ACCEDER A LA ACTIVIDAD PARA TESTEARLA YENDO A APP>EDIT CONFIGURATIONS>Launch(Specified Activity)>actividad_partners.

        ListAdapter mAdapter;
        List<Partner> partners;
        RecyclerView recyclerView;
        Button bAgregar;
        Button bBorrar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_partners);
            bAgregar = findViewById(R.id.bPartnerAgregar);
            bBorrar = findViewById(R.id.bPartnerBorrar);
            bAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Actividad_Partners.this, Actividad_Altasocio.class);
                    startActivity(intent);
                }
            });

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

        // Método para parsear tu XML de partners
        private List<Partner> parsePartnersXML() {
            List<Partner> partners = new ArrayList<>();
            Partner currentPartner = null;
            String currentTag = null;

            try {
                File file = new File(getFilesDir(), "partners.xml");
                if (!file.exists()) {
                    return partners; // El archivo no existe, devolvemos la lista vacía
                }
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
                                        currentPartner.setTelefono(parser.getText());
                                        break;
                                    case "email":
                                        currentPartner.setEmail(parser.getText());
                                        break;
                                }
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if ("partner".equals(tagName) && currentPartner != null) {
                                partners.add(currentPartner);
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

            return partners;
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
        private void borrarRegistros() {
            try {
                File file = new File(getFilesDir(), "partners.xml");

                // Leer el contenido actual del archivo XML en una estructura de datos
                List<Partner> partners = parsePartnersXML();

                Partner Socio = partners.isEmpty() ? null : partners.get(0);
                partners.clear();

                // Escribir la estructura de datos modificada en el archivo XML
                writePartnersToXML(file, partners);
                recreate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void writePartnersToXML(File file, List<Partner> partners) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fos, "UTF-8");
                serializer.startDocument("UTF-8", true);
                serializer.text("\n");
                serializer.startTag(null, "partners");
                serializer.text("\n");
                serializer.endTag(null, "partners");
                serializer.endDocument();
                serializer.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace(); // Manejo de errores
            }
        }
    }

