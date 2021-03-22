package com.delpradosergio.orderbox.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.Pedido;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;
import com.delpradosergio.orderbox.Interfaces.PedidoDAO;

import java.util.List;

public class Repositorio {

    LiveData<List<PedidoConArticulos>> liveDataListaPedidosConArticulos;
    AppDatabase appDatabase;
    PedidoDAO pedidoDAO;

    public Repositorio(Application application){
        appDatabase = AppDatabase.getInstance(application); //Obtener la base de datos
        pedidoDAO = appDatabase.pedidoDAO();
        liveDataListaPedidosConArticulos = pedidoDAO.getPedidoConArticulos();
    }

    public LiveData<List<PedidoConArticulos>> getLiveDataListaPedidosConArticulos() {
        return liveDataListaPedidosConArticulos;
    }

    public void anadirPedidoConArticulos(Pedido pedido, List<Articulo> articulos) {
        AppExecutors.getInstance().diskIO().execute(()->{
            long[] idPedido = pedidoDAO.insertarPedido(pedido); //Solo tiene un elemento: idPedido[0]
            for(Articulo articulo : articulos) {
                articulo.setNPedidoPerteneciente(idPedido[0]);
                pedidoDAO.insertarArticulo(articulo);
            }
        });
    }

    public void borrarPedidoConArticulos(PedidoConArticulos pedidoConArticulos){
        AppExecutors.getInstance().diskIO().execute(()->{
            pedidoDAO.deletePedidoByNPedido(pedidoConArticulos.pedido.getNPedido());
            for(Articulo articulo : pedidoConArticulos.articulos) {
                pedidoDAO.deleteArticuloByCodArticulo(articulo.getCodArticulo());
            }
        });
    }

    public void borrarArticulo(Articulo articulo){
        AppExecutors.getInstance().diskIO().execute(()->{
            pedidoDAO.deleteArticuloByCodArticulo(articulo.getCodArticulo());
        });
    }

    public void borrarTodosPedidos(){
        AppExecutors.getInstance().diskIO().execute(()->{
            pedidoDAO.borrarPedidos();
        });
    }

    public void actualizarPedido(Pedido pedido) {
        AppExecutors.getInstance().diskIO().execute(()->{
            pedidoDAO.updateEntidad(pedido);
        });
    }

}
