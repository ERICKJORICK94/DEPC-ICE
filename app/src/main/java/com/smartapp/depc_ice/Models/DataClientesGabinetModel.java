package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.ClienteGabinet;

import java.util.List;

public class DataClientesGabinetModel {

    private List<List<ClienteGabinet>> ListarGabinetCliente;

    public List<List<ClienteGabinet>> getListarGabinetCliente() {
        return ListarGabinetCliente;
    }

    public void setListarGabinetCliente(List<List<ClienteGabinet>> listarGabinetCliente) {
        ListarGabinetCliente = listarGabinetCliente;
    }
}
