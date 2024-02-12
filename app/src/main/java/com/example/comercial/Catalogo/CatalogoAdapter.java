package com.example.comercial.Catalogo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comercial.BBDD.Catalogo;
import com.example.comercial.R;
import java.util.List;

public class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<Catalogo> catalogoList;


    public CatalogoAdapter(Context context, List<Catalogo> catalogoList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.catalogoList = catalogoList;
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_element_catalogo, parent, false);
        return new CatalogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        Catalogo catalogo = catalogoList.get(position);
        holder.bind(catalogo);
    }

    @Override
    public int getItemCount() {
        return catalogoList.size();
    }

     //Método para actualizar la lista de catálogo en el adaptador
    public void setCatalogo(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
       notifyDataSetChanged(); // Notifica que los datos han cambiado
    }

    public class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView idArticuloTextView, nombreTextView, descripcionTextView,  proveedorTextView, prVentaTextView, prCosteTextView, existenciasTextView;
        ImageView iconImageView;
        ImageButton btnDecrease, btnIncrease;
        TextView quantityText;

        public CatalogoViewHolder(View itemView) {
            super(itemView);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            quantityText = itemView.findViewById(R.id.editTextCantidad); // Suponiendo que usas TextView
            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Incrementa la cantidad
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Catalogo catalogo = catalogoList.get(position);
                        catalogo.incrementQuantity();
                        notifyItemChanged(position);
                    }
                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Decrementa la cantidad pero no permite que sea menor que 1
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Catalogo catalogo = catalogoList.get(position);
                        if (catalogo.getQuantity() > 1) {
                            catalogo.decrementQuantity();
                            notifyItemChanged(position);
                        }
                    }
                }
            });


            iconImageView = itemView.findViewById(R.id.iconImageCatalogoView);
            idArticuloTextView = itemView.findViewById(R.id.idArticuloCatalogoTextView);
            nombreTextView = itemView.findViewById(R.id.nombreCatalogoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionCatalogoTextView);
            proveedorTextView = itemView.findViewById(R.id.proveedorCatalogoTextView);
            prVentaTextView = itemView.findViewById(R.id.precioCatalogoTextView);
            prCosteTextView = itemView.findViewById(R.id.precioCostoCatalogoTextView);
            existenciasTextView = itemView.findViewById(R.id.ExistenciasCatalogoTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la posición del artículo en el adaptador
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Cambiar el estado de selección del artículo
                        Catalogo catalogo = catalogoList.get(position);
                        catalogo.setSelected(!catalogo.isSelected());
                        notifyItemChanged(position);
                    }
                }
            });

        }

        public void bind(final Catalogo catalogo) {
            nombreTextView.setText(catalogo.getNombre());
            descripcionTextView.setText(catalogo.getDescripcion());
            proveedorTextView.setText(catalogo.getProveedor());
            prVentaTextView.setText(String.valueOf(catalogo.getPvVent()));
            prCosteTextView.setText(String.valueOf(catalogo.getPvCost()));
            existenciasTextView.setText(String.valueOf(catalogo.getExistencias()));
            quantityText.setText(String.valueOf(catalogo.getQuantity()));

            // Estableciendo la imagen del ImageView
            // Asumiendo que el nombre de la imagen en el objeto Catalogo corresponde a un recurso en drawable
            String imageName = catalogo.getImagen();
            if (imageName != null && !imageName.trim().isEmpty()) {
                try {
                    byte[] decodedString = Base64.decode(imageName, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    iconImageView.setImageBitmap(decodedBitmap);
                } catch (IllegalArgumentException e) {
                    // Este bloque catch captura la excepción de Base64 mal formado
                    Log.e("CatalogoAdapter", "Error al decodificar Base64", e);
                    // Opcional: establece una imagen predeterminada o maneja el error como desees
                    iconImageView.setImageResource(R.drawable.articulo_imagen_pordefecto);
                }
            }else {
                // Maneja el caso en el que imageName es null, por ejemplo, estableciendo una imagen predeterminada
                iconImageView.setImageResource(R.drawable.articulo_imagen_pordefecto);
            }
            itemView.setSelected(catalogo.isSelected()); // Aquí puedes cambiar el fondo o cualquier otro indicador
            if (catalogo.isSelected()) {
                // Cambia el fondo para elementos seleccionados
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedItemBackground)); // Define este color en tus recursos
            } else {
                // Restablece el fondo para elementos no seleccionados
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultItemBackground)); // Define este color en tus recursos
            }
        }
    }
}