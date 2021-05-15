package com.smartapp.depc_ice.Entities;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.io.Serializable;

@DatabaseTable(tableName = Const.TABLE_CLIENTE_VISITA)
public class ClientesVisitas implements Serializable {

    @DatabaseField(generatedId = true,uniqueIndex = true)
    private int id;
    @DatabaseField
    private String CODIGO;
    @DatabaseField
    private String CODIGO1;
    @DatabaseField
    private String COMENTARIO;
    @DatabaseField
    private String FECHA;
    @DatabaseField
    private String FECHA_PROXIMA;
    @DatabaseField
    private String GC_MOTIVO;
    @DatabaseField
    private String HORA_INICIO;
    @DatabaseField
    private String NOM_VENDEDOR;
    @DatabaseField
    private String RAZON_SOCIAL;
    @DatabaseField
    private String VENDEDOR;
    @DatabaseField
    private String VISITA;
    @DatabaseField
    private String HORA_FIN ;
    @DatabaseField
    private String ValorVenta ;
    @DatabaseField
    private String ValorCobro ;
    @DatabaseField
    private String ValorVentaGuardada ;
    @DatabaseField
    private String ValorCobroGuardada;

    @DatabaseField
    private String HORA_INICIO_GUARDADA ;
    @DatabaseField
    private String HORA_FIN_GUARDADA ;
    @DatabaseField
    private Boolean CHECK_HORA_INICIO = false ;
    @DatabaseField
    private Boolean CHECK_HORA_FIN  = false ;

    @DatabaseField
    private String ID_MOTIVO = "";

    @DatabaseField
    private String FECHA_REAGENDAR = "";

    @DatabaseField
    private int PENDIENTE = 0;

    @DatabaseField
    private int PENDIENTE_REAGENDAR = 0;

    @DatabaseField
    private String FECHA_PROXIMA_GUARDADA ;

    @DatabaseField
    private String COMENTARIO_GUARDADO ;

    @DatabaseField
    private String LATITUD;

    @DatabaseField
    private String LONGITUD ;
    @DatabaseField
    private int idAgendar;
    @DatabaseField
    private String GC_VALOR_VENTA;
    @DatabaseField
    private String GC_VALOR_COBRO;

    public String getValorVenta() {
        return ValorVenta;
    }

    public String getValorVentaGuardada() {
        return ValorVentaGuardada;
    }

    public void setValorVentaGuardada(String valorVentaGuardada) {
        ValorVentaGuardada = valorVentaGuardada;
    }

    public String getValorCobroGuardada() {
        return ValorCobroGuardada;
    }

    public void setValorCobroGuardada(String valorCobroGuardada) {
        ValorCobroGuardada = valorCobroGuardada;
    }

    public void setValorVenta(String valorVenta) {
        ValorVenta = valorVenta;
    }

    public String getValorCobro() {
        return ValorCobro;
    }

    public void setValorCobro(String valorCobro) {
        ValorCobro = valorCobro;
    }

    public int getIdAgendar() {
        return idAgendar;
    }

    public void setIdAgendar(int idAgendar) {
        this.idAgendar = idAgendar;
    }

    public String getLATITUD() {
        return LATITUD;
    }

    public void setLATITUD(String LATITUD) {
        this.LATITUD = LATITUD;
    }

    public String getLONGITUD() {
        return LONGITUD;
    }

    public void setLONGITUD(String LONGITUD) {
        this.LONGITUD = LONGITUD;
    }

    public String getFECHA_REAGENDAR() {
        return FECHA_REAGENDAR;
    }

    public void setFECHA_REAGENDAR(String FECHA_REAGENDAR) {
        this.FECHA_REAGENDAR = FECHA_REAGENDAR;
    }

    public int getPENDIENTE_REAGENDAR() {
        return PENDIENTE_REAGENDAR;
    }

    public void setPENDIENTE_REAGENDAR(int PENDIENTE_REAGENDAR) {
        this.PENDIENTE_REAGENDAR = PENDIENTE_REAGENDAR;
    }

    public String getHORA_INICIO_GUARDADA() {
        return HORA_INICIO_GUARDADA;
    }

    public void setHORA_INICIO_GUARDADA(String HORA_INICIO_GUARDADA) {
        this.HORA_INICIO_GUARDADA = HORA_INICIO_GUARDADA;
    }

    public String getHORA_FIN_GUARDADA() {
        return HORA_FIN_GUARDADA;
    }

    public void setHORA_FIN_GUARDADA(String HORA_FIN_GUARDADA) {
        this.HORA_FIN_GUARDADA = HORA_FIN_GUARDADA;
    }

