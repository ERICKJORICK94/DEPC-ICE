package com.smartapp.depc_ice.Models;

import java.util.List;

public class CrearPreventaModel {

    private String usuario_id = "";
    private String cliente_id = "";
    private String total_neto = "";
    private String forma_pago = "";
    private String dias_credito = "";
    private String total = "";
    private String cobrar_iva = "";
    private String observaciones = "";
    private String forma_pago_id = "";
    private List<DetalleCrearPreventaModel> detalle;
    private String metodo = "";

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getTotal_neto() {
        return total_neto;
    }

    public void setTotal_neto(String total_neto) {
        this.total_neto = total_neto;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getDias_credito() {
        return dias_credito;
    }

    public void setDias_credito(String dias_credito) {
        this.dias_credito = dias_credito;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCobrar_iva() {
        return cobrar_iva;
    }

    public void setCobrar_iva(String cobrar_iva) {
        this.cobrar_iva = cobrar_iva;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getForma_pago_id() {
        return forma_pago_id;
    }

    public void setForma_pago_id(String forma_pago_id) {
        this.forma_pago_id = forma_pago_id;
    }

    public List<DetalleCrearPreventaModel> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleCrearPreventaModel> detalle) {
        this.detalle = detalle;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
