package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Zonas;

import java.util.List;

public class DataZonaModel {

    private List<List<Zonas>> ListarBodegas;

    public List<List<Zonas>> getListarBodegas() {
        return ListarBodegas;
    }

    public void setListarBodegas(List<List<Zonas>> listarBodegas) {
        ListarBodegas = listarBodegas;
    }
}
