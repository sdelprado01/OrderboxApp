package com.delpradosergio.orderbox.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;

import com.delpradosergio.orderbox.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmarBorrarPedido_Dialog extends DialogFragment {

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        long numPedido = ConfirmarBorrarPedido_DialogArgs.fromBundle(getArguments()).getNumPedido();

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirmar_borrar_pedido, null, false);
        ViewModelCarnitronic viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);
        PedidoConArticulos pedidoConArticulos = viewModel.getPedidoConArticulo(numPedido);

        Button botonConfirmar = view.findViewById(R.id.buttonConfirmarBorrado);
        Button botonCancelar = view.findViewById(R.id.buttonCancelarBorrado);

        botonConfirmar.setOnClickListener(v->{
                viewModel.borrarPedidoConArticulos(pedidoConArticulos);
                NavHostFragment.findNavController(this).popBackStack();
                Toast.makeText(getActivity(), "Pedido borrado con éxito", Toast.LENGTH_LONG).show();
        });

        botonCancelar.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());


        AlertDialog dialog = new MaterialAlertDialogBuilder(requireActivity()).setView(view).create();
        return dialog;
    }
}