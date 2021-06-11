package com.smartapp.depc_ice.Models;

import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.FormaPago;

import java.util.List;

public class DataFormaPagoModel {

    private List<List<FormaPago>> ListarFormasPago;

    public List<List<FormaPago>> getListarFormasPago() {
        return ListarFormasPago;
    }

    public void setListarFormasPago(List<List<FormaPago>> listarFormasPago) {
        ListarFormasPago = listarFormasPago;
    }
}
