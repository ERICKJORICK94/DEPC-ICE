package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;

import java.util.List;

public class DataBodegaModel {

    private List<List<Bodega>> ListarBodegas;

    public List<List<Bodega>> getListarBodegas() {
        return ListarBodegas;
    }

    public void setListarBodegas(List<List<Bodega>> listarBodegas) {
        ListarBodegas = listarBodegas;
    }
}
