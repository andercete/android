package com.example.comercial.Catalogo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
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
        TextView idArticuloTextView, nombreTextView, descripcionTextView, proveedorTextView, prVentaTextView, prCosteTextView, existenciasTextView;
        ImageView iconImageView;

        public CatalogoViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageCatalogoView);
            idArticuloTextView = itemView.findViewById(R.id.idArticuloCatalogoTextView);
            nombreTextView = itemView.findViewById(R.id.nombreCatalogoTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionCatalogoTextView);
            proveedorTextView = itemView.findViewById(R.id.proveedorCatalogoTextView);
            prVentaTextView = itemView.findViewById(R.id.precioCatalogoTextView);
            prCosteTextView = itemView.findViewById(R.id.precioCostoCatalogoTextView);
            existenciasTextView = itemView.findViewById(R.id.ExistenciasCatalogoTextView);

        }

        public void bind(final Catalogo catalogo) { // Cambiado de Articulos a Catalogo en el parámetro del método bind
            // Actualización de las vistas con información de Catalogo
            idArticuloTextView.setText(String.valueOf(catalogo.getIdArticulo()));
            nombreTextView.setText(catalogo.getNombre());
            descripcionTextView.setText(catalogo.getDescripcion());
            proveedorTextView.setText(catalogo.getProveedor());
            prVentaTextView.setText(String.valueOf(catalogo.getPvVent()));
            prCosteTextView.setText(String.valueOf(catalogo.getPvCost()));
            existenciasTextView.setText(String.valueOf(catalogo.getExistencias()));

            // Estableciendo la imagen del ImageView
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
            } else {
                // Maneja el caso en el que imageName es null, por ejemplo, estableciendo una imagen predeterminada
                iconImageView.setImageResource(R.drawable.articulo_imagen_pordefecto);
            }


        }
    }
}

