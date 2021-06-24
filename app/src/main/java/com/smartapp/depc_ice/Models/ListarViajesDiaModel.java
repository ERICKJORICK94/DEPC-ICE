package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ListarViajesDia;

import java.util.List;

public class ListarViajesDiaModel {

    private List<ListarViajesDia> ListarViajesDia;

    public List<com.smartapp.depc_ice.Entities.ListarViajesDia> getListarViajesDia() {
        return ListarViajesDia;
    }

    public void setListarViajesDia(List<com.smartapp.depc_ice.Entities.ListarViajesDia> listarViajesDia) {
        ListarViajesDia = listarViajesDia;
    }
}
