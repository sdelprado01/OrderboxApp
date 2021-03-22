package com.delpradosergio.orderbox.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Interfaces.AdaptadorListaArticulos;
import com.delpradosergio.orderbox.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class AnadirArticulo_Fragment extends Fragment implements AdaptadorListaArticulos.EditableArticulos {

   //private String [] prueba = new String[] {"Lunes", "Martes", "Miercoles"};
    private Button completarArticulos, anadirArticulo;
    private Spinner tipoCantidadSpinner;
    private Articulo miArticulo;
    private AutoCompleteTextView nombreArticulo;
    private TextView cantidad, detalles;
    private ImageButton botonRetroceder;
    private RecyclerView recyclerViewArticulos;
    private AdaptadorListaArticulos adapter;
    private LiveData<List<Articulo>> liveDataListaArticulos;

    private int posArticuloEditado;

    ViewModelCarnitronic viewModel;

    public AnadirArticulo_Fragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posArticuloEditado = (savedInstanceState!=null) ? savedInstanceState.getInt("posArticuloEditado",-1) : -1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("posArticuloEditado",posArticuloEditado);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_anadir_articulo, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelCarnitronic.class);

        nombreArticulo = vista.findViewById(R.id.editTextNombreArticulo);
        //nombreArticulo.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, prueba));
        cantidad = vista.findViewById(R.id.editTextCantidad);
        detalles = vista.findViewById(R.id.editTextDetalles);
        tipoCantidadSpinner = vista.findViewById(R.id.spinner);

        anadirArticulo = vista.findViewById(R.id.buttonAñadirArticuloEnAddArticulos);
        anadirArticulo.setOnClickListener(v->{

            if (comprobarCampos(nombreArticulo, cantidad)==true) {
                detalles.clearFocus();
                if (detalles != null) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(detalles.getWindowToken(), 0);
                    }
                }

                miArticulo = new Articulo(nombreArticulo.getText().toString(), cantidad.getText().toString(),
                        tipoCantidadSpinner.getSelectedItem().toString(), detalles.getText().toString().trim());
                if(posArticuloEditado==-1) {
                    viewModel.anadirArticuloSinConfirmar(miArticulo);
                }
                else { //estamos editando un articulo de la lista
                    viewModel.editarArticuloSinConfirmar(miArticulo,posArticuloEditado);
                }
                limpiarCampos(nombreArticulo, cantidad, detalles);
            } else {
                Toast.makeText(getActivity(), "Rellena todos los campos antes de añadir el artículo", Toast.LENGTH_LONG).show();
            }

        });

        completarArticulos = vista.findViewById(R.id.buttonCompletarArticulo);
        completarArticulos.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        recyclerViewArticulos = vista.findViewById(R.id.recylcerViewDescripcionArticulosAddArticulos);
        recyclerViewArticulos.setLayoutManager(new LinearLayoutManager(getActivity()));
        liveDataListaArticulos = viewModel.getArticulosSinConfirmar();
        liveDataListaArticulos.observe(getViewLifecycleOwner(), listaArticulos -> {
            adapter.setListaArticulos(listaArticulos);
            adapter.notifyDataSetChanged();
        });
        adapter = new AdaptadorListaArticulos(new ArrayList<>(), viewModel, this);
        recyclerViewArticulos.setAdapter(adapter);

        //2. Crear un itemtouch helper y pasarle como parametro el simpleCallback que hemos creado
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //3. enlazar el itemtouchhelper con el recycler view
        itemTouchHelper.attachToRecyclerView(recyclerViewArticulos);

        botonRetroceder = vista.findViewById(R.id.imageButtonRetrocederAddArticulos);
        botonRetroceder.setOnClickListener(v->NavHostFragment.findNavController(this).popBackStack());

        int posicion = AnadirArticulo_FragmentArgs.fromBundle(getArguments()).getPosicion();
        if(posicion != -1) {
            editarArticulos(posicion);
        }

        return vista;
    }

    //Implementar on swipe
    //1. Crear el simple callback habilitado para deslizar derecha e izquierda
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * Permite a las vistas swipe realizar acciones
         * @param viewHolder
         * @param direction
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int posicion = viewHolder.getAdapterPosition();
            Articulo articuloModificado = adapter.getListaArticulos().get(posicion);

            //4. Creamos las acciones para cada una de las posibles acciones de swipe
            switch (direction){
                //Derecha editar
                case ItemTouchHelper.RIGHT:
                    editarArticulos(posicion);
                    break;

                //Izquiera eliminar
                case ItemTouchHelper.LEFT:
                    adapter.getListaArticulos().remove(posicion);
                    adapter.notifyItemRemoved(posicion);
                    Snackbar snackbar = Snackbar.make(recyclerViewArticulos, "Has borrado el articulo", Snackbar.LENGTH_LONG);
                    BaseTransientBottomBar.BaseCallback<Snackbar> callbackSnackbar = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            viewModel.borrarArticulosinConfirmar(articuloModificado);
                        }
                    };
                    snackbar.addCallback(callbackSnackbar);
                    snackbar.setAction("Deshacer", v -> {
                        adapter.getListaArticulos().add(posicion, articuloModificado);
                        adapter.notifyItemInserted(posicion);
                        snackbar.removeCallback(callbackSnackbar);
                    }).show();
                    break;
            }
        }

        /**
         * Da formato a las vistas swipe de los artículos
         *
         * @param c
         * @param recyclerView
         * @param viewHolder
         * @param dX
         * @param dY
         * @param actionState
         * @param isCurrentlyActive
         */
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getActivity(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightActionIcon(R.drawable.ic_create)
                    .addSwipeRightLabel("Editar artículo")
                    .addSwipeLeftLabel("Eliminar artículo")
                    /*
                    .setSwipeLeftLabelColor(getColor(R.color.colorWhite))
                    .addSwipeLeftBackgroundColor(getColor(R.color.colorRedDark))
                    .addSwipeRightBackgroundColor(getColor(R.color.lightGreen))
                     */
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

     //Coger el color en si y no el ID
    private int getColor(int colorID) {
        return ContextCompat.getColor(requireContext(),colorID);
    }

    /**
     * Comprueba que los campos del formulario estén rellenados y si lo estan permite añadir el nuevo artículo
     * @param nombreArticulo EditText del nombre del artículo
     * @param cantidad EditText con la cantidad deseada
     * @return True si los campos están rellenos y false si no
     */
    public boolean comprobarCampos(TextView nombreArticulo, TextView cantidad){
        if(nombreArticulo.getText().toString().isEmpty() || cantidad.getText().toString().isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Limpia los campos del formulario
     * @param textViews
     */
    public void limpiarCampos(TextView ... textViews){
        for(TextView textView : textViews) {
            textView.setText("");
        }
        posArticuloEditado = -1;
    }


    /**
     * Edita el artículo que se le pase por parámetro y se actualiza la lista de articulos actuales
     * @param posicion Posición del artículo sin confirmar que se quiere editar
     */
    @Override
    public void editarArticulos(int posicion) {
        Articulo articuloModificado = viewModel.getArticulosSinConfirmar().getValue().get(posicion);
        posArticuloEditado = posicion;
        nombreArticulo.setText(articuloModificado.getNombreArticulo());
        cantidad.setText(articuloModificado.getCantidad());
        String[] opcionesSpinner = getResources().getStringArray(R.array.tipo_cantidad_articulo);
        int tipoCantidad = 0;
        for (int i = 0; i < opcionesSpinner.length; i++) {
            if(opcionesSpinner[i].equals(articuloModificado.getTipoCantidad())) {
                tipoCantidad = i;
            }
        }
        tipoCantidadSpinner.setSelection(tipoCantidad,true);
        detalles.setText(articuloModificado.getDescripcion());

        adapter.notifyItemChanged(posicion);
    }

}
