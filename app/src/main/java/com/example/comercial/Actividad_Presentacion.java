package com.example.comercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// implements OnMapReadyCallback
public class Actividad_Presentacion extends AppCompatActivity {
    GoogleMap mMap;
    Button bCitas,bPartner,bPedidos,bDelegacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_presentacion);

      /*  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); */

        bCitas = findViewById(R.id.bPresentacionCitas);
        bPartner = findViewById(R.id.bPresentacionPartners);
        bPedidos = findViewById(R.id.bPresentacionPedidos);
        bDelegacion = findViewById(R.id.bPresentacionDelegacion);

        bCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCitas = new Intent (Actividad_Presentacion.this, Actividad_Agenda.class);
                startActivity(intentCitas);
            }
        });
        bPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPartner = new Intent (Actividad_Presentacion.this, Actividad_Partners.class);
                startActivity(intentPartner);
            }
        });
        bPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPedidos = new Intent (Actividad_Presentacion.this, Actividad_Pedidos.class);
                startActivity(intentPedidos);
            }
        });
        bDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String para = "DelegeacionGuipuzcoa@gem.com";
                String para = "jiz.j12.a4@gmail.com";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));

                emailIntent.setType("message/rfc822");




                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{para});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Partners y pedidos de " + obtenerFechaActual());

                // Obtain the file paths
                String rutaArchivoPartner = "/data/data/com.example.comercial/files/partners/" + getNombreArchivoPartner();
//                String rutaArchivo2 = "/ruta/del/segundo/archivo";

                File archivoPartners = new File(rutaArchivoPartner);
                if (!archivoPartners.exists() || !archivoPartners.canRead()) {
                    Log.e("info", "No existe el archivo: " + rutaArchivoPartner);

                    return;
                }

                Log.e("info", "Si existe el archivo: " + rutaArchivoPartner);

                // Convert the file paths to Uris
                Uri uriArchivoPartner = Uri.fromFile(archivoPartners);
//                Uri uriArchivo2 = Uri.fromFile(new File(rutaArchivo2));

                // Create an ArrayList of Uris
                ArrayList<Uri> archivosAdjuntos = new ArrayList<>();
                archivosAdjuntos.add(uriArchivoPartner);
//                archivosAdjuntos.add(uriArchivo2);

                // Add the attachments to the intent
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, archivosAdjuntos);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Envío de partners y pedidos"));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Actividad_Presentacion.this,
                            "Ha habido un error al intentar abrir el correo electrónico.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String obtenerFechaActual() {
        String fechaActual;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        fechaActual = sdf.format(new Date());
        return fechaActual;
    }

    private String getNombreArchivoPartner() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + ".xml";
        return nombrearchivo;
    }

   /* @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng donosti = new LatLng(43.30419712367967, -2.0165662074674695);
        mMap.addMarker(new MarkerOptions().position(donosti).title("Gurmet Euskadi Market"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(donosti));
    }*/

}