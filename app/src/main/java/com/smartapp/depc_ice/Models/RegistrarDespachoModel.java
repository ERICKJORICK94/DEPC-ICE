package com.smartapp.depc_ice.Models;

public class RegistrarDespachoModel {

    private String factura_id = "";
    private String foto_entrega = "";
    private String id_motivo_noentrega = "";
    private String firma_persona_recibe = "";
    private String nombre_persona_recibe = "";
    private String usuario_procesa = "";
    private String observacion_entrega = "";
    private String estado = "";
    private String metodo = "";

    public String getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(String factura_id) {
        this.factura_id = factura_id;
    }

    public String getFoto_entrega() {
        return foto_entrega;
    }

    public void setFoto_entrega(String foto_entrega) {
        this.foto_entrega = foto_entrega;
    }

    public String getId_motivo_noentrega() {
        return id_motivo_noentrega;
    }

    public void setId_motivo_noentrega(String id_motivo_noentrega) {
        this.id_motivo_noentrega = id_motivo_noentrega;
    }

    public String getFirma_persona_recibe() {
        return firma_persona_recibe;
    }

    public void setFirma_persona_recibe(String firma_persona_recibe) {
        this.firma_persona_recibe = firma_persona_recibe;
    }

    public String getNombre_persona_recibe() {
        return nombre_persona_recibe;
    }

    public void setNombre_persona_recibe(String nombre_persona_recibe) {
        this.nombre_persona_recibe = nombre_persona_recibe;
    }

    public String getUsuario_procesa() {
        return usuario_procesa;
    }

    public void setUsuario_procesa(String usuario_procesa) {
        this.usuario_procesa = usuario_procesa;
    }

    public String getObservacion_entrega() {
        return observacion_entrega;
    }

    public void setObservacion_entrega(String observacion_entrega) {
        this.observacion_entrega = observacion_entrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
