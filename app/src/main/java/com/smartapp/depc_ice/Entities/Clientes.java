package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.io.Serializable;

@DatabaseTable(tableName = Const.TABLE_CLIENTES)
public class Clientes implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String atrasado;
    @DatabaseField
    private String bloqueado;
    @DatabaseField
    private String categoria_cliente;
    @DatabaseField
    private String categoria_id;
    @DatabaseField
    private String celular;
    @DatabaseField
    private String ciudad;
    @DatabaseField
    private String ciudad_id;
    @DatabaseField
    private String clase_cliente;
    @DatabaseField
    private String clasificacion;
    @DatabaseField
    private String cliente_contabilidad;
    @DatabaseField
    private String cliente_especial;
    @DatabaseField
    private String cliente_origen_ingresos;
    @DatabaseField
    private String cliente_recordvenci;
    @DatabaseField
    private String cobrar_iva;
    @DatabaseField
    private String codigo_cliente_id;
    @DatabaseField
    private String cod_cliente_empresa;
    @DatabaseField
    private String contribuyente_especial;
    @DatabaseField
    private String descuento_maximo;
    @DatabaseField
    private String dias_atrasados;
    @DatabaseField
    private String dias_credito;
    @DatabaseField
    private String dia_max;
    @DatabaseField
    private String direccion;
    @DatabaseField
    private String direccion2;
    @DatabaseField
    private String direccion_cobro;
    @DatabaseField
    private String dir_imagen;
    @DatabaseField
    private String d_promedios;
    @DatabaseField
    private String email;
    @DatabaseField
    private String empresa_id;
    @DatabaseField
    private String usuario;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String estado_civil;
    @DatabaseField
    private String exportador;
    @DatabaseField
    private String fecha_primera_fct;
    @DatabaseField
    private String fecha_registro;
    @DatabaseField
    private String forma_pago;
    @DatabaseField
    private String galonage;
    @DatabaseField
    private String grupo_id;
    @DatabaseField
    private String monto_credito;
    @DatabaseField
    private String nombre_comercial;
    @DatabaseField
    private String nombre_tercero;
    @DatabaseField
    private String no_aplica_ret;
    @DatabaseField
    private String n_atrasado;
    @DatabaseField
    private String observaciones;
    @DatabaseField
    private String pais_id;
    @DatabaseField
    private String parroquia_id;
    @DatabaseField
    private String posfechado;
    @DatabaseField
    private String provincia_id;
    @DatabaseField
    private String representante;
    @DatabaseField
    private String retencion_id;
    @DatabaseField
    private String retencion_oblig;
    @DatabaseField
    private String sector_id;
    @DatabaseField
    private String sexo;
    @DatabaseField
    private String sociedad;
    @DatabaseField
    private String sw_persona_juridica;
    @DatabaseField
    private String telefono1;
    @DatabaseField
    private String telefono2;
    @DatabaseField
    private String tercero_id;
    @DatabaseField
    private String tipo_cliente;
    @DatabaseField
    private String tipo_id_tercero;
    @DatabaseField
    private String tipo_pago_id;
    @DatabaseField
    private String usuario_id;
    @DatabaseField
    private String verificado;
    @DatabaseField
    private String zona;
    @DatabaseField
    private String zona_id;
    @DatabaseField
    private String cliente_id;
    @DatabaseField
    private String documentopdf;
    @DatabaseField
    private String telefono;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAtrasado() {
        return atrasado;
    }

    public void setAtrasado(String atrasado) {
        this.atrasado = atrasado;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getCategoria_cliente() {
        return categoria_cliente;
    }

    public void setCategoria_cliente(String categoria_cliente) {
        this.categoria_cliente = categoria_cliente;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCiudad_id() {
        return ciudad_id;
    }

    public void setCiudad_id(String ciudad_id) {
        this.ciudad_id = ciudad_id;
    }

    public String getClase_cliente() {
        return clase_cliente;
    }

    public void setClase_cliente(String clase_cliente) {
        this.clase_cliente = clase_cliente;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getCliente_contabilidad() {
        return cliente_contabilidad;
    }

    public void setCliente_contabilidad(String cliente_contabilidad) {
        this.cliente_contabilidad = cliente_contabilidad;
    }

    public String getCliente_especial() {
        return cliente_especial;
    }

    public void setCliente_especial(String cliente_especial) {
        this.cliente_especial = cliente_especial;
    }

    public String getCliente_origen_ingresos() {
        return cliente_origen_ingresos;
    }

    public void setCliente_origen_ingresos(String cliente_origen_ingresos) {
        this.cliente_origen_ingresos = cliente_origen_ingresos;
    }

    public String getCliente_recordvenci() {
        return cliente_recordvenci;
    }

    public void setCliente_recordvenci(String cliente_recordvenci) {
        this.cliente_recordvenci = cliente_recordvenci;
    }

    public String getCobrar_iva() {
        return cobrar_iva;
    }

    public void setCobrar_iva(String cobrar_iva) {
        this.cobrar_iva = cobrar_iva;
    }

    public String getCodigo_cliente_id() {
        return codigo_cliente_id;
    }

    public void setCodigo_cliente_id(String codigo_cliente_id) {
        this.codigo_cliente_id = codigo_cliente_id;
    }

    public String getCod_cliente_empresa() {
        return cod_cliente_empresa;
    }

    public void setCod_cliente_empresa(String cod_cliente_empresa) {
        this.cod_cliente_empresa = cod_cliente_empresa;
    }

    public String getContribuyente_especial() {
        return contribuyente_especial;
    }

    public void setContribuyente_especial(String contribuyente_especial) {
        this.contribuyente_especial = contribuyente_especial;
    }

    public String getDescuento_maximo() {
        return descuento_maximo;
    }

    public void setDescuento_maximo(String descuento_maximo) {
        this.descuento_maximo = descuento_maximo;
    }

    public String getDias_atrasados() {
        return dias_atrasados;
    }

    public void setDias_atrasados(String dias_atrasados) {
        this.dias_atrasados = dias_atrasados;
    }

    public String getDias_credito() {
        return dias_credito;
    }

    public void setDias_credito(String dias_credito) {
        this.dias_credito = dias_credito;
    }

    public String getDia_max() {
        return dia_max;
    }

    public void setDia_max(String dia_max) {
        this.dia_max = dia_max;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getDireccion_cobro() {
        return direccion_cobro;
    }

    public void setDireccion_cobro(String direccion_cobro) {
        this.direccion_cobro = direccion_cobro;
    }

    public String getDir_imagen() {
        return dir_imagen;
    }

    public void setDir_imagen(String dir_imagen) {
        this.dir_imagen = dir_imagen;
    }

    public String getD_promedios() {
        return d_promedios;
    }

    public void setD_promedios(String d_promedios) {
        this.d_promedios = d_promedios;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpresa_id() {
        return empresa_id;
    }

    public void setEmpresa_id(String empresa_id) {
        this.empresa_id = empresa_id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public String getExportador() {
        return exportador;
    }

    public void setExportador(String exportador) {
        this.exportador = exportador;
    }

    public String getFecha_primera_fct() {
        return fecha_primera_fct;
    }

    public void setFecha_primera_fct(String fecha_primera_fct) {
        this.fecha_primera_fct = fecha_primera_fct;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getGalonage() {
        return galonage;
    }

    public void setGalonage(String galonage) {
        this.galonage = galonage;
    }

    public String getGrupo_id() {
        return grupo_id;
    }

    public void setGrupo_id(String grupo_id) {
        this.grupo_id = grupo_id;
    }

    public String getMonto_credito() {
        return monto_credito;
    }

    public void setMonto_credito(String monto_credito) {
        this.monto_credito = monto_credito;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getNombre_tercero() {
        return nombre_tercero;
    }

    public void setNombre_tercero(String nombre_tercero) {
        this.nombre_tercero = nombre_tercero;
    }

    public String getNo_aplica_ret() {
        return no_aplica_ret;
    }

    public void setNo_aplica_ret(String no_aplica_ret) {
        this.no_aplica_ret = no_aplica_ret;
    }

    public String getN_atrasado() {
        return n_atrasado;
    }

    public void setN_atrasado(String n_atrasado) {
        this.n_atrasado = n_atrasado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPais_id() {
        return pais_id;
    }

    public void setPais_id(String pais_id) {
        this.pais_id = pais_id;
    }

    public String getParroquia_id() {
        return parroquia_id;
    }

    public void setParroquia_id(String parroquia_id) {
        this.parroquia_id = parroquia_id;
    }

    public String getPosfechado() {
        return posfechado;
    }

    public void setPosfechado(String posfechado) {
        this.posfechado = posfechado;
    }

    public String getProvincia_id() {
        return provincia_id;
    }

    public void setProvincia_id(String provincia_id) {
        this.provincia_id = provincia_id;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getRetencion_id() {
        return retencion_id;
    }

    public void setRetencion_id(String retencion_id) {
        this.retencion_id = retencion_id;
    }

    public String getRetencion_oblig() {
        return retencion_oblig;
    }

    public void setRetencion_oblig(String retencion_oblig) {
        this.retencion_oblig = retencion_oblig;
    }

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSociedad() {
        return sociedad;
    }

    public void setSociedad(String sociedad) {
        this.sociedad = sociedad;
    }

    public String getSw_persona_juridica() {
        return sw_persona_juridica;
    }

    public void setSw_persona_juridica(String sw_persona_juridica) {
        this.sw_persona_juridica = sw_persona_juridica;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getTercero_id() {
        return tercero_id;
    }

    public void setTercero_id(String tercero_id) {
        this.tercero_id = tercero_id;
    }

    public String getTipo_cliente() {
        return tipo_cliente;
    }

    public void setTipo_cliente(String tipo_cliente) {
        this.tipo_cliente = tipo_cliente;
    }

    public String getTipo_id_tercero() {
        return tipo_id_tercero;
    }

    public void setTipo_id_tercero(String tipo_id_tercero) {
        this.tipo_id_tercero = tipo_id_tercero;
    }

    public String getTipo_pago_id() {
        return tipo_pago_id;
    }

    public void setTipo_pago_id(String tipo_pago_id) {
        this.tipo_pago_id = tipo_pago_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getVerificado() {
        return verificado;
    }

    public void setVerificado(String verificado) {
        this.verificado = verificado;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getZona_id() {
        return zona_id;
    }

    public void setZona_id(String zona_id) {
        this.zona_id = zona_id;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getDocumentopdf() {
        return documentopdf;
    }

    public void setDocumentopdf(String documentopdf) {
        this.documentopdf = documentopdf;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
