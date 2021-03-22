package com.delpradosergio.orderbox.Fragments;



import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.Pedido;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;
import com.delpradosergio.orderbox.Interfaces.AdaptadorListaArticulos;
import com.delpradosergio.orderbox.R;

import java.util.ArrayList;
import java.util.List;


public class AnadirPedido_Fragment extends Fragment implements AdaptadorListaArticulos.EditableArticulos {

    Button addPedidoButton;
    EditText nombreCliente, telefono, fechaEntrega;
    ViewModelCarnitronic miViewModel;
    ImageButton botonRetroceder, addArticuloButton;
    TextView articulosDelPedido, titulo;

    LiveData<List<Articulo>> liveDataListaArticulos;

    AdaptadorListaArticulos adapter;
    RecyclerView recyclerViewDescripcion;
    PedidoConArticulos pedidoParaEditar;

    private boolean primeraVez;

    public AnadirPedido_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        primeraVez = (savedInstanceState == null) || savedInstanceState.getBoolean("primeraVez",true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("primeraVez", primeraVez);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        NavDestination currentDestination = NavHostFragment.findNavController(this).getCurrentDestination();
        if(currentDestination==null || currentDestination.getId()!=R.id.addPedidos) {
            miViewModel.descartarPedidoYArticulosSinConfirmar();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        miViewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);

        View vista = inflater.inflate(R.layout.fragment_anadir_pedido, container, false);
        addPedidoButton = vista.findViewById(R.id.buttonAddPedido);
        addArticuloButton = vista.findViewById(R.id.imageButtonAñadirArticulos);
        nombreCliente = vista.findViewById(R.id.editTextNombre);
        telefono = vista.findViewById(R.id.editTextTelefono);
        fechaEntrega = vista.findViewById(R.id.editTextFecha);
        recyclerViewDescripcion = vista.findViewById(R.id.recyclerViewDescrpcionPedidos);
        articulosDelPedido = vista.findViewById(R.id.textViewArticulosDelPedidoAddPedidos);
        botonRetroceder = vista.findViewById(R.id.imageButtonRetrocederAddPedidos);
        titulo = vista.findViewById(R.id.textViewPedidoAddPedido);

        titulo.setText(R.string.anadirPedido_Titulo);
        addPedidoButton.setText(R.string.anadirPedido_CompletarPedido);
        addArticuloButton.setImageDrawable(getImage(R.drawable.ic_add));

        recyclerViewDescripcion.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new AdaptadorListaArticulos(new ArrayList<>(), miViewModel,this);
        recyclerViewDescripcion.setAdapter(adapter);
        liveDataListaArticulos = miViewModel.getArticulosSinConfirmar();
        liveDataListaArticulos.observe(getViewLifecycleOwner(), listaArticulos -> {
            adapter.setListaArticulos(listaArticulos);
            adapter.notifyDataSetChanged();
        });

        long numPedido = AnadirPedido_FragmentArgs.fromBundle(requireArguments()).getNumPedido();
        if(numPedido!=0 && primeraVez==true) {

            pedidoParaEditar = miViewModel.getPedidoConArticulo(numPedido);
            if(pedidoParaEditar==null) throw new IllegalStateException("Numero de pedido no valido");

            titulo.setText(R.string.anadirPedido_EditarPedido);
            nombreCliente.setText(pedidoParaEditar.pedido.getNombreCliente());
            telefono.setText(""+pedidoParaEditar.pedido.getTelefono());
            fechaEntrega.setText(pedidoParaEditar.pedido.getFechaEntrega());
            miViewModel.anadirListaArticulosSinConfirmar(pedidoParaEditar.articulos);
        }

        primeraVez = false;

        addPedidoButton.setOnClickListener(v -> {
            if(comprobarCampos(nombreCliente, telefono, fechaEntrega)==true){
                //Crear Pedido con Articulos
                Pedido pedido = new Pedido(numPedido, false, false, nombreCliente.getText().toString(), Long.parseLong(telefono.getText().toString()), fechaEntrega.getText().toString());

                miViewModel.setPedidoSinConfirmar(pedido);
                NavHostFragment.findNavController(this).navigate(R.id.checkPedido);
            } else {
                Toast.makeText(getActivity(), "Rellena todos los campos antes de completar el pedido", Toast.LENGTH_LONG).show();
            }
        });


        fechaEntrega.setOnClickListener(v -> {
            fechaEntrega.clearFocus();
            if (fechaEntrega != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(fechaEntrega.getWindowToken(), 0);
                }
            }
            showDatePickerDialog(fechaEntrega);
        });

        addArticuloButton.setOnClickListener(v->{
            quitarTeclado(nombreCliente);
            NavHostFragment.findNavController(this).navigate(R.id.action_addPedidos_to_addArticulo);
        });

        articulosDelPedido.setOnClickListener(v -> {
            quitarTeclado(nombreCliente);
            NavHostFragment.findNavController(this).navigate(R.id.action_addPedidos_to_addArticulo);
        });

        botonRetroceder.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        return vista;
    }

    public void quitarTeclado(EditText nombreCliente){
        nombreCliente.clearFocus();
        if (nombreCliente != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(nombreCliente.getWindowToken(), 0);
            }
        }
    }


    public boolean comprobarCampos(EditText nombre, EditText telefono, EditText fechaEntrega){

        if(nombre.getText().toString().isEmpty() || telefono.getText().toString().isEmpty() || fechaEntrega.getText().toString().isEmpty()
                || miViewModel.getArticulosSinConfirmar().getValue().isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    //Coger el color en si y no el ID
    private Drawable getImage(int imageID) {
        return ContextCompat.getDrawable(requireContext(),imageID);
    }


    @Override
    public void editarArticulos(int posicion) {
        AnadirPedido_FragmentDirections.ActionAddPedidosToAddArticulo action =
                AnadirPedido_FragmentDirections.actionAddPedidosToAddArticulo();
        action.setPosicion(posicion);
        NavHostFragment.findNavController(this).navigate(action);
    }


}


