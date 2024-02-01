package com.example.comercial.calendario;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.Evento;
import com.example.comercial.R;

import java.util.List;

public class EventoListAdapter extends RecyclerView.Adapter<EventoListAdapter.EventoViewHolder> {
    private List<Evento> listaEventos;

    public EventoListAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vistaEvento = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element_eventos, parent, false);
        return new EventoViewHolder(vistaEvento);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);
        holder.tituloTextView.setText(evento.getTitle());
        holder.fechaTextView.setText("Fecha: " + evento.getDate());
      //  holder.ubicacionTextView.setText("Ubicación: " + evento.getUbicacion());
        holder.descripcionTextView.setText("Descripción: " + evento.getDescription());

        // Manejar el clic del botón de borrar
        holder.borrarButton.setOnClickListener(v -> {
            // Obtener la posición del evento que se va a borrar
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listaEventos.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);

                // Llamada al método guardarEventosEnSharedPreferences en MainActivity
                ((Actividad_Eventos)holder.itemView.getContext()).guardarEventosEnSharedPreferences();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public void setEventList(List<Evento> eventList) {
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