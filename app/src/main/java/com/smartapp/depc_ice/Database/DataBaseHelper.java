package com.smartapp.depc_ice.Database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.smartapp.depc_ice.Entities.Bancos;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.CuentaBancos;
import com.smartapp.depc_ice.Entities.DetalleFacturas;
import com.smartapp.depc_ice.Entities.DetalleFormaPago;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.DetalleViaje;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.EstadoFacturasDespacho;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.FormaPago;
import com.smartapp.depc_ice.Entities.Gabinet;
import com.smartapp.depc_ice.Entities.GabinetGeneral;
import com.smartapp.depc_ice.Entities.ListarViajesDia;
import com.smartapp.depc_ice.Entities.MotivosNoEntrega;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Entities.PuntosVenta;
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

    //HELPER DETALLE FORMA DE PAGO
    public static void saveDetalleFormaPago(DetalleFormaPago emp, Dao<DetalleFormaPago, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDetalleFormaPago(Dao<DetalleFormaPago, Integer> userDao) throws SQLException {
        DeleteBuilder<DetalleFormaPago, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static void deleteDetalleFormaPago(Dao<DetalleFormaPago, Integer> userDao, int id) throws SQLException {
        DeleteBuilder<DetalleFormaPago, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.where().eq("id",id);
        deleteBuilder.delete();
    }

    public static List<DetalleFormaPago> getDetalleFormaPago(Dao<DetalleFormaPago, Integer> userDao) throws SQLException {

        List<DetalleFormaPago> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_FORMA_PAGO;
        GenericRawResults<DetalleFormaPago> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<DetalleFormaPago> getDetalleFormaPagoByFactura(Dao<DetalleFormaPago, Integer> userDao, String factura_id) throws SQLException {

        List<DetalleFormaPago> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_FORMA_PAGO+" WHERE factura_id = '"+factura_id+"'";
        GenericRawResults<DetalleFormaPago> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }


    //HELPER MOTIVO NO ENTREGA
    public static void saveMotivosNoEntrega(MotivosNoEntrega emp, Dao<MotivosNoEntrega, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMotivosNoEntrega(Dao<MotivosNoEntrega, Integer> userDao) throws SQLException {
        DeleteBuilder<MotivosNoEntrega, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<MotivosNoEntrega> getMotivosNoEntrega(Dao<MotivosNoEntrega, Integer> userDao) throws SQLException {

        List<MotivosNoEntrega> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_MOTIVOSNO_ENTREGA;
        GenericRawResults<MotivosNoEntrega> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER LISTAR VIAJE DIA
    /*public static void saveListarViajesDia(ListarViajesDia emp, Dao<ListarViajesDia, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public static void saveListarViajesDia(ListarViajesDia cliente, Dao<ListarViajesDia, Integer> clienteDao) {
        try {

            List<ListarViajesDia> cls = getListaViajesDiaByID(clienteDao, "" + cliente.getId_viaje());
            if (cls != null) {
                if (cls.size() > 0) {
                    ListarViajesDia cl = cls.get(0);
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

    public static List<ListarViajesDia> getListaViajesDiaByID(Dao<ListarViajesDia, Integer> clienteDao, String id_viaje) throws SQLException {

        List<ListarViajesDia> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_LISTAR_VIAJES_DIA + " WHERE id_viaje = '" + id_viaje + "'";
        GenericRawResults<ListarViajesDia> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }

    public static void deleteListarViajesDia(Dao<ListarViajesDia, Integer> userDao) throws SQLException {
        DeleteBuilder<ListarViajesDia, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<ListarViajesDia> getListarViajesDia(Dao<ListarViajesDia, Integer> userDao) throws SQLException {

        List<ListarViajesDia> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_LISTAR_VIAJES_DIA;
        GenericRawResults<ListarViajesDia> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<ListarViajesDia> getListarViajesDiaByDate(Dao<ListarViajesDia, Integer> userDao, String fecha) throws SQLException {

        List<ListarViajesDia> usuarios = null;
        String query = "SELECT * FROM "+ Const.TABLE_LISTAR_VIAJES_DIA+" WHERE fecha_inicio LIKE '%"+fecha+"%'";
        GenericRawResults<ListarViajesDia> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER DETALLE VIAJE
    /*public static void saveListarViajesDia(ListarViajesDia emp, Dao<ListarViajesDia, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public static void saveDetalleViaje(DetalleViaje cliente, Dao<DetalleViaje, Integer> clienteDao) {
        try {

            List<DetalleViaje> cls = getDetalleViajeByID(clienteDao, "" + cliente.getFactura_id());
            if (cls != null) {
                if (cls.size() > 0) {
                    DetalleViaje cl = cls.get(0);
                    cliente.setPrimary(cl.getPrimary());
                    clienteDao.update(cliente);
                    return;
                }
            }
            clienteDao.create(cliente);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<DetalleViaje> getDetalleViajeByID(Dao<DetalleViaje, Integer> clienteDao, String factura_id) throws SQLException {

        List<DetalleViaje> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_VIAJE + " WHERE factura_id = '" + factura_id + "'";
        GenericRawResults<DetalleViaje> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }

    public static List<DetalleViaje> getDetalleViajeByViaje(Dao<DetalleViaje, Integer> clienteDao, String id_viaje) throws SQLException {

        List<DetalleViaje> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_VIAJE + " WHERE id_viaje = '" + id_viaje + "'";
        GenericRawResults<DetalleViaje> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }


    public static void deleteDetalleViaje(Dao<DetalleViaje, Integer> userDao) throws SQLException {
        DeleteBuilder<DetalleViaje, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<DetalleViaje> getDetalleViaje(Dao<DetalleViaje, Integer> userDao) throws SQLException {

        List<DetalleViaje> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_VIAJE;
        GenericRawResults<DetalleViaje> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER DETALLE FACTURAS
    public static void saveDetalleFacturas(DetalleFacturas emp, Dao<DetalleFacturas, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDetalleFacturas(Dao<DetalleFacturas, Integer> userDao) throws SQLException {
        DeleteBuilder<DetalleFacturas, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static void deleteDetalleFacturasByIDFactura(Dao<DetalleFacturas, Integer> detailDao,String factura_id) throws SQLException {
        DeleteBuilder<DetalleFacturas, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.where().eq("factura_id",factura_id);
        deleteBuilder.delete();
    }

    public static List<DetalleFacturas> getDetalleFacturas(Dao<DetalleFacturas, Integer> userDao) throws SQLException {

        List<DetalleFacturas> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_FACTURAS;
        GenericRawResults<DetalleFacturas> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<DetalleFacturas> getDetalleFacturasByIDFactura(Dao<DetalleFacturas, Integer> userDao, String factura_id) throws SQLException {

        List<DetalleFacturas> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DETALLE_FACTURAS+" WHERE factura_id = '"+factura_id+"'";
        GenericRawResults<DetalleFacturas> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER CUENTA BANCOS
    public static void saveCuentaBancos(CuentaBancos emp, Dao<CuentaBancos, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCuentaBancos(Dao<CuentaBancos, Integer> userDao) throws SQLException {
        DeleteBuilder<CuentaBancos, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<CuentaBancos> getCuentaBancos(Dao<CuentaBancos, Integer> userDao) throws SQLException {

        List<CuentaBancos> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_CUENTA_BANCOS;
        GenericRawResults<CuentaBancos> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<CuentaBancos> getCuentaBancosByBanco(Dao<CuentaBancos, Integer> userDao, String banco) throws SQLException {

        List<CuentaBancos> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_CUENTA_BANCOS+" WHERE banco = '"+banco+"'";
        GenericRawResults<CuentaBancos> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static void deleteCuentaBancosByBanco(Dao<CuentaBancos, Integer> detailDao,String banco) throws SQLException {
        DeleteBuilder<CuentaBancos, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.where().eq("banco",banco);
        deleteBuilder.delete();
    }


    //HELPER ESTADO FACTURA DESPACHO
    public static void saveEstadoFacturasDespacho(EstadoFacturasDespacho emp, Dao<EstadoFacturasDespacho, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEstadoFacturasDespacho(Dao<EstadoFacturasDespacho, Integer> userDao) throws SQLException {
        DeleteBuilder<EstadoFacturasDespacho, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<EstadoFacturasDespacho> getEstadoFacturasDespacho(Dao<EstadoFacturasDespacho, Integer> userDao) throws SQLException {

        List<EstadoFacturasDespacho> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_ESTADO_FACTURAS_DESPACHO;
        GenericRawResults<EstadoFacturasDespacho> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }


    //HELPER FORMA PAGO
    public static void saveFormaPago(FormaPago emp, Dao<FormaPago, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFormaPago(Dao<FormaPago, Integer> userDao) throws SQLException {
        DeleteBuilder<FormaPago, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<FormaPago> getFormaPago(Dao<FormaPago, Integer> userDao) throws SQLException {

        List<FormaPago> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_FORMA_PAGO;
        GenericRawResults<FormaPago> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER FORMA BANCOS
    public static void saveBancos(Bancos emp, Dao<Bancos, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBancos(Dao<Bancos, Integer> userDao) throws SQLException {
        DeleteBuilder<Bancos, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Bancos> getBancos(Dao<Bancos, Integer> userDao) throws SQLException {

        List<Bancos> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_BANCOS;
        GenericRawResults<Bancos> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    //HELPER ESTADO GABINET
    public static void saveEstadoGabinet(EstadoGabinet emp, Dao<EstadoGabinet, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEstadoGabinet(Dao<EstadoGabinet, Integer> userDao) throws SQLException {
        DeleteBuilder<EstadoGabinet, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<EstadoGabinet> getEstadoGabinet(Dao<EstadoGabinet, Integer> userDao) throws SQLException {

        List<EstadoGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_ESTADO_GABINET;
        GenericRawResults<EstadoGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<EstadoGabinet> getEstadoGabinetByEstado(Dao<EstadoGabinet, Integer> userDao, String num_estado) throws SQLException {

        List<EstadoGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_ESTADO_GABINET+" WHERE num_estado = '"+num_estado+"'";
        GenericRawResults<EstadoGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
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

    public static List<Clientes> getClientesLimit(Dao<Clientes, Integer> clienteDao) throws SQLException {

        List<Clientes> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_CLIENTES + " ORDER BY nombre_comercial LIMIT "+10;
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

    public static List<Bodega> getBodegaByID(Dao<Bodega, Integer> userDao, String bodega_id) throws SQLException {

        List<Bodega> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_BODEGAS+" WHERE bodega_id = '"+bodega_id+"'";
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


    //HELPER PUNTOS DE VENTA
    public static void savePuntosVenta(PuntosVenta emp, Dao<PuntosVenta, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePuntosVenta(Dao<PuntosVenta, Integer> userDao) throws SQLException {
        DeleteBuilder<PuntosVenta, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<PuntosVenta> getPuntosVenta(Dao<PuntosVenta, Integer> userDao) throws SQLException {

        List<PuntosVenta> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_PUNTOS_VENTA;
        GenericRawResults<PuntosVenta> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }


    //HELPER GABINET GENERAL
    public static void saveGabinetGeneral(GabinetGeneral gab, Dao<GabinetGeneral, Integer> userDao) {
        try {

            List<GabinetGeneral> cls = getGabinetGeneralByID(userDao, "" + gab.getId_congelador());
            if (cls != null) {
                if (cls.size() > 0) {
                    GabinetGeneral cl = cls.get(0);
                    gab.setId(cl.getId());

                    gab.setId(cl.getId());
                    if (cl.getObservacion() != null) {
                        gab.setObservacion("" + cl.getObservacion());
                    }
                    if (cl.getFoto() != null) {
                        gab.setFoto("" + cl.getFoto());
                    }

                    userDao.update(gab);
                    return;
                }
            }

            userDao.create(gab);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGabinetGeneral(GabinetGeneral gab, Dao<GabinetGeneral, Integer> userDao) {
        try {
            userDao.update(gab);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<GabinetGeneral> getGabinetGeneralByID(Dao<GabinetGeneral, Integer> userDao, String id_congelador) throws SQLException {

        List<GabinetGeneral> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_GENERAL+" WHERE id_congelador = '"+id_congelador+"'";
        GenericRawResults<GabinetGeneral> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<GabinetGeneral> getGabinetGeneralCondicion(Dao<GabinetGeneral, Integer> clienteDao, String condicion) throws SQLException {

        List<GabinetGeneral> clientes = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_GENERAL+ " WHERE " + condicion + " LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<GabinetGeneral> rawResults = clienteDao.queryRaw(query, clienteDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }

    public static void deleteGabinetGeneral(Dao<GabinetGeneral, Integer> userDao) throws SQLException {
        DeleteBuilder<GabinetGeneral, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<GabinetGeneral> getGabinetGeneral(Dao<GabinetGeneral, Integer> userDao) throws SQLException {

        List<GabinetGeneral> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_GENERAL+" LIMIT "+Const.PARAM_MAX_ROW;
        GenericRawResults<GabinetGeneral> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }


    //HELPER GABINET
    public static void saveGabinet(Gabinet emp, Dao<Gabinet, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGabinet(Dao<Gabinet, Integer> userDao) throws SQLException {
        DeleteBuilder<Gabinet, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Gabinet> getGabinet(Dao<Gabinet, Integer> userDao) throws SQLException {

        List<Gabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET;
        GenericRawResults<Gabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<Gabinet> getGabinetByPtoVta(Dao<Gabinet, Integer> userDao, String pto_vta_id) throws SQLException {

        List<Gabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET+" pto_vta_id = '"+pto_vta_id+"'";
        GenericRawResults<Gabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }







    //HELPER CLIENTE GABINET
    public static void saveClienteGabinet(ClienteGabinet gab, Dao<ClienteGabinet, Integer> userDao) {
        try {

            List<ClienteGabinet> cls = getClienteGabinetByID(userDao, "" + gab.getId_congelador());
            if (cls != null) {
                if (cls.size() > 0) {
                    ClienteGabinet cl = cls.get(0);
                    gab.setId(cl.getId());
                    if (cl.getObservacion() != null) {
                        gab.setObservacion("" + cl.getObservacion());
                    }
                    if (cl.getFoto() != null) {
                        gab.setFoto("" + cl.getFoto());
                    }
                    userDao.update(gab);
                    return;
                }
            }
            userDao.create(gab);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateClienteGabinet(ClienteGabinet emp, Dao<ClienteGabinet, Integer> userDao) {
        try {
            userDao.update(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteClienteGabinet(Dao<ClienteGabinet, Integer> userDao) throws SQLException {
        DeleteBuilder<ClienteGabinet, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static void deleteClienteGabinetByCliente(Dao<ClienteGabinet, Integer> detailDao,String id_cliente) throws SQLException {
        DeleteBuilder<ClienteGabinet, Integer> deleteBuilder = detailDao.deleteBuilder();
        deleteBuilder.where().eq("id_cliente",id_cliente);
        deleteBuilder.delete();
    }

    public static List<ClienteGabinet> getClienteGabinet(Dao<ClienteGabinet, Integer> userDao) throws SQLException {

        List<ClienteGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_CLIENTE;
        GenericRawResults<ClienteGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<ClienteGabinet> getClienteGabinetByCliente(Dao<ClienteGabinet, Integer> userDao, String id_cliente) throws SQLException {

        List<ClienteGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_CLIENTE+" WHERE id_cliente = '"+id_cliente+"'";
        GenericRawResults<ClienteGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<ClienteGabinet> getClienteGabinetByClienteAndDireccion(Dao<ClienteGabinet, Integer> userDao, String id_cliente, String id_direccion_cliente) throws SQLException {

        List<ClienteGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_CLIENTE+" WHERE id_cliente = '"+id_cliente+"' AND id_direccion_cliente = '"+id_direccion_cliente+"'";
        GenericRawResults<ClienteGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<ClienteGabinet> getClienteGabinetByID(Dao<ClienteGabinet, Integer> userDao, String id_congelador) throws SQLException {

        List<ClienteGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_CLIENTE+" WHERE id_congelador = '"+id_congelador+"'";
        GenericRawResults<ClienteGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<ClienteGabinet> getClienteGabinetById(Dao<ClienteGabinet, Integer> userDao, String id) throws SQLException {

        List<ClienteGabinet> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_GABINET_CLIENTE+" WHERE id = "+id;
        GenericRawResults<ClienteGabinet> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
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

    public static void updateDirecciones(Direcciones emp, Dao<Direcciones, Integer> userDao) {
        try {
            userDao.update(emp);
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

    public static List<Direcciones> getDireccionesBYIdCliente(Dao<Direcciones, Integer> userDao, String id) throws SQLException {

        List<Direcciones> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DIRECCIONES+" WHERE id = '"+id+"'";
        GenericRawResults<Direcciones> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<Direcciones> getDireccionesBYId(Dao<Direcciones, Integer> userDao, String idCliente) throws SQLException {

        List<Direcciones> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DIRECCIONES+" WHERE cliente_id = '"+idCliente+"'";
        GenericRawResults<Direcciones> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

    public static List<Direcciones> getDireccionesBYIdClienteDESC(Dao<Direcciones, Integer> userDao, String idCliente) throws SQLException {

        List<Direcciones> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_DIRECCIONES+" WHERE cliente_id = '"+idCliente+"' ORDER BY id DESC";
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

    public static List<Pedidos> getPedidosByCuenta(Dao<Pedidos, Integer> pedidoDao, String cuenta_id) throws SQLException {

        List<Pedidos> pedidos = null;
        String query = "SELECT * FROM " + Const.TABLE_PEDIDO+" WHERE cuenta_id = '"+cuenta_id+"'";
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


    //HELPER CLIENTES VISITAS
    public static void saveClientesVisitas(ClientesVisitas clientes, Dao<ClientesVisitas, Integer> clientesDao) {
        try {
            List<ClientesVisitas> cls = getClienteVisitaByID(clientesDao, "" + clientes.getId());
            if (cls != null) {
                if (cls.size() > 0) {
                    ClientesVisitas cl = cls.get(0);
                    clientes.setPrimaryKey(cl.getPrimaryKey());
                    if (clientes.getFecha().equals(cl.getFecha())){
                        clientes.setHora(cl.getHora());
                        clientes.setEstado(cl.getEstado());
                        clientes.setAtiende(cl.getAtiende());
                        clientes.setComentario(cl.getComentario());
                        clientes.setFirma(cl.getFirma());
                    }
                    clientesDao.update(clientes);
                    return;

                }
            }
            clientesDao.create(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateClienteVisitas(ClientesVisitas clientes, Dao<ClientesVisitas, Integer> clientesDao) {
        try {
            clientesDao.update(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteClientesVisitas(Dao<ClientesVisitas, Integer> clientesDao) throws SQLException {
        DeleteBuilder<ClientesVisitas, Integer> deleteBuilder = clientesDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<ClientesVisitas> getClienteVisita(Dao<ClientesVisitas, Integer> clientesDao) throws SQLException {

        List<ClientesVisitas> clientes = null;
        String query = "SELECT * FROM "+ Const.TABLE_CLIENTE_VISITA;
        GenericRawResults<ClientesVisitas> rawResults = clientesDao.queryRaw(query, clientesDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }

    public static List<ClientesVisitas> getClienteVisitaDiaVisita(Dao<ClientesVisitas, Integer> clientesDao, String dia_visita) throws SQLException {

        List<ClientesVisitas> clientes = null;
        String query = "SELECT * FROM "+ Const.TABLE_CLIENTE_VISITA+" WHERE dia_visita = '"+dia_visita+"'";
        GenericRawResults<ClientesVisitas> rawResults = clientesDao.queryRaw(query, clientesDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }


    public static void deleteClientesVisitas(Dao<ClientesVisitas, Integer> clienteDao, int primaryKey) throws SQLException {
        DeleteBuilder<ClientesVisitas, Integer> deleteBuilder = clienteDao.deleteBuilder();
        deleteBuilder.where().eq("primaryKey",primaryKey);
        deleteBuilder.delete();
    }


    public static List<ClientesVisitas> getClienteVisitaByID(Dao<ClientesVisitas, Integer> clientesDao,String id) throws SQLException {

        List<ClientesVisitas> clientes = null;
        String query = "SELECT * FROM "+ Const.TABLE_CLIENTE_VISITA+" WHERE id = '"+id+"'";
        GenericRawResults<ClientesVisitas> rawResults = clientesDao.queryRaw(query, clientesDao.getRawRowMapper());
        clientes = rawResults.getResults();

        return clientes;
    }




}
