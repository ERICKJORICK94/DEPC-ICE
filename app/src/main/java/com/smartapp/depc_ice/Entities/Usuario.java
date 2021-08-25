package com.smartapp.depc_ice.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.smartapp.depc_ice.Utils.Const;

@DatabaseTable(tableName = Const.TABLE_USUARIO)
public class Usuario {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String usuario;
    @DatabaseField
    private String login;
    @DatabaseField
    private String nombrescompletos;
    @DatabaseField
    private String direccion;
    @DatabaseField
    private String telefono;
    @DatabaseField
    private String fechanacimiento;
    @DatabaseField
    private int id_personal_operaciones;
    @DatabaseField
    private int pto_vta_id_operaciones;
    @DatabaseField
    private int perfil;

    @DatabaseField
    private String ruc;
    @DatabaseField
    private String n_empresa;
    @DatabaseField
    private String n_comercial;
    @DatabaseField
    private String provincia_matriz;
    @DatabaseField
    private String pais_matriz;
    @DatabaseField
    private String numero_contribuyente_especial;
    @DatabaseField
    private String obligado_contabilidad;
    @DatabaseField
    private String direccion_sucursal;
    @DatabaseField
    private String ciudad_sucursal;
    @DatabaseField
    private String provincia_sucursal;
    @DatabaseField
    private String pais_sucursal;
    @DatabaseField
    private String ciudad_matriz;
    @DatabaseField
    private String telefono_sucursal;
    @DatabaseField
    private String direccion_matriz;
    @DatabaseField
    private String telefono_matriz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombrescompletos() {
        return nombrescompletos;
    }

    public void setNombrescompletos(String nombrescompletos) {
        this.nombrescompletos = nombrescompletos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(String fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public int getId_personal_operaciones() {
        return id_personal_operaciones;
    }

    public void setId_personal_operaciones(int id_personal_operaciones) {
        this.id_personal_operaciones = id_personal_operaciones;
    }

    public int getPto_vta_id_operaciones() {
        return pto_vta_id_operaciones;
    }

    public void setPto_vta_id_operaciones(int pto_vta_id_operaciones) {
        this.pto_vta_id_operaciones = pto_vta_id_operaciones;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getN_empresa() {
        return n_empresa;
    }

    public void setN_empresa(String n_empresa) {
        this.n_empresa = n_empresa;
    }

    public String getN_comercial() {
        return n_comercial;
    }

    public void setN_comercial(String n_comercial) {
        this.n_comercial = n_comercial;
    }

    public String getProvincia_matriz() {
        return provincia_matriz;
    }

    public void setProvincia_matriz(String provincia_matriz) {
        this.provincia_matriz = provincia_matriz;
    }

    public String getPais_matriz() {
        return pais_matriz;
    }

    public void setPais_matriz(String pais_matriz) {
        this.pais_matriz = pais_matriz;
    }

    public String getNumero_contribuyente_especial() {
        return numero_contribuyente_especial;
    }

    public void setNumero_contribuyente_especial(String numero_contribuyente_especial) {
        this.numero_contribuyente_especial = numero_contribuyente_especial;
    }

    public String getObligado_contabilidad() {
        return obligado_contabilidad;
    }

    public void setObligado_contabilidad(String obligado_contabilidad) {
        this.obligado_contabilidad = obligado_contabilidad;
    }

    public String getDireccion_sucursal() {
        return direccion_sucursal;
    }

    public void setDireccion_sucursal(String direccion_sucursal) {
        this.direccion_sucursal = direccion_sucursal;
    }

    public String getCiudad_sucursal() {
        return ciudad_sucursal;
    }

    public void setCiudad_sucursal(String ciudad_sucursal) {
        this.ciudad_sucursal = ciudad_sucursal;
    }

    public String getProvincia_sucursal() {
        return provincia_sucursal;
    }

    public void setProvincia_sucursal(String provincia_sucursal) {
        this.provincia_sucursal = provincia_sucursal;
    }

    public String getPais_sucursal() {
        return pais_sucursal;
    }

    public void setPais_sucursal(String pais_sucursal) {
        this.pais_sucursal = pais_sucursal;
    }

    public String getCiudad_matriz() {
        return ciudad_matriz;
    }

    public void setCiudad_matriz(String ciudad_matriz) {
        this.ciudad_matriz = ciudad_matriz;
    }

    public String getTelefono_sucursal() {
        return telefono_sucursal;
    }

    public void setTelefono_sucursal(String telefono_sucursal) {
        this.telefono_sucursal = telefono_sucursal;
    }

    public String getDireccion_matriz() {
        return direccion_matriz;
    }

    public void setDireccion_matriz(String direccion_matriz) {
        this.direccion_matriz = direccion_matriz;
    }

    public String getTelefono_matriz() {
        return telefono_matriz;
    }

    public void setTelefono_matriz(String telefono_matriz) {
        this.telefono_matriz = telefono_matriz;
    }
}
