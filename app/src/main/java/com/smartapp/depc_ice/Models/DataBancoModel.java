package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bancos;
import com.smartapp.depc_ice.Entities.FormaPago;

import java.util.List;

public class DataBancoModel {

    private List<List<Bancos>> ListarBancos;


    public List<List<Bancos>> getListarBancos() {
        return ListarBancos;
    }

    public void setListarBancos(List<List<Bancos>> listarBancos) {
        ListarBancos = listarBancos;
    }
}
