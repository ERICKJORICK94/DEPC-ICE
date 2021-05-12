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
    private String Campana;
    @DatabaseField
    private String Vendedor;
    @DatabaseField
    private String NombreVendedor;
    @DatabaseField
    private String FormaPagoCliente;
    @DatabaseField
    private String NombreFormaPagoCliente;
    @DatabaseField
    private String Transportista;
    @DatabaseField
    private String NombreTransportista;
    @DatabaseField
    protected String FormaPago;
    @DatabaseField
    private String Comentario;
    @DatabaseField
    private String Consignacion;
    @DatabaseField
    private String NivelPrecio;
    @DatabaseField
    private String NumeroFilas;
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
    private String NumeroAprobacion;
    @DatabaseField
    private String NumeroPedido;
    @DatabaseField
    private String NumeroDocumento;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String NumeroMovimiento;
    @DatabaseField
    //private String porcentajeDescuentoAdicional;
    private String porcentajeDescuentoAdi;

    @DatabaseField
    private String Direccione_entrega;
    @DatabaseField
    private String Secuencia_Direccion_Entrega;



    @DatabaseField
    private String descuentoAdicional;
    @DatabaseField
    private String CodigoEmbalador;

    public String getCodigoEmbalador() {
        return CodigoEmbalador;
    }

    public void setCodigoEmbalador(String codigoEmbalador) {
        CodigoEmbalador = codigoEmbalador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBodega() {
        return Bodega;
    }

    public String getDireccionEntrega() {
        return DireccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        DireccionEntrega = direccionEntrega;
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

    public String getFormaPagoCliente() {
        return FormaPagoCliente;
    }

    public void setFormaPagoCliente(String formaPagoCliente) {
        FormaPagoCliente = formaPagoCliente;
    }

    public String getNombreFormaPagoCliente() {
        return NombreFormaPagoCliente;
    }

    public void setNombreFormaPagoCliente(String nombreFormaPagoCliente) {
        NombreFormaPagoCliente = nombreFormaPagoCliente;
    }

    public String getTransportista() {
        return Transportista;
    }

    public void setTransportista(String transportista) {
        Transportista = transportista;
    }

    public String getNombreTransportista() {
        return NombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        NombreTransportista = nombreTransportista;
    }

    public String getFormaPago() {
        return FormaPago;
    }

    public void setFormaPago(String formaPago) {
        FormaPago = formaPago;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public String getConsignacion() {
        return Consignacion;
    }

    public void setConsignacion(String consignacion) {
        Consignacion = consignacion;
    }

    public String getNivelPrecio() {
        return NivelPrecio;
    }

    public void setNivelPrecio(String nivelPrecio) {
        NivelPrecio = nivelPrecio;
    }

    public String getNumeroFilas() {
        return NumeroFilas;
    }

    public void setNumeroFilas(String numeroFilas) {
        NumeroFilas = numeroFilas;
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

    public String getNumeroAprobacion() {
        return NumeroAprobacion;
    }

    public void setNumeroAprobacion(String numeroAprobacion) {
        NumeroAprobacion = numeroAprobacion;
    }

    public String getNumeroPedido() {
        return NumeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        NumeroPedido = numeroPedido;
    }

    public String getNumeroDocumento() {
        return NumeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        NumeroDocumento = numeroDocumento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescuentoAdicional() {
        return descuentoAdicional;
    }

    public void setDescuentoAdicional(String descuentoAdicional) {
        this.descuentoAdicional = descuentoAdicional;
    }

    public String getNumeroMovimiento() {
        return NumeroMovimiento;
    }

    public void setNumeroMovimiento(String numeroMovimiento) {
        NumeroMovimiento = numeroMovimiento;
    }

    public String getPorcentajeDescuentoAdi() {
        return porcentajeDescuentoAdi;
    }

    public void setPorcentajeDescuentoAdi(String porcentajeDescuentoAdi) {
        this.porcentajeDescuentoAdi = porcentajeDescuentoAdi;
    }

    public String getCampana() {
        return Campana;
    }

    public void setCampana(String campana) {
        Campana = campana;
    }

    // Cambio de Joseph 27-07-2020
    /*
    public String getPorcentajeDescuentoAdicional() {
        return porcentajeDescuentoAdicional;
    }

    public void setPorcentajeDescuentoAdicional(String porcentajeDescuentoAdicional) {
        this.porcentajeDescuentoAdicional = porcentajeDescuentoAdicional;
    }
*/

    public String getDireccione_entrega() {
        return Direccione_entrega;
    }

    public void setDireccione_entrega(String direccione_entrega) {
        Direccione_entrega = direccione_entrega;
    }

    public String getSecuencia_Direccion_Entrega() {
        return Secuencia_Direccion_Entrega;
    }

    public void setSecuencia_Direccion_Entrega(String secuencia_Direccion_Entrega) {
        Secuencia_Direccion_Entrega = secuencia_Direccion_Entrega;
    }
}
