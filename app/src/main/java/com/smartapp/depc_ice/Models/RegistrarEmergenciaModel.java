package com.smartapp.depc_ice.Models;

public class RegistrarEmergenciaModel {

    private String pto_vta_id = "";
    private String bodega = "";
    private String id_direccion_cliente = "";
    private String estado = "";
    private String congelador_id = "";
    private String observacion = "";
    private String usuario_id = "";
    private String foto = "";
    private String metodo = "";

    public String getPto_vta_id() {
        return pto_vta_id;
    }

    public void setPto_vta_id(String pto_vta_id) {
        this.pto_vta_id = pto_vta_id;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getId_direccion_cliente() {
        return id_direccion_cliente;
    }

    public void setId_direccion_cliente(String id_direccion_cliente) {
        this.id_direccion_cliente = id_direccion_cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCongelador_id() {
        return congelador_id;
    }

    public void setCongelador_id(String congelador_id) {
        this.congelador_id = congelador_id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
