package com.example.comercial.partners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.BBDD.Partner;
import com.example.comercial.R;

import java.util.List;

public class Actividad_Partners extends AppCompatActivity {

    private PartnerListAdapter mAdapter;
    private RecyclerView recyclerView;
    private Button bAgregar, bBorrar, bImportar;
    private AnderBD db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_partners);

        db = new AnderBD(this); // Inicializa la base de datos

        bAgregar = findViewById(R.id.bPartnerAgregar);
        bBorrar = findViewById(R.id.bBorrar);
        bImportar = findViewById(R.id.bImportar);

        bAgregar.setOnClickListener(v -> startActivity(new Intent(Actividad_Partners.this, Actividad_AltaPartner.class)));

        bBorrar.setOnClickListener(v -> borrarRegistros());

        bImportar.setOnClickListener(v -> {
            // Aquí agregarías lógica para importar socios, posiblemente desde un archivo XML o una fuente externa
            Toast.makeText(Actividad_Partners.this, "Funcionalidad de importación no implementada", Toast.LENGTH_SHORT).show();
        });

        // Inicializa el RecyclerView con la lista de partners desde la base de datos
        List<Partner> partnersList = db.getAllPartners(); // Asumiendo que tienes un método getAllPartners() en AnderBD
        initRecyclerView(partnersList);
    }

    private void initRecyclerView(List<Partner> partners) {
        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PartnerListAdapter(partners, this);
        recyclerView.setAdapter(mAdapter);
    }

    // Método para borrar todos los registros de partners, adaptar según necesidades
    private void borrarRegistros() {
        db.deleteAllPartners(); // Asumiendo que tienes un método deleteAllPartners() en AnderBD
        Toast.makeText(this, "Todos los registros han sido borrados", Toast.LENGTH_SHORT).show();
        List<Partner> updatedList = db.getAllPartners(); // Recarga la lista de partners
        mAdapter.setItems(updatedList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualiza la lista de partners
        List<Partner> updatedPartnersList = db.getAllPartners();
        mAdapter.setItems(updatedPartnersList);
        mAdapter.notifyDataSetChanged();
    }
}
