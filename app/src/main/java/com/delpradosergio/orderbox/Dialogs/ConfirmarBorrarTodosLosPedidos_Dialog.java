package com.delpradosergio.orderbox.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;


public class ConfirmarBorrarTodosLosPedidos_Dialog extends DialogFragment {


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirmar_borrar_todos_los_pedidos, null, false);

        Button botonConfirmar = view.findViewById(R.id.buttonConfirmarBorrado);
        Button botonCancelar = view.findViewById(R.id.buttonCancelarBorrado);
        ViewModelCarnitronic viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);

        botonConfirmar.setOnClickListener(v->{
            viewModel.borrarTodosPedidos();
            NavHostFragment.findNavController(this).popBackStack();
            Toast.makeText(getActivity(), "Se han borrado todos los pedidos", Toast.LENGTH_LONG).show();
        });

        botonCancelar.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        AlertDialog dialog = new MaterialAlertDialogBuilder(getActivity()).setView(view).create();
        return dialog;
    }
}