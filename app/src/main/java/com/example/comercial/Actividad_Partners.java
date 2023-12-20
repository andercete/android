    package com.example.comercial;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.os.Bundle;
    import android.util.Xml;

    import org.xmlpull.v1.XmlPullParser;

    import java.io.InputStream;
    import java.util.ArrayList;
    import java.util.List;

    public class Actividad_Partners extends AppCompatActivity {

        RecyclerView mRecyclerView;
        MyAdapter mAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_partners);

            mRecyclerView = findViewById(R.id.mRecyclerview);

            // Utilizando LinearLayoutManager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);

            // Suponiendo que ya tienes una lista de partners parseada
            List<Partner> partnersList = parsePartnersXML();

            mAdapter = new MyAdapter(partnersList);
            mRecyclerView.setAdapter(mAdapter);
        }

        // Método para parsear tu XML de partners
        private List<Partner> parsePartnersXML() {
            List<Partner> partners = new ArrayList<>();
            Partner currentPartner = null;
            String currentTag = null;

            try {
                InputStream inputStream = getAssets().open("partners.xml");
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
    }
