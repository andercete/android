package com.example.comercial.Catalogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.BBDD.Articulos;
import com.example.comercial.BBDD.Catalogo;
import com.example.comercial.R;
import java.util.List;

public class CatalogoAdapterVer extends RecyclerView.Adapter<CatalogoAdapterVer.CatalogoViewHolder> {
    private List<Catalogo> catalogoList; // Cambiado de Articulos a Catalogo
    private LayoutInflater inflater;
    private Context context;

    public CatalogoAdapterVer(Context context, List<Catalogo> catalogoList) { // Cambiado de Articulos a Catalogo
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.catalogoList = catalogoList; // Cambiado de articulosList a catalogoList
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_element_catalogover, parent, false);
        return new CatalogoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        Catalogo catalogo = catalogoList.get(position); // Cambiado de Articulos a Catalogo
        holder.bind(catalogo);
    }

    @Override
    public int getItemCount() {
        return catalogoList.size(); // Cambiado de articulosList a catalogoList
    }

    public void setCatalogo(List<Catalogo> catalogoList) { // Cambiado de setArticulos a setCatalogo
        this.catalogoList = catalogoList; // Cambiado de articulosList a catalogoList
        notifyDataSetChanged();
    }

    public class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, descripcionTextView,  proveedorTextView, prVentaTextView, prCosteTextView, existenciasTextView;
ImageView iconImageView;
        public CatalogoViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageCatalogoView);
            nombreTextView = itemView.findViewById(R.id.nombreCatalogoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionCatalogoTextView);
            proveedorTextView = itemView.findViewById(R.id.proveedorCatalogoTextView);
            prVentaTextView = itemView.findViewById(R.id.precioCatalogoTextView);
            prCosteTextView = itemView.findViewById(R.id.precioCostoCatalogoTextView);
            existenciasTextView = itemView.findViewById(R.id.ExistenciasCatalogoTextView);        }

        public void bind(final Catalogo catalogo) { // Cambiado de Articulos a Catalogo en el parámetro del método bind
            // Actualización de las vistas con información de Catalogo
            nombreTextView.setText(catalogo.getNombre());
            descripcionTextView.setText(catalogo.getDescripcion());
            proveedorTextView.setText(catalogo.getProveedor());
            prVentaTextView.setText(String.valueOf(catalogo.getPvVent()));
            prCosteTextView.setText(String.valueOf(catalogo.getPvCost()));
            existenciasTextView.setText(String.valueOf(catalogo.getExistencias()));

            // Estableciendo la imagen del ImageView
            // Asumiendo que el nombre de la imagen en el objeto Catalogo corresponde a un recurso en drawable
            String imageName = catalogo.getDireccionImagen();
            if (imageName != null && !imageName.isEmpty()) {
                int imageResId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                // Check if the resource was found
                if (imageResId != 0) {
                    iconImageView.setImageResource(imageResId);
                } else {
                    // Resource not found, set a default image
                    iconImageView.setImageResource(R.drawable.ic_launcher_background); // Use your default image here
                }
            } else {
                // imageName is null or empty, set a default image
                iconImageView.setImageResource(R.drawable.iconemail); // Use your default image here
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