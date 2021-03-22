package com.delpradosergio.orderbox.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.Pedido;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;

import java.util.ArrayList;
import java.util.List;

public class ViewModelCarnitronic extends AndroidViewModel {

    private Repositorio repositorio;
    private List<PedidoConArticulos> listaPedidos;
    private MutableLiveData<List<Articulo>> articulosPedidoSinConfirmar;
    private Pedido pedidoSinConfirmar;

    public ViewModelCarnitronic(@NonNull Application application) {
        super(application);
        this.repositorio = new Repositorio(application);
        this.pedidoSinConfirmar = null;
        this.articulosPedidoSinConfirmar = new MutableLiveData<>(new ArrayList<>());
    }

    //Viene de la base de datos
    public LiveData<List<PedidoConArticulos>> getLiveDataPedidosConArticulos() {
        return repositorio.getLiveDataListaPedidosConArticulos();
    }

    public PedidoConArticulos getPedidoConArticulo(long id) {
        for(PedidoConArticulos pedidoConArticulos : repositorio.getLiveDataListaPedidosConArticulos().getValue()) {
            if(pedidoConArticulos.pedido.getNPedido()==id) {
                return pedidoConArticulos;
            }
        }
        return null;
    }

    public void anadirPedidoConArticulos(){
        repositorio.anadirPedidoConArticulos(pedidoSinConfirmar, articulosPedidoSinConfirmar.getValue());
    }

    public Pedido getPedidoSinConfirmar() {
        return pedidoSinConfirmar;
    }

    public void descartarPedidoYArticulosSinConfirmar() {
        pedidoSinConfirmar = null;
        articulosPedidoSinConfirmar.setValue(new ArrayList<>());
    }

    public void setPedidoSinConfirmar(Pedido pedidoSinConfirmar) {
        this.pedidoSinConfirmar = pedidoSinConfirmar;
    }

    public void anadirArticuloSinConfirmar(Articulo articulo, int pos) {
        this.articulosPedidoSinConfirmar.getValue().add(pos, articulo);
        this.articulosPedidoSinConfirmar.setValue(this.articulosPedidoSinConfirmar.getValue());
    }

    public void anadirArticuloSinConfirmar(Articulo articulo) {
        this.articulosPedidoSinConfirmar.getValue().add(articulo);
        this.articulosPedidoSinConfirmar.setValue(this.articulosPedidoSinConfirmar.getValue());
    }

    public void editarArticuloSinConfirmar(Articulo articulo, int pos) {
        this.articulosPedidoSinConfirmar.getValue().remove(pos);
        this.articulosPedidoSinConfirmar.getValue().add(pos,articulo);
        this.articulosPedidoSinConfirmar.setValue(this.articulosPedidoSinConfirmar.getValue());
    }

    public void anadirListaArticulosSinConfirmar(List<Articulo> lista) {
        this.articulosPedidoSinConfirmar.getValue().addAll(lista);
        this.articulosPedidoSinConfirmar.setValue(this.articulosPedidoSinConfirmar.getValue());
    }

    public LiveData<List<Articulo>> getArticulosSinConfirmar() {
        return articulosPedidoSinConfirmar;
    }

    public List<PedidoConArticulos> busquedaPedidoConArticuloPorFecha(String fecha) {
        List<PedidoConArticulos> encontradosPorFecha = new ArrayList<>();
        for(PedidoConArticulos pedidoConArticulos : repositorio.getLiveDataListaPedidosConArticulos().getValue()) {
            if(pedidoConArticulos.pedido.getFechaEntrega().equals(fecha)) {
                encontradosPorFecha.add(pedidoConArticulos);
            }
        }
        return encontradosPorFecha;
    }

    public List<PedidoConArticulos> busquedaPedidoConArticuloPorNombre(String nombreCliente) {
        List<PedidoConArticulos> encontradosPorNombre = new ArrayList<>();
        for(PedidoConArticulos pedidoConArticulos : repositorio.getLiveDataListaPedidosConArticulos().getValue()) {
            if(pedidoConArticulos.pedido.getNombreCliente().toLowerCase().contains(nombreCliente.toLowerCase())) {
                encontradosPorNombre.add(pedidoConArticulos);
            }
        }
        return encontradosPorNombre;
    }

    public void borrarPedidoConArticulos (PedidoConArticulos pedidoConArticulos){
        repositorio.borrarPedidoConArticulos(pedidoConArticulos);
    }


    public void borrarArticulosinConfirmar(Articulo articulo){
        repositorio.borrarArticulo(articulo);
    }

    public void borrarTodosPedidos(){
        repositorio.borrarTodosPedidos();
    }

    public void actualizarPedido(Pedido pedido) {
        repositorio.actualizarPedido(pedido);
    }



}
