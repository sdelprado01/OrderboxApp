package com.delpradosergio.orderbox.Interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delpradosergio.orderbox.Database.ViewModelCarnitronic;
import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AdaptadorListaArticulos extends RecyclerView.Adapter<AdaptadorListaArticulos.ViewHolderDescripcionPedidos>{

    private List<Articulo> listaArticulos;
    private EditableArticulos editableArticulos;

    public AdaptadorListaArticulos(List<Articulo> listaArticulos, ViewModelCarnitronic viewModel, EditableArticulos editableArticulos){
        this.editableArticulos = editableArticulos;
        this.listaArticulos = listaArticulos;
        this.viewModel = viewModel;
    }

    public void setListaArticulos(List<Articulo> listaArticulos){
        this.listaArticulos = listaArticulos;
    }

    public List<Articulo> getListaArticulos() {
        return listaArticulos;
    }

    private ViewModelCarnitronic viewModel;

    @NonNull
    @Override
    public ViewHolderDescripcionPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_lista_articulos, parent, false);

        return new ViewHolderDescripcionPedidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDescripcionPedidos holder, int position) {

        Articulo articuloModificado = listaArticulos.get(position);

        holder.nombreArticulo.setText(listaArticulos.get(position).getNombreArticulo());
        holder.cantidad.setText(listaArticulos.get(position).getCantidadFormateada());
        holder.descripcionArticulo.setText(listaArticulos.get(position).getDescripcion());

        holder.botonBorarArticulo.setOnClickListener(v -> {
            listaArticulos.remove(position);
            notifyItemRemoved(position);

            Snackbar snackbar = Snackbar.make(v, R.string.Message_ArticuloBorrado, Snackbar.LENGTH_LONG);
            BaseTransientBottomBar.BaseCallback<Snackbar> callbackSnackbar = new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    viewModel.borrarArticulosinConfirmar(articuloModificado);
                }
            };
            snackbar.addCallback(callbackSnackbar);
            snackbar.setAction("Deshacer", e -> {
                getListaArticulos().add(position, articuloModificado);
                notifyItemInserted(position);
                snackbar.removeCallback(callbackSnackbar);
            }).show();
        });

        holder.botonEditarArticulo.setOnClickListener(v -> {
            editableArticulos.editarArticulos(position);
        });

    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ViewHolderDescripcionPedidos extends RecyclerView.ViewHolder {

        TextView nombreArticulo, cantidad, descripcionArticulo;
        MaterialCardView tarjeta;
        ImageButton botonEditarArticulo, botonBorarArticulo;

        public ViewHolderDescripcionPedidos(@NonNull View itemView) {
            super(itemView);
            nombreArticulo = itemView.findViewById(R.id.textViewNombreArticulo_descripcion);
            cantidad = itemView.findViewById(R.id.textViewCantidadPedido_descripcion);
            descripcionArticulo = itemView.findViewById(R.id.textViewDescripcionArticulo_descripcion);
            botonBorarArticulo = itemView.findViewById(R.id.imageButtonBorrarArticulo);
            botonEditarArticulo = itemView.findViewById(R.id.imageButtonEditarArticulo);
        }
    }


    public interface EditableArticulos {
        void editarArticulos(int posicion);
    }
}
