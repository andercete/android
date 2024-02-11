package com.example.comercial.pedidos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comercial.BBDD.LineasPedido;
import com.example.comercial.R;

import java.util.List;

public class LineasPedidoAdapter extends RecyclerView.Adapter<LineasPedidoAdapter.LineaPedidoViewHolder> {

    private List<LineasPedido> lineasPedidoList;

    public LineasPedidoAdapter(List<LineasPedido> lineasPedidoList) {
        this.lineasPedidoList = lineasPedidoList;
    }

    @NonNull
    @Override
    public LineaPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_element_lineapedido, parent, false);
        return new LineaPedidoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LineaPedidoViewHolder holder, int position) {
        LineasPedido lineaPedido = lineasPedidoList.get(position);

        // Configurar los TextViews con los datos de la línea de pedido
        holder.textViewIdLinea.setText(String.valueOf(lineaPedido.getIdLinea()));
        holder.textViewIdArticulo.setText(String.valueOf(lineaPedido.getIdArticulo()));
        holder.textViewCantidad.setText(String.valueOf(lineaPedido.getCantidad()));
        holder.textViewDescuento.setText(String.valueOf(lineaPedido.getDescuento()));
        holder.textViewPrecio.setText(String.valueOf(lineaPedido.getPrecio()));
    }

    @Override
    public int getItemCount() {
        return lineasPedidoList.size();
    }

    public class LineaPedidoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewIdLinea, textViewIdArticulo, textViewCantidad, textViewDescuento, textViewPrecio;

        public LineaPedidoViewHolder(View view) {
            super(view);
            textViewIdLinea = view.findViewById(R.id.textViewIdLinea);
            textViewIdArticulo = view.findViewById(R.id.textViewIdArticulo);
            textViewCantidad = view.findViewById(R.id.textViewCantidad);

            textViewPrecio = view.findViewById(R.id.textViewPrecio);
        }
    }
}

