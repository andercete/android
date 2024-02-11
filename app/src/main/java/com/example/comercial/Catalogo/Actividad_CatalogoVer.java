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

        setupRecyclerView();
        setupImportarCatalogoButton();
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
