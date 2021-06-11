package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_BANCOS)
public class Bancos {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String banco;
    @DatabaseField
    private String nombre_entidad;
    @DatabaseField
    private String telefono_entidad;
    @DatabaseField
    private String codigo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNombre_entidad() {
        return nombre_entidad;
    }

    public void setNombre_entidad(String nombre_entidad) {
        this.nombre_entidad = nombre_entidad;
    }

    public String getTelefono_entidad() {
        return telefono_entidad;
    }

    public void setTelefono_entidad(String telefono_entidad) {
        this.telefono_entidad = telefono_entidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
