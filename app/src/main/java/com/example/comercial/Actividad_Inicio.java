package com.example.comercial;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.partners.Partner;
import java.io.File;
import java.io.FileOutputStream;

import java.util.List;

import org.xmlpull.v1.XmlSerializer;


import com.example.comercial.calendario.Actividad_Eventos;
import com.example.comercial.partners.Actividad_Partners;
import com.example.comercial.Catalogo.Actividad_Catalogo;
import java.util.ArrayList;

// implements OnMapReadyCallback
public class Actividad_Inicio extends AppCompatActivity {
    //GoogleMap mMap;
    Button bCitas, bPartner, bCatalogo, bDelegacion;
    ImageButton bTelefono, bEmail;
    AlertDialog.Builder dialog;
    private AnderBD db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inicio);

        /* Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cargar el mapa", Toast.LENGTH_SHORT).show();
        }*/

        bCitas = findViewById(R.id.bAgenda);
        bPartner = findViewById(R.id.bPresentacionPartners);
        bCatalogo = findViewById(R.id.bCatalogo);
        bDelegacion = findViewById(R.id.bPresentacionDelegacion);
        db = new AnderBD(this);
        bTelefono = findViewById(R.id.bTelefono);
        bEmail = findViewById(R.id.bEmail);

        bCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCitas = new Intent(Actividad_Inicio.this, Actividad_Eventos.class);
                startActivity(intentCitas);
            }
        });
        bPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPartner = new Intent(Actividad_Inicio.this, Actividad_Partners.class);
                startActivity(intentPartner);
            }
        });
        bCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPedidos = new Intent(Actividad_Inicio.this, Actividad_Catalogo.class);
                startActivity(intentPedidos);
            }
        });

        /*bDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean existePedido = true;
                boolean existePartner = true;

                String para = "DelegacionGuipuzcoa@gem.com";

                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{para});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Partners y pedidos de " + Metodos.obtenerFechaActual());

                // Obtener las rutas de los archivos
                String rutaArchivoPartner = new File(getFilesDir(), "partners/" + Metodos.getNombreArchivoPartners()).getAbsolutePath();
                String rutaArchivoPedidos = new File(getFilesDir(), "pedidos/" + Metodos.getNombreArchivoPedidos()).getAbsolutePath();

                // Validar si existen los archivos
                File archivoPartners = new File(rutaArchivoPartner);
                if (!archivoPartners.exists() || !archivoPartners.canRead()) {
                    Log.e("info", "No existe el archivo: " + rutaArchivoPartner);
                    existePartner = false;
                }

                File archivoPedidos = new File(rutaArchivoPedidos);
                if (!archivoPedidos.exists() || !archivoPedidos.canRead()) {
                    Log.e("info", "No existe el archivo: " + rutaArchivoPedidos);
                    existePedido = false;
                }

                // Convertir las rutas de los archivos a  Uri usando FileProvider
                Uri uriArchivoPartner = FileProvider.getUriForFile(Actividad_Inicio.this, "com.example.comercial.fileprovider", archivoPartners);
                Uri uriArchivoPedidos = FileProvider.getUriForFile(Actividad_Inicio.this, "com.example.comercial.fileprovider", archivoPedidos);

                // Crear un ArrayList para guardar las URIs de los archivos adjuntados
                ArrayList<Uri> archivosAdjuntos = new ArrayList<>();

                // Si no existen ni nuevos pedidos ni nuevos partners no se abrirá le correo y aparecerá el mensaje.
                // Si solo hay un nuevo pedido/partner, solo se adjuntara ese archivo.
                if (!existePedido && !existePartner) {
                    Metodos.mostrarAlerta("Advertencia","No existen nuevos partners ni pedidos este día.", Actividad_Inicio.this);
                    // lanzarToast("No existen nuevos partners ni pedidos este día.");
                    return;
                } else {
                    if (existePartner) {
                        archivosAdjuntos.add(uriArchivoPartner);
                    }

                    if (existePedido) {
                        archivosAdjuntos.add(uriArchivoPedidos);
                    }
                }

                // Añadir el ArrayList de los archivos adjuntos al intent
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, archivosAdjuntos);

                // Dar permisos a la aplicación
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Envío de partners y pedidos"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Metodos.lanzarToast("Ha habido un error al intentar abrir el correo electrónico.",Actividad_Inicio.this);
                }
            }

        });*/
        bDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Partner> partnersDeHoy = db.getPartnersOfToday();
                if (partnersDeHoy.isEmpty()) {
                    Toast.makeText(Actividad_Inicio.this, "No hay partners para enviar hoy.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nombreArchivo = "partners/partners_hoy.xml";
                generarArchivoXML(partnersDeHoy, nombreArchivo);

                File archivoXml = new File(getFilesDir(), nombreArchivo);
                Uri uriArchivoXml = FileProvider.getUriForFile(Actividad_Inicio.this, "com.example.comercial.fileprovider", archivoXml);
                ArrayList<Uri> archivosAdjuntos = new ArrayList<>();
                archivosAdjuntos.add(uriArchivoXml);

                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("text/xml");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"DelegacionGuipuzcoa@gem.com"}); // Cambia esto por la dirección real de correo electrónico de la delegación.
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Partners y pedidos de " + Metodos.obtenerFechaActual());
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, archivosAdjuntos);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Envío de partners y pedidos"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Actividad_Inicio.this, "No hay cliente de correo electrónico instalado.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        bTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTelefono = new Intent(Intent.ACTION_DIAL);
                intentTelefono.setData(Uri.parse("tel:+34 635985923"));
                startActivity(intentTelefono);
            }
        });

        bEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String para = "DelegacionGuipuzcoa@gem.com";

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + para));

                try {
                    startActivity(emailIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Metodos.lanzarToast("Ha habido un error al intentar abrir el correo electrónico.",Actividad_Inicio.this);
                }
            }

        });
    }
    private void generarArchivoXML(List<Partner> partners, String nombreArchivo) {
        try {
            File archivoXml = new File(getFilesDir(), nombreArchivo);
            FileOutputStream fos = new FileOutputStream(archivoXml, false);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.TRUE);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "Partners");

            for (Partner partner : partners) {
                serializer.startTag(null, "Partner");
                // Repite este patrón para cada atributo de Partner
                serializer.startTag(null, "Nombre");
                serializer.text(partner.getNombre());
                serializer.endTag(null, "Nombre");
                // Continúa con los demás campos...
                serializer.endTag(null, "Partner");
            }

            serializer.endTag(null, "Partners");
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onDestroy() {
        if (db != null) {
            db.close();
        }
        super.onDestroy();
    }
  /*  @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // this.mMap.setOnMapClickListener(this);
        // this.mMap.setOnMapLongClickListener(this);

        LatLng donosti = new LatLng(43.30419712367967, -2.0165662074674695);
        mMap.addMarker(new MarkerOptions().position(donosti).title("Gurmet Euskadi Market"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(donosti));

    }*/
}