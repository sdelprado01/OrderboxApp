package com.delpradosergio.orderbox.Entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidos")
public class Pedido {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "nPedido")
    private long nPedido;

    @ColumnInfo(name = "productoEntregado")
    private boolean entregado;

    @ColumnInfo(name = "productoPreparado")
    private boolean preparado;

    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @ColumnInfo(name = "telefono")
    private long telefono;

    @ColumnInfo(name = "fechaEntrega")
    private String fechaEntrega;

    public Pedido(boolean entregado, boolean preparado, String nombreCliente, long telefono, String fechaEntrega) {
        this.entregado = entregado;
        this.preparado = preparado;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.fechaEntrega = fechaEntrega;
    }

    @Ignore
    public Pedido(long nPedido, boolean entregado, boolean preparado, String nombreCliente, long telefono, String fechaEntrega) {
        this.nPedido = nPedido;
        this.entregado = entregado;
        this.preparado = preparado;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.fechaEntrega = fechaEntrega;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public boolean isPreparado() { return preparado; }

    public void setPreparado(boolean preparado) {
        this.preparado = preparado;
    }

    public long getNPedido() {
        return nPedido;
    }

    public void setNPedido(long nPedido) {
        this.nPedido = nPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

}
