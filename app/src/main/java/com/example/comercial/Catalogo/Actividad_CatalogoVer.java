package com.example.comercial.Catalogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.Metodos;
import com.example.comercial.R;
import com.example.comercial.pedidos.Actividad_AltaPedido;

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

public class Actividad_CatalogoVer extends AppCompatActivity {

    CatalogoAdapterVer mAdapter;
    RecyclerView rvCatalogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_catalogo);

        copiarXMLaAlmacenamientoInterno();
        List<Catalogo> catalogoList = parseCatalogoXML();
        initRecyclerView(catalogoList);
    }
    private void copiarXMLaAlmacenamientoInterno() {
        // Crear la carpeta 'catalogo' dentro de 'files'
        File directorioCatalogo = new File(getFilesDir(), "catalogo");
        if (!directorioCatalogo.exists()) {
            directorioCatalogo.mkdirs(); // Crea la carpeta si no existe
        }

        // Crear el archivo en la carpeta 'catalogo'
        File file = new File(directorioCatalogo, "CATALOGO.xml");

        // Verificar si el archivo ya existe para evitar la copia redundante
        if (!file.exists()) {
            try {
                // Abrir el archivo 'catalogo.xml' desde los assets
                InputStream in = getAssets().open("catalogo.xml");

                // Crear un nuevo archivo en la carpeta 'catalogo'
                OutputStream out = new FileOutputStream(file);

                // Copiar el contenido del archivo desde los assets al almacenamiento interno
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
                Toast.makeText(this, "Error al copiar el archivo del catálogo", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Opcionalmente, puedes informar que el archivo ya fue copiado anteriormente
            Toast.makeText(this, "El archivo del catálogo ya está copiado al almacenamiento interno.", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView(List<Catalogo> catalogoList) {
        CatalogoAdapterVer catalogoAdapterVer = new CatalogoAdapterVer(this, catalogoList);
        rvCatalogo = findViewById(R.id.rCatalogo); // Asegúrate de que este ID está correcto
        rvCatalogo.setHasFixedSize(true);
        rvCatalogo.setLayoutManager(new LinearLayoutManager(this));
        rvCatalogo.setAdapter(catalogoAdapterVer);
    }
    private List<Catalogo> parseCatalogoXML() {
        List<Catalogo> catalogoList = new ArrayList<>();
        File file = new File(getFilesDir(), "catalogo/CATALOGO.xml");

        if (file.exists()) {
            catalogoList.addAll(parseXMLFile(file));
        } else {
            Toast.makeText(this, "El archivo CATALOGO.xml no existe", Toast.LENGTH_SHORT).show();
        }
        return catalogoList;
    }

    private List<Catalogo> parseXMLFile(File file) {
        List<Catalogo> catalogoList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(fis, null);

            Catalogo currentCatalogo = null;
            String currentTag = null; // Temporary variable to hold the current tag name
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if ("articulo".equals(currentTag)) {
                            currentCatalogo = new Catalogo();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (currentCatalogo != null && currentTag != null) {
                            switch (currentTag) {
                                case "idArticulo":
                                    currentCatalogo.setIdArticulo(text);
                                    break;
                                case "nombre":
                                    currentCatalogo.setNombre(text);
                                    break;
                                case "descripcion":
                                    currentCatalogo.setDescripcion(text);
                                    break;
                                case "categoria":
                                    currentCatalogo.setCategoria(text);
                                    break;
                                case "proveedor":
                                    currentCatalogo.setProveedor(text);
                                    break;
                                case "prVenta":
                                    currentCatalogo.setPrVenta(Float.parseFloat(text));
                                    break;
                                case "prCoste":
                                    currentCatalogo.setPrCoste(Float.parseFloat(text));
                                    break;
                                case "existencias":
                                    currentCatalogo.setExistencias(Integer.parseInt(text));
                                    break;
                                case "Direccion_Imagen":
                                    currentCatalogo.setImageName(text);
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("articulo".equals(parser.getName()) && currentCatalogo != null) {
                            catalogoList.add(currentCatalogo);
                            currentCatalogo = null; // Reset currentCatalogo for the next item
                        }
                        currentTag = null; // Reset currentTag after processing end tag
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Assuming this method is in an Activity. If not, you'll need to modify how you show the toast.
            Toast.makeText(this, "Error al parsear CATALOGO.xml", Toast.LENGTH_SHORT).show();
        }

        return catalogoList;
    }



    @Override
        protected void onResume() {
            super.onResume();
            List<Catalogo> updatedCatalogoList = parseCatalogoXML();
            if (mAdapter != null) {
                mAdapter.setCatalogo(updatedCatalogoList);
            }
        }


    private void borrarRegistros() {
        File file = new File(new File(getFilesDir(), "pedidos"), Metodos.getNombreArchivoPedidos());
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