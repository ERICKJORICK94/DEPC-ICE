package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Productos;

import java.util.List;

public class DataProductosModel {

    public List<List<Productos>> getListarProductosBodegas() {
        return ListarProductosBodegas;
    }

    public void setListarProductosBodegas(List<List<Productos>> listarProductosBodegas) {
        ListarProductosBodegas = listarProductosBodegas;
    }

    private List<List<Productos>> ListarProductosBodegas;

}
