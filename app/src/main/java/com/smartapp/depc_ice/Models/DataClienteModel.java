package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Clientes;

import java.util.List;

public class DataClienteModel {

    private List<List<Clientes>> ListaClientes;

    public List<List<Clientes>> getListaClientes() {
        return ListaClientes;
    }

    public void setListaClientes(List<List<Clientes>> listaClientes) {
        ListaClientes = listaClientes;
    }
}
