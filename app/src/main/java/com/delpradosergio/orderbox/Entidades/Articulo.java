package com.delpradosergio.orderbox.Entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "articulos", foreignKeys = @ForeignKey(entity = Pedido.class, parentColumns = "nPedido",
        childColumns = "pedidoPerteneciente", onDelete = ForeignKey.CASCADE), indices = {@Index(value = {"pedidoPerteneciente"})})
public class Articulo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = "codArticulo")
    private long codArticulo;

    @ColumnInfo(name = "pedidoPerteneciente")
    private long nPedidoPerteneciente;

    @ColumnInfo(name = "nombreArticulo")
    private String nombreArticulo;

    @ColumnInfo(name = "cantidad")
    private String cantidad;

    @ColumnInfo(name = "tipoCantidad")
    private String tipoCantidad;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    public Articulo(String nombreArticulo, String cantidad, String tipoCantidad, String descripcion) {
        this.nombreArticulo = nombreArticulo;
        this.cantidad = cantidad;
        this.tipoCantidad = tipoCantidad;
        this.descripcion = descripcion;
    }

    public long getCodArticulo() {
        return codArticulo;
    }

    public void setCodArticulo(long codArticulo) {
        this.codArticulo = codArticulo;
    }

    public void setNPedidoPerteneciente(long nPedidoPerteneciente) {
        this.nPedidoPerteneciente = nPedidoPerteneciente;
    }

    public long getNPedidoPerteneciente() {
        return nPedidoPerteneciente;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public String getCantidadFormateada() {
        return cantidad + " " + tipoCantidad;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoCantidad() {
        return tipoCantidad;
    }

    public void setTipoCantidad(String tipoCantidad) {
        this.tipoCantidad = tipoCantidad;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}


