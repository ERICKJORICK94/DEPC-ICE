package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Gabinet;
import com.smartapp.depc_ice.Entities.GabinetGeneral;

import java.util.List;

public class DataGabinetGeneralModel {

    private List<List<GabinetGeneral>> ListarGabinet;

    public List<List<GabinetGeneral>> getListarGabinet() {
        return ListarGabinet;
    }

    public void setListarGabinet(List<List<GabinetGeneral>> listarGabinet) {
        ListarGabinet = listarGabinet;
    }
}
