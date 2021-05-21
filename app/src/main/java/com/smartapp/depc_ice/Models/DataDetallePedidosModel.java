package com.smartapp.depc_ice.Models;

import java.util.List;

public class DataDetallePedidosModel {

    private List<List<ListarDetalleProformas>> ListarProformas;

    public List<List<ListarDetalleProformas>> getListarProformas() {
        return ListarProformas;
    }

    public void setListarProformas(List<List<ListarDetalleProformas>> listarProformas) {
        ListarProformas = listarProformas;
    }
}
