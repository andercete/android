package com.example.comercial.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comercial.DBHelper;
import com.example.comercial.Evento;
import com.example.comercial.R;

import java.util.List;

public class Actividad_Eventos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventoListAdapter eventAdapter;
    private Button botonAbrirActividad;
    private List<Evento> eventList;
    private DBHelper dbHelper;

    private static final int REQUEST_CODE_NUEVO_EVENTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_eventos);

        dbHelper = new DBHelper(this);
        eventList = dbHelper.getAllEventos();

        botonAbrirActividad = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventoListAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        botonAbrirActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Actividad_Eventos.this, Actividad_AltaEvento.class);
                startActivityForResult(intent, REQUEST_CODE_NUEVO_EVENTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NUEVO_EVENTO && resultCode == RESULT_OK) {
            // Actualiza la lista de eventos y notifica al adaptador
            eventList = dbHelper.getAllEventos();
            eventAdapter.setEventList(eventList);
            eventAdapter.notifyDataSetChanged();
        }
    }
}
