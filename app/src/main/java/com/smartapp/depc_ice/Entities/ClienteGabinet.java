package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_GABINET_CLIENTE)
public class ClienteGabinet {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String id_cliente;
    @DatabaseField
    private String id_congelador;
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
    private String direccion_cliente_gabinet;
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
    @DatabaseField
    private String ruta;
    @DatabaseField
    private String codigo;
    @DatabaseField
    private String observacion;
    @DatabaseField
    private String foto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_congelador() {
        return id_congelador;
    }

    public void setId_congelador(String id_congelador) {
        this.id_congelador = id_congelador;
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

    public String getId_cliente() {
        return id_cliente;
    }


    public String getDireccion_cliente_gabinet() {
        return direccion_cliente_gabinet;
    }

    public void setDireccion_cliente_gabinet(String direccion_cliente_gabinet) {
        this.direccion_cliente_gabinet = direccion_cliente_gabinet;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
