package com.smartapp.depc_ice.Database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Utils.Const;

import java.sql.SQLException;
import java.util.List;

public class DataBaseHelper {


    //HELPER USUARIO
    public static void saveUsuario(Usuario emp, Dao<Usuario, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUUsuario(Dao<Usuario, Integer> userDao) throws SQLException {
        DeleteBuilder<Usuario, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Usuario> getUsuario(Dao<Usuario, Integer> userDao) throws SQLException {

        List<Usuario> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_USUARIO;
        GenericRawResults<Usuario> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER CLIENTES
    public static void saveClientes(Clientes cliente, Dao<Clientes, Integer> clienteDao) {
        try {

            List<Clientes> cls = getClientesByCODCLiente(clienteDao, "" + cliente.getCodigo_cliente_id());
            if (cls != null) {
                if (cls.size() > 0) {
                    Clientes cl = cls.get(0);
                    cliente.setId(cl.getId());
                    clienteDao.update(cliente);
                    return;
                }
            }
            clienteDao.create(cliente);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateClientes(Clientes cliente, Dao<Clientes, Integer> clienteDao) {
        try {

            clienteDao.update(cliente);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteClientes(Dao<Clientes, Integer> clienteDao) throws SQLException {
        DeleteBuilder<Clientes, Integer> deleteBuilder = clienteDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Clientes> getClientes(Dao<Clientes, Integer> clienteDao) throws SQLException {

        List<Clientes> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_CLIENTES + " ORDER BY nombre_comercial LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<Clientes> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }

    public static List<Clientes> getClientesSearch(Dao<Clientes, Integer> clienteDao, String condicion) throws SQLException {

        List<Clientes> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_CLIENTES + " WHERE " + condicion + " ORDER BY nombre_comercial LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<Clientes> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }


    public static List<Clientes> getClientesByCODCLiente(Dao<Clientes, Integer> clienteDao, String codigoCleinte) throws SQLException {

        List<Clientes> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_CLIENTES + " WHERE codigo_cliente_id = '" + codigoCleinte + "'";
        GenericRawResults<Clientes> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }

    //HELPER BODEGAS
    public static void saveBodega(Bodega emp, Dao<Bodega, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBodega(Dao<Bodega, Integer> userDao) throws SQLException {
        DeleteBuilder<Bodega, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Bodega> getBodega(Dao<Bodega, Integer> userDao) throws SQLException {

        List<Bodega> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_BODEGAS;
        GenericRawResults<Bodega> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<Bodega> getBodegaSearch(Dao<Bodega, Integer> userDao, String condicion) throws SQLException {

        List<Bodega> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_BODEGAS + " WHERE " + condicion;
        GenericRawResults<Bodega> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }


    //HELPER PRODUCTO
    public static void saveProduto(Productos producto, Dao<Productos, Integer> proDao) {
        try {
            List<Productos> prs = getProductoByCOD(proDao, "" + producto.getCodigo_item());
            if (prs != null) {
                if (prs.size() > 0) {
                    Productos pr = prs.get(0);
                    producto.setId(pr.getId());
                    proDao.update(producto);
                    return;
                }
            }
            proDao.create(producto);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProducto(Dao<Productos, Integer> proDao) throws SQLException {
        DeleteBuilder<Productos, Integer> deleteBuilder = proDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Productos> getProductos(Dao<Productos, Integer> proDao) throws SQLException {

        List<Productos> productos = null;
        String query = "SELECT * FROM "+ Const.TABLE_PRODUCTOS+ " LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<Productos> rawResults = proDao.queryRaw(query, proDao.getRawRowMapper());
        productos = rawResults.getResults();

        return productos;
    }

    public static List<Productos> getProductosByBodega(Dao<Productos, Integer> proDao, String idBodega) throws SQLException {

        List<Productos> productos = null;
        String query = "SELECT * FROM "+ Const.TABLE_PRODUCTOS+ " WHERE bodega_id = '"+idBodega+"' LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<Productos> rawResults = proDao.queryRaw(query, proDao.getRawRowMapper());
        productos = rawResults.getResults();

        return productos;
    }

    public static List<Productos> getProductoByCOD(Dao<Productos, Integer> proDao, String codigo) throws SQLException {

        List<Productos> productos = null;
        String query = "SELECT * FROM " + Const.TABLE_PRODUCTOS + " WHERE codigo_item = '" + codigo + "'";
        GenericRawResults<Productos> rawResults = proDao.queryRaw(query, proDao.getRawRowMapper());
        productos = rawResults.getResults();

        return productos;
    }

    public static List<Productos> getProductoSearch(Dao<Productos, Integer> proDao, String condicion) throws SQLException {

        List<Productos> productos = null;
        String query = "SELECT * FROM " + Const.TABLE_PRODUCTOS + " WHERE " + condicion+ " LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<Productos> rawResults = proDao.queryRaw(query, proDao.getRawRowMapper());
        productos = rawResults.getResults();

        return productos;
    }

    //HELPER ZONAS
    public static void saveZonas(Zonas emp, Dao<Zonas, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteZonas(Dao<Zonas, Integer> userDao) throws SQLException {
        DeleteBuilder<Zonas, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }


    public static List<Zonas> getZonas(Dao<Zonas, Integer> userDao) throws SQLException {

        List<Zonas> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_ZONAS;
        GenericRawResults<Zonas> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER DIRECCIONES
    public static void saveDirecciones(Direcciones emp, Dao<Direcciones, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDirecciones(Dao<Direcciones, Integer> userDao) throws SQLException {
        DeleteBuilder<Direcciones, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static void deleteDireccionesById(Dao<Direcciones, Integer> cabeceraDao, String cliente_id) throws SQLException {
        DeleteBuilder<Direcciones, Integer> deleteBuilder = cabeceraDao.deleteBuilder();
        deleteBuilder.where().eq("cliente_id",cliente_id);
        deleteBuilder.delete();
    }

    public static List<Direcciones> getDirecciones(Dao<Direcciones, Integer> userDao) throws SQLException {

        List<Direcciones> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DIRECCIONES;
        GenericRawResults<Direcciones> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<Direcciones> getDireccionesBYIdCliente(Dao<Direcciones, Integer> userDao, String idCliente) throws SQLException {

        List<Direcciones> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DIRECCIONES+" WHERE cliente_id = '"+idCliente+"'";
        GenericRawResults<Direcciones> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

}
