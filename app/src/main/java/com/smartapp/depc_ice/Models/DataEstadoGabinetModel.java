package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.EstadoGabinet;

import java.util.List;

public class DataEstadoGabinetModel {

    private List<List<EstadoGabinet>> ListarEstadoGabinet;

    public List<List<EstadoGabinet>> getListarEstadoGabinet() {
        return ListarEstadoGabinet;
    }

    public void setListarEstadoGabinet(List<List<EstadoGabinet>> listarEstadoGabinet) {
        ListarEstadoGabinet = listarEstadoGabinet;
    }
}
