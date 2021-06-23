package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.ListarViajesDia;

import java.util.List;

public class ListarViajesDiaModel {

    private List<List<ListarViajesDia>> ListarViajesDia;

    public List<List<com.smartapp.depc_ice.Entities.ListarViajesDia>> getListarViajesDia() {
        return ListarViajesDia;
    }

    public void setListarViajesDia(List<List<com.smartapp.depc_ice.Entities.ListarViajesDia>> listarViajesDia) {
        ListarViajesDia = listarViajesDia;
    }
}
