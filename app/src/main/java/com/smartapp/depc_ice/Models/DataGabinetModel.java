package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Gabinet;
import com.smartapp.depc_ice.Entities.PuntosVenta;

import java.util.List;

public class DataGabinetModel {

    private List<List<Gabinet>> ListarGabinet;

    public List<List<Gabinet>> getListarGabinet() {
        return ListarGabinet;
    }

    public void setListarGabinet(List<List<Gabinet>> listarGabinet) {
        ListarGabinet = listarGabinet;
    }
}
