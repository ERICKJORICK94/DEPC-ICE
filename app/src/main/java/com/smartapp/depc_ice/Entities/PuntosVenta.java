package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_PUNTOS_VENTA)
public class PuntosVenta {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String pto_vta_id;
    @DatabaseField
    private String nombre;
    @DatabaseField
    private String sucursal_id;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String centro_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPto_vta_id() {
        return pto_vta_id;
    }

    public void setPto_vta_id(String pto_vta_id) {
        this.pto_vta_id = pto_vta_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSucursal_id() {
        return sucursal_id;
    }

    public void setSucursal_id(String sucursal_id) {
        this.sucursal_id = sucursal_id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCentro_id() {
        return centro_id;
    }

    public void setCentro_id(String centro_id) {
        this.centro_id = centro_id;
    }
}
