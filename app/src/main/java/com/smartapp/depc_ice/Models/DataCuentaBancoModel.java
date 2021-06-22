package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bancos;
import com.smartapp.depc_ice.Entities.CuentaBancos;

import java.util.List;

public class DataCuentaBancoModel {

    private List<List<CuentaBancos>> ListarCuentasBancos;

    public List<List<CuentaBancos>> getListarCuentasBancos() {
        return ListarCuentasBancos;
    }

    public void setListarCuentasBancos(List<List<CuentaBancos>> listarCuentasBancos) {
        ListarCuentasBancos = listarCuentasBancos;
    }
}
