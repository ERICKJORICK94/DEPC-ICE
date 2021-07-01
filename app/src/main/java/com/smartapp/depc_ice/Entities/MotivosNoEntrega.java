package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_MOTIVOSNO_ENTREGA)
public class MotivosNoEntrega {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String descripcion;
    @DatabaseField
    private String num_motivo;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String usuario_crea;
    @DatabaseField
    private String fecha_crea;
    @DatabaseField
    private String usuario_actualiza;
    @DatabaseField
    private String fecha_actualiza;
    @DatabaseField
    private String usuario_anula;
    @DatabaseField
    private String fecha_anula;


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

    public String getNum_motivo() {
        return num_motivo;
    }

    public void setNum_motivo(String num_motivo) {
        this.num_motivo = num_motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario_crea() {
        return usuario_crea;
    }

    public void setUsuario_crea(String usuario_crea) {
        this.usuario_crea = usuario_crea;
    }

    public String getFecha_crea() {
        return fecha_crea;
    }

    public void setFecha_crea(String fecha_crea) {
        this.fecha_crea = fecha_crea;
    }

    public String getUsuario_actualiza() {
        return usuario_actualiza;
    }

    public void setUsuario_actualiza(String usuario_actualiza) {
        this.usuario_actualiza = usuario_actualiza;
    }

    public String getFecha_actualiza() {
        return fecha_actualiza;
    }

    public void setFecha_actualiza(String fecha_actualiza) {
        this.fecha_actualiza = fecha_actualiza;
    }

    public String getUsuario_anula() {
        return usuario_anula;
    }

    public void setUsuario_anula(String usuario_anula) {
        this.usuario_anula = usuario_anula;
    }

    public String getFecha_anula() {
        return fecha_anula;
    }

    public void setFecha_anula(String fecha_anula) {
        this.fecha_anula = fecha_anula;
    }
}
