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
}
