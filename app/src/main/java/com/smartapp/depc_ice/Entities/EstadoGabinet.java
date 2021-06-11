package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_ESTADO_GABINET)
public class EstadoGabinet {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String num_estado;
    @DatabaseField
    private String descripcion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum_estado() {
        return num_estado;
    }

    public void setNum_estado(String num_estado) {
        this.num_estado = num_estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
