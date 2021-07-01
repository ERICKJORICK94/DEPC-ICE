package com.smartapp.depc_ice.Models;

public class DespachoItemFactura {

    private String observacion_noentrega = "";
    private String foto_noentrega = "";
    private String cntdespacho = "";
    private String fct_det_id = "";
    private String metodo = "";

    public String getObservacion_noentrega() {
        return observacion_noentrega;
    }

    public void setObservacion_noentrega(String observacion_noentrega) {
        this.observacion_noentrega = observacion_noentrega;
    }

    public String getFoto_noentrega() {
        return foto_noentrega;
    }

    public void setFoto_noentrega(String foto_noentrega) {
        this.foto_noentrega = foto_noentrega;
    }

    public String getCntdespacho() {
        return cntdespacho;
    }

    public void setCntdespacho(String cntdespacho) {
        this.cntdespacho = cntdespacho;
    }

    public String getFct_det_id() {
        return fct_det_id;
    }

    public void setFct_det_id(String fct_det_id) {
        this.fct_det_id = fct_det_id;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}
