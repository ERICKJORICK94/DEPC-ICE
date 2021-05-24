package com.smartapp.depc_ice.Activities.Pedido;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Entities.Usuario;;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IConsultaDetallePedido;
import com.smartapp.depc_ice.Interface.IConsultaPedido;
import com.smartapp.depc_ice.Interface.IPedido;
import com.smartapp.depc_ice.Interface.IRemoverPedido;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.ConsultasDetallePedidosModel;
import com.smartapp.depc_ice.Models.ConsultasPedidosModel;
import com.smartapp.depc_ice.Models.CrearPreventaModel;
import com.smartapp.depc_ice.Models.DetalleCrearPreventaModel;
import com.smartapp.depc_ice.Models.ListarDetalleProformas;
import com.smartapp.depc_ice.Models.ListarProformas;
import com.smartapp.depc_ice.Models.RemoverPedidoModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetallePedidoActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private TextView ruc;
    private TextView fecha;
    private TextView direccion;
    private String codCliente = "";
    private String direccionEntrega = "";
    private boolean isCotizacion = false;
    private Call<IPedido.dataPedido> call;
    private IPedido.dataPedido data;
    private Call<IConsultaDetallePedido.dataPedido> callDetalle;
    private IConsultaDetallePedido.dataPedido dataDetalle;

    IRemoverPedido.dataPedido dataRemove;

    private String idPedido = "";
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3,floatingActionButton4;

    private TextView clienteName;
    private Pedidos pedido;

    private ExpandableListView expandable;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<Productos>> listDataChild;
    private ScrollView scroll;
    private Button recalcular;

    private TextView subtotalEdtit;
    private TextView descuaneto_adicional;
    private TextView subtotal_neto;
    private TextView impuesto;
    private TextView total_pedido;
    private TextView add_descuento;
    private TextView documento;
    private Spinner spinner;
    private RelativeLayout linear_fondo;

    private Button enviar;
    private Button anular;
    private Button modificar;
    private Button ver_detalle;
    private Clientes cliente;
    private boolean isModificar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_pedido);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cliente = DepcApplication.getApplication().getCliente();

        documento = (TextView) layout.findViewById(R.id.documento);
        clienteName = (TextView) layout.findViewById(R.id.clienteName);
        ruc = (TextView) layout.findViewById(R.id.ruc);
        fecha = (TextView) layout.findViewById(R.id.fecha);
        direccion = (TextView) layout.findViewById(R.id.direccion);
        scroll = (ScrollView) layout.findViewById(R.id.scroll);
        subtotalEdtit = (TextView) layout.findViewById(R.id.subtotal);
        descuaneto_adicional = (TextView) layout.findViewById(R.id.descuaneto_adicional);
        subtotal_neto = (TextView) layout.findViewById(R.id.subtotal_neto);
        impuesto = (TextView) layout.findViewById(R.id.impuesto);
        total_pedido = (TextView) layout.findViewById(R.id.total_pedido);
        add_descuento = (TextView) layout.findViewById(R.id.add_descuento);
        recalcular = (Button) layout.findViewById(R.id.recalcular);
        enviar = (Button) layout.findViewById(R.id.enviar);
        ver_detalle = (Button) layout.findViewById(R.id.ver_detalle);
        anular = (Button) layout.findViewById(R.id.anular);
        modificar = (Button) layout.findViewById(R.id.modificar);
        spinner = (Spinner) layout.findViewById(R.id.spinner);
        linear_fondo = (RelativeLayout) layout.findViewById(R.id.linear_fondo);

        modificar.setVisibility(View.GONE);

        recalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPedidos();
            }
        });


        expandable = (ExpandableListView) layout.findViewById(R.id.expandable);

        if (getIntent() != null) {
            idPedido = getIntent().getStringExtra(Const.ID_PEDIDOS);
        }

        try {
            List<Pedidos> pedidos = DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(), idPedido);
            pedido = pedidos.get(0);


            materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

            floatingActionButton4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item4);

            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu first item clicked
                    Intent intent = new Intent(DetallePedidoActivity.this, BusquedaPedidoActivity.class);
                    intent.putExtra(Const.PEDIDO_PASS, pedido);
                    startActivity(intent);
                    materialDesignFAM.close(false);

                    materialDesignFAM.close(true);

                }
            });

            floatingActionButton4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu third item clicked

                    materialDesignFAM.close(true);
                    AlertDialog.Builder alertDelete = new AlertDialog.Builder(
                            DetallePedidoActivity.this);
                    alertDelete.setTitle("Alerta");
                    alertDelete.setMessage("¿Está seguro que desea borrar el pedido");
                    alertDelete.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {

                                if (pedido.getEstadoPedido().equals("1")){
                                    dialog.dismiss();
                                    removePedido();
                                }else {
                                    DataBaseHelper.deleteDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), "" + pedido.getId());
                                    DataBaseHelper.deletePedidosByID(DepcApplication.getApplication().getPedidosDao(), "" + pedido.getId());
                                    dialog.dismiss();
                                    finish();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    alertDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });

                    alertDelete.show();
                }
            });


            ver_detalle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {

                        Clientes cliente = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).get(0);
                        Intent intent = new Intent(DetallePedidoActivity.this, RegistroPedidoActivity.class);
                        intent.putExtra(Const.ID_PEDIDO, pedido);
                        startActivity(intent);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }


        add_descuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(DetallePedidoActivity.this)
                        .title("Atención")
                        .content("Inserte porcentaje adicional")
                        .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                        .input("0", "0", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                if (input.length() == 0) {
                                    descuaneto_adicional.setText(""+0);
                                }else{
                                    descuaneto_adicional.setText(""+input);
                                }
                                pedido.setPorcentajeDescuentoAdi(""+input);

                                try {
                                    DataBaseHelper.updatePedido(pedido, DepcApplication.getApplication().getPedidosDao());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                getPedidos();
                            }
                        }).show();


            }
        });



        if (pedido != null){
            if (pedido.getEstadoPedido().equals("1")){

                try {
                    boolean flag = true;
                    List<DetallePedido> detalles = DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pedido.getId());
                    if (detalles != null) {
                        if (detalles.size() > 0) {
                            flag = false;
                        }
                    }

                    if (flag){
                        //pedir detalles
                        getDetallesPedido();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


            }
        }


    }

    private void getDetallesPedido(){

        showProgressWait();

        //JSON SEND
        ConsultasDetallePedidosModel model = new ConsultasDetallePedidosModel();
        model.setCuenta_id(""+pedido.getCuenta_id());
        model.setMetodo("ConsultarPreventaClienteDetalle");
        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IConsultaDetallePedido request = DepcApplication.getApplication().getRestAdapter().create(IConsultaDetallePedido.class);
            callDetalle = request.getPedido(body);
            callDetalle.enqueue(new Callback<IConsultaDetallePedido.dataPedido>() {
                @Override
                public void onResponse(Call<IConsultaDetallePedido.dataPedido> call, Response<IConsultaDetallePedido.dataPedido> response) {
                    if (response.isSuccessful()) {

                        dataDetalle = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataDetalle != null) {
                                if (dataDetalle.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataDetalle.getData() != null){
                                        if (dataDetalle.getData().getListarProformas() != null) {
                                            if (dataDetalle.getData().getListarProformas().size() > 0) {

                                                final List<ListarDetalleProformas> proformas;
                                                proformas = dataDetalle.getData().getListarProformas().get(0);

                                                if (proformas != null) {
                                                    for (ListarDetalleProformas prof : proformas) {

                                                        double descuento = 0;
                                                        double cantidad = 0;
                                                        double subtotal = 0;
                                                        double subtotalNeto = 0;
                                                        double total = 0;
                                                        double iva = 12;
                                                        double subtotalIva = 0;
                                                        double precioFijo = 0;

                                                        cantidad = Float.parseFloat("" + prof.getCantidad());
                                                        precioFijo = Float.parseFloat("" + prof.getPrecio());
                                                        double desc = Float.parseFloat("" + prof.getDescuento());

                                                        try {
                                                            if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()) != null){
                                                                if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).size() > 0) {
                                                                    Clientes cl = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).get(0);
                                                                    if (cl.getCobrar_iva() != null){
                                                                        if (cl.getCobrar_iva().toLowerCase().equals("0")){
                                                                            iva = 0;
                                                                        }
                                                                    }

                                                                }

                                                            }
                                                        } catch (SQLException e) {
                                                            e.printStackTrace();
                                                        }


                                                        descuento = (desc * 100 ) / precioFijo;
                                                        subtotal = (cantidad * precioFijo);
                                                        subtotalNeto = (cantidad * precioFijo) - ((cantidad * precioFijo) * (descuento / 100));
                                                        subtotalIva = (subtotalNeto * (iva / 100));

                                                        total = (subtotalNeto) + subtotalIva;

                                                        DetallePedido detalle = new DetallePedido();

                                                        detalle.setIdPedido("" + pedido.getId());
                                                        detalle.setNumeroItem("" + prof.getCodigo_item());
                                                        detalle.setCodigo("" + prof.getCodigo_item());
                                                        detalle.setDescripcion("" + prof.getDescripcion());
                                                        detalle.setTipoInventario("");
                                                        detalle.setCantidad("" + prof.getCantidad());
                                                        detalle.setCosto(""+prof.getCosto());
                                                        detalle.setPrecioUnitario("" + prof.getPrecio());
                                                        detalle.setSubtotal("" + String.format("%.2f",subtotal));
                                                        detalle.setSubtotalNeto("" + String.format("%.2f", subtotalNeto));
                                                        detalle.setSubtotalNetoFijo("" + String.format("%.2f" , subtotalNeto));
                                                        detalle.setPorcentajeDescuento("" + String.format("%.2f", descuento));
                                                        double ds = ((cantidad * precioFijo) * (descuento / 100));
                                                        //ds = Utils.roundFloat(ds,4);
                                                        detalle.setDescuento("" + String.format("%.2f", ds ));
                                                        detalle.setPorcentajeDescuentoAdicional("0");
                                                        detalle.setDescuentoAdicional("0");
                                                        detalle.setNeto("" + String.format("%.2f", subtotalNeto));
                                                        detalle.setPorcentajeIva("" + String.format("%.2f", iva)  /*Utils.roundFloat(iva,2)*/);
                                                        detalle.setIva("" + String.format("%.2f" , subtotalIva ) /*Utils.roundFloat(subtotalIva,2)*/ );
                                                        detalle.setTotal("" + String.format("%.2f", total ) /*Utils.roundFloat(total,2)*/ );
                                                        detalle.setPromocion("0");
                                                        pedido.setBodega(""+prof.getBodega_id());


                                                        String nombreBodega = "";

                                                        boolean flag = true;
                                                        if(DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(), prof.getCodigo_item()) != null){
                                                            if(DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(),  prof.getCodigo_item()).size() > 0) {
                                                                flag = false;
                                                            }
                                                        }

                                                        if(DataBaseHelper.getBodegaByID(DepcApplication.getApplication().getBodegaDao(), prof.getBodega_id()) != null){
                                                            if(DataBaseHelper.getBodegaByID(DepcApplication.getApplication().getBodegaDao(), prof.getBodega_id()).size() > 0) {
                                                                Bodega bg = DataBaseHelper.getBodegaByID(DepcApplication.getApplication().getBodegaDao(), prof.getBodega_id()).get(0);
                                                                nombreBodega = bg.getDescripcion();
                                                            }
                                                        }

                                                        if (flag){
                                                            Productos p = new Productos();
                                                            p.setCodigo_item(""+prof.getCodigo_item());
                                                            p.setDescripcion(""+prof.getDescripcion());
                                                            p.setDescripcion_abrev(""+prof.getDescripcion());
                                                            p.setPvp(""+prof.getPrecio());
                                                            p.setBodega_id(""+prof.getBodega_id());
                                                            p.setDescripcion_bodega(""+nombreBodega);
                                                            p.setExistencia("0");
                                                            p.setCosto(""+prof.getCosto());
                                                            DataBaseHelper.saveProduto(p, DepcApplication.getApplication().getProductosDao());
                                                        }

                                                        DataBaseHelper.updatePedido(pedido,DepcApplication.getApplication().getPedidosDao());
                                                        DataBaseHelper.saveDetallePedidoCreate(detalle, DepcApplication.getApplication().getDetallePedidoDao());

                                                    }
                                                }

                                                getPedidos();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataDetalle.getStatus_message() != null){
                                        mensajeError = data.getStatus_message();
                                    }
                                }
                            }

                            getPedidos();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            getPedidos();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        getPedidos();
                    }
                }

                @Override
                public void onFailure(Call<IConsultaDetallePedido.dataPedido> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    getPedidos();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            getPedidos();

        }
    }


    public void getPedidos(){

        enviar.setEnabled(false);

        if (pedido != null){
            clienteName.setText(""+pedido.getNombreCliente());
            ruc.setText(""+pedido.getRucCliente());
            fecha.setText(""+pedido.getFecha());
            direccion.setText(""+pedido.getDireccionCliente());
            documento.setText("" + pedido.getCuenta_id());

            anular.setVisibility(View.GONE);
            if (pedido.getEstadoPedido().equals("0")){
                modificar.setVisibility(View.GONE);
                enviar.setVisibility(View.VISIBLE);
            }else if (pedido.getEstadoPedido().equals("1")){
                modificar.setVisibility(View.VISIBLE);
                enviar.setVisibility(View.GONE);
            }else if (pedido.getEstadoPedido().equals("2")){
                modificar.setVisibility(View.GONE);
                enviar.setVisibility(View.GONE);
                materialDesignFAM.setVisibility(View.GONE);
            }
        }


        try {
            if(DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(),""+pedido.getId()) != null){
                List<Pedidos> pedidos = DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(),""+pedido.getId());

                float cantidad = 0;
                double subtotal = 0;
                double subtotalNetos = 0;
                double base0 = 0;
                double base12 = 0;
                double descuentosNetos = 0;
                double iva = 0;
                double subtotalIva = 0;


                if (pedido.getPorcentajeDescuentoAdi() != null){
                    if(pedido.getPorcentajeDescuentoAdi().length() > 0){
                        descuaneto_adicional.setText(""+pedido.getPorcentajeDescuentoAdi());
                    }
                }


                for(Pedidos pd : pedidos){
                    pedido = pd;


                    if (DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pd.getId()) != null){
                        List<DetallePedido> detalles = DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pd.getId());

                        //********************
                        if (descuaneto_adicional != null){
                            if (Utils.isNumber(descuaneto_adicional.getText().toString()) || Utils.isNumberDecimal(descuaneto_adicional.getText().toString())){
                                float descuentoFactura = Float.parseFloat(descuaneto_adicional.getText().toString());
                                if (descuentoFactura >= 0){
                                    for(DetallePedido dt : detalles){

                                        float descuento = descuentoFactura;
                                        float subtotalNeto = Float.parseFloat(dt.getSubtotalNetoFijo());
                                        double newsubtotalNeto = subtotalNeto  - (subtotalNeto * (descuento / 100));

                                        double iva1 =  0;

                                        try {
                                            if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()) != null){
                                                if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).size() > 0) {
                                                    Clientes cl = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).get(0);
                                                    if (cl.getCobrar_iva() != null){
                                                        if (cl.getCobrar_iva().toLowerCase().equals("1")){
                                                            iva1 = 12;
                                                        }
                                                    }

                                                }

                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                        double subtotalIva1 = (subtotalNeto * (iva1 / 100));
                                        double total = (subtotalNeto) + subtotalIva1;
                                        double ds = (subtotalNeto * (descuento / 100));
                                        dt.setSubtotalNeto(""+newsubtotalNeto);
                                        dt.setNeto(""+newsubtotalNeto);
                                        dt.setIva(""+subtotalIva1);
                                        dt.setTotal(""+total);
                                        dt.setPorcentajeDescuentoAdicional(""+descuento);
                                        dt.setDescuentoAdicional(""+ds);
                                        DataBaseHelper.updateDetallePedidoCreate(dt, DepcApplication.getApplication().getDetallePedidoDao());

                                    }
                                }
                            }
                        }
                        //**********************************

                        List<DetallePedido> detalles1 = DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pd.getId());

                        for(DetallePedido dt : detalles1){
                            if (dt.getCantidad() != null) {
                                cantidad += Float.parseFloat(""+dt.getCantidad());
                                if (dt.getSubtotal() != null)
                                    subtotal += Double.parseDouble(""+dt.getSubtotal());
                                    subtotalNetos += Double.parseDouble(""+dt.getSubtotalNeto());

                                    descuentosNetos += Double.parseDouble(""+dt.getDescuento());
                                    descuentosNetos += Double.parseDouble(""+dt.getDescuentoAdicional());

                                    if (Double.parseDouble(""+dt.getPorcentajeIva()) == 0){
                                        base0 += Double.parseDouble(""+dt.getSubtotalNeto());
                                    }else{
                                        iva = Double.parseDouble(""+dt.getPorcentajeIva());
                                        base12 += Double.parseDouble(""+dt.getSubtotalNeto());
                                    }
                            }
                        }


                        Double descueto = (subtotalNetos * ((Double.parseDouble(descuaneto_adicional.getText().toString())) / 100));
                        descueto = Utils.roundFloat(descueto,4);
                        descuentosNetos = Utils.roundFloat(descuentosNetos,4);



                        Double subTotalNeto = base0 + base12;
                        subTotalNeto = Utils.roundFloat(subTotalNeto,4);
                        subtotal = Utils.roundFloat(subtotal,4);

                        base0 = Utils.roundFloat(base0,4);
                        base12 = Utils.roundFloat(base12,4);


                        subtotalEdtit.setText(""+Utils.foramatearMiles(String.format("%.2f",subtotal)) /*Utils.roundFloat(subtotal,2)*/);
                        subtotal_neto.setText(""+ Utils.foramatearMiles(String.format("%.2f",subTotalNeto))/*Utils.roundFloat(subTotalNeto,2)*/ );
                        subtotalIva = base12 * (iva / 100);
                        subtotalIva = Utils.roundFloat(subtotalIva,4);
                        impuesto.setText(""+Utils.foramatearMiles(String.format("%.2f",subtotalIva))  /*Utils.roundFloat(subtotalIva,2)*/ );
                        Double total = subTotalNeto + subtotalIva;
                        total =  Utils.roundFloat(total,2);
                        total_pedido.setText(""+Utils.foramatearMiles(String.format("%.2f", total)) /*Utils.roundFloat(total,2)*/);

                        pedido.setSubtotal(""+subtotal);
                        pedido.setBase0(""+base0);
                        pedido.setBase12(""+base12);
                        pedido.setDescuentoAdicional(""+descueto  /*Utils.roundFloat(descueto,2)*/ );
                        pedido.setDescuento(""+descuentosNetos  /*Utils.roundFloat(descuentosNetos,2)*/);
                        pedido.setIva(""+subtotalIva  /*Utils.roundFloat(iva,2)*/);
                        pedido.setTotal(""+ total /*Utils.roundFloat(total,2)*/);

                        DataBaseHelper.savePedido(pedido,DepcApplication.getApplication().getPedidosDao());
                        prepareListData(detalles1);


                        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, idPedido, isCotizacion);
                        expandable.setAdapter(listAdapter);

                        if(listDataChild.size() > 0) {
                            enviar.setEnabled(true);
                            enviar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("TAG---", "Listo para enviar pedido");
                                    String mensajeAlert = "¿Desea enviar el pedido?";

                                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                                DetallePedidoActivity.this);
                                        alert.setTitle("Atención");
                                        alert.setMessage(mensajeAlert);
                                        alert.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sendPedido(false);
                                                dialog.dismiss();

                                            }
                                        });
                                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        });

                                        alert.show();

                                }
                            });



                            modificar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String mensajeAlert = "¿Desea modificar el pedido?";

                                    AlertDialog.Builder alert = new AlertDialog.Builder(
                                            DetallePedidoActivity.this);
                                    alert.setTitle("Atención");
                                    alert.setMessage(mensajeAlert);
                                    alert.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sendPedido(true);
                                            dialog.dismiss();

                                        }
                                    });
                                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });

                                    alert.show();

                                }
                            });
                        }


                        Utils.setExpandableListViewHeight(expandable, scroll);
                        expandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v,
                                                        int groupPosition, long id) {
                                Utils.setListViewHeight(parent, groupPosition);
                                return false;
                            }
                        });

                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        getPedidos();
    }

    private void prepareListData(List<DetallePedido> detalles) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Productos>>();
        List<Productos> productos = new ArrayList<Productos>();


        List<List<Productos>> prd = new ArrayList<List<Productos>>();

        for(DetallePedido detail: detalles){

            try {

                if(DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(), detail.getCodigo()) != null){
                    if(DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(), detail.getCodigo()).size() > 0) {
                        Productos p = DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(), detail.getCodigo()).get(0);
                        p.setIdDetail(detail.getId());
                        productos.add(p);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        for(Productos pr : productos){
            if(listDataHeader.contains("BODEGA "+pr.getDescripcion_bodega())){
                int retval = listDataHeader.indexOf("BODEGA "+pr.getDescripcion_bodega());
                prd.get(retval).add(pr);

            }else{
                listDataHeader.add("BODEGA "+pr.getDescripcion_bodega());
                List<Productos > aux = new ArrayList<Productos>();
                aux.add(pr);
                prd.add(aux);
            }

        }

        int contador = 0;
        for(String title : listDataHeader){
            listDataChild.put(title, prd.get(contador));
            contador++;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(materialDesignFAM != null){
                if(materialDesignFAM.isOpened()){
                    materialDesignFAM.close(true);
                    return true;
                }else{
                    finish();
                }
            }else{
                finish();
            }
        }
        return true;
    }


    @Override
    public void doRetry() {

    }


    private void removePedido(){

        showProgressWait();

        //JSON SEND
        RemoverPedidoModel model = new RemoverPedidoModel();
        model.setCuenta_id(""+pedido.getCuenta_id());
        model.setMetodo("EliminarPreventaCliente");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {


            IRemoverPedido request = DepcApplication.getApplication().getRestAdapter().create(IRemoverPedido.class);
            Call<IRemoverPedido.dataPedido> call = request.getRemoverPedido(body);
            call.enqueue(new Callback<IRemoverPedido.dataPedido>() {
                @Override
                public void onResponse(Call<IRemoverPedido.dataPedido> call, Response<IRemoverPedido.dataPedido> response) {
                    if (response.isSuccessful()) {

                        dataRemove = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataRemove != null) {
                                if (dataRemove.getStatus() == Const.COD_ERROR_SUCCESS) {

                                    DataBaseHelper.deleteDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), "" + pedido.getId());
                                    DataBaseHelper.deletePedidosByID(DepcApplication.getApplication().getPedidosDao(), "" + pedido.getId());

                                    new AlertDialog.Builder(DetallePedidoActivity.this)
                                            .setTitle("Atención")
                                            .setMessage("Pedido Eliminado")
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();


                                }else{
                                    if (data.getStatus_message() != null){
                                        mensajeError = data.getStatus_message();
                                    }

                                    showAlert(mensajeError);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                        }

                    } else {
                        hideProgressWait();
                        showAlert(Const.ERROR_DEFAULT);

                    }
                }

                @Override
                public void onFailure(Call<IRemoverPedido.dataPedido> call, Throwable t) {
                    hideProgressWait();
                    showAlert(Const.ERROR_DEFAULT);
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showAlert(Const.ERROR_DEFAULT);

        }

    }

    private void sendPedido(Boolean isActualizar){

        showProgressWait();

        String codigoUsuario = "";
        try {
            List<Usuario> usuario = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuario != null){
                if (usuario.size() > 0){
                    Usuario user = usuario.get(0);
                    codigoUsuario = user.getUsuario();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //JSON SEND
        CrearPreventaModel model = new CrearPreventaModel();
        model.setUsuario_id(""+codigoUsuario);
        model.setCliente_id(""+cliente.getCodigo_cliente_id());
        model.setTotal_neto(""+pedido.getSubtotal());
        model.setForma_pago(""+pedido.getForma_pago());
        model.setDias_credito(""+cliente.getDias_credito());
        model.setTotal(""+pedido.getTotal());
        model.setCobrar_iva(""+cliente.getCobrar_iva());
        model.setObservaciones(""+pedido.getComentario());
        model.setDireccion_envio_id(""+pedido.getCodigo_direccione_entrega());
        model.setForma_pago_id(""+pedido.getForma_pago_id());
        model.setFoto(""+pedido.getFoto());

        List<DetalleCrearPreventaModel> detalleModel = new ArrayList<DetalleCrearPreventaModel>();

        try {
            List<DetallePedido> detalles = DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pedido.getId());

            if (detalles != null){
                for (DetallePedido dt : detalles){
                    DetalleCrearPreventaModel md = new DetalleCrearPreventaModel();

                    double descuento = 0;

                    if (dt.getPorcentajeDescuento() != null) {
                        if (Float.parseFloat(dt.getPorcentajeDescuento()) > 0) {
                            descuento = (Float.parseFloat("" + dt.getPrecioUnitario()) * (Float.parseFloat(dt.getPorcentajeDescuento()) / 100));
                            descuento = Utils.roundFloat(descuento, 2);
                        }

                    }

                    md.setCantidad(""+dt.getCantidad());
                    md.setPrecio(""+dt.getPrecioUnitario());
                    md.setDescuento(""+descuento);
                    md.setBodega_id(""+pedido.getBodega());
                    md.setCodigo_item(""+dt.getCodigo());
                    md.setDescripcion(""+dt.getDescripcion());
                    md.setCosto(""+dt.getCosto());
                    md.setCodigo_unidad(""+dt.getCodigo());
                    detalleModel.add(md);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        model.setDetalle(detalleModel);
        if (!isActualizar) {
            model.setMetodo("CrearPreventa");
        }else{
            model.setMetodo("ModificarPreventa");
            model.setCuenta_id(""+pedido.getCuenta_id());
        }

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);
        try {
            IPedido request = DepcApplication.getApplication().getRestAdapter().create(IPedido.class);
            call = request.getPedido(body);
            call.enqueue(new Callback<IPedido.dataPedido>() {
                @Override
                public void onResponse(Call<IPedido.dataPedido> call, Response<IPedido.dataPedido> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();
                            String mensajeError = Const.ERROR_COBERTURA;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    //success

                                    if (data.getData().getCuenta_id() != null){
                                        pedido.setCuenta_id(""+data.getData().getCuenta_id());
                                        pedido.setEstadoPedido("1");
                                        DataBaseHelper.updatePedido(pedido, DepcApplication.getApplication().getPedidosDao());
                                    }

                                    if (isActualizar) {
                                        showAlert("Preventa modificada con éxito");
                                    }else{
                                        showAlert("Preventa creada con éxito");
                                    }
                                    getPedidos();

                                    return;
                                }else{
                                    //Error

                                    if (data.getStatus_message() != null){
                                        if (data.getStatus_message().length() > 0){
                                            mensajeError = data.getStatus_message();
                                        }
                                    }

                                    showAlert(""+mensajeError);

                                    return;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            showError();
                        }

                    } else {
                        showError();
                    }
                }

                @Override
                public void onFailure(Call<IPedido.dataPedido> call, Throwable t) {

                    showError();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showError();

        }

    }

    private void showError(){
        hideProgressWait();
        showAlert(""+Const.ERROR_COBERTURA);
    }


}
