package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_GABINET)
public class Gabinet {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String id_congenlador;
    @DatabaseField
    private String pto_vta_id;
    @DatabaseField
    private String bodega;
    @DatabaseField
    private String codigo_tipo_congelador;
    @DatabaseField
    private String serie;
    @DatabaseField
    private String id_direccion_cliente;
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
    private String fecha_anula;
    @DatabaseField
    private String ruta;
    @DatabaseField
    private String codigo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_congenlador() {
        return id_congenlador;
    }

    public void setId_congenlador(String id_congenlador) {
        this.id_congenlador = id_congenlador;
    }

    public String getPto_vta_id() {
        return pto_vta_id;
    }

    public void setPto_vta_id(String pto_vta_id) {
        this.pto_vta_id = pto_vta_id;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getCodigo_tipo_congelador() {
        return codigo_tipo_congelador;
    }

    public void setCodigo_tipo_congelador(String codigo_tipo_congelador) {
        this.codigo_tipo_congelador = codigo_tipo_congelador;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getId_direccion_cliente() {
        return id_direccion_cliente;
    }

    public void setId_direccion_cliente(String id_direccion_cliente) {
        this.id_direccion_cliente = id_direccion_cliente;
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

    public String getFecha_anula() {
        return fecha_anula;
    }

    public void setFecha_anula(String fecha_anula) {
        this.fecha_anula = fecha_anula;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
