package com.smartapp.depc_ice.Models;

public class AsignarGabinetModel {

    private String id_congelador = "";
    private String id_direccion_cliente = "";
    private String metodo = "";

    public String getId_congelador() {
        return id_congelador;
    }

    public void setId_congelador(String id_congelador) {
        this.id_congelador = id_congelador;
    }

    public String getId_direccion_cliente() {
        return id_direccion_cliente;
    }

    public void setId_direccion_cliente(String id_direccion_cliente) {
        this.id_direccion_cliente = id_direccion_cliente;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
