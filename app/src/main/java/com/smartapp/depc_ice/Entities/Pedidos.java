package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.io.Serializable;

@DatabaseTable(tableName = Const.TABLE_PEDIDO)
public class Pedidos implements Serializable {

    @DatabaseField(generatedId = true,allowGeneratedIdInsert=true)
    private int id;
    @DatabaseField
    private String Bodega;
    @DatabaseField
    private String NombreBodega;
    @DatabaseField
    private String Cliente;
    @DatabaseField
    private String NombreCliente;
    @DatabaseField
    private String RucCliente;
    @DatabaseField
    private String DireccionCliente;
    @DatabaseField
    private String DireccionEntrega;
    @DatabaseField
    private String TelefonoCliente;
    @DatabaseField
    private String Fecha;
    @DatabaseField
    private String Vendedor;
    @DatabaseField
    private String NombreVendedor;
    @DatabaseField
    private String Comentario;
    @DatabaseField
    private String Subtotal;
    @DatabaseField
    private String Base0;
    @DatabaseField
    private String Base12;
    @DatabaseField
    private String Descuento;
    @DatabaseField
    private String Iva;
    @DatabaseField
    private String Total;
    @DatabaseField
    private String codigoPedido;
    @DatabaseField
    private String codigo_direccione_entrega;
    @DatabaseField
    private String PorcentajeDescuentoAdi;
    @DatabaseField
    private String DescuentoAdicional;
    @DatabaseField
    private String estadoPedido = "0";
    @DatabaseField
    private String forma_pago;
    @DatabaseField
    private String forma_pago_id;
    @DatabaseField
    private String dias_credito;
    @DatabaseField
    private String cuenta_id;
    @DatabaseField
    private String foto;
    @DatabaseField
    private String id_congelador;
    @DatabaseField
    private String id_putto_vta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBodega() {
        return Bodega;
    }

    public void setBodega(String bodega) {
        Bodega = bodega;
    }

    public String getNombreBodega() {
        return NombreBodega;
    }

    public void setNombreBodega(String nombreBodega) {
        NombreBodega = nombreBodega;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        NombreCliente = nombreCliente;
    }

    public String getRucCliente() {
        return RucCliente;
    }

    public void setRucCliente(String rucCliente) {
        RucCliente = rucCliente;
    }

    public String getDireccionCliente() {
        return DireccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        DireccionCliente = direccionCliente;
    }

    public String getDireccionEntrega() {
        return DireccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        DireccionEntrega = direccionEntrega;
    }

    public String getTelefonoCliente() {
        return TelefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        TelefonoCliente = telefonoCliente;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getVendedor() {
        return Vendedor;
    }

    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }

    public String getNombreVendedor() {
        return NombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        NombreVendedor = nombreVendedor;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String subtotal) {
        Subtotal = subtotal;
    }

    public String getBase0() {
        return Base0;
    }

    public void setBase0(String base0) {
        Base0 = base0;
    }

    public String getBase12() {
        return Base12;
    }

    public void setBase12(String base12) {
        Base12 = base12;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
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

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getCodigo_direccione_entrega() {
        return codigo_direccione_entrega;
    }

    public void setCodigo_direccione_entrega(String codigo_direccione_entrega) {
        this.codigo_direccione_entrega = codigo_direccione_entrega;
    }

    public String getPorcentajeDescuentoAdi() {
        return PorcentajeDescuentoAdi;
    }

    public void setPorcentajeDescuentoAdi(String porcentajeDescuentoAdi) {
        PorcentajeDescuentoAdi = porcentajeDescuentoAdi;
    }

    public String getDescuentoAdicional() {
        return DescuentoAdicional;
    }

    public void setDescuentoAdicional(String descuentoAdicional) {
        DescuentoAdicional = descuentoAdicional;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getForma_pago_id() {
        return forma_pago_id;
    }

    public void setForma_pago_id(String forma_pago_id) {
        this.forma_pago_id = forma_pago_id;
    }

    public String getDias_credito() {
        return dias_credito;
    }

    public void setDias_credito(String dias_credito) {
        this.dias_credito = dias_credito;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) {
        this.cuenta_id = cuenta_id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    public String getId_congelador() {
        return id_congelador;
    }

    public void setId_congelador(String id_congelador) {
        this.id_congelador = id_congelador;
    }

    public String getId_putto_vta() {
        return id_putto_vta;
    }

    public void setId_putto_vta(String id_putto_vta) {
        this.id_putto_vta = id_putto_vta;
    }
}
