package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.util.List;

@DatabaseTable(tableName = Const.TABLE_DETALLE_VIAJE)
public class DetalleViaje {

    @DatabaseField(generatedId = true)
    private int primary;
    @DatabaseField
    private String id;
    @DatabaseField
    private String id_viaje;
    @DatabaseField
    private String cuenta_id;
    @DatabaseField
    private String factura_id;
    @DatabaseField
    private String prefijo1;
    @DatabaseField
    private String prefijo2;
    @DatabaseField
    private String factura_fiscal;
    @DatabaseField
    private String guia_id;
    @DatabaseField
    private String serie;
    @DatabaseField
    private String secuencia;
    @DatabaseField
    private String id_despacho;
    @DatabaseField
    private String id_ref_doc;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String nombre_estado;
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
    private String usuario_procesa;
    @DatabaseField
    private String fecha_procesa;
    @DatabaseField
    private String nombre_persona_recibe;
    @DatabaseField
    private String firma_persona_recibe;
    @DatabaseField
    private String foto_entrega;
    @DatabaseField
    private String id_motivo_noentrega;
    @DatabaseField
    private String observacion_entrega;
    @DatabaseField
    private String direccion_envio;
    @DatabaseField
    private String latitud;
    @DatabaseField
    private String longitud;

    public String getNombre_estado() {
        return nombre_estado;
    }

    public void setNombre_estado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    private List<DetalleFacturas> DetalleFacturas;

    public List<com.smartapp.depc_ice.Entities.DetalleFacturas> getDetalleFacturas() {
        return DetalleFacturas;
    }

    public void setDetalleFacturas(List<com.smartapp.depc_ice.Entities.DetalleFacturas> detalleFacturas) {
        DetalleFacturas = detalleFacturas;
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(String id_viaje) {
        this.id_viaje = id_viaje;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) {
        this.cuenta_id = cuenta_id;
    }

    public String getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(String factura_id) {
        this.factura_id = factura_id;
    }

    public String getPrefijo1() {
        return prefijo1;
    }

    public void setPrefijo1(String prefijo1) {
        this.prefijo1 = prefijo1;
    }

    public String getPrefijo2() {
        return prefijo2;
    }

    public void setPrefijo2(String prefijo2) {
        this.prefijo2 = prefijo2;
    }

    public String getFactura_fiscal() {
        return factura_fiscal;
    }

    public void setFactura_fiscal(String factura_fiscal) {
        this.factura_fiscal = factura_fiscal;
    }

    public String getGuia_id() {
        return guia_id;
    }

    public void setGuia_id(String guia_id) {
        this.guia_id = guia_id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    public String getId_despacho() {
        return id_despacho;
    }

    public void setId_despacho(String id_despacho) {
        this.id_despacho = id_despacho;
    }

    public String getId_ref_doc() {
        return id_ref_doc;
    }

    public void setId_ref_doc(String id_ref_doc) {
        this.id_ref_doc = id_ref_doc;
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

    public String getUsuario_procesa() {
        return usuario_procesa;
    }

    public void setUsuario_procesa(String usuario_procesa) {
        this.usuario_procesa = usuario_procesa;
    }

    public String getFecha_procesa() {
        return fecha_procesa;
    }

    public void setFecha_procesa(String fecha_procesa) {
        this.fecha_procesa = fecha_procesa;
    }

    public String getNombre_persona_recibe() {
        return nombre_persona_recibe;
    }

    public void setNombre_persona_recibe(String nombre_persona_recibe) {
        this.nombre_persona_recibe = nombre_persona_recibe;
    }

    public String getFirma_persona_recibe() {
        return firma_persona_recibe;
    }

    public void setFirma_persona_recibe(String firma_persona_recibe) {
        this.firma_persona_recibe = firma_persona_recibe;
    }

    public String getFoto_entrega() {
        return foto_entrega;
    }

    public void setFoto_entrega(String foto_entrega) {
        this.foto_entrega = foto_entrega;
    }

    public String getId_motivo_noentrega() {
        return id_motivo_noentrega;
    }

    public void setId_motivo_noentrega(String id_motivo_noentrega) {
        this.id_motivo_noentrega = id_motivo_noentrega;
    }

    public String getObservacion_entrega() {
        return observacion_entrega;
    }

    public void setObservacion_entrega(String observacion_entrega) {
        this.observacion_entrega = observacion_entrega;
    }

    public String getDireccion_envio() {
        return direccion_envio;
    }

    public void setDireccion_envio(String direccion_envio) {
        this.direccion_envio = direccion_envio;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
