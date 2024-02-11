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
            // Asumiendo que el nombre de la imagen en el objeto Catalogo corresponde a un recurso en drawable
            String imageName = catalogo.getImageName();
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

        }
    }
}

