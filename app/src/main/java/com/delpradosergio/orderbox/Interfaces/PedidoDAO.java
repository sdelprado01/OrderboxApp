package com.delpradosergio.orderbox.Interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.Pedido;
import com.delpradosergio.orderbox.Entidades.PedidoConArticulos;

import java.util.List;

@Dao
public interface PedidoDAO {
    //DECLARACION DE OPERACIONES CRUD PARA LA BASE DE DATOS

    //Contar el numero de pedidos existentes
    @Query("SELECT COUNT (*) FROM pedidos")
    int countPedidos();

    //Insertar pedido
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertarPedido (Pedido ... pedidos);

    //Insertar articulo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertarArticulo (Articulo ... articulos);


    //Eliminar
    @Query("DELETE FROM pedidos WHERE nPedido = :nPedido")
    void deletePedidoByNPedido(long nPedido);

    @Query("DELETE FROM articulos WHERE codArticulo = :codArticulo")
    void deleteArticuloByCodArticulo(long codArticulo);

    //Actualizar
    @Update
    int updateEntidad(Pedido obj);

    @Transaction
    @Query("SELECT * FROM pedidos")
    LiveData<List<PedidoConArticulos>> getPedidoConArticulos();

    @Query("DELETE FROM pedidos")
    void borrarPedidos();

    @Query("SELECT nombreArticulo FROM articulos")
    String[] recogerNombreArticulos();

}
