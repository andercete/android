package com.example.comercial.partners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Actividad_Partners extends AppCompatActivity {

    private PartnerListAdapter mAdapter;
    private RecyclerView recyclerView;
    private Button bAgregar, bImportar;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_partners);

        db = new DbHelper(this); // Inicializa la base de datos

        bAgregar = findViewById(R.id.bPartnerAgregar);
        bImportar = findViewById(R.id.bImportar);

        bAgregar.setOnClickListener(v -> startActivity(new Intent(Actividad_Partners.this, Actividad_AltaPartner.class)));


        bImportar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml"); // Asegúrate de que el tipo MIME corresponda a tus archivos XML.
            startActivityForResult(intent, 1); // '1' es un código de solicitud arbitrario para identificar el resultado.
        });

        // Inicializa el RecyclerView con la lista de partners desde la base de datos
        List<Partner> partnersList = db.getAllPartners(); // Asumiendo que tienes un método getAllPartners() en AnderBD
        initRecyclerView(partnersList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData(); // Obtiene la URI del archivo seleccionado.
                importarDatosDesdeXML(uri); // Llama al método que procesará e insertará los datos.
            }
        }
    }
    private List<Partner> parseXMLFile(InputStream inputStream) {
        List<Partner> partnerList = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            Partner currentPartner = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if ("Partner".equals(tagName)) {
                            currentPartner = new Partner();
                        } else if (currentPartner != null) {
                            switch (tagName) {
                                case "IdZona":
                                    currentPartner.setIdZona(Integer.parseInt(parser.nextText()));
                                    break;
                                case "Nombre":
                                    currentPartner.setNombre(parser.nextText());
                                    break;
                                case "CIF":
                                    currentPartner.setCif(parser.nextText());
                                    break;
                                case "Direccion":
                                    currentPartner.setDireccion(parser.nextText());
                                    break;
                                case "Telefono":
                                    currentPartner.setTelefono(parser.nextText());
                                    break;
                                case "Correo":
                                    currentPartner.setCorreo(parser.nextText());
                                    break;
                                case "FechaRegistro":
                                    currentPartner.setFechaRegistro(parser.nextText());
                                    break;
                                // Completar con el resto de campos si los hay
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if ("Partner".equals(tagName) && currentPartner != null) {
                            partnerList.add(currentPartner);
                            currentPartner = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return partnerList;
    }
    private void importarDatosDesdeXML(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            List<Partner> partners = parseXMLFile(inputStream); // Utiliza el método de parseo que definiste.
            for (Partner partner : partners) {
                db.addPartner(partner); // Inserta cada partner en la base de datos.
            }
            Toast.makeText(this, "Datos importados con éxito", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error al importar datos", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void initRecyclerView(List<Partner> partners) {
        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PartnerListAdapter(partners, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualiza la lista de partners
        List<Partner> updatedPartnersList = db.getAllPartners();
        mAdapter.setItems(updatedPartnersList);
        mAdapter.notifyDataSetChanged();
    }

}
