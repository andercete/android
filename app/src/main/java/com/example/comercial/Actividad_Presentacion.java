package com.example.comercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Actividad_Presentacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    GoogleMap mMap;
    Button bCitas,bPartner,bPedidos,bDelegacion;

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
                Intent intentDelegacion = new Intent (Actividad_Presentacion.this, Actividad_EnvioDelegacion.class);
                startActivity(intentDelegacion);
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng donosti = new LatLng(43.30419712367967, -2.0165662074674695);
        mMap.addMarker(new MarkerOptions().position(donosti).title("Gurmet Euskadi Market"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(donosti));
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }
}