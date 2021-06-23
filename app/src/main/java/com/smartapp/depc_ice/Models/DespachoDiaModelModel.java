package com.smartapp.depc_ice.Models;

public class DespachoDiaModelModel {

    private String fecha_inicio = "";
    private String pto_vta_id = "";
    private String usuario_id = "";
    private String metodo = "";

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getPto_vta_id() {
        return pto_vta_id;
    }

    public void setPto_vta_id(String pto_vta_id) {
        this.pto_vta_id = pto_vta_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
