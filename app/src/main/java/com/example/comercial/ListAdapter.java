package com.example.comercial;
//Clase MyAdapter, necesaria para el uso de RecyclerView (programado en Actividad_Partners)
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    // Aquí iría la lista de datos que el adaptador manejará, por ejemplo, una lista de objetos Partner
    private List<Partner> partnerList;
    private LayoutInflater mInflater;
    private Context context;

    // Constructor para inicializar la lista de datos
    public ListAdapter(List<Partner> partnerList, Context context) {
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
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Aquí inflas la vista de cada elemento de la lista
        // Por ejemplo, usando LayoutInflater para inflar un layout XML personalizado
        View view = mInflater.inflate(R.layout.list_element,parent, false);
        return new ListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        // Aquí vinculas los datos de partnerList con los elementos de la vista
        // Por ejemplo, estableciendo el texto de un TextView
        holder.bindData(partnerList.get(position));
    }

    public void setItems(List<Partner> items) {
        partnerList = items;
    }

    // Esta es una clase ViewHolder interna que necesitarás definir
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Aquí defines los componentes de la vista, por ejemplo, un TextView
        ImageView iconImage;
        TextView nombre,direccion,telefono,email;
        public ViewHolder(View itemView) {
            super(itemView);
            // Aquí inicializas los componentes de la vista
            iconImage = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            direccion = itemView.findViewById(R.id.direccionTextView);
            telefono = itemView.findViewById(R.id.telefonoTextView);
            email = itemView.findViewById(R.id.emailTextView);
        }

        void bindData(final Partner item) {
            iconImage.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            nombre.setText(item.getNombre());
            direccion.setText(item.getDireccion());
            telefono.setText(item.getTelefono());
            email.setText(item.getEmail());
        }
    }
}
