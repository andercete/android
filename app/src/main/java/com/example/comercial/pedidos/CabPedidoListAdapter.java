package com.example.comercial.pedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.comercial.BBDD.CabPedidos;
import com.example.comercial.R;

import java.util.List;

public class CabPedidoListAdapter extends RecyclerView.Adapter<CabPedidoListAdapter.ViewHolder> {

    private List<CabPedidos> pedidoList;
    private LayoutInflater mInflater;
    private Context context;


    public CabPedidoListAdapter(List<CabPedidos> pedidoList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.pedidoList = pedidoList;
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    // Añade este método para obtener un elemento en una posición específica
    public CabPedidos getItem(int position) {
        if (position >= 0 && position < pedidoList.size()) {
            return pedidoList.get(position);
        }
        return null;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element_cabpedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(pedidoList.get(position));
        final CabPedidos cabPedido = pedidoList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Actividad_CabPedidos) context).abrirSegundaActividad(cabPedido.getIdPedido());
            }
        });

        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Actividad_CabPedidos) {
                    // Castear el contexto a Actividad_CabPedidos y llamar al método
                    ((Actividad_CabPedidos) context).borrarRegistroPorId(cabPedido.getIdPedido());
                }
            }
        });
    }

    public void setItems(List<CabPedidos> items) {
        pedidoList = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnBorrar;
        TextView idPedidoTextView, idPartnerTextView, idComercialTextView, fechaPedidoTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            idPedidoTextView = itemView.findViewById(R.id.idPedidoTextView);
            idPartnerTextView = itemView.findViewById(R.id.idPartnerTextview);
            idComercialTextView = itemView.findViewById(R.id.idComercialTextView);
            fechaPedidoTextView = itemView.findViewById(R.id.fechaPedidoTextView);
            btnBorrar = itemView.findViewById(R.id.bCabPedidoBorrar);
        }

        void bindData(final CabPedidos item) {
            idPedidoTextView.setText("ID PEDIDO: " + item.getIdPedido());
            idPartnerTextView.setText("Partner: " + item.getIdPartner());
            idComercialTextView.setText("Comercial: " + item.getIdComercial());
            fechaPedidoTextView.setText("Fecha: " + item.getFechaPedido());
        }
    }
}
