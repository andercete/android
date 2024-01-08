package com.example.comercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
                String para = "DelegeacionGuipuzcoa@gem.com";
                String cc = "jiz.j12.a4@gmail.com";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{para});
                emailIntent.putExtra(Intent.EXTRA_CC, new String[]{cc});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Partners y pedidos de " + obtenerFechaActual());

                // Update file path using context.getFilesDir()
                String rutaArchivoPartner = new File(getFilesDir(), "partners/" + getNombreArchivoPartners()).getAbsolutePath();
                String rutaArchivoPedidos = new File(getFilesDir(), "pedidos/" + getNombreArchivoPedidos()).getAbsolutePath();

                File archivoPartners = new File(rutaArchivoPartner);
                if (!archivoPartners.exists() || !archivoPartners.canRead()) {
                    Log.e("info", "No existe el archivo: " + rutaArchivoPartner);
                    lanzarToast("No existe el archivo: " + rutaArchivoPartner);
                    return;
                }

                File archivoPedidos = new File(rutaArchivoPedidos);
                if (!archivoPedidos.exists() || !archivoPedidos.canRead()) {
                    Log.e("info", "No existe el archivo: " + rutaArchivoPedidos);
                    lanzarToast("No existe el archivo: " + rutaArchivoPedidos);
                    return;
                }

                Log.e("info", "Si existe el archivo: " + rutaArchivoPedidos);

                // Convert the file path to Uri using FileProvider
                Uri uriArchivoPartner = FileProvider.getUriForFile(Actividad_Presentacion.this, "com.example.comercial.fileprovider", archivoPartners);
                Uri uriArchivoPedidos = FileProvider.getUriForFile(Actividad_Presentacion.this, "com.example.comercial.fileprovider", archivoPedidos);

                // Add the attachment to the intent
                emailIntent.putExtra(Intent.EXTRA_STREAM, uriArchivoPartner);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uriArchivoPedidos);

                // Grant read permissions to the receiving app
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Envío de partners y pedidos"));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    lanzarToast("Ha habido un error al intentar abrir el correo electrónico.");
                }
            }
        });


    }

    private void lanzarToast(String mensaje)
    {
        Toast.makeText(Actividad_Presentacion.this,
                mensaje, Toast.LENGTH_SHORT).show();
    }

    private String obtenerFechaActual() {
        String fechaActual;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        fechaActual = sdf.format(new Date());
        return fechaActual;
    }

    private String getNombreArchivoPartners() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + ".xml";
        return nombrearchivo;
    }

    private String getNombreArchivoPedidos() {
        String nombrearchivo;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        nombrearchivo = sdf.format(new Date()) + "_pedidos.xml";
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