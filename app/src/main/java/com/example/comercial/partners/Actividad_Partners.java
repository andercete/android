package com.example.comercial.partners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.BBDD.Partner;
import com.example.comercial.R;

import java.io.InputStream;
import java.util.ArrayList;
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
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml"); // Asegúrate de que esto coincide con el tipo MIME de tus archivos XML.
            startActivityForResult(intent, 1); // Usa un requestCode que prefieras
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

    private void procesarArchivoXML(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
            List<Partner> partners = new ArrayList<>();
            // Aquí usarías un parser XML para leer el archivo y llenar la lista de partners
            // Por ejemplo: partners = tuMétodoDeParseo(inputStream);

            // Inserta los socios en la base de datos
            for (Partner partner : partners) {
                db.addPartner(partner);
            }
            Toast.makeText(this, "Partners importados con éxito", Toast.LENGTH_SHORT).show();

            // Actualiza la lista en el RecyclerView
            mAdapter.setItems(db.getAllPartners());
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error al importar partners", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
