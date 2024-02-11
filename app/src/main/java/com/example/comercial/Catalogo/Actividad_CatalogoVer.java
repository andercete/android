package com.example.comercial.Catalogo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Actividad_CatalogoVer extends AppCompatActivity {

    private CatalogoAdapterVer mAdapter;
    private RecyclerView rvCatalogo;
    private Button bImportarCatalogo;
    private AnderBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_catalogo);

        db = new AnderBD(this);

        bImportarCatalogo = findViewById(R.id.bImportar2);
        rvCatalogo = findViewById(R.id.rCatalogo);

        setupRecyclerView();
        setupImportarCatalogoButton();
    }

    private void setupRecyclerView() {
        List<Catalogo> catalogos = db.getAllArticulos(); // Actualizar nombre del método si necesario
        mAdapter = new CatalogoAdapterVer(this, catalogos);
        rvCatalogo.setLayoutManager(new LinearLayoutManager(this));
        rvCatalogo.setAdapter(mAdapter);
    }

    private void setupImportarCatalogoButton() {
        bImportarCatalogo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            importarCatalogoDesdeXML(uri);
        }
    }

    private void importarCatalogoDesdeXML(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            // Vaciamos el catalogo para que esté acualizado y sin registros duplicados
            db.vaciarCatalogo();

            List<Catalogo> catalogos = parseXMLFile(inputStream);
            for (Catalogo catalogo : catalogos) {
                db.addArticulo(catalogo); // Ajustar a la implementación correcta
            }
            mAdapter.setCatalogo(catalogos);
            mAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Catálogo importado con éxito", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al importar catálogo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private List<Catalogo> parseXMLFile(InputStream inputStream) {
        List<Catalogo> catalogoList = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);
            int eventType = parser.getEventType();
            Catalogo currentCatalogo = null;
            String tagName = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if ("Articulo".equals(tagName)) {
                            currentCatalogo = new Catalogo();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (currentCatalogo != null && tagName != null) {
                            switch (tagName) {
                                case "IdArticulo":
                                    currentCatalogo.setIdArticulo(text);
                                    break;
                                case "Nombre":
                                    currentCatalogo.setNombre(text);
                                    break;
                                case "Descripcion":
                                    currentCatalogo.setDescripcion(text);
                                    break;
                                case "Proveedor":
                                    currentCatalogo.setProveedor(text);
                                    break;
                                case "PrVenta":
                                    currentCatalogo.setPrVent(Float.parseFloat(text));
                                    break;
                                case "PrCoste":
                                    currentCatalogo.setPrCost(Float.parseFloat(text));
                                    break;
                                case "Existencias":
                                    currentCatalogo.setExistencias(Integer.parseInt(text));
                                    break;
                                case "ImagenBase64":
                                    currentCatalogo.setImageName(text);
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("Articulo".equals(parser.getName()) && currentCatalogo != null) {
                            catalogoList.add(currentCatalogo);
                            currentCatalogo = null; // Reset para el próximo artículo
                        }
                        tagName = null; // Reset del nombre de tag para el próximo evento
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al parsear el archivo XML", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return catalogoList;
    }


    @Override
    protected void onResume() {
        super.onResume();
        List<Catalogo> updatedCatalogoList = db.getAllArticulos(); // Ajustar nombre del método si necesario
        mAdapter.setCatalogo(updatedCatalogoList);
        mAdapter.notifyDataSetChanged();
    }
}
