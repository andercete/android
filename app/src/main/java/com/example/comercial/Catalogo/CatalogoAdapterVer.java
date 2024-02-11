package com.example.comercial.Catalogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import com.example.comercial.BBDD.Articulos;
import com.example.comercial.R;
import java.util.List;

public class CatalogoAdapterVer extends RecyclerView.Adapter<CatalogoAdapterVer.CatalogoViewHolder> {
    private List<Catalogo> catalogoList;
    private LayoutInflater inflater;
    private Context context;

    public CatalogoAdapterVer(Context context, List<Catalogo> catalogoList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.catalogoList = catalogoList;
    }

    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_element_catalogover, parent, false);
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

    // Método para actualizar la lista de catálogo en el adaptador
    public void setCatalogo(List<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
        notifyDataSetChanged(); // Notifica que los datos han cambiado
    }

    public class CatalogoViewHolder extends RecyclerView.ViewHolder {
        TextView idArticuloTextView, nombreTextView, descripcionTextView, categoriaTextView, proveedorTextView, prVentaTextView, prCosteTextView, existenciasTextView;
        ImageView iconImageView;

        public CatalogoViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageCatalogoView);
            idArticuloTextView = itemView.findViewById(R.id.idArticuloCatalogoTextView);
            nombreTextView = itemView.findViewById(R.id.nombreCatalogoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionCatalogoTextView);
            categoriaTextView = itemView.findViewById(R.id.categoriaCatalogoTextView);
            proveedorTextView = itemView.findViewById(R.id.proveedorCatalogoTextView);
            prVentaTextView = itemView.findViewById(R.id.precioCatalogoTextView);
            prCosteTextView = itemView.findViewById(R.id.precioCostoCatalogoTextView);
            existenciasTextView = itemView.findViewById(R.id.ExistenciasCatalogoTextView);

        }

        public void bind(final Catalogo catalogo) {
            // Estableciendo los textos de los TextViews
            idArticuloTextView.setText(String.valueOf(catalogo.getIdArticulo()));
            nombreTextView.setText(catalogo.getNombre());
            descripcionTextView.setText(catalogo.getDescripcion());
            proveedorTextView.setText(catalogo.getProveedor());
            prVentaTextView.setText(String.valueOf(catalogo.getPrVent()));
            prCosteTextView.setText(String.valueOf(catalogo.getPrCost()));
            existenciasTextView.setText(String.valueOf(catalogo.getExistencias()));

            // Estableciendo la imagen del ImageView

            String imageName = catalogo.getImageName();
            if (imageName != null) {
                byte[] decodedString = Base64.decode(imageName, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                iconImageView.setImageBitmap(decodedBitmap);
            } else {
                // Maneja el caso en el que imageName es null, por ejemplo, estableciendo una imagen predeterminada
                iconImageView.setImageResource(R.drawable.articulo_imagen_pordefecto);
            }

        }
    }
}

