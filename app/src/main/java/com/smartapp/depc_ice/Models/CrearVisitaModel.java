package com.smartapp.depc_ice.Models;

import java.util.List;

public class CrearVisitaModel {

    private String cliente_id = "";
    private String vendedor_id = "";
    private String direccion_id = "";
    private String atiende = "";
    private String hora = "";
    private String firma = "";
    private String foto = "";
    private String metodo = "";

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getVendedor_id() {
        return vendedor_id;
    }

    public void setVendedor_id(String vendedor_id) {
        this.vendedor_id = vendedor_id;
    }

    public String getDireccion_id() {
        return direccion_id;
    }

    public void setDireccion_id(String direccion_id) {
        this.direccion_id = direccion_id;
    }

    public String getAtiende() {
        return atiende;
    }

    public void setAtiende(String atiende) {
        this.atiende = atiende;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
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
