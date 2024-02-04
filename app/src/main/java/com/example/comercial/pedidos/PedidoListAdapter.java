package com.example.comercial.pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.R;

import java.util.List;

public class PedidoListAdapter extends RecyclerView.Adapter<PedidoListAdapter.ViewHolder> {

    private List<Pedido> pedidoList;
    private LayoutInflater mInflater;
    private Context context;

    public PedidoListAdapter(List<Pedido> pedidoList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.pedidoList = pedidoList;
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element_pedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(pedidoList.get(position));
    }

    public void setItems(List<Pedido> items) {
        pedidoList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView idPartner, idArticulo, cantidad, descuento;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageCatalogoView); // Puedes personalizar la vista según tus necesidades
            idPartner = itemView.findViewById(R.id.idArticuloCatalogoTextView);
            idArticulo = itemView.findViewById(R.id.descripcionCatalogoTextView);
            cantidad = itemView.findViewById(R.id.categoriaCatalogoTextView);
            descuento = itemView.findViewById(R.id.proveedorCatalogoTextView);
        }

        void bindData(final Pedido item) {
            idPartner.setText(item.getIdPartner());
            idArticulo.setText(item.getIdArticulo());
            cantidad.setText(String.valueOf(item.getCantidad()));
            descuento.setText(String.valueOf(item.getDescuento()));
        }
    }
}
