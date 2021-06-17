package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bancos;
import com.smartapp.depc_ice.Entities.EstadoFacturasDespacho;

import java.util.List;

public class DataEstadoFacturaDespachoModel {

    private List<List<EstadoFacturasDespacho>> ListarEstadoFacturasDespacho;

    public List<List<EstadoFacturasDespacho>> getListarEstadoFacturasDespacho() {
        return ListarEstadoFacturasDespacho;
    }

    public void setListarEstadoFacturasDespacho(List<List<EstadoFacturasDespacho>> listarEstadoFacturasDespacho) {
        ListarEstadoFacturasDespacho = listarEstadoFacturasDespacho;
    }
}
