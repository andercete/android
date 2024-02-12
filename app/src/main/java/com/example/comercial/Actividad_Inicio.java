package com.example.comercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.Partner;
import java.io.File;
import java.io.FileOutputStream;

import java.io.StringWriter;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;


import com.example.comercial.calendario.Actividad_Eventos;
import com.example.comercial.partners.Actividad_Partners;
import com.example.comercial.Catalogo.Actividad_CatalogoVer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// implements OnMapReadyCallback
public class Actividad_Inicio extends AppCompatActivity {
    //GoogleMap mMap;
    Button bCitas, bPartner, bCatalogo, bDelegacion;

    TextView textViewUsuario;
    ImageButton bTelefono, bEmail;
    AlertDialog.Builder dialog;
    private DbHelper db;

    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inicio);

        bCitas = findViewById(R.id.bAgenda);
        bPartner = findViewById(R.id.bPresentacionPartners);
        bCatalogo = findViewById(R.id.bCatalogo);
        bDelegacion = findViewById(R.id.bPresentacionDelegacion);
        db = new DbHelper(this);
        bTelefono = findViewById(R.id.bTelefono);
        bEmail = findViewById(R.id.bEmail);
        textViewUsuario = findViewById(R.id.textView12);
        SharedPreferences sharedPref = getSharedPreferences("PreferenciasComerciales", Context.MODE_PRIVATE);
        String nombreComercial = sharedPref.getString("NombreComercial", "");

        // Establecer el nombre del comercial en el TextView
        textViewUsuario.setText("Bienvenido, " + nombreComercial);

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
                Intent intentPedidos = new Intent(Actividad_Inicio.this, Actividad_CatalogoVer.class);
                startActivity(intentPedidos);
            }
        });


        bDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Partner> partnersDeHoy = db.getPartnersOfToday();
                if (partnersDeHoy.isEmpty()) {
                    Toast.makeText(Actividad_Inicio.this, "No hay partners para enviar hoy.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nombreArchivo = "partners_hoy.xml";
                generarArchivoXML(partnersDeHoy, nombreArchivo);

                File archivoXml = new File(getFilesDir(), "partners/"+nombreArchivo);
                Uri uriArchivoXml = FileProvider.getUriForFile(
                        Actividad_Inicio.this,
                        "com.example.comercial.fileprovider",
                        archivoXml);
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
                String para = "andercete@gmail.com";

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
    public void generarArchivoXML(List<Partner> partners, String nombreArchivo) {
        // Construimos el nombre del archivo con la fecha de hoy

        File directorioPartners = new File(getFilesDir(), "partners");
        if (!directorioPartners.exists()) {
            directorioPartners.mkdirs();
        }

        File archivoXml = new File(directorioPartners, nombreArchivo);

        try (FileOutputStream fos = new FileOutputStream(archivoXml)) {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);

            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "Partners");

            for (Partner partner : partners) {
                serializer.startTag("", "Partner");

                serializer.startTag("", "Nombre");
                serializer.text(partner.getNombre());
                serializer.endTag("", "Nombre");

                serializer.startTag("", "CIF");
                serializer.text(partner.getCif());
                serializer.endTag("", "CIF");

                serializer.startTag("", "Direccion");
                serializer.text(partner.getDireccion());
                serializer.endTag("", "Direccion");

                serializer.startTag("", "Telefono");
                serializer.text(partner.getTelefono());
                serializer.endTag("", "Telefono");

                serializer.startTag("", "Correo");
                serializer.text(partner.getCorreo());
                serializer.endTag("", "Correo");

                serializer.startTag("", "FechaRegistro");
                serializer.text(partner.getFechaRegistro());
                serializer.endTag("", "FechaRegistro");

                serializer.endTag("", "Partner");
            }

            serializer.endTag("", "Partners");
            serializer.endDocument();

            // Aquí solo necesitas escribir el contenido una vez
            fos.write(writer.toString().getBytes());
            // No necesitas llamar a fos.close() aquí, el try-with-resources se encarga de ello
        } catch (Exception e) {
            Log.e("XMLGenerationError", "Error generando el archivo XML", e);
        }
    }

    protected void onDestroy() {
        if (db != null) {
            db.close();
        }
        super.onDestroy();
    }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//      mMap = googleMap;
//      this.mMap.setOnMapClickListener(this);
//      this.mMap.setOnMapLongClickListener(this);
//
//      LatLng donosti = new LatLng(43.30419712367967, -2.0165662074674695);
//      mMap.addMarker(new MarkerOptions().position(donosti).title("Gurmet Euskadi Market"));
//      mMap.moveCamera(CameraUpdateFactory.newLatLng(donosti));
//
//
//      //  Google Maps
//      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//      try {
//          mapFragment.getMapAsync(this);
//      } catch (Exception e) {
//          e.printStackTrace();
//          Toast.makeText(this, "Error al cargar el mapa", Toast.LENGTH_SHORT).show();
//      }
//    }

}