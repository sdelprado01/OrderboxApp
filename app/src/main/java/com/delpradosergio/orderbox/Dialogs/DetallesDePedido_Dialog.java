package com.delpradosergio.orderbox.Dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;
import com.delpradosergio.orderbox.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DetallesDePedido_Dialog extends DialogFragment {

    ViewModelCarnitronic viewModel;
    Button eliminarPedido, marcarComoPreparado, marcarComoEntregado;
    ImageView botonRetroceder, botonEditar;
    TextView telefono;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        long numPedido = DetallesDePedido_DialogArgs.fromBundle(getArguments()).getNumPedido();

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_detalles_de_pedido, null, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);
        PedidoConArticulos pedidoConArticulos = viewModel.getPedidoConArticulo(numPedido);

        eliminarPedido = view.findViewById(R.id.buttonEliminarPedido);
        eliminarPedido.setOnClickListener(v->{
            DetallesDePedido_DialogDirections.ActionDetailPedidoToConfirmarEliminarPedido
                    action = DetallesDePedido_DialogDirections.actionDetailPedidoToConfirmarEliminarPedido(numPedido);
            NavHostFragment.findNavController(this).navigate(action);
            getDialog().hide();
        });

        botonEditar = view.findViewById(R.id.imageButtonEditarPedidoDetailPedido);
        botonEditar.setOnClickListener(v-> {
            DetallesDePedido_DialogDirections.ActionDetailPedidoToAddPedidos action = DetallesDePedido_DialogDirections.actionDetailPedidoToAddPedidos();
            action.setNumPedido(numPedido);
            NavHostFragment.findNavController(this).navigate(action);
        });


        marcarComoPreparado = view.findViewById(R.id.buttonMarcarComoPreparado);
        marcarComoPreparado.setText(textoPreparado(pedidoConArticulos.pedido.isPreparado()));
        marcarComoPreparado.setOnClickListener(v -> {
            pedidoConArticulos.pedido.setPreparado(!pedidoConArticulos.pedido.isPreparado());
            marcarComoPreparado.setText(textoPreparado(pedidoConArticulos.pedido.isPreparado()));
            Toast.makeText(getContext(), "Pedido marcado como " + textoPreparado(pedidoConArticulos.pedido.isPreparado()), Toast.LENGTH_LONG).show();
            viewModel.actualizarPedido(pedidoConArticulos.pedido);
        });


        marcarComoEntregado = view.findViewById(R.id.buttonMarcarComoEntregado);
        marcarComoEntregado.setText(textoEntregado(pedidoConArticulos.pedido.isEntregado()));
        marcarComoEntregado.setOnClickListener(v -> {
            pedidoConArticulos.pedido.setEntregado(!pedidoConArticulos.pedido.isEntregado());
            marcarComoEntregado.setText(textoEntregado(pedidoConArticulos.pedido.isEntregado()));
            Toast.makeText(getContext(), "Pedido marcado como " + textoEntregado(pedidoConArticulos.pedido.isEntregado()), Toast.LENGTH_LONG).show();
            viewModel.actualizarPedido(pedidoConArticulos.pedido);
        });

        telefono = view.findViewById(R.id.textViewTelefonoDetails);
        telefono.setOnClickListener(v->{
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + telefono.getText()));
                startActivity(intent);
        });


        TextView nPedido = view.findViewById(R.id.textViewNPedidoDetails);
        nPedido.setText(String.format(getString(R.string.NumeroPedido), numPedido));
        TextView nombreCliente = view.findViewById(R.id.textViewNombreDetail);
        nombreCliente.setText(pedidoConArticulos.pedido.getNombreCliente());
        TextView telefono = view.findViewById(R.id.textViewTelefonoDetails);
        telefono.setText(String.format(getString(R.string.tfno_subrayado), pedidoConArticulos.pedido.getTelefono()));
        TextView fechaEntrega = view.findViewById(R.id.textViewFechaDetail);
        fechaEntrega.setText(pedidoConArticulos.pedido.getFechaEntrega());
        TextView descripcion = view.findViewById(R.id.listaDescripcion);
        StringBuilder sb = new StringBuilder();
        for(Articulo articulo : pedidoConArticulos.articulos) {
            sb.append("• "+ articulo.getNombreArticulo()+", "+articulo.getCantidadFormateada()+" ("+articulo.getDescripcion()+")\n");
        }
        descripcion.setText(sb.toString());

        botonRetroceder = view.findViewById(R.id.imageViewBackDetail);
        botonRetroceder.setOnClickListener(v-> NavHostFragment.findNavController(this).popBackStack());

        AlertDialog dialog = new MaterialAlertDialogBuilder(getActivity()).setView(view).create();
        return dialog;
    }

    private String textoEntregado(boolean pedidoEntregado){
        return  pedidoEntregado ? getString(R.string.SingleWord_Entregado) : getString(R.string.SingleWord_NoEntregado);
    }

    //Pasa por parámetro el estado del pedido y devuelve un string con el estado (Preparado o no preparado)
    private String textoPreparado(boolean pedidoPreparado){
        return pedidoPreparado ? getString(R.string.SingleWord_Preparado) : getString(R.string.SingleWord_NoEntregado);
    }
}