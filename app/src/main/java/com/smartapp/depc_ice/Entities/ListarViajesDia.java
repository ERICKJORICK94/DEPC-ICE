package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.util.List;

@DatabaseTable(tableName = Const.TABLE_LISTAR_VIAJES_DIA)
public class ListarViajesDia {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String id_viaje;
    @DatabaseField
    private String tipo_viaje;
    @DatabaseField
    private String id_tipo_vehiculo;
    @DatabaseField
    private String id_tipo_equipo;
    @DatabaseField
    private String id_vehiculo;
    @DatabaseField
    private String chofer_id;
    @DatabaseField
    private String ayudante_id;
    @DatabaseField
    private String estibador_id;
    @DatabaseField
    private String deposito_id;
    @DatabaseField
    private String cliente_id;
    @DatabaseField
    private String total_peso_kg;
    @DatabaseField
    private String total_peso_m3;
    @DatabaseField
    private String fecha_inicio;
    @DatabaseField
    private String fecha_fin;
    @DatabaseField
    private String pto_vta_id;
    @DatabaseField
    private String provincia_destino_id;
    @DatabaseField
    private String ciudad_destino_id;
    @DatabaseField
    private String zona_destino;
    @DatabaseField
    private String punto_llegada;
    @DatabaseField
    private String destino_final;
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
    private String usuario_cierra;
    @DatabaseField
    private String fecha_cierra;
    @DatabaseField
    private String usuario_aprueba;
    @DatabaseField
    private String fecha_aprueba;
    @DatabaseField
    private String fecha_inicio_real;
    @DatabaseField
    private String fecha_fin_real;
    @DatabaseField
    private String genera_guia;
    @DatabaseField
    private String guia_id;
    @DatabaseField
    private String serie;
    @DatabaseField
    private String secuencia;
    @DatabaseField
    private String lleva_contanedor;
    @DatabaseField
    private String contenedor;
    @DatabaseField
    private String tipo_contenedor;
    @DatabaseField
    private String url_foto_contenedor;
    @DatabaseField
    private String motivo_traslado;
    @DatabaseField
    private String lugar_carga;
    @DatabaseField
    private String observacion;
    @DatabaseField
    private String temp_contenedor;
    @DatabaseField
    private String url_foto_temp_contenedor;
    @DatabaseField
    private String usuario_anula;
    @DatabaseField
    private String fecha_anula;
    @DatabaseField
    private String usuario_inicio_standby;
    @DatabaseField
    private String fecha_inicio_standby;
    @DatabaseField
    private String usuario_fin_standby;
    @DatabaseField
    private String fecha_fin_standby;
    @DatabaseField
    private String cedula_persona_recibe;
    @DatabaseField
    private String nombre_persona_recibe;
    @DatabaseField
    private String url_firma_persona_recibe;
    @DatabaseField
    private String id_motivo_noentrega;
    @DatabaseField
    private String observacion_entrega;
    @DatabaseField
    private String url_foto_entrega;
    @DatabaseField
    private String num_equipo;
    @DatabaseField
    private String num_generador;
    @DatabaseField
    private String id_naviera;
    @DatabaseField
    private String tipo_movimiento;
    @DatabaseField
    private String id_intermediario;
    @DatabaseField
    private String id_trx;
    @DatabaseField
    private String cobrado_transporte;
    @DatabaseField
    private String numero_dt;
    @DatabaseField
    private String fecha_llegada_punto_carga;
    @DatabaseField
    private String fecha_inicio_carga;
    @DatabaseField
    private String fecha_fin_carga;
    @DatabaseField
    private String fecha_salida_punto_carga;
    @DatabaseField
    private String valor_viaje;
    @DatabaseField
    private String adicional;
    @DatabaseField
    private String standby;
    @DatabaseField
    private String flete_falso;
    @DatabaseField
    private String doble_flete;
    @DatabaseField
    private String ajuste;
    @DatabaseField
    private String valor_total;
    @DatabaseField
    private String url_archivo_contenedor;
    @DatabaseField
    private String url_excel_ruta;
    @DatabaseField
    private String url_archivo_contenedor2;

