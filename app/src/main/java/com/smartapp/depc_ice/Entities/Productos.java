package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.io.Serializable;

@DatabaseTable(tableName = Const.TABLE_PRODUCTOS)
public class Productos implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String codigo_item  ;
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String descripcion_abrev;
    @DatabaseField
    private String pvp;
    @DatabaseField
    private String bodega_id;
    @DatabaseField
    private String descripcion_bodega;
    @DatabaseField
    private String existencia;
    @DatabaseField
    private int idDetail;
    @DatabaseField
    private String costo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo_item() {
        return codigo_item;
    }

    public void setCodigo_item(String codigo_item) {
        this.codigo_item = codigo_item;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion_abrev() {
        return descripcion_abrev;
    }

    public void setDescripcion_abrev(String descripcion_abrev) {
        this.descripcion_abrev = descripcion_abrev;
    }

    public String getPvp() {
        return pvp;
    }

    public void setPvp(String pvp) {
        this.pvp = pvp;
    }

    public String getBodega_id() {
        return bodega_id;
    }

    public void setBodega_id(String bodega_id) {
        this.bodega_id = bodega_id;
    }

    public String getDescripcion_bodega() {
        return descripcion_bodega;
    }

    public void setDescripcion_bodega(String descripcion_bodega) {
        this.descripcion_bodega = descripcion_bodega;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }
}
