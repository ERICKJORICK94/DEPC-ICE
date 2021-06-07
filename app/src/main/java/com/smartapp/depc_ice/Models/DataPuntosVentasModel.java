package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.PuntosVenta;
import com.smartapp.depc_ice.Entities.Zonas;

import java.util.List;

public class DataPuntosVentasModel {

    private List<List<PuntosVenta>> ListarPuntos;

    public List<List<PuntosVenta>> getListarPuntos() {
        return ListarPuntos;
    }

    public void setListarPuntos(List<List<PuntosVenta>> listarPuntos) {
        ListarPuntos = listarPuntos;
    }
}
