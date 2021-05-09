package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Zonas;

import java.util.List;

public class DataDireccionesModel {

    private List<List<Direcciones>> ListaClientes;

    public List<List<Direcciones>> getListaClientes() {
        return ListaClientes;
    }

    public void setListaClientes(List<List<Direcciones>> listaClientes) {
        ListaClientes = listaClientes;
    }
}
