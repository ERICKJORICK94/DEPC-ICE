package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_DETALLE_PEDIDO)
public class DetallePedido {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String idPedido;
    @DatabaseField
    private String NumeroItem;
    @DatabaseField
    private String Codigo;
    @DatabaseField
    private String Descripcion;
    @DatabaseField
    private String TipoInventario;
    @DatabaseField
    private String Cantidad;
    @DatabaseField
    private String Costo;
    @DatabaseField
    private String PrecioUnitario;
    @DatabaseField
    private String Subtotal;
    @DatabaseField
    private String PorcentajeDescuento;
    @DatabaseField
    private String Descuento;
    @DatabaseField
    private String PorcentajeDescuentoAdicional;
    @DatabaseField
    private String DescuentoAdicional;
    @DatabaseField
    private String Neto;
    @DatabaseField
    private String PorcentajeIva;
    @DatabaseField
    private String Iva;
    @DatabaseField
    private String Total;
    @DatabaseField
    private String Promocion;
    @DatabaseField
    private String subtotalNeto;

    @DatabaseField
    private String subtotalNetoFijo;

    @DatabaseField
    private int idDetallePromocionCantidad;

    @DatabaseField
    private String SECPRO;


    public String getSubtotalNetoFijo() {
        return subtotalNetoFijo;
    }

    public void setSubtotalNetoFijo(String subtotalNetoFijo) {
        this.subtotalNetoFijo = subtotalNetoFijo;
    }

    public String getNumeroItem() {
        return NumeroItem;
    }

    public void setNumeroItem(String numeroItem) {
        NumeroItem = numeroItem;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTipoInventario() {
        return TipoInventario;
    }

    public void setTipoInventario(String tipoInventario) {
        TipoInventario = tipoInventario;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getCosto() {
        return Costo;
    }

    public void setCosto(String costo) {
        Costo = costo;
    }

    public String getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        PrecioUnitario = precioUnitario;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String subtotal) {
        Subtotal = subtotal;
    }

    public String getPorcentajeDescuento() {
        return PorcentajeDescuento;
    }

    public void setPorcentajeDescuento(String porcentajeDescuento) {
        PorcentajeDescuento = porcentajeDescuento;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getPorcentajeDescuentoAdicional() {
        return PorcentajeDescuentoAdicional;
    }

    public void setPorcentajeDescuentoAdicional(String porcentajeDescuentoAdicional) {
        PorcentajeDescuentoAdicional = porcentajeDescuentoAdicional;
    }

    public String getDescuentoAdicional() {
        return DescuentoAdicional;
    }

    public void setDescuentoAdicional(String descuentoAdicional) {
        DescuentoAdicional = descuentoAdicional;
    }

    public String getNeto() {
        return Neto;
    }

    public void setNeto(String neto) {
        Neto = neto;
    }

    public String getPorcentajeIva() {
        return PorcentajeIva;
    }

    public void setPorcentajeIva(String porcentajeIva) {
        PorcentajeIva = porcentajeIva;
    }

    public String getIva() {
        return Iva;
    }

    public void setIva(String iva) {
        Iva = iva;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getPromocion() {
        return Promocion;
    }

    public void setPromocion(String promocion) {
        Promocion = promocion;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubtotalNeto() {
        return subtotalNeto;
    }

    public void setSubtotalNeto(String subtotalNeto) {
        this.subtotalNeto = subtotalNeto;
    }


    public String getSECPRO() {
        return SECPRO;
    }

    public void setSECPRO(String SECPRO) {
        this.SECPRO = SECPRO;
    }

    public int getIdDetallePromocionCantidad() {
        return idDetallePromocionCantidad;
    }

    public void setIdDetallePromocionCantidad(int idDetallePromocionCantidad) {
        this.idDetallePromocionCantidad = idDetallePromocionCantidad;
    }
}
