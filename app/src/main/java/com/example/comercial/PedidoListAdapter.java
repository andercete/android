package com.example.comercial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
    public PedidoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent, false);
        return new PedidoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PedidoListAdapter.ViewHolder holder, final int position) {
        holder.bindData(pedidoList.get(position));
    }

    public void setItems(List<Pedido> items) {
        pedidoList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView idPedido, idPartner, idLinea, idArticulo, cantidad, descuento, precio;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView); // Puedes personalizar la vista según tus necesidades
            idPedido = itemView.findViewById(R.id.idPedidoTextView);
            idPartner = itemView.findViewById(R.id.idPartnerTextView);
            idLinea = itemView.findViewById(R.id.idLineaTextView);
            idArticulo = itemView.findViewById(R.id.idArticuloTextView);
            cantidad = itemView.findViewById(R.id.cantidadTextView);
            descuento = itemView.findViewById(R.id.descuentoTextView);
            precio = itemView.findViewById(R.id.precioTextView);
        }

        void bindData(final Pedido item) {
            idPedido.setText(item.getIdPedido());
            idPartner.setText(item.getIdPartner());
            idLinea.setText(item.getIdLinea());
            idArticulo.setText(item.getIdArticulo());
            cantidad.setText(String.valueOf(item.getCantidad()));
            descuento.setText(String.valueOf(item.getDescuento()));
            precio.setText(String.valueOf(item.getPrecio()));
        }
    }
}
