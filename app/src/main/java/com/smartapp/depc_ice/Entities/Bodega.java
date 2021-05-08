package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_BODEGAS)
public class Bodega {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String departamento;
    @DatabaseField
    private String ubicacion;
    @DatabaseField
    private String responsable;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String recibe_compra;
    @DatabaseField
    private String bodega_id;
    @DatabaseField
    private String responsable_email;
    @DatabaseField
    private String cod_ciudad;
    @DatabaseField
    private String rma;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRecibe_compra() {
        return recibe_compra;
    }

    public void setRecibe_compra(String recibe_compra) {
        this.recibe_compra = recibe_compra;
    }

    public String getBodega_id() {
        return bodega_id;
    }

    public void setBodega_id(String bodega_id) {
        this.bodega_id = bodega_id;
    }

    public String getResponsable_email() {
        return responsable_email;
    }

    public void setResponsable_email(String responsable_email) {
        this.responsable_email = responsable_email;
    }

    public String getCod_ciudad() {
        return cod_ciudad;
    }

    public void setCod_ciudad(String cod_ciudad) {
        this.cod_ciudad = cod_ciudad;
    }

    public String getRma() {
        return rma;
    }

    public void setRma(String rma) {
        this.rma = rma;
    }
}
