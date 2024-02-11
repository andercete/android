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
import com.example.comercial.BBDD.Articulos;
import com.example.comercial.BBDD.Catalogo;
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
        añadirArticulosPorDefecto();
        setupRecyclerView();
        setupImportarCatalogoButton();
    }
    private void añadirArticulosPorDefecto() {
        if (!db.existeArticulo("Carne pollo")) {
            db.addArticulo(new Catalogo(0, "Carne pollo", "Muslos", "Euskal Okela", 5.0, 5.0, 5, "carne"));
        }
        if (!db.existeArticulo("Carne Ternera Solomillo")) {
            db.addArticulo(new Catalogo(0, "Carne Ternera Solomillo", "Solomillo de ternera", "Basaxterri", 13.0, 10.0, 10, "solomillo"));
        }
        if (!db.existeArticulo("Carne Ternera Chuletón")) {
            db.addArticulo(new Catalogo(0, "Carne Ternera Chuletón", "Chuletón de ternera",  "Baserriberrí", 15.0, 12.0, 7, "chuleton"));
        }
        if (!db.existeArticulo("Carne Cerdo Lomo")) {
            db.addArticulo(new Catalogo(0, "Carne Cerdo Lomo", "Lomo de cerdo",  "Euskal Txerria", 9.0, 7.0, 15, "lomo"));
        }
        if (!db.existeArticulo("Carne Cordero Chuletas")) {
            db.addArticulo(new Catalogo(0, "Carne Cordero Chuletas", "Chuletas de cordero", "Raza latxa", 12.0, 9.0, 20, "chuletascordero"));
        }
        if (!db.existeArticulo("Pescado Anchoas del Cantábrico")) {
            db.addArticulo(new Catalogo(0, "Pescado Anchoas del Cantábrico", "Anchoas del Cantábrico", "Bizkaiko Ura", 20.0, 16.0, 5, "anchoas"));
        }
        if (!db.existeArticulo("Pescado Bonito del Norte")) {
            db.addArticulo(new Catalogo(0, "Pescado Bonito del Norte", "Bonito del Norte","Bonito del Norte", 18.0, 14.0, 8, "bonito"));
        }
        if (!db.existeArticulo("Bebida Sidra Zapian")) {
            db.addArticulo(new Catalogo(0, "Bebida Sidra Zapian", "Sidra Zapian", "Bilbo Bodegak", 5.0, 3.0, 30, "sidra"));
        }
        if (!db.existeArticulo("Bebida Txakoli Ameztoi")) {
            db.addArticulo(new Catalogo(0, "Bebida Txakoli Ameztoi", "Txakoli Ameztoi", "Txakoli Ameztoi", 7.0, 5.0, 25, "tx"));
        }
    }
    private void setupRecyclerView() {
        List<Catalogo> catalogoArticulos = db.getAllArticulos(); // Actualizar nombre del método si necesario
        mAdapter = new CatalogoAdapterVer(this, catalogoArticulos);
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
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        List<Catalogo> updatedCatalogoList = db.getAllArticulos(); // Ajustar nombre del método si necesario
        mAdapter.setCatalogo(updatedCatalogoList);
        mAdapter.notifyDataSetChanged();
    }
}