    public Boolean getCHECK_HORA_INICIO() {
        return CHECK_HORA_INICIO;
    }

    public void setCHECK_HORA_INICIO(Boolean CHECK_HORA_INICIO) {
        this.CHECK_HORA_INICIO = CHECK_HORA_INICIO;
    }

    public Boolean getCHECK_HORA_FIN() {
        return CHECK_HORA_FIN;
    }

    public void setCHECK_HORA_FIN(Boolean CHECK_HORA_FIN) {
        this.CHECK_HORA_FIN = CHECK_HORA_FIN;
    }

    @DatabaseField
    private String RUTA_COMERCIAL;

    public String getRUTA_COMERCIAL() {
        return RUTA_COMERCIAL;
    }

    public void setRUTA_COMERCIAL(String RUTA_COMERCIAL) {
        this.RUTA_COMERCIAL = RUTA_COMERCIAL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public String getCODIGO1() {
        return CODIGO1;
    }

    public void setCODIGO1(String CODIGO1) {
        this.CODIGO1 = CODIGO1;
    }

    public String getCOMENTARIO() {
        return COMENTARIO;
    }

    public void setCOMENTARIO(String COMENTARIO) {
        this.COMENTARIO = COMENTARIO;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getFECHA_PROXIMA() {
        return FECHA_PROXIMA;
    }

    public void setFECHA_PROXIMA(String FECHA_PROXIMA) {
        this.FECHA_PROXIMA = FECHA_PROXIMA;
    }

    public String getGC_MOTIVO() {
        return GC_MOTIVO;
    }

    public void setGC_MOTIVO(String GC_MOTIVO) {
        this.GC_MOTIVO = GC_MOTIVO;
    }

    public String getHORA_INICIO() {
        return HORA_INICIO;
    }

    public void setHORA_INICIO(String HORA_INICIO) {
        this.HORA_INICIO = HORA_INICIO;
    }

    public String getNOM_VENDEDOR() {
        return NOM_VENDEDOR;
    }

    public void setNOM_VENDEDOR(String NOM_VENDEDOR) {
        this.NOM_VENDEDOR = NOM_VENDEDOR;
    }

    public String getRAZON_SOCIAL() {
        return RAZON_SOCIAL;
    }

    public void setRAZON_SOCIAL(String RAZON_SOCIAL) {
        this.RAZON_SOCIAL = RAZON_SOCIAL;
    }

    public String getVENDEDOR() {
        return VENDEDOR;
    }

    public void setVENDEDOR(String VENDEDOR) {
        this.VENDEDOR = VENDEDOR;
    }

    public String getVISITA() {
        return VISITA;
    }

    public void setVISITA(String VISITA) {
        this.VISITA = VISITA;
    }

    public String getHORA_FIN() {
        return HORA_FIN;
    }

    public void setHORA_FIN(String HORA_FIN) {
        this.HORA_FIN = HORA_FIN;
    }

    public String getCOMENTARIO_GUARDADO() {
        return COMENTARIO_GUARDADO;
    }

    public void setCOMENTARIO_GUARDADO(String COMENTARIO_GUARDADO) {
        this.COMENTARIO_GUARDADO = COMENTARIO_GUARDADO;
    }

    public String getID_MOTIVO() {
        return ID_MOTIVO;
    }

    public void setID_MOTIVO(String ID_MOTIVO) {
        this.ID_MOTIVO = ID_MOTIVO;
    }


    public int getPENDIENTE() {
        return PENDIENTE;
    }

    public void setPENDIENTE(int PENDIENTE) {
        this.PENDIENTE = PENDIENTE;
    }

    public String getFECHA_PROXIMA_GUARDADA() {
        return FECHA_PROXIMA_GUARDADA;
    }

    public void setFECHA_PROXIMA_GUARDADA(String FECHA_PROXIMA_GUARDADA) {
        this.FECHA_PROXIMA_GUARDADA = FECHA_PROXIMA_GUARDADA;
    }

    public String getGC_VALOR_VENTA() {
        return GC_VALOR_VENTA;
    }

    public void setGC_VALOR_VENTA(String GC_VALOR_VENTA) {
        this.GC_VALOR_VENTA = GC_VALOR_VENTA;
    }

    public String getGC_VALOR_COBRO() {
        return GC_VALOR_COBRO;
    }

    public void setGC_VALOR_COBRO(String GC_VALOR_COBRO) {
        this.GC_VALOR_COBRO = GC_VALOR_COBRO;
    }
}
