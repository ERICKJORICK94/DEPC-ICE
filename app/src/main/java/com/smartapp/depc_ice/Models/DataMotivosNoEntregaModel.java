package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.MotivosNoEntrega;

import java.util.List;

public class DataMotivosNoEntregaModel {

    private List<List<MotivosNoEntrega>> ListarMotivosNoEntrega;

    public List<List<MotivosNoEntrega>> getListarMotivosNoEntrega() {
        return ListarMotivosNoEntrega;
    }

    public void setListarMotivosNoEntrega(List<List<MotivosNoEntrega>> listarMotivosNoEntrega) {
        ListarMotivosNoEntrega = listarMotivosNoEntrega;
    }
}
