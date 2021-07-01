package com.smartapp.depc_ice;

import android.app.Application;
import android.os.StrictMode;
import com.j256.ormlite.dao.Dao;
import com.smartapp.depc_ice.Database.Database;
import com.smartapp.depc_ice.Entities.Bancos;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.CuentaBancos;
import com.smartapp.depc_ice.Entities.DetalleFacturas;
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
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DepcApplication extends Application {

    private static DepcApplication instance;
    private Retrofit mRestAdapter;
    private Database databaseHelper = null;
    private Dao<Usuario, Integer> usuario = null;
    private Dao<Clientes, Integer> clientes = null;
    private Dao<Bodega, Integer> bodegas = null;
    private Dao<Productos, Integer> productos = null;
    private Dao<Zonas, Integer> zonas = null;
    private Dao<Direcciones, Integer> direcciones = null;
    private Dao<Pedidos, Integer> pedidos = null;
    private Dao<DetallePedido, Integer> detallePedidos = null;
    private Dao<ClientesVisitas, Integer> clientesVisitas = null;
    private Dao<PuntosVenta, Integer> puntosVentas = null;
    private Dao<Gabinet, Integer> gabinets = null;
    private Dao<ClienteGabinet, Integer> clienteGabinets = null;
    private Dao<EstadoGabinet, Integer> estadoGabinets = null;
    private Dao<FormaPago, Integer> formaPagos = null;
    private Dao<Bancos, Integer> bancos = null;
    private Dao<GabinetGeneral, Integer> gabinetGenerals = null;
    private Dao<EstadoFacturasDespacho, Integer> estadoFacturasDespachos = null;
    private Dao<CuentaBancos, Integer> cuentaBancos = null;
    private Dao<ListarViajesDia, Integer> listarViajesDias = null;
    private Dao<DetalleViaje, Integer> detalleViajes = null;
    private Dao<DetalleFacturas, Integer> detalleFacturas = null;
    private Dao<MotivosNoEntrega, Integer> motivosNoEntregas = null;
    private static String latitud = "";
    private static String longitud = "";
    private static Clientes cliente = null;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        instance = this;
        databaseHelper = new Database(this);

    }

    public Retrofit getRestAdapter() {
        return this.mRestAdapter;
    }

    public Dao<Usuario, Integer> getUsuarioDao() throws SQLException {
        if (usuario == null) {
            usuario = databaseHelper.getDao(Usuario.class);
        }
        return usuario;
    }

    public Dao<Clientes, Integer> getClientesDao() throws SQLException {
        if (clientes == null) {
            clientes = databaseHelper.getDao(Clientes.class);
        }
        return clientes;
    }

    public Dao<Bodega, Integer> getBodegaDao() throws SQLException {
        if (bodegas == null) {
            bodegas = databaseHelper.getDao(Bodega.class);
        }
        return bodegas;
    }

    public Dao<Productos, Integer> getProductosDao() throws SQLException {
        if (productos == null) {
            productos = databaseHelper.getDao(Productos.class);
        }
        return productos;
    }

    public Dao<Zonas, Integer> getZonasDao() throws SQLException {
        if (zonas == null) {
            zonas = databaseHelper.getDao(Zonas.class);
        }
        return zonas;
    }

    public Dao<Direcciones, Integer> getDireccionesDao() throws SQLException {
        if (direcciones == null) {
            direcciones = databaseHelper.getDao(Direcciones.class);
        }
        return direcciones;
    }

    public Dao<Pedidos, Integer> getPedidosDao() throws SQLException {
        if (pedidos == null) {
            pedidos = databaseHelper.getDao(Pedidos.class);
        }
        return pedidos;
    }

    public Dao<DetallePedido, Integer> getDetallePedidoDao() throws SQLException {
        if (detallePedidos == null) {
            detallePedidos = databaseHelper.getDao(DetallePedido.class);
        }
        return detallePedidos;
    }

    public Dao<ClientesVisitas, Integer> getClientesVisitasDao() throws SQLException {
        if (clientesVisitas == null) {
            clientesVisitas = databaseHelper.getDao(ClientesVisitas.class);
        }
        return clientesVisitas;
    }

    public Dao<PuntosVenta, Integer> getPuntosVentaDao() throws SQLException {
        if (puntosVentas == null) {
            puntosVentas = databaseHelper.getDao(PuntosVenta.class);
        }
        return puntosVentas;
    }

    public Dao<Gabinet, Integer> getGabinetDao() throws SQLException {
        if (gabinets == null) {
            gabinets = databaseHelper.getDao(Gabinet.class);
        }
        return gabinets;
    }

    public Dao<Bancos, Integer> getBancosDao() throws SQLException {
        if (bancos == null) {
            bancos = databaseHelper.getDao(Bancos.class);
        }
        return bancos;
    }

    public Dao<ClienteGabinet, Integer> getClienteGabinetDao() throws SQLException {
        if (clienteGabinets == null) {
            clienteGabinets = databaseHelper.getDao(ClienteGabinet.class);
        }
        return clienteGabinets;
    }

    public Dao<FormaPago, Integer> getFormaPagoDao() throws SQLException {
        if (formaPagos == null) {
            formaPagos = databaseHelper.getDao(FormaPago.class);
        }
        return formaPagos;
    }

    public Dao<EstadoGabinet, Integer> getEstadoGabinetDao() throws SQLException {
        if (estadoGabinets == null) {
            estadoGabinets = databaseHelper.getDao(EstadoGabinet.class);
        }
        return estadoGabinets;
    }

    public Dao<GabinetGeneral, Integer> getGabinetGeneralDao() throws SQLException {
        if (gabinetGenerals == null) {
            gabinetGenerals = databaseHelper.getDao(GabinetGeneral.class);
        }
        return gabinetGenerals;
    }

    public Dao<EstadoFacturasDespacho, Integer> getEstadoFacturasDespachoDao() throws SQLException {
        if (estadoFacturasDespachos == null) {
            estadoFacturasDespachos = databaseHelper.getDao(EstadoFacturasDespacho.class);
        }
        return estadoFacturasDespachos;
    }

    public Dao<CuentaBancos, Integer> getCuentaBancosDao() throws SQLException {
        if (cuentaBancos == null) {
            cuentaBancos = databaseHelper.getDao(CuentaBancos.class);
        }
        return cuentaBancos;
    }

    public Dao<ListarViajesDia, Integer> getListarViajesDiaDao() throws SQLException {
        if (listarViajesDias == null) {
            listarViajesDias = databaseHelper.getDao(ListarViajesDia.class);
        }
        return listarViajesDias;
    }

    public Dao<DetalleViaje, Integer> getDetalleViajeDao() throws SQLException {
        if (detalleViajes == null) {
            detalleViajes = databaseHelper.getDao(DetalleViaje.class);
        }
        return detalleViajes;
    }

    public Dao<DetalleFacturas, Integer> getDetalleFacturasDao() throws SQLException {
        if (detalleFacturas == null) {
            detalleFacturas = databaseHelper.getDao(DetalleFacturas.class);
        }
        return detalleFacturas;
    }

    public Dao<MotivosNoEntrega, Integer> getMotivosNoEntregaDao() throws SQLException {
        if (motivosNoEntregas == null) {
            motivosNoEntregas = databaseHelper.getDao(MotivosNoEntrega.class);
        }
        return motivosNoEntregas;
    }

    public Clientes getCliente() {
        return this.cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public void setLatitud(String latitud){
        this.latitud =  latitud;
    }

    public String getLatitud(){

        if (latitud != null){
            return latitud;
        }
            return "";
    }

    public void setLongitud(String longitud){
        this.longitud = longitud;
    }

    public String getLongitud(){

        if (longitud != null){
            return longitud;
        }
        return "";
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static DepcApplication getApplication() {
        return instance;
    }
}

