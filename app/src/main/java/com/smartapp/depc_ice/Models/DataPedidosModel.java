package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bodega;

import java.util.List;

public class DataPedidosModel {

    private List<List<ListarProformas>> ListarProformas;

    public List<List<com.smartapp.depc_ice.Models.ListarProformas>> getListarProformas() {
        return ListarProformas;
    }

    public void setListarProformas(List<List<com.smartapp.depc_ice.Models.ListarProformas>> listarProformas) {
        ListarProformas = listarProformas;
    }
}
