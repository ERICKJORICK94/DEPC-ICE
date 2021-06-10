package com.smartapp.depc_ice.Utils;

public class Const {

    public static final String BASE_URL = "https://webserver.depconsa.com/";
    public static final int COD_ERROR_SUCCESS = 200;

    //PREFERENCE
    public static final String SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME";
    public static final String IS_LOGIN = "is_login";
    public static final String IS_IMEI = "is_imei";


    public static final int PARAM_MAX_ROW  = 30;
    public static final String APPLICATION_JSON = "application/json; charset=utf-8";
    public static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    //DATA BASE
    public static final String DATABASE_NAME_ = "DEPC-ICE";
    public static final String TABLE_USUARIO = "Usuario";
    public static final String TABLE_CLIENTES = "Clientes";
    public static final String TABLE_BODEGAS = "Bodegas";
    public static final String TABLE_DIRECCIONES = "DIRECCIONES";
    public static final String TABLE_PRODUCTOS = "Productos";
    public static final String TABLE_ZONAS = "Zonas";
    public static final String TABLE_PUNTOS_VENTA = "Puntos_Venta";
    public static final String TABLE_GABINET = "Gabinet";
    public static final String TABLE_GABINET_CLIENTE = "Gabinet_Cliente";
    public static final String TABLE_DETALLE_PEDIDO = "Detalle_pedido";
    public static final String TABLE_PEDIDO = "Pedido";
    public static final String TABLE_CLIENTE_VISITA = "clientes_visita";
    public static final String ID_PEDIDO = "pedido_id";
    public static final String ID_PEDIDOS = "id_pedidos";
    public static final String PEDIDO_PASS= "pedido_pass";


    public static final String ERROR_DEFAULT = "Ha ocurrido un error intente nuevamente";
    public static final String IS_SIN_IMPRESORA = "is_sin_impresora";
    public static final String IS_SELECCT = "is_select";
    public static final String IS_ZEBRA = "is_zebra";
    public static final String ERROR_NO_RESULT = "NO SE ENCONTRARON COINCIDENCIAS";
    public static final String ERROR_COBERTURA = "NO HAY COBERTURA EN ESTE MOMENTO INTENTE SINCRONIZAR MAS ADELANTE";
    public static final String PRODUCT_DETAIL= "product_detail";
    public static final String DETALLE_CLIENTE = "detalle_cliente";
    public static final String EN_CONSTRUCCION = "En construcción";
    public static final String DETALLE_DIRECCION = "detalle_direccion";

    //FORMA DE PAGO
    public static final String FORMA_EF = "EF";
    public static final String FORMA_CHQ = "CHQ";
    public static final String FORMA_DEP = "DEP";
    public static final String FORMA_TRA = "TRA";
    public static final String FORMA_NC = "NC";
    public static final String FORMA_RF = "RF";
    public static final String FORMA_RI = "RI";
    public static final String FORMA_LET = "LET";
    public static final String FORMA_COM = "COM";
    public static final String FORMA_COMF = "COMF";
    public static final String FORMA_TC = "TC";
    public static final String FORMA_PAG = "PAG";
    public static final String FORMA_DB = "DB";

    //ESTADOS GABINETS
    public static final String ESTADO_0 = "0";
    public static final String ESTADO_1 = "1";
    public static final String ESTADO_2 = "2";
    public static final String ESTADO_3 = "3";
    public static final String ESTADO_4 = "4";
    public static final String ESTADO_5 = "-1";

    public static final String ESTADO_DES_0 = "DADO DE BAJA";
    public static final String ESTADO_DES_1 = "DISPONIBLE";
    public static final String ESTADO_DES_2 = "CONSIGNADO";
    public static final String ESTADO_DES_3 = "EN TALLER";
    public static final String ESTADO_DES_4 = "PENDIENTE DE REPUESTO";
    public static final String ESTADO_DES_5 = "PERDIDO";



}
