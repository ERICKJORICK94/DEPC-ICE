package com.smartapp.depc_ice.Database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Pedidos;
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

    public static List<Direcciones> getDireccionesBYIdClienteOrder(Dao<Direcciones, Integer> userDao, String idCliente) throws SQLException {

        List<Direcciones> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DIRECCIONES+" WHERE cliente_id = '"+idCliente+"' ORDER BY id DESC";
        GenericRawResults<Direcciones> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }



    //HELPER PEDIDO
    public static void savePedido(Pedidos pedido, Dao<Pedidos, Integer> pedidoDao) {
        try {


            if (getPedidosByID(pedidoDao, "" + pedido.getId()) != null) {
                if (getPedidosByID(pedidoDao, "" + pedido.getId()).size() > 0) {
                    pedidoDao.update(pedido);
                    return;
                }
            }
            pedidoDao.create(pedido);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int savePedidoID(Pedidos pedido, Dao<Pedidos, Integer> pedidoDao) {
        try {


            if (getPedidosByID(pedidoDao, "" + pedido.getId()) != null) {
                if (getPedidosByID(pedidoDao, "" + pedido.getId()).size() > 0) {
                    pedidoDao.update(pedido);
                    return pedido.getId();
                }
            }
            pedidoDao.create(pedido);
            return  pedido.getId();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void updatePedido(Pedidos pedido, Dao<Pedidos, Integer> pedidoDao) {
        try {
            pedidoDao.update(pedido);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePedidos(Dao<Pedidos, Integer> pedidoDao) throws SQLException {
        DeleteBuilder<Pedidos, Integer> deleteBuilder = pedidoDao.deleteBuilder();
        deleteBuilder.delete();
    }


    public static void deleteClientePendiente(Dao<Clientes, Integer> pedidoDao) throws SQLException {
        DeleteBuilder<Clientes, Integer> deleteBuilder = pedidoDao.deleteBuilder();
        deleteBuilder.where().eq("ESTADO_SINCRO","N");
        deleteBuilder.delete();
    }
    public static void deletePedidosByID(Dao<Pedidos, Integer> pedidoDao, String idPedido) throws SQLException {
        DeleteBuilder<Pedidos, Integer> deleteBuilder = pedidoDao.deleteBuilder();
        deleteBuilder.where().eq("id",idPedido);
        deleteBuilder.delete();
    }

    public static List<Pedidos> getPedidos(Dao<Pedidos, Integer> pedidoDao) throws SQLException {

        List<Pedidos> pedidos = null;
        String query = "SELECT * FROM " + Const.TABLE_PEDIDO;
        GenericRawResults<Pedidos> rawResults = pedidoDao.queryRaw(query, pedidoDao.getRawRowMapper());
        pedidos = rawResults.getResults();

        return pedidos;
    }


    public static List<Pedidos> getPedidosByID(Dao<Pedidos, Integer> pedidoDao, String id) throws SQLException {

        List<Pedidos> pedidos = null;
        String query = "SELECT * FROM " + Const.TABLE_PEDIDO+" WHERE id = "+id;
        GenericRawResults<Pedidos> rawResults = pedidoDao.queryRaw(query, pedidoDao.getRawRowMapper());
        pedidos = rawResults.getResults();

        return pedidos;
    }

    public static List<Pedidos> getPedidosByCliente(Dao<Pedidos, Integer> pedidoDao, String codigo) throws SQLException {

        List<Pedidos> pedidos = null;
        String query = "SELECT * FROM " + Const.TABLE_PEDIDO +" WHERE Cliente = '"+codigo+"';";
        GenericRawResults<Pedidos> rawResults = pedidoDao.queryRaw(query, pedidoDao.getRawRowMapper());
        pedidos = rawResults.getResults();

        return pedidos;
    }


    //HELPER DETALLE PEDIDO
    public static void saveDetallePedido(DetallePedido detail, Dao<DetallePedido, Integer> detailDao) {
        try {

            if (getProductoByPedidoId(detailDao, "" + detail.getIdPedido(), detail.getCodigo()) != null) {
                if (getProductoByPedidoId(detailDao, "" + detail.getIdPedido(),detail.getCodigo()).size() > 0) {
                    detailDao.update(detail);
                    return;
                }
            }
            detailDao.create(detail);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<DetallePedido> getProductoByPedidoId(Dao<DetallePedido, Integer> detailDao, String pedidoId, String codigoProducto) throws SQLException {

        List<DetallePedido> detalles = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_PEDIDO + " WHERE idPedido = '" + pedidoId + "'  AND Codigo = '"+codigoProducto+"'";
        GenericRawResults<DetallePedido> rawResults = detailDao.queryRaw(query, detailDao.getRawRowMapper());
        detalles = rawResults.getResults();

        return detalles;
    }


    public static void saveDetallePedidoCreate(DetallePedido detail, Dao<DetallePedido, Integer> detailDao) {
        try {
            detailDao.create(detail);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDetallePedidoCreate(DetallePedido detail, Dao<DetallePedido, Integer> detailDao) {
        try {
            detailDao.update(detail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDetallePedido(Dao<DetallePedido, Integer> detailDao) throws SQLException {
        DeleteBuilder<DetallePedido, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.delete();
    }


    public static void deleteDetallePedidoByProductoCodigo(Dao<DetallePedido, Integer> detailDao, String codigo,String idPedido) throws SQLException {
        DeleteBuilder<DetallePedido, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.where().eq("Codigo",codigo).and().eq("idPedido",idPedido);
        deleteBuilder.delete();
    }

    public static void deleteDetallePedidoByProductoCodigo(Dao<DetallePedido, Integer> detailDao, String id) throws SQLException {
        DeleteBuilder<DetallePedido, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.where().eq("id",id);
        deleteBuilder.delete();
    }

    public static void deleteDetallePedidoByID(Dao<DetallePedido, Integer> detailDao,String idPedido) throws SQLException {
        DeleteBuilder<DetallePedido, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.where().eq("idPedido",idPedido);
        deleteBuilder.delete();
    }

    public static List<DetallePedido> getDetallePedido(Dao<DetallePedido, Integer> detailDao) throws SQLException {

        List<DetallePedido> detallePedido = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_PEDIDO;
        GenericRawResults<DetallePedido> rawResults = detailDao.queryRaw(query, detailDao.getRawRowMapper());
        detallePedido = rawResults.getResults();

        return detallePedido;
    }



    public static List<DetallePedido> getDetallePedidoByID(Dao<DetallePedido, Integer> detailDao, String ipPedido) throws SQLException {

        List<DetallePedido> detallePedido = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_PEDIDO+" WHERE idPedido = '"+ipPedido+"'";
        GenericRawResults<DetallePedido> rawResults = detailDao.queryRaw(query, detailDao.getRawRowMapper());
        detallePedido = rawResults.getResults();

        return detallePedido;
    }

    public static List<DetallePedido> getDetallePedidoCodigo(Dao<DetallePedido, Integer> detailDao, String codigo, String idPedido) throws SQLException {

        List<DetallePedido> detallePedido = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_PEDIDO+" WHERE Codigo = '"+codigo+"' AND idPedido = '"+idPedido+"'";
        GenericRawResults<DetallePedido> rawResults = detailDao.queryRaw(query, detailDao.getRawRowMapper());
        detallePedido = rawResults.getResults();

        return detallePedido;
    }

    public static List<DetallePedido> getDetallePedidoByIDA(Dao<DetallePedido, Integer> detailDao, int id) throws SQLException {

        List<DetallePedido> detallePedido = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_PEDIDO+" WHERE id = "+id;
        GenericRawResults<DetallePedido> rawResults = detailDao.queryRaw(query, detailDao.getRawRowMapper());
        detallePedido = rawResults.getResults();

        return detallePedido;
    }


}
