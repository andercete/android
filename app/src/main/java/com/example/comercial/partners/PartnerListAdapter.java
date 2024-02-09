package com.example.comercial.partners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.BBDD.AnderBD;
import com.example.comercial.R;
import com.example.comercial.pedidos.Actividad_CabPedidos;

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

        // Manejar el clic en el botón de borrar
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Partner currentPartner = partnerList.get(currentPosition);

                    // Mostrar un diálogo de confirmación
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmar Borrado")
                            .setMessage("¿Seguro que quieres borrar este socio?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Borrar el socio de la base de datos
                                    AnderBD db = new AnderBD(context); // Asegúrate de tener la instancia correcta de tu base de datos
                                    db.deletePartner(currentPartner.getIdPartner());

                                    // Actualizar la lista en el RecyclerView
                                    partnerList.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);

                                    Toast.makeText(context, "Socio borrado con éxito", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });

        // Manejar el clic en el elemento de la lista para abrir la actividad
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Partner currentPartner = partnerList.get(currentPosition);
                    // Aquí manejas el clic en el ítem
                    Intent intent = new Intent(context, Actividad_CabPedidos.class);
                    // Asegúrate de obtener los ID del objeto Partner y añadirlos al Intent
                    intent.putExtra("partnerId", currentPartner.getIdPartner());
                    intent.putExtra("partnerNombre", currentPartner.getNombre());
                    intent.putExtra("partnerDireccion", currentPartner.getDireccion());
                    intent.putExtra("partnerCif", currentPartner.getCif());
                    intent.putExtra("partnerTelefono", currentPartner.getTelefono());
                    intent.putExtra("partnerEmail", currentPartner.getCorreo());
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
        TextView nombre, direccion,cif, telefono, email;
        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageCatalogoView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            direccion = itemView.findViewById(R.id.direccionTextView);
            cif = itemView.findViewById(R.id.cifTextView);
            telefono = itemView.findViewById(R.id.telefonoTextView);
            email = itemView.findViewById(R.id.emailTextView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bindData(final Partner item) {
            nombre.setText(item.getNombre());
            direccion.setText(item.getDireccion());
            cif.setText(item.getCif());
            telefono.setText(item.getTelefono());
            email.setText(item.getCorreo());
            // Puedes establecer una imagen predeterminada o manejar casos específicos aquí
            // iconImage.setImageDrawable(...);
        }
    }
}
