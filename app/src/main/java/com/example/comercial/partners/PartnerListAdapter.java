package com.example.comercial.partners;
//Clase MyAdapter, necesaria para el uso de RecyclerView (programado en Actividad_Partners)
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.R;

import java.util.List;

public class PartnerListAdapter extends RecyclerView.Adapter<PartnerListAdapter.ViewHolder> {

    // Aquí iría la lista de datos que el adaptador manejará, por ejemplo, una lista de objetos Partner
    private List<Partner> partnerList;
    private LayoutInflater mInflater;
    private Context context;

    // Constructor para inicializar la lista de datos
    public PartnerListAdapter(List<Partner> partnerList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.partnerList = partnerList;
    }

    @Override
    public int getItemCount() {
        // Devuelve el tamaño de tu lista de datos
        return partnerList.size();
    }

    @Override
    public PartnerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Aquí inflas la vista de cada elemento de la lista
        // Por ejemplo, usando LayoutInflater para inflar un layout XML personalizado
        View view = mInflater.inflate(R.layout.list_element_partners,parent, false);
        return new PartnerListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final PartnerListAdapter.ViewHolder holder, final int position) {
        // Vincula los datos del ítem con la vista
        holder.bindData(partnerList.get(position));

        // Establece el listener para el clic
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén la posición actual del ítem
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    // Ahora usa esta posición actualizada para obtener los datos
                    Partner currentPartner = partnerList.get(currentPosition);

                    // Aquí manejas el clic en el ítem
                    Intent intent = new Intent(context, Actividad_PartnerPedidos.class);
                    // Suponiendo que Partner tiene un método getId() para obtener un identificador único
                    intent.putExtra("partnerNombre", currentPartner.getNombre());
                    intent.putExtra("partnerDireccion", currentPartner.getDireccion());
                    intent.putExtra("partnerPoblacion", currentPartner.getPoblacion());
                    intent.putExtra("partnerCif", currentPartner.getCif());
                    intent.putExtra("partnerTelefono", currentPartner.getTelefono());
                    intent.putExtra("partnerEmail", currentPartner.getEmail());
                    context.startActivity(intent);
                }
            }
        });
    }

    public void setItems(List<Partner> items) {
        partnerList = items;
    }

    // Esta es una clase ViewHolder interna que necesitarás definir
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Aquí defines los componentes de la vista, por ejemplo, un TextView
        ImageView iconImage;
        TextView nombre,direccion,poblacion,cif,telefono,email;
        public ViewHolder(View itemView) {
            super(itemView);
            // Aquí inicializas los componentes de la vista
            iconImage = itemView.findViewById(R.id.iconImageCatalogoView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            direccion = itemView.findViewById(R.id.direccionTextView);
            poblacion = itemView.findViewById(R.id.poblacionTextView);
            cif = itemView.findViewById(R.id.cifTextView);
            telefono = itemView.findViewById(R.id.telefonoTextView);
            email = itemView.findViewById(R.id.emailTextView);
        }

        void bindData(final Partner item) {
            nombre.setText(item.getNombre());
            direccion.setText(item.getDireccion());
            poblacion.setText(item.getPoblacion());
            cif.setText(item.getCif());
            telefono.setText(String.valueOf(item.getTelefono()));
            email.setText(item.getEmail());
        }
    }
}
