package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_CUENTA_BANCOS)
public class CuentaBancos {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String empresa;
    @DatabaseField
    private String cuenta;
    @DatabaseField
    private String banco;
    @DatabaseField
    private String moneda;
    @DatabaseField
    private String num_cuenta;
    @DatabaseField
    private String tipo_cta;
    @DatabaseField
    private String f_apertura;
    @DatabaseField
    private String oficial_credito;
    @DatabaseField
    private String saldo_fecha;
    @DatabaseField
    private String centro_cta;
    @DatabaseField
    private String elemento_cta;
    @DatabaseField
    private String auxiliar_cta;
    @DatabaseField
    private String idioma_che;
    @DatabaseField
    private String chequea_sec;
    @DatabaseField
    private String inicio_sec;
    @DatabaseField
    private String sucursal_banco;
    @DatabaseField
    private String formato_rep;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String saldo_inicial;
    @DatabaseField
    private String centro_costo_id;
    @DatabaseField
    private String cta_rol;
    @DatabaseField
    private String es_cobro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getNum_cuenta() {
        return num_cuenta;
    }

    public void setNum_cuenta(String num_cuenta) {
        this.num_cuenta = num_cuenta;
    }

    public String getTipo_cta() {
        return tipo_cta;
    }

    public void setTipo_cta(String tipo_cta) {
        this.tipo_cta = tipo_cta;
    }

    public String getF_apertura() {
        return f_apertura;
    }

    public void setF_apertura(String f_apertura) {
        this.f_apertura = f_apertura;
    }

    public String getOficial_credito() {
        return oficial_credito;
    }

    public void setOficial_credito(String oficial_credito) {
        this.oficial_credito = oficial_credito;
    }

    public String getSaldo_fecha() {
        return saldo_fecha;
    }

    public void setSaldo_fecha(String saldo_fecha) {
        this.saldo_fecha = saldo_fecha;
    }

    public String getCentro_cta() {
        return centro_cta;
    }

    public void setCentro_cta(String centro_cta) {
        this.centro_cta = centro_cta;
    }

    public String getElemento_cta() {
        return elemento_cta;
    }

    public void setElemento_cta(String elemento_cta) {
        this.elemento_cta = elemento_cta;
    }

    public String getAuxiliar_cta() {
        return auxiliar_cta;
    }

    public void setAuxiliar_cta(String auxiliar_cta) {
        this.auxiliar_cta = auxiliar_cta;
    }

    public String getIdioma_che() {
        return idioma_che;
    }

    public void setIdioma_che(String idioma_che) {
        this.idioma_che = idioma_che;
    }

    public String getChequea_sec() {
        return chequea_sec;
    }

    public void setChequea_sec(String chequea_sec) {
        this.chequea_sec = chequea_sec;
    }

    public String getInicio_sec() {
        return inicio_sec;
    }

    public void setInicio_sec(String inicio_sec) {
        this.inicio_sec = inicio_sec;
    }

    public String getSucursal_banco() {
        return sucursal_banco;
    }

    public void setSucursal_banco(String sucursal_banco) {
        this.sucursal_banco = sucursal_banco;
    }

    public String getFormato_rep() {
        return formato_rep;
    }

    public void setFormato_rep(String formato_rep) {
        this.formato_rep = formato_rep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(String saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public String getCentro_costo_id() {
        return centro_costo_id;
    }

    public void setCentro_costo_id(String centro_costo_id) {
        this.centro_costo_id = centro_costo_id;
    }

    public String getCta_rol() {
        return cta_rol;
    }

    public void setCta_rol(String cta_rol) {
        this.cta_rol = cta_rol;
    }

    public String getEs_cobro() {
        return es_cobro;
    }

    public void setEs_cobro(String es_cobro) {
        this.es_cobro = es_cobro;
    }
}
