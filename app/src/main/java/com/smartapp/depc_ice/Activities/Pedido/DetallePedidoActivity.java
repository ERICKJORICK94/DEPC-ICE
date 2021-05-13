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
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
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
                                DataBaseHelper.deleteDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(),""+pedido.getId());
                                DataBaseHelper.deletePedidosByID(DepcApplication.getApplication().getPedidosDao(),""+pedido.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                            finish();


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


    }


    public void getPedidos(){

        enviar.setEnabled(false);

        if (pedido != null){
            clienteName.setText(""+pedido.getNombreCliente());
            ruc.setText(""+pedido.getRucCliente());
            fecha.setText(""+pedido.getFecha());
            direccion.setText(""+pedido.getDireccionCliente());
            documento.setText("" + pedido.getId());

            if (pedido.getEstadoPedido().equals("0")){
                anular.setVisibility(View.GONE);
                modificar.setVisibility(View.GONE);
                enviar.setVisibility(View.VISIBLE);
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

                                        Productos prod = DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(), dt.getCodigo()).get(0);

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
                                                try {

                                                    final Pedidos ped =  DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(), idPedido).get(0);
                                                    //sendPedido();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
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


}
