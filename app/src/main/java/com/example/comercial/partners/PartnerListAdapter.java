package com.example.comercial.partners;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comercial.BBDD.Partner;
import com.example.comercial.R;
import java.util.List;

public class PartnerListAdapter extends RecyclerView.Adapter<PartnerListAdapter.ViewHolder> {

    private List<Partner> partnerList;
    private LayoutInflater mInflater;
    private Context context;

    public PartnerListAdapter(List<Partner> partnerList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.partnerList = partnerList;
    }

    @Override
    public int getItemCount() {
        return partnerList.size();
    }

    @Override
    public PartnerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element_partners, parent, false);
        return new PartnerListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PartnerListAdapter.ViewHolder holder, final int position) {
        holder.bindData(partnerList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Partner currentPartner = partnerList.get(currentPosition);
                    // Aquí manejas el clic en el ítem
                    Intent intent = new Intent(context, Actividad_PartnerPedidos.class);
                    intent.putExtra("partnerId", currentPartner.getIdPartner());
                    context.startActivity(intent);
                }
            }
        });
    }

    public void setItems(List<Partner> items) {
        partnerList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView nombre, direccion, poblacion, cif, telefono, email;
        public ViewHolder(View itemView) {
            super(itemView);
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
            // poblacion no es un campo en Partner, asume que es parte de la dirección o elimínalo
            // poblacion.setText(item.getPoblacion());
            cif.setText(item.getCif());
            telefono.setText(item.getTelefono());
            email.setText(item.getCorreo());
            // iconImage no está definido en Partner, establece una imagen por defecto o elimínala
            // iconImage.setImageDrawable(...);
        }
    }
}
