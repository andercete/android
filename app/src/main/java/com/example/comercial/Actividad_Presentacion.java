package com.example.comercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
public class Actividad_Presentacion extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    Button bCitas, bPartner, bPedidos, bDelegacion;
    ImageButton bTelefono, bEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_presentacion);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bCitas = findViewById(R.id.bPresentacionCitas);
        bPartner = findViewById(R.id.bPresentacionPartners);
        bPedidos = findViewById(R.id.bPresentacionPedidos);
        bDelegacion = findViewById(R.id.bPresentacionDelegacion);

        bTelefono = findViewById(R.id.bTelefono);
        bEmail = findViewById(R.id.bEmail);

        actualizarColorBoton();
        setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        bCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCitas = new Intent(Actividad_Presentacion.this, Actividad_Agenda.class);
                startActivity(intentCitas);
            }
        });
        bPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPartner = new Intent(Actividad_Presentacion.this, Actividad_Partners.class);
                startActivity(intentPartner);
            }
        });
        bPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPedidos = new Intent(Actividad_Presentacion.this, Actividad_Pedidos.class);
                startActivity(intentPedidos);
            }
        });

        bDelegacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean existePedido = true;
                boolean existePartner = true;

                String para = "DelegacionGuipuzcoa@gem.com";

                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{para});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Partners y pedidos de " + obtenerFechaActual());

                // Obtener las rutas de los archivos
                String rutaArchivoPartner = new File(getFilesDir(), "partners/" + getNombreArchivoPartners()).getAbsolutePath();
                String rutaArchivoPedidos = new File(getFilesDir(), "pedidos/" + getNombreArchivoPedidos()).getAbsolutePath();

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
                Uri uriArchivoPartner = FileProvider.getUriForFile(Actividad_Presentacion.this, "com.example.comercial.fileprovider", archivoPartners);
                Uri uriArchivoPedidos = FileProvider.getUriForFile(Actividad_Presentacion.this, "com.example.comercial.fileprovider", archivoPedidos);

                // Crear un ArrayList para guardar las URIs de los archivos adjuntados
                ArrayList<Uri> archivosAdjuntos = new ArrayList<>();

                // Si no existen ni nuevos pedidos ni nuevos partners no se abrirá le correo y aparecerá el mensaje.
                // Si solo hay un nuevo pedido/partner, solo se adjuntara ese archivo.
                if (!existePedido && !existePartner) {
                    lanzarToast("No existen nuevos partners ni pedidos este día.");
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
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    lanzarToast("Ha habido un error al intentar abrir el correo electrónico.");
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
                    lanzarToast("Ha habido un error al intentar abrir el correo electrónico.");
                }
            }

        });
    }


    private void lanzarToast(String mensaje) {
        Toast.makeText(Actividad_Presentacion.this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private String obtenerFechaActual() {
        String fechaActual;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        fechaActual = sdf.format(new Date());
        return fechaActual;
    }
    private void actualizarColorBoton() {
        // Obtén el tema actual de la aplicación
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Determina si el tema actual es oscuro
        boolean isDarkTheme = currentTheme == Configuration.UI_MODE_NIGHT_YES;

        // Configura el color de fondo del botón según el tema
        if (isDarkTheme) {
            bCitas.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonOscuro));
            bDelegacion.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonOscuro));
            bPedidos.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonOscuro));
            bPartner.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonOscuro));
        } else {
            bCitas.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonClaro));
            bDelegacion.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonClaro));
            bPedidos.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonClaro));
            bPartner.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBotonClaro));
        }
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // this.mMap.setOnMapClickListener(this);
        // this.mMap.setOnMapLongClickListener(this);

        LatLng donosti = new LatLng(43.30419712367967, -2.0165662074674695);
        mMap.addMarker(new MarkerOptions().position(donosti).title("Gurmet Euskadi Market"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(donosti));

    }
}