package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_DETALLE_FORMA_PAGO)
public class DetalleFormaPago {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String id_viaje;
    @DatabaseField
    private String factura_id;
    @DatabaseField
    private String cuenta_id;
    @DatabaseField
    private String prefijo1;
    @DatabaseField
    private String prefijo2;
    @DatabaseField
    private String factura_fiscal;
    @DatabaseField
    private String forma_pago;
    @DatabaseField
    private String valor;
    @DatabaseField
    private String banco_origen;
    @DatabaseField
    private String num_cuenta_origen;
    @DatabaseField
    private String num_documento;
    @DatabaseField
    private String cuenta_bancaria;
    @DatabaseField
    private String usuario_crea;
    @DatabaseField
    private String nombre_persona_paga;
    @DatabaseField
    private String firma_persona_paga;
    @DatabaseField
    private String foto_cobro;
    @DatabaseField
    private String metodo;
    @DatabaseField
    private String nombre_forma_de_pago;
    @DatabaseField
    private String nombre_corto_forma_de_pago;
    @DatabaseField
    private Boolean estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(String id_viaje) {
        this.id_viaje = id_viaje;
    }

    public String getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(String factura_id) {
        this.factura_id = factura_id;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) {
        this.cuenta_id = cuenta_id;
    }

    public String getPrefijo1() {
        return prefijo1;
    }

    public void setPrefijo1(String prefijo1) {
        this.prefijo1 = prefijo1;
    }

    public String getPrefijo2() {
        return prefijo2;
    }

    public void setPrefijo2(String prefijo2) {
        this.prefijo2 = prefijo2;
    }

    public String getFactura_fiscal() {
        return factura_fiscal;
    }

    public void setFactura_fiscal(String factura_fiscal) {
        this.factura_fiscal = factura_fiscal;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getBanco_origen() {
        return banco_origen;
    }

    public void setBanco_origen(String banco_origen) {
        this.banco_origen = banco_origen;
    }

    public String getNum_cuenta_origen() {
        return num_cuenta_origen;
    }

    public void setNum_cuenta_origen(String num_cuenta_origen) {
        this.num_cuenta_origen = num_cuenta_origen;
    }

    public String getNum_documento() {
        return num_documento;
    }

    public void setNum_documento(String num_documento) {
        this.num_documento = num_documento;
    }

    public String getCuenta_bancaria() {
        return cuenta_bancaria;
    }

    public void setCuenta_bancaria(String cuenta_bancaria) {
        this.cuenta_bancaria = cuenta_bancaria;
    }

    public String getUsuario_crea() {
        return usuario_crea;
    }

    public void setUsuario_crea(String usuario_crea) {
        this.usuario_crea = usuario_crea;
    }

    public String getNombre_persona_paga() {
        return nombre_persona_paga;
    }

    public void setNombre_persona_paga(String nombre_persona_paga) {
        this.nombre_persona_paga = nombre_persona_paga;
    }

    public String getFirma_persona_paga() {
        return firma_persona_paga;
    }

    public void setFirma_persona_paga(String firma_persona_paga) {
        this.firma_persona_paga = firma_persona_paga;
    }

    public String getFoto_cobro() {
        return foto_cobro;
    }

    public void setFoto_cobro(String foto_cobro) {
        this.foto_cobro = foto_cobro;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getNombre_forma_de_pago() {
        return nombre_forma_de_pago;
    }

    public void setNombre_forma_de_pago(String nombre_forma_de_pago) {
        this.nombre_forma_de_pago = nombre_forma_de_pago;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNombre_corto_forma_de_pago() {
        return nombre_corto_forma_de_pago;
    }

    public void setNombre_corto_forma_de_pago(String nombre_corto_forma_de_pago) {
        this.nombre_corto_forma_de_pago = nombre_corto_forma_de_pago;
    }
}
