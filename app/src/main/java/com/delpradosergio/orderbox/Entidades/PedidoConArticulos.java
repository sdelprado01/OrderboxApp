package com.delpradosergio.orderbox.Entidades;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PedidoConArticulos {
    @Embedded
    public Pedido pedido;
    @Relation(
            parentColumn = "nPedido",
            entityColumn = "pedidoPerteneciente"
    )
    public List<Articulo> articulos;
}
