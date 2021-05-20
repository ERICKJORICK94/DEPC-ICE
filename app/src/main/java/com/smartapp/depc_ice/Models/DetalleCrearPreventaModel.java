package com.smartapp.depc_ice.Models;

public class DetalleCrearPreventaModel {

    private String cantidad = "";
    private String precio = "";
    private String descuento = "";
    private String bodega_id = "";
    private String codigo_item = "";
    private String descripcion = "";
    private String costo = "";
    private String codigo_unidad = "";

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getBodega_id() {
        return bodega_id;
    }

    public void setBodega_id(String bodega_id) {
        this.bodega_id = bodega_id;
    }

    public String getCodigo_item() {
        return codigo_item;
    }

    public void setCodigo_item(String codigo_item) {
        this.codigo_item = codigo_item;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getCodigo_unidad() {
        return codigo_unidad;
    }

    public void setCodigo_unidad(String codigo_unidad) {
        this.codigo_unidad = codigo_unidad;
    }
}
