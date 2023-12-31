package com.example.comercial;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {
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
            // Obtener la posición del evento que se va a borrar
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listaEventos.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);

                // Llamada al método guardarEventosEnSharedPreferences en MainActivity
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