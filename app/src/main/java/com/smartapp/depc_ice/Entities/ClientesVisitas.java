package com.smartapp.depc_ice.Entities;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

import java.io.Serializable;

@DatabaseTable(tableName = Const.TABLE_CLIENTE_VISITA)
public class ClientesVisitas implements Serializable {

    @DatabaseField(generatedId = true)
    private int primaryKey;
    @DatabaseField
    private String id;
    @DatabaseField
    private String cliente_id;
    @DatabaseField
    private String direccion_envio;
    @DatabaseField
    private String zona_id;
    @DatabaseField
    private String nombre_contacto;
    @DatabaseField
    private String telefono_contacto;
    @DatabaseField
    private String extension_contacto;
    @DatabaseField
    private String email_contacto;
    @DatabaseField
    private String pais;
    @DatabaseField
    private String provincia;
    @DatabaseField
    private String ciudad;
    @DatabaseField
    private String parroquia;
    @DatabaseField
    private String dia_visita;
    @DatabaseField
    private String latitud;
    @DatabaseField
    private String longitud;
    @DatabaseField
    private String foto;
    @DatabaseField
    private String codigo;
    @DatabaseField
    private String tipo_id_tercero;
    @DatabaseField
    private String tercero_id;
    @DatabaseField
    private String direccion;
    @DatabaseField
    private String telefono1;
    @DatabaseField
    private String telefono2;
    @DatabaseField
    private String celular;
    @DatabaseField
    private String email;
    @DatabaseField
    private String sw_persona_juridica;
    @DatabaseField
    private String usuario_id;
    @DatabaseField
    private String fecha_registro;
    @DatabaseField
    private String nombre_tercero;
    @DatabaseField
    private String representante;
    @DatabaseField
    private String direccion_cobro;
    @DatabaseField
    private String nombre_comercial;
    @DatabaseField
    private String pais_id;
    @DatabaseField
    private String provincia_id;
    @DatabaseField
    private String ciudad_id;
    @DatabaseField
    private String parroquia_id;
    @DatabaseField
    private String direccion2;
    @DatabaseField
    private String sector_id;
    @DatabaseField
    private String pto_vta_id;
    @DatabaseField
    private String hora;
    @DatabaseField
    private String estado;
    @DatabaseField
    private String fecha;
    @DatabaseField
    private String firma;
    @DatabaseField
    private String comentario;
    @DatabaseField
    private String atiende;

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getDireccion_envio() {
        return direccion_envio;
    }

    public void setDireccion_envio(String direccion_envio) {
        this.direccion_envio = direccion_envio;
    }

    public String getZona_id() {
        return zona_id;
    }

    public void setZona_id(String zona_id) {
        this.zona_id = zona_id;
    }

    public String getNombre_contacto() {
        return nombre_contacto;
    }

    public void setNombre_contacto(String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }

    public String getTelefono_contacto() {
        return telefono_contacto;
    }

    public void setTelefono_contacto(String telefono_contacto) {
        this.telefono_contacto = telefono_contacto;
    }

    public String getExtension_contacto() {
        return extension_contacto;
    }

    public void setExtension_contacto(String extension_contacto) {
        this.extension_contacto = extension_contacto;
    }

    public String getEmail_contacto() {
        return email_contacto;
    }

    public void setEmail_contacto(String email_contacto) {
        this.email_contacto = email_contacto;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getDia_visita() {
        return dia_visita;
    }

    public void setDia_visita(String dia_visita) {
        this.dia_visita = dia_visita;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo_id_tercero() {
        return tipo_id_tercero;
    }

    public void setTipo_id_tercero(String tipo_id_tercero) {
        this.tipo_id_tercero = tipo_id_tercero;
    }

    public String getTercero_id() {
        return tercero_id;
    }

    public void setTercero_id(String tercero_id) {
        this.tercero_id = tercero_id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSw_persona_juridica() {
        return sw_persona_juridica;
    }

    public void setSw_persona_juridica(String sw_persona_juridica) {
        this.sw_persona_juridica = sw_persona_juridica;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getNombre_tercero() {
        return nombre_tercero;
    }

    public void setNombre_tercero(String nombre_tercero) {
        this.nombre_tercero = nombre_tercero;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getDireccion_cobro() {
        return direccion_cobro;
    }

    public void setDireccion_cobro(String direccion_cobro) {
        this.direccion_cobro = direccion_cobro;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }

    public String getPais_id() {
        return pais_id;
    }

    public void setPais_id(String pais_id) {
        this.pais_id = pais_id;
    }

    public String getProvincia_id() {
        return provincia_id;
    }

    public void setProvincia_id(String provincia_id) {
        this.provincia_id = provincia_id;
    }

    public String getCiudad_id() {
        return ciudad_id;
    }

    public void setCiudad_id(String ciudad_id) {
        this.ciudad_id = ciudad_id;
    }

    public String getParroquia_id() {
        return parroquia_id;
    }

    public void setParroquia_id(String parroquia_id) {
        this.parroquia_id = parroquia_id;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getPto_vta_id() {
        return pto_vta_id;
    }

    public void setPto_vta_id(String pto_vta_id) {
        this.pto_vta_id = pto_vta_id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getAtiende() {
        return atiende;
    }

    public void setAtiende(String atiende) {
        this.atiende = atiende;
    }
}
