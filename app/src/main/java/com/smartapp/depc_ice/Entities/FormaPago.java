package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_FORMA_PAGO)
public class FormaPago {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String id_forma_pago;
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String desc_abrev;
    @DatabaseField
    private String id_congenlador;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String cuenta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_forma_pago() {
        return id_forma_pago;
    }

    public void setId_forma_pago(String id_forma_pago) {
        this.id_forma_pago = id_forma_pago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDesc_abrev() {
        return desc_abrev;
    }

    public void setDesc_abrev(String desc_abrev) {
        this.desc_abrev = desc_abrev;
    }

    public String getId_congenlador() {
        return id_congenlador;
    }

    public void setId_congenlador(String id_congenlador) {
        this.id_congenlador = id_congenlador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }
}
