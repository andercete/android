package com.example.comercial.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comercial.BBDD.DbHelper;
import com.example.comercial.BBDD.Evento;
import com.example.comercial.R;
import java.util.List;

public class EventoListAdapter extends RecyclerView.Adapter<EventoListAdapter.EventoViewHolder> {

    private List<Evento> eventList;
    private DbHelper dbHelper;

    public EventoListAdapter(List<Evento> eventList, DbHelper dbHelper) {
        this.eventList = eventList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element_eventos, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        holder.bind(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventList(List<Evento> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, dateTextView, timeTextView, locationTextView, descriptionTextView;
        private Button deleteButton;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tituloTextView);
            dateTextView = itemView.findViewById(R.id.fechaTextView);
            timeTextView = itemView.findViewById(R.id.horaTexView);
            locationTextView = itemView.findViewById(R.id.ubicacionTextView);
            descriptionTextView = itemView.findViewById(R.id.descripcionTextView);
            deleteButton = itemView.findViewById(R.id.button2);
        }

        public void bind(Evento event) {
            titleTextView.setText(event.getTitle());
            dateTextView.setText(event.getDate());
            timeTextView.setText(event.getTime());
            locationTextView.setText(event.getLocation());
            descriptionTextView.setText(event.getDescription());

            // Configura el clic del botón de eliminación
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Lógica para eliminar el evento
                    eliminarEvento(getAdapterPosition());
                }
            });
        }
    }

    private void eliminarEvento(int position) {
        Evento eventoAEliminar = eventList.get(position);
        dbHelper.eliminarEvento(eventoAEliminar.getId());
        eventList.remove(position);
        notifyItemRemoved(position);
    }
}
