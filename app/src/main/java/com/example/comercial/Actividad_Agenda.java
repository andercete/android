package com.example.comercial;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;
        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.List;

public class Actividad_Agenda extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventoAdapter adaptador;
    private List<Evento> listaEventos;
    private static final int CODIGO_CREAR_EVENTO = 1;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_agenda);

        recyclerView = findViewById(R.id.recyclerView);
        listaEventos = new ArrayList<>();
        adaptador = new EventoAdapter(listaEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

        sharedPreferences = getSharedPreferences("EventosPref", MODE_PRIVATE);

        cargarEventosDesdeSharedPreferences();

        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent(Actividad_Agenda.this, CrearEventoActivity.class);
            startActivityForResult(intent, CODIGO_CREAR_EVENTO);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_CREAR_EVENTO && resultCode == RESULT_OK && data != null) {
            String titulo = data.getStringExtra("titulo");
            String fecha = data.getStringExtra("fecha");
            String ubicacion = data.getStringExtra("ubicacion");
            String descripcion = data.getStringExtra("descripcion");

            Evento nuevoEvento = new Evento(titulo, fecha, ubicacion, descripcion);

            listaEventos.add(nuevoEvento);
            adaptador.notifyDataSetChanged();

            guardarEventosEnSharedPreferences();
        }
    }

    private void cargarEventosDesdeSharedPreferences() {
        String eventosJson = sharedPreferences.getString("eventos", "");
        if (!eventosJson.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Evento>>(){}.getType();
            listaEventos = gson.fromJson(eventosJson, type);
            adaptador = new EventoAdapter(listaEventos);
            recyclerView.setAdapter(adaptador);
        }
    }

    public void guardarEventosEnSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String eventosJson = gson.toJson(listaEventos);
        editor.putString("eventos", eventosJson);
        editor.apply();
    }

    public static class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {
        private List<Evento> listaEventos;

        public EventoAdapter(List<Evento> listaEventos) {
            this.listaEventos = listaEventos;
        }

        @NonNull
        @Override
        public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View vistaEvento = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
            return new EventoViewHolder(vistaEvento);
        }

        @Override
        public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
            Evento evento = listaEventos.get(position);
            holder.tituloTextView.setText(evento.getTitulo());
            holder.fechaTextView.setText("Fecha: " + evento.getFecha());
            holder.ubicacionTextView.setText("Ubicación: " + evento.getUbicacion());
            holder.descripcionTextView.setText("Descripción: " + evento.getDescripcion());

            // Manejar el clic del botón de borrar
            holder.borrarButton.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listaEventos.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    ((Actividad_Agenda)holder.itemView.getContext()).guardarEventosEnSharedPreferences();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaEventos.size();
        }

        public static class EventoViewHolder extends RecyclerView.ViewHolder {
            TextView tituloTextView;
            TextView fechaTextView;
            TextView ubicacionTextView;
            TextView descripcionTextView;
            Button borrarButton;

            public EventoViewHolder(View itemView) {
                super(itemView);
                tituloTextView = itemView.findViewById(R.id.tituloTextView);
                fechaTextView = itemView.findViewById(R.id.fechaTextView);
                ubicacionTextView = itemView.findViewById(R.id.ubicacionTextView);
                descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
                borrarButton = itemView.findViewById(R.id.button2); // Asignar el botón de borrar
            }
        }
    }
}

