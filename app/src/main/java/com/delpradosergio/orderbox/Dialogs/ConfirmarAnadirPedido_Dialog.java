package com.delpradosergio.orderbox.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.Pedido;
import com.delpradosergio.orderbox.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class ConfirmarAnadirPedido_Dialog extends DialogFragment {

    Button botonAceptar;
    Button botonCancelar;

    ViewModelCarnitronic viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirmar_anadir_pedido, null, false);

        botonAceptar = view.findViewById(R.id.botonAceptar);
        botonCancelar = view.findViewById(R.id.buttonCancelar);

        Pedido pedidoSinConfirmar = viewModel.getPedidoSinConfirmar();

        TextView confirmacionPedido = view.findViewById(R.id.textViewConfirmacionPedido);
        String s = String.format(getString(R.string.Message_ConfirmarAnadirPedido),pedidoSinConfirmar.getNombreCliente(),pedidoSinConfirmar.getFechaEntrega());
        confirmacionPedido.setText(Html.fromHtml(s));

        botonAceptar.setOnClickListener(v -> {
            viewModel.anadirPedidoConArticulos();
            viewModel.descartarPedidoYArticulosSinConfirmar();
            NavHostFragment.findNavController(this).navigate(R.id.action_checkPedido_to_mainFragment);
            Toast.makeText(getActivity(), "Pedido añadido con éxito", Toast.LENGTH_LONG).show();
        });

        botonCancelar.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        AlertDialog dialog = new MaterialAlertDialogBuilder(getActivity()).setView(view).create();
        return dialog;
    }


}
