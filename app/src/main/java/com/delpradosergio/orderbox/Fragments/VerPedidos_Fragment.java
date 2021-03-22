package com.delpradosergio.orderbox.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;
import com.delpradosergio.orderbox.R;
import com.delpradosergio.orderbox.Interfaces.AdaptadorListaPedidos;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class VerPedidos_Fragment extends Fragment {

    ViewModelCarnitronic viewModel;
    LiveData<List<PedidoConArticulos>> liveDataListaPedidos;
    RecyclerView recyclerViewPedidos;
    AdaptadorListaPedidos adapter;
    ImageButton botonBusquedaPorFecha, botonLimpiarBusqueda, botonRetroceder;
    EditText editTextBusquedas;

    public VerPedidos_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ver_pedidos, container, false);

        editTextBusquedas = view.findViewById(R.id.editTextBusquedas);
        recyclerViewPedidos = view.findViewById(R.id.RecyclerIDVerPedidos);

        //Llenamos el recycler view con los datos
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);
        liveDataListaPedidos = viewModel.getLiveDataPedidosConArticulos();

        liveDataListaPedidos.observe(getViewLifecycleOwner(), new Observer<List<PedidoConArticulos>>() {
            @Override
            public void onChanged(List<PedidoConArticulos> pedidoConArticulos) {
                simpleCallback.setListaPedidos(pedidoConArticulos);
                adapter.setListaPedidos(pedidoConArticulos);
                adapter.notifyDataSetChanged();
            }
        });

        //Enviamos al Adaptador ver pedidos el Arraylist que hemos generado en el metodo llenar ver pedidos
        adapter = new AdaptadorListaPedidos(new ArrayList<>());
        //Luego establecemos el adaptador del recycler view con el adapter que acabamos de rellenar
        recyclerViewPedidos.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewPedidos);

        //Seleccion de fecha al pulsar el boton de busqueda
        botonBusquedaPorFecha = view.findViewById(R.id.imageButtonBusquedaPorFecha);
        botonLimpiarBusqueda = view.findViewById(R.id.imageButtonRetroceder);
        botonBusquedaPorFecha.setOnClickListener(v -> {
            editTextBusquedas.clearFocus();
            if (editTextBusquedas != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(editTextBusquedas.getWindowToken(), 0);
                }
            }
            showDatePickerDialog();
        });

        botonLimpiarBusqueda.setOnClickListener(v ->{
            editTextBusquedas.setText("");
            //Ocultar teclado
            editTextBusquedas.clearFocus();
            if (editTextBusquedas != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(editTextBusquedas.getWindowToken(), 0);
                }
            }
            liveDataListaPedidos.observe(getViewLifecycleOwner(), new Observer<List<PedidoConArticulos>>() {
                @Override
                public void onChanged(List<PedidoConArticulos> pedidoConArticulos) {
                    simpleCallback.setListaPedidos(pedidoConArticulos);
                    adapter.setListaPedidos(pedidoConArticulos);
                    adapter.notifyDataSetChanged();
                }
            });
        });


        editTextBusquedas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<PedidoConArticulos> pedidoBuscado = viewModel.busquedaPedidoConArticuloPorNombre(s.toString());
                liveDataListaPedidos.removeObservers(getViewLifecycleOwner());
                simpleCallback.setListaPedidos(pedidoBuscado);
                adapter.setListaPedidos(pedidoBuscado);
                adapter.notifyDataSetChanged();
            }
        });

        botonRetroceder = view.findViewById(R.id.imageViewBackVerPedido);
        botonRetroceder.setOnClickListener(v-> NavHostFragment.findNavController(this).popBackStack());

        return view;
    }




    //Metodo de configuracion del evento Swipe de las tarjetas
    private ItemTouchHelperNew simpleCallback = new ItemTouchHelperNew(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

    class ItemTouchHelperNew extends ItemTouchHelper.SimpleCallback {

        public ItemTouchHelperNew(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        List<PedidoConArticulos> listaPedidos;
        public void setListaPedidos(List<PedidoConArticulos> listaPedidos) {
            this.listaPedidos = listaPedidos;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    PedidoConArticulos pedidoEntregado = listaPedidos.get(position);
                    if(pedidoEntregado.pedido.isEntregado()){
                        pedidoEntregado.pedido.setEntregado(false);
                    }else{
                        pedidoEntregado.pedido.setEntregado(true);
                    }
                    adapter.notifyItemChanged(position);
                    Snackbar.make(recyclerViewPedidos, "Pedido marcado como " + textoEntregado(pedidoEntregado.pedido.isEntregado()),
                            BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(pedidoEntregado.pedido.isEntregado()){
                                        pedidoEntregado.pedido.setEntregado(false);
                                    }else{
                                        pedidoEntregado.pedido.setEntregado(true);
                                    }
                                    adapter.notifyItemChanged(position);
                                    Toast.makeText(getActivity(), "Has deshecho la acción", Toast.LENGTH_LONG).show();
                                }
                            }).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    //Cambiar el estado de completado en la posicion del pedido al que se haya hecho swipe
                    PedidoConArticulos pedidoPreparado = listaPedidos.get(position);
                    if(pedidoPreparado.pedido.isPreparado()){
                        pedidoPreparado.pedido.setPreparado(false);
                    }else{
                        pedidoPreparado.pedido.setPreparado(true);
                    }
                    adapter.notifyItemChanged(position);
                    Snackbar.make(recyclerViewPedidos, "Pedido marcado como " + textoPreparado(pedidoPreparado.pedido.isPreparado()),
                            BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(pedidoPreparado.pedido.isPreparado()){
                                        pedidoPreparado.pedido.setPreparado(false);
                                    }else{
                                        pedidoPreparado.pedido.setPreparado(true);
                                    }
                                    adapter.notifyItemChanged(position);
                                    Toast.makeText(getActivity(), "Has deshecho la acción", Toast.LENGTH_LONG).show();
                                }
                            }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_done_all)
                    .addSwipeRightActionIcon(R.drawable.ic_done_24px)
                    .addSwipeRightLabel("Marcar como preparado")
                    .addSwipeLeftLabel("Marcar como entregado")
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    //Calendario en el SearchView
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                editTextBusquedas.setText(selectedDate);
                List<PedidoConArticulos> pedidoBuscado = viewModel.busquedaPedidoConArticuloPorFecha(selectedDate);
                liveDataListaPedidos.removeObservers(getViewLifecycleOwner());
                simpleCallback.setListaPedidos(pedidoBuscado);
                adapter.setListaPedidos(pedidoBuscado);
                adapter.notifyDataSetChanged();
            }
        });

        newFragment.show(getParentFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private String textoEntregado(boolean pedidoEntregado){
        if (pedidoEntregado == true){
            return "entregado";
        } else {
            return "no entregado";
        }
    }

    private String textoPreparado(boolean pedidoPreparado){
        if (pedidoPreparado==true){
            return "preparado";
        } else {
            return "no preparado";
        }
    }

}