    private List<DetalleViaje> DetalleViaje;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(String id_viaje) {
        this.id_viaje = id_viaje;
    }

    public String getTipo_viaje() {
        return tipo_viaje;
    }

    public void setTipo_viaje(String tipo_viaje) {
        this.tipo_viaje = tipo_viaje;
    }

    public String getId_tipo_vehiculo() {
        return id_tipo_vehiculo;
    }

    public void setId_tipo_vehiculo(String id_tipo_vehiculo) {
        this.id_tipo_vehiculo = id_tipo_vehiculo;
    }

    public String getId_tipo_equipo() {
        return id_tipo_equipo;
    }

    public void setId_tipo_equipo(String id_tipo_equipo) {
        this.id_tipo_equipo = id_tipo_equipo;
    }

    public String getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(String id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getChofer_id() {
        return chofer_id;
    }

    public void setChofer_id(String chofer_id) {
        this.chofer_id = chofer_id;
    }

    public String getAyudante_id() {
        return ayudante_id;
    }

    public void setAyudante_id(String ayudante_id) {
        this.ayudante_id = ayudante_id;
    }

    public String getEstibador_id() {
        return estibador_id;
    }

    public void setEstibador_id(String estibador_id) {
        this.estibador_id = estibador_id;
    }

    public String getDeposito_id() {
        return deposito_id;
    }

    public void setDeposito_id(String deposito_id) {
        this.deposito_id = deposito_id;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getTotal_peso_kg() {
        return total_peso_kg;
    }

    public void setTotal_peso_kg(String total_peso_kg) {
        this.total_peso_kg = total_peso_kg;
    }

    public String getTotal_peso_m3() {
        return total_peso_m3;
    }

    public void setTotal_peso_m3(String total_peso_m3) {
        this.total_peso_m3 = total_peso_m3;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getPto_vta_id() {
        return pto_vta_id;
    }

    public void setPto_vta_id(String pto_vta_id) {
        this.pto_vta_id = pto_vta_id;
    }

    public String getProvincia_destino_id() {
        return provincia_destino_id;
    }

    public void setProvincia_destino_id(String provincia_destino_id) {
        this.provincia_destino_id = provincia_destino_id;
    }

    public String getCiudad_destino_id() {
        return ciudad_destino_id;
    }

    public void setCiudad_destino_id(String ciudad_destino_id) {
        this.ciudad_destino_id = ciudad_destino_id;
    }

    public String getZona_destino() {
        return zona_destino;
    }

    public void setZona_destino(String zona_destino) {
        this.zona_destino = zona_destino;
    }

    public String getPunto_llegada() {
        return punto_llegada;
    }

    public void setPunto_llegada(String punto_llegada) {
        this.punto_llegada = punto_llegada;
    }

    public String getDestino_final() {
        return destino_final;
    }

    public void setDestino_final(String destino_final) {
        this.destino_final = destino_final;
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

    public String getUsuario_cierra() {
        return usuario_cierra;
    }

    public void setUsuario_cierra(String usuario_cierra) {
        this.usuario_cierra = usuario_cierra;
    }

    public String getFecha_cierra() {
        return fecha_cierra;
    }

    public void setFecha_cierra(String fecha_cierra) {
        this.fecha_cierra = fecha_cierra;
    }

    public String getUsuario_aprueba() {
        return usuario_aprueba;
    }

    public void setUsuario_aprueba(String usuario_aprueba) {
        this.usuario_aprueba = usuario_aprueba;
    }

    public String getFecha_aprueba() {
        return fecha_aprueba;
    }

    public void setFecha_aprueba(String fecha_aprueba) {
        this.fecha_aprueba = fecha_aprueba;
    }

    public String getFecha_inicio_real() {
        return fecha_inicio_real;
    }

    public void setFecha_inicio_real(String fecha_inicio_real) {
        this.fecha_inicio_real = fecha_inicio_real;
    }

    public String getFecha_fin_real() {
        return fecha_fin_real;
    }

    public void setFecha_fin_real(String fecha_fin_real) {
        this.fecha_fin_real = fecha_fin_real;
    }

    public String getGenera_guia() {
        return genera_guia;
    }

    public void setGenera_guia(String genera_guia) {
        this.genera_guia = genera_guia;
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

    public String getLleva_contanedor() {
        return lleva_contanedor;
    }

    public void setLleva_contanedor(String lleva_contanedor) {
        this.lleva_contanedor = lleva_contanedor;
    }

    public String getContenedor() {
        return contenedor;
    }

    public void setContenedor(String contenedor) {
        this.contenedor = contenedor;
    }

    public String getTipo_contenedor() {
        return tipo_contenedor;
    }

    public void setTipo_contenedor(String tipo_contenedor) {
        this.tipo_contenedor = tipo_contenedor;
    }

    public String getUrl_foto_contenedor() {
        return url_foto_contenedor;
    }

    public void setUrl_foto_contenedor(String url_foto_contenedor) {
        this.url_foto_contenedor = url_foto_contenedor;
    }

    public String getMotivo_traslado() {
        return motivo_traslado;
    }

    public void setMotivo_traslado(String motivo_traslado) {
        this.motivo_traslado = motivo_traslado;
    }

    public String getLugar_carga() {
        return lugar_carga;
    }

    public void setLugar_carga(String lugar_carga) {
        this.lugar_carga = lugar_carga;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTemp_contenedor() {
        return temp_contenedor;
    }

    public void setTemp_contenedor(String temp_contenedor) {
        this.temp_contenedor = temp_contenedor;
    }

    public String getUrl_foto_temp_contenedor() {
        return url_foto_temp_contenedor;
    }

    public void setUrl_foto_temp_contenedor(String url_foto_temp_contenedor) {
        this.url_foto_temp_contenedor = url_foto_temp_contenedor;
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

    public String getUsuario_inicio_standby() {
        return usuario_inicio_standby;
    }

    public void setUsuario_inicio_standby(String usuario_inicio_standby) {
        this.usuario_inicio_standby = usuario_inicio_standby;
    }

    public String getFecha_inicio_standby() {
        return fecha_inicio_standby;
    }

    public void setFecha_inicio_standby(String fecha_inicio_standby) {
        this.fecha_inicio_standby = fecha_inicio_standby;
    }

    public String getUsuario_fin_standby() {
        return usuario_fin_standby;
    }

    public void setUsuario_fin_standby(String usuario_fin_standby) {
        this.usuario_fin_standby = usuario_fin_standby;
    }

    public String getFecha_fin_standby() {
        return fecha_fin_standby;
    }

    public void setFecha_fin_standby(String fecha_fin_standby) {
        this.fecha_fin_standby = fecha_fin_standby;
    }

    public String getCedula_persona_recibe() {
        return cedula_persona_recibe;
    }

    public void setCedula_persona_recibe(String cedula_persona_recibe) {
        this.cedula_persona_recibe = cedula_persona_recibe;
    }

    public String getNombre_persona_recibe() {
        return nombre_persona_recibe;
    }

    public void setNombre_persona_recibe(String nombre_persona_recibe) {
        this.nombre_persona_recibe = nombre_persona_recibe;
    }

    public String getUrl_firma_persona_recibe() {
        return url_firma_persona_recibe;
    }

    public void setUrl_firma_persona_recibe(String url_firma_persona_recibe) {
        this.url_firma_persona_recibe = url_firma_persona_recibe;
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

    public String getUrl_foto_entrega() {
        return url_foto_entrega;
    }

    public void setUrl_foto_entrega(String url_foto_entrega) {
        this.url_foto_entrega = url_foto_entrega;
    }

    public String getNum_equipo() {
        return num_equipo;
    }

    public void setNum_equipo(String num_equipo) {
        this.num_equipo = num_equipo;
    }

    public String getNum_generador() {
        return num_generador;
    }

    public void setNum_generador(String num_generador) {
        this.num_generador = num_generador;
    }

    public String getId_naviera() {
        return id_naviera;
    }

    public void setId_naviera(String id_naviera) {
        this.id_naviera = id_naviera;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public String getId_intermediario() {
        return id_intermediario;
    }

    public void setId_intermediario(String id_intermediario) {
        this.id_intermediario = id_intermediario;
    }

    public String getId_trx() {
        return id_trx;
    }

    public void setId_trx(String id_trx) {
        this.id_trx = id_trx;
    }

    public String getCobrado_transporte() {
        return cobrado_transporte;
    }

    public void setCobrado_transporte(String cobrado_transporte) {
        this.cobrado_transporte = cobrado_transporte;
    }

    public String getNumero_dt() {
        return numero_dt;
    }

    public void setNumero_dt(String numero_dt) {
        this.numero_dt = numero_dt;
    }

    public String getFecha_llegada_punto_carga() {
        return fecha_llegada_punto_carga;
    }

    public void setFecha_llegada_punto_carga(String fecha_llegada_punto_carga) {
        this.fecha_llegada_punto_carga = fecha_llegada_punto_carga;
    }

    public String getFecha_inicio_carga() {
        return fecha_inicio_carga;
    }

    public void setFecha_inicio_carga(String fecha_inicio_carga) {
        this.fecha_inicio_carga = fecha_inicio_carga;
    }

    public String getFecha_fin_carga() {
        return fecha_fin_carga;
    }

    public void setFecha_fin_carga(String fecha_fin_carga) {
        this.fecha_fin_carga = fecha_fin_carga;
    }

    public String getFecha_salida_punto_carga() {
        return fecha_salida_punto_carga;
    }

    public void setFecha_salida_punto_carga(String fecha_salida_punto_carga) {
        this.fecha_salida_punto_carga = fecha_salida_punto_carga;
    }

    public String getValor_viaje() {
        return valor_viaje;
    }

    public void setValor_viaje(String valor_viaje) {
        this.valor_viaje = valor_viaje;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public String getStandby() {
        return standby;
    }

    public void setStandby(String standby) {
        this.standby = standby;
    }

    public String getFlete_falso() {
        return flete_falso;
    }

    public void setFlete_falso(String flete_falso) {
        this.flete_falso = flete_falso;
    }

    public String getDoble_flete() {
        return doble_flete;
    }

    public void setDoble_flete(String doble_flete) {
        this.doble_flete = doble_flete;
    }

    public String getAjuste() {
        return ajuste;
    }

    public void setAjuste(String ajuste) {
        this.ajuste = ajuste;
    }

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }

    public String getUrl_archivo_contenedor() {
        return url_archivo_contenedor;
    }

    public void setUrl_archivo_contenedor(String url_archivo_contenedor) {
        this.url_archivo_contenedor = url_archivo_contenedor;
    }

    public String getUrl_excel_ruta() {
        return url_excel_ruta;
    }

    public void setUrl_excel_ruta(String url_excel_ruta) {
        this.url_excel_ruta = url_excel_ruta;
    }

    public String getUrl_archivo_contenedor2() {
        return url_archivo_contenedor2;
    }

    public void setUrl_archivo_contenedor2(String url_archivo_contenedor2) {
        this.url_archivo_contenedor2 = url_archivo_contenedor2;
    }

    public List<com.smartapp.depc_ice.Entities.DetalleViaje> getDetalleViaje() {
        return DetalleViaje;
    }

    public void setDetalleViaje(List<com.smartapp.depc_ice.Entities.DetalleViaje> detalleViaje) {
        DetalleViaje = detalleViaje;
    }
}
