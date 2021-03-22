package com.delpradosergio.orderbox.Interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;

import com.delpradosergio.orderbox.Fragments.VerPedidos_FragmentDirections;
import com.delpradosergio.orderbox.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AdaptadorListaPedidos extends RecyclerView.Adapter<AdaptadorListaPedidos.ViewHolderVerPedidos>{

    private List<PedidoConArticulos> listaPedidos;

    public AdaptadorListaPedidos(List<PedidoConArticulos> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public void setListaPedidos(List<PedidoConArticulos> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public ViewHolderVerPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_lista_pedidos, parent, false);
        return new ViewHolderVerPedidos(view);

    }

    //Cogemos los datos de los pedidos guardados en el arraylist y ponemos los resultados en el holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolderVerPedidos holder, int position) {
        //Operador ternario: es como un if { ? "true" : "false" } -> ?=si

        /* Traduccion al ingles (Arreglar)
        holder.preparado.setText(listaPedidos.get(position).pedido.isPreparado() ? getString(R.string.SingleWord_Preparado) : getString(R.string.SingleWord_NoEntregado));
        holder.entregado.setText(listaPedidos.get(position).pedido.isEntregado() ? getString(R.string.SingleWord_Entregado) : getString(R.string.SingleWord_NoEntregado));
        holder.nPedido.setText(String.format(getString(R.string.NumeroPedido)), listaPedidos.get(position).pedido.getNPedido());

         */
        holder.preparado.setText(listaPedidos.get(position).pedido.isPreparado() ? "Preparado" : "No preparado");
        holder.entregado.setText(listaPedidos.get(position).pedido.isEntregado() ? "Entregado" : "No entregado");
        holder.nPedido.setText("Pedido Nº:" + listaPedidos.get(position).pedido.getNPedido());

        holder.nombreCliente.setText(listaPedidos.get(position).pedido.getNombreCliente());
        holder.fechaEntrega.setText(listaPedidos.get(position).pedido.getFechaEntrega());
        //holder.imagePreparado.setColorFilter(listaPedidos.get(position).pedido.isPreparado() ? R.color.lightGreen : R.color.darkRed);
        //holder.imageEntregado.setColorFilter(listaPedidos.get(position).pedido.isEntregado() ? R.color.lightGreen : R.color.darkRed);

        StringBuilder sb = new StringBuilder();
        for(Articulo articulo : listaPedidos.get(position).articulos) {
            sb.append("• "+articulo.getNombreArticulo()+", "+articulo.getCantidadFormateada()+"\n");
        }
        holder.descripcionPedido.setText(sb.toString());


        holder.tarjeta.setOnClickListener(v -> {
            long idPedido = listaPedidos.get(position).pedido.getNPedido();
            VerPedidos_FragmentDirections.ActionVerPedidosToDetailPedido action = VerPedidos_FragmentDirections.actionVerPedidosToDetailPedido(idPedido);
            Navigation.findNavController(holder.tarjeta).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }


    public class ViewHolderVerPedidos extends RecyclerView.ViewHolder {

        TextView nPedido, nombreCliente, descripcionPedido, fechaEntrega, preparado, entregado;
        MaterialCardView tarjeta;
        ImageView imagePreparado, imageEntregado;

        public ViewHolderVerPedidos(@NonNull View itemView) {
            super(itemView);
            preparado = itemView.findViewById(R.id.textViewPreparado);
            entregado = itemView.findViewById(R.id.textViewEntregado);
            nPedido=itemView.findViewById(R.id.textViewNumeroPedido);
            nombreCliente=itemView.findViewById(R.id.textViewNombreClienteAlerta);
            descripcionPedido=itemView.findViewById(R.id.textViewPedido);
            fechaEntrega=itemView.findViewById(R.id.textViewFecha);
            tarjeta = itemView.findViewById(R.id.tarjetaPedido);
            imageEntregado = itemView.findViewById(R.id.imageViewEntregado);
            imagePreparado = itemView.findViewById(R.id.imageViewPreparado);
        }
    }
}
