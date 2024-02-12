package com.example.comercial.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.CabPedidos;
import com.example.comercial.R;

public class Actividad_CabPedidos extends AppCompatActivity  {
    TextView tNombre,tDireccion,tCif,tTelefono,tEmail;
    private CabPedidoListAdapter mAdapter;
    private RecyclerView recyclerView;
    private Button bAgregar, bBorrar;
    private DbHelper db;
    private int idPartner; // Declare idPartner globally


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cabpedidos);

        tNombre = findViewById(R.id.tPartnerNombre);
        tDireccion = findViewById(R.id.tPartnerDireccion);
        tCif = findViewById(R.id.tPartnerCif);
        tTelefono = findViewById(R.id.tTelefono);
        tEmail = findViewById(R.id.tEmail);
        bAgregar = findViewById(R.id.bAgregarPedido);

        db = new DbHelper(this); // Asegúrate de que esta línea esté antes de cualquier uso de db

        Intent intent = getIntent();
        idPartner = intent.getIntExtra("partnerId", -1); // El ID del partner seleccionado

        if (idPartner != -1) {
            List<CabPedidos> cabPedidosList = db.getAllCabPedidos(idPartner);
            initRecyclerView(cabPedidosList);
        }


        String nombre = intent.getStringExtra("partnerNombre");
        String direccion = intent.getStringExtra("partnerDireccion");
        String cif = intent.getStringExtra("partnerCif");
        // Note: Assuming telefono is an integer. If it's a String, use getStringExtra.
        String telefono = intent.getStringExtra("partnerTelefono");
        String email = intent.getStringExtra("partnerEmail");

        tNombre.setText(nombre);
        tDireccion.setText(direccion);
        tCif.setText(cif);
        tTelefono.setText(String.valueOf(telefono));
        tEmail.setText(email);

        db = new DbHelper(this); // Inicializa la base de datos

        bAgregar = findViewById(R.id.bAgregarPedido);
      //  bBorrar = findViewById(R.id.bBorrarPedidos);
        bAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad_CabPedidos.this, Actividad_AltaPedido.class);
                // Asegúrate de obtener los ID del objeto Partner y añadirlos al Intent
                intent.putExtra("partnerId", idPartner);
                startActivity(intent);
            }
        });

        // Inicializa el RecyclerView con la lista de CabPedidos desde la base de datos

        List<CabPedidos> cabPedidosList = db.getAllCabPedidos(idPartner);
        initRecyclerView(cabPedidosList);



    }

    // En tu Actividad_CabPedidos
    public void abrirSegundaActividad(int idPedido) {
        Intent intent = new Intent(this, Actividad_VerPedido.class);
        intent.putExtra("idPedido", idPedido);
        startActivity(intent);
    }


    private void initRecyclerView(List<CabPedidos> cabPedidos) {
        recyclerView = findViewById(R.id.listRecyclerViewCabPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CabPedidoListAdapter(cabPedidos, this);
        recyclerView.setAdapter(mAdapter);
    }

    public void borrarRegistroPorId(int idPedido) {
        db.deleteCabPedido(idPedido);
        Toast.makeText(this, "Registro de pedido con ID " + idPedido + " ha sido borrado", Toast.LENGTH_SHORT).show();
        List<CabPedidos> updatedList = db.getAllCabPedidos(idPartner); // Vuelve a cargar la lista actualizada
        mAdapter.setItems(updatedList);
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Actualiza la lista de CabPedidos
        List<CabPedidos> updatedCabPedidosList = db.getAllCabPedidos(idPartner);
        mAdapter.setItems(updatedCabPedidosList);
        mAdapter.notifyDataSetChanged();
    }
}
