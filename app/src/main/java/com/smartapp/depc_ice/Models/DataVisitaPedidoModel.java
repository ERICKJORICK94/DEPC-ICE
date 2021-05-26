package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;

import java.util.List;

public class DataVisitaPedidoModel {

    private List<List<ClientesVisitas>> ListaClientes;

    public List<List<ClientesVisitas>> getListaClientes() {
        return ListaClientes;
    }

    public void setListaClientes(List<List<ClientesVisitas>> listaClientes) {
        ListaClientes = listaClientes;
    }
}
