package com.example.comercial.Catalogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
        View view = inflater.inflate(R.layout.list_element_catalogo, parent, false);
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
            // Inicialización de vistas aquí permanece igual
        }

        public void bind(final Catalogo catalogo) { // Cambiado de Articulos a Catalogo en el parámetro del método bind
            // Actualización de las vistas con información de Catalogo
            nombreTextView.setText(catalogo.getNombre());
            descripcionTextView.setText(catalogo.getDescripcion());
            proveedorTextView.setText(catalogo.getProveedor());
            prVentaTextView.setText(String.valueOf(catalogo.getPvVent()));
            prCosteTextView.setText(String.valueOf(catalogo.getPvCost()));
            existenciasTextView.setText(String.valueOf(catalogo.getExistencias()));

            // La lógica para establecer la imagen del ImageView basada en el nombre de la imagen permanece igual
            String imageName = catalogo.getDireccionImagen();
            // Resto de la implementación para establecer la imagen y el estado de selección permanece igual
        }
    }
}
