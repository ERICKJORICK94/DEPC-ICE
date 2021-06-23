package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_DETALLE_FACTURAS)
public class DetalleFacturas {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String factura_id;
    @DatabaseField
    private String cuenta_id;
    @DatabaseField
    private String prefijo1;
    @DatabaseField
    private String prefijo2;
    @DatabaseField
    private String factura_fiscal;
    @DatabaseField
    private String impuesto;
    @DatabaseField
    private String base_imponible;
    @DatabaseField
    private String total_descuentos;
    @DatabaseField
    private String subtotal;
    @DatabaseField
    private String total_factura;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String notas;
    @DatabaseField
    private String tipo_comprobante_id;
    @DatabaseField
    private String lista_ids_detalles;
    @DatabaseField
    private String transporte;
    @DatabaseField
    private String switchcompleto;
    @DatabaseField
    private String fecha_emision;
    @DatabaseField
    private String cliente_id;
    @DatabaseField
    private String forma_pago;
    @DatabaseField
    private String dias_credito;
    @DatabaseField
    private String usuario_id;
    @DatabaseField
    private String ptovta_id;
    @DatabaseField
    private String max_items;
    @DatabaseField
    private String asiento;
    @DatabaseField
    private String asiento_costos;
    @DatabaseField
    private String vendedor_id;
    @DatabaseField
    private String blq_despacho;
    @DatabaseField
    private String fct_ptovta_doc_id;
    @DatabaseField
    private String sw_reingreso;
    @DatabaseField
    private String prioridad_id;
    @DatabaseField
    private String fct_zona;
    @DatabaseField
    private String fct_direccion_envio;
    @DatabaseField
    private String cliente_destinatario;
    @DatabaseField
    private String ref_guia;
    @DatabaseField
    private String fct_direccion_origen;
    @DatabaseField
    private String punto_cobro;
    @DatabaseField
    private String guias_fct;
    @DatabaseField
    private String vendedor_tipo;
    @DatabaseField
    private String vendedor_id2;
    @DatabaseField
    private String tipo_factura;
    @DatabaseField
    private String saldo;
    @DatabaseField
    private String observaciones;
    @DatabaseField
    private String banco;
    @DatabaseField
    private String ordcompra_cliente;
    @DatabaseField
    private String fecha_despacho;
    @DatabaseField
    private String fecha_entregada;
    @DatabaseField
    private String exportacion;
    @DatabaseField
    private String fecha_recibida;
    @DatabaseField
    private String instancia_comp_elect;
    @DatabaseField
    private String forma_pago_sri;
    @DatabaseField
    private String fecha_vencimiento;
    @DatabaseField
    private String ot_interna;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFactura_id() {
        return factura_id;
    }

    public void setFactura_id(String factura_id) {
        this.factura_id = factura_id;
    }

    public String getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(String cuenta_id) {
        this.cuenta_id = cuenta_id;
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

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public String getBase_imponible() {
        return base_imponible;
    }

    public void setBase_imponible(String base_imponible) {
        this.base_imponible = base_imponible;
    }

    public String getTotal_descuentos() {
        return total_descuentos;
    }

    public void setTotal_descuentos(String total_descuentos) {
        this.total_descuentos = total_descuentos;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal_factura() {
        return total_factura;
    }

    public void setTotal_factura(String total_factura) {
        this.total_factura = total_factura;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getTipo_comprobante_id() {
        return tipo_comprobante_id;
    }

    public void setTipo_comprobante_id(String tipo_comprobante_id) {
        this.tipo_comprobante_id = tipo_comprobante_id;
    }

    public String getLista_ids_detalles() {
        return lista_ids_detalles;
    }

    public void setLista_ids_detalles(String lista_ids_detalles) {
        this.lista_ids_detalles = lista_ids_detalles;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public String getSwitchcompleto() {
        return switchcompleto;
    }

    public void setSwitchcompleto(String switchcompleto) {
        this.switchcompleto = switchcompleto;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getDias_credito() {
        return dias_credito;
    }

    public void setDias_credito(String dias_credito) {
        this.dias_credito = dias_credito;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getPtovta_id() {
        return ptovta_id;
    }

    public void setPtovta_id(String ptovta_id) {
        this.ptovta_id = ptovta_id;
    }

    public String getMax_items() {
        return max_items;
    }

    public void setMax_items(String max_items) {
        this.max_items = max_items;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public String getAsiento_costos() {
        return asiento_costos;
    }

    public void setAsiento_costos(String asiento_costos) {
        this.asiento_costos = asiento_costos;
    }

    public String getVendedor_id() {
        return vendedor_id;
    }

    public void setVendedor_id(String vendedor_id) {
        this.vendedor_id = vendedor_id;
    }

    public String getBlq_despacho() {
        return blq_despacho;
    }

    public void setBlq_despacho(String blq_despacho) {
        this.blq_despacho = blq_despacho;
    }

    public String getFct_ptovta_doc_id() {
        return fct_ptovta_doc_id;
    }

    public void setFct_ptovta_doc_id(String fct_ptovta_doc_id) {
        this.fct_ptovta_doc_id = fct_ptovta_doc_id;
    }

    public String getSw_reingreso() {
        return sw_reingreso;
    }

    public void setSw_reingreso(String sw_reingreso) {
        this.sw_reingreso = sw_reingreso;
    }

    public String getPrioridad_id() {
        return prioridad_id;
    }

    public void setPrioridad_id(String prioridad_id) {
        this.prioridad_id = prioridad_id;
    }

    public String getFct_zona() {
        return fct_zona;
    }

    public void setFct_zona(String fct_zona) {
        this.fct_zona = fct_zona;
    }

    public String getFct_direccion_envio() {
        return fct_direccion_envio;
    }

    public void setFct_direccion_envio(String fct_direccion_envio) {
        this.fct_direccion_envio = fct_direccion_envio;
    }

    public String getCliente_destinatario() {
        return cliente_destinatario;
    }

    public void setCliente_destinatario(String cliente_destinatario) {
        this.cliente_destinatario = cliente_destinatario;
    }

    public String getRef_guia() {
        return ref_guia;
    }

    public void setRef_guia(String ref_guia) {
        this.ref_guia = ref_guia;
    }

    public String getFct_direccion_origen() {
        return fct_direccion_origen;
    }

    public void setFct_direccion_origen(String fct_direccion_origen) {
        this.fct_direccion_origen = fct_direccion_origen;
    }

    public String getPunto_cobro() {
        return punto_cobro;
    }

    public void setPunto_cobro(String punto_cobro) {
        this.punto_cobro = punto_cobro;
    }

    public String getGuias_fct() {
        return guias_fct;
    }

    public void setGuias_fct(String guias_fct) {
        this.guias_fct = guias_fct;
    }

    public String getVendedor_tipo() {
        return vendedor_tipo;
    }

    public void setVendedor_tipo(String vendedor_tipo) {
        this.vendedor_tipo = vendedor_tipo;
    }

    public String getVendedor_id2() {
        return vendedor_id2;
    }

    public void setVendedor_id2(String vendedor_id2) {
        this.vendedor_id2 = vendedor_id2;
    }

    public String getTipo_factura() {
        return tipo_factura;
    }

    public void setTipo_factura(String tipo_factura) {
        this.tipo_factura = tipo_factura;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getOrdcompra_cliente() {
        return ordcompra_cliente;
    }

    public void setOrdcompra_cliente(String ordcompra_cliente) {
        this.ordcompra_cliente = ordcompra_cliente;
    }

    public String getFecha_despacho() {
        return fecha_despacho;
    }

    public void setFecha_despacho(String fecha_despacho) {
        this.fecha_despacho = fecha_despacho;
    }

    public String getFecha_entregada() {
        return fecha_entregada;
    }

    public void setFecha_entregada(String fecha_entregada) {
        this.fecha_entregada = fecha_entregada;
    }

    public String getExportacion() {
        return exportacion;
    }

    public void setExportacion(String exportacion) {
        this.exportacion = exportacion;
    }

    public String getFecha_recibida() {
        return fecha_recibida;
    }

    public void setFecha_recibida(String fecha_recibida) {
        this.fecha_recibida = fecha_recibida;
    }

    public String getInstancia_comp_elect() {
        return instancia_comp_elect;
    }

    public void setInstancia_comp_elect(String instancia_comp_elect) {
        this.instancia_comp_elect = instancia_comp_elect;
    }

    public String getForma_pago_sri() {
        return forma_pago_sri;
    }

    public void setForma_pago_sri(String forma_pago_sri) {
        this.forma_pago_sri = forma_pago_sri;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getOt_interna() {
        return ot_interna;
    }

    public void setOt_interna(String ot_interna) {
        this.ot_interna = ot_interna;
    }
}
