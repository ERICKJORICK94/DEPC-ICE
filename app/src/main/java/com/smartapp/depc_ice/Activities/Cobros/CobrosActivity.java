package com.smartapp.depc_ice.Activities.Cobros;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.stmt.QueryBuilder;
import com.smartapp.depc_ice.Activities.Cobros.Adapter.ListaCobrosAdapter;
import com.smartapp.depc_ice.Activities.Despachos.Adapter.ListaDespachoAdapter;
import com.smartapp.depc_ice.Activities.Despachos.DetalleDespachosPlanificacionActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetalleFacturas;
import com.smartapp.depc_ice.Entities.DetalleFormaPago;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.FormaPago;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IFormaPago;
import com.smartapp.depc_ice.Interface.IFormaPago;
import com.smartapp.depc_ice.Models.EstadoGabinetModel;
import com.smartapp.depc_ice.Provider.WebServices;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.BTDeviceList;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.NonScrollListView;
import com.smartapp.depc_ice.Utils.UIHelper;
import com.smartapp.depc_ice.Utils.Utils;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;
import com.smartapp.depc_ice.Models.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CobrosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private ListView lista;
    private Button cancela;
    private Button cobro_rapido;
    private ScrollView scroll;
    private TextView valor_total;
    private TextView edt_por_vencer;
    private TextView edt_vencido;
    private TextView edt_total;
    private TextView fac;
    private TextView nv;
    private TextView let;
    private TextView nd;
    private TextView pag;
    private double valorTotal = 0;
    private TextView impresora;
    private androidx.appcompat.app.AlertDialog dialog;
    private Connection connection = null;
    private UIHelper helper = new UIHelper(this);
    private int selectPrinter = -1;
    private String zpl = "";
    private ArrayList<Device> mDeviceList = new ArrayList<Device>();
    private BluetoothAdapter mBluetoothAdapter;
    private double totalPagar = 0;
    private double totalPagarDEbitar = 0;
    private double ingreso = 0;
    private EditText edt_referencia;
    private EditText edit_numero_ingreso;
    private String factura_id = "";
    private String id_vaje = "";
    private String cuenta_id = "";
    private List<DetalleFacturas> detalleFacturas;
    private  Usuario user;
    DetalleFormaPago pago;

    private List<String> listaVentas = new ArrayList<String>();
    int positionSelectFactura = - 1;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    private Call<IFormaPago.dataBodega> call;
    private IFormaPago.dataBodega data;

    private String pagadoPor = "";
    int indexFactura = -1;
    private String nombreCliente = "";
    private String URL_ENVIO = "https://webserver.depconsa.com/DepWSR/application/libraries/wsapp.php";

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();



            // When discovery finds a new device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Device deviceEntity = new Device();
                deviceEntity.setName(""+device.getName());
                deviceEntity.setAddress(""+device.getAddress());

                if (mDeviceList != null) {
                    boolean flag = true;
                    for(Device dv : mDeviceList){
                        if(dv.getAddress().equals(""+deviceEntity.getAddress())){
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        mDeviceList.add(deviceEntity);
                    }
                }

                paintList();
            }

            // When discovery cycle finished
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if(mDeviceList.size() == 0){
                    if (progress != null){
                        if(progress.isShowing()){
                            progress.dismiss();
                        }
                    }
                    showAlert("No se encontraron dispositivos");
                }else{
                    //paintList();
                }
            }


        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.cobros_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);


        //scroll = (ScrollView) layout.findViewById(R.id.scroll);
        impresora = (TextView) layout.findViewById(R.id.impresora);
        lista = (ListView) layout.findViewById(R.id.lista);
        cancela = (Button) layout.findViewById(R.id.cancela);
        cobro_rapido = (Button) layout.findViewById(R.id.cobro_rapido);
        valor_total = (TextView) layout.findViewById(R.id.valor_total);
        edt_vencido = (TextView) layout.findViewById(R.id.edt_vencido);
        edt_por_vencer = (TextView) layout.findViewById(R.id.edt_por_vencer);
        edt_total = (TextView) layout.findViewById(R.id.edt_total);
        fac = (TextView) layout.findViewById(R.id.fac);
        nv = (TextView) layout.findViewById(R.id.nv);
        let = (TextView) layout.findViewById(R.id.let);
        nd = (TextView) layout.findViewById(R.id.nd);
        pag = (TextView) layout.findViewById(R.id.pag);

        //lista.setFocusable(false);

        impresora.setVisibility(View.GONE);

        if (Utils.getDevices(CobrosActivity.this) != null){
            if (Utils.getDevices(CobrosActivity.this).size() > 0){
                mDeviceList = Utils.getDevices(CobrosActivity.this);
                selectPrinter = 0;
                //buscar_impresora.setText(""+mDeviceList.get(0).getName()+" \n "+mDeviceList.get(0).getAddress());
                impresora.setVisibility(View.VISIBLE);
                impresora.setText("IMPRESORA: "+mDeviceList.get(0).getName()+"  "+mDeviceList.get(0).getAddress());
            }
        }

        String recaudadorString = "";
        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0){
                    user = usuarios.get(0);
                    if (user.getNombrescompletos() != null){
                        recaudadorString = ""+user.getNombrescompletos();
                    }
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (indexFactura == -1){
                    showAlert("SELECCIONE UNA FACTURA ANTES DE CONTINUAR");
                    return;
                }

                Intent intent = new Intent(CobrosActivity.this, DetalleCobroActivity.class);
                intent.putExtra("detalle_factura",detalleFacturas.get(indexFactura));
                intent.putExtra("id_vaje",id_vaje);
                intent.putExtra("cuenta_id",cuenta_id);
                intent.putExtra("nombreCliente",nombreCliente);
                startActivity(intent);
                return;
            }
        });


        cobro_rapido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (indexFactura == -1){
                        showAlert("SELECCIONE UNA FACTURA ANTES DE CONTINUAR");
                        return;
                    }

                    showAlertPagoRapido();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });



        if (getIntent() != null){
            factura_id = getIntent().getStringExtra("factura_id");
            id_vaje = getIntent().getStringExtra("id_vaje");
            cuenta_id = getIntent().getStringExtra("cuenta_id");
            nombreCliente = getIntent().getStringExtra("nombreCliente");

        }
        showList();
        getFormaPagos();
    }

    private void getFormaPagos(){

        showProgressWait();

        //JSON SEND
        EstadoGabinetModel model = new EstadoGabinetModel();
        model.setMetodo("ListarFormasPago");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IFormaPago request = DepcApplication.getApplication().getRestAdapter().create(IFormaPago.class);
            call = request.getBodegas(body);
            call.enqueue(new Callback<IFormaPago.dataBodega>() {
                @Override
                public void onResponse(Call<IFormaPago.dataBodega> call, Response<IFormaPago.dataBodega> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListarFormasPago() != null) {
                                            if (data.getData().getListarFormasPago().size() > 0) {


                                                final List<FormaPago> bodegas;
                                                bodegas = data.getData().getListarFormasPago().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteFormaPago(DepcApplication.getApplication().getFormaPagoDao());
                                                    DepcApplication.getApplication().getFormaPagoDao().callBatchTasks(new Callable<FormaPago>() {
                                                        @Override
                                                        public FormaPago call() throws Exception {
                                                            for (FormaPago cl : bodegas) {
                                                                DataBaseHelper.saveFormaPago(cl, DepcApplication.getApplication().getFormaPagoDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                //showListZonas();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (data.getStatus_message() != null){
                                        mensajeError = data.getStatus_message();
                                    }
                                }
                            }

                            //showListZonas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            //showListZonas();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        //showListZonas();
                    }
                }

                @Override
                public void onFailure(Call<IFormaPago.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    //showListZonas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            //showListZonas();

        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Utils.getSinImpresora(CobrosActivity.this)){
            getMenuInflater().inflate(R.menu.impresora, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_print:
                if(impresora != null){
                    if (impresora.getVisibility() == View.VISIBLE){
                        if(zpl != null){
                            if(zpl.length() > 0){
                                if (Utils.getZebra(getBaseContext())){
                                    sendPrint();
                                }else {
                                    connect();
                                }
                                return true;
                            }
                        }
                    }
                }

                if (validateStatus()){

                    if (Utils.getZebra(getBaseContext())) {

                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        mBluetoothAdapter.startDiscovery();

                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(mReceiver, filter);

                        progress = ProgressDialog.show(CobrosActivity.this, "",
                                "Buscando Impresora...", true);
                        progress.setCancelable(true);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setOnCancelListener(new DialogInterface.OnCancelListener(){
                            @Override
                            public void onCancel(DialogInterface dialog){
                                if (mReceiver != null) {
                                    unregisterReceiver(mReceiver);
                                }
                            }});
                    }else{
                        connect();
                    }

                }

                return true;
        }

        finish();

        return true;
    }


    private boolean validateStatus(){

        hideProgressWait();
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(CobrosActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // ask permissions here using below code
                ActivityCompat.requestPermissions(CobrosActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        10000);
            }
        }


        if (Utils.isBluetoothActivate()){
            //code status On


            return true;
        }else{
            //code status Off
            showAlert("Por favor, encienda el Bluetooth para continuar con la impresión");
            return false;
        }

    }


    protected void connect() {
        //getCobros();
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), BTDeviceList.class);
            this.startActivityForResult(BTIntent, BTDeviceList.REQUEST_CONNECT_BT);
        }
        else{

            OutputStream opstream = null;
            try {
                opstream = btsocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btoutputstream = opstream;
            print_bt(generatedZPL());

        }

    }

    private void print_bt(String text) {
        try {

            btoutputstream = btsocket.getOutputStream();

            byte[] printformat = { 0x1B, 0x21, FONT_TYPE };
            btoutputstream.write(printformat);
            String msg = text+"\n\n\n";
            btoutputstream.write(msg.getBytes());
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.write(0x0D);
            btoutputstream.flush();


            //zpl = "";

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //getCobros();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
            //getCobros();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                btoutputstream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            btsocket = BTDeviceList.getSocket();
            if(btsocket != null){
                print_bt(generatedZPL());
            }else{
                //getCobros();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //getCobros();
        }
    }



    private void paintList(){

        if (progress != null){
            if (progress.isShowing()){
                progress.dismiss();
            }
        }

        if (dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
        }

        androidx.appcompat.app.AlertDialog.Builder builderSingle = new androidx.appcompat.app.AlertDialog.Builder(this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Seleccione impresora: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);

        for (Device dv : mDeviceList){
            arrayAdapter.add(""+dv.getName() + "\n" + dv.getAddress());
        }


        builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                selectPrinter = which;

                ArrayList<Device> auxDevice = new ArrayList<Device>();
                auxDevice.add(mDeviceList.get(which));
                Utils.saveDevices(auxDevice, CobrosActivity.this);

                androidx.appcompat.app.AlertDialog.Builder builderInner = new androidx.appcompat.app.AlertDialog.Builder(CobrosActivity.this);
                builderInner.setMessage(strName);
                //buscar_impresora.setText(strName);
                impresora.setVisibility(View.VISIBLE);
                impresora.setText("IMPRESORA: "+mDeviceList.get(0).getName()+"  "+mDeviceList.get(0).getAddress());
                builderInner.setTitle("Seleccionastes: ");
                builderInner.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mReceiver != null) {
                            unregisterReceiver(mReceiver);
                        }
                        dialog.dismiss();
                        Log.e("TAG--","pasdasdlas----- zpl: "+zpl);
                        if (zpl != null){
                            if (zpl.length() > 0){
                                Log.e("TAG--","plas----- zpl");
                                //sendPrint();
                                if (Utils.getZebra(getBaseContext())){
                                    sendPrint();
                                }else {
                                    connect();
                                }

                            }
                        }
                    }
                });
                builderInner.show();
            }
        });
        //builderSingle.show();
        dialog = builderSingle.create();
        dialog.show();


    }

    private void showList(){

        /*edt_vencido.setText(""+ Utils.foramatearMiles(String.format("%.2f", 123.32)));
        edt_por_vencer.setText(""+Utils.foramatearMiles(String.format("%.2f", 23.32)));
        edt_total.setText(""+Utils.foramatearMiles(String.format("%.2f", 123.32 + 23.32)));
        totalPagar = 123.32 + 23.32;

        fac.setText(""+Utils.foramatearMiles(String.format("%.2f", 10.30)));
        nv.setText(""+Utils.foramatearMiles(String.format("%.2f", 36.56)));
        let.setText(""+Utils.foramatearMiles(String.format("%.2f", 89.10)));
        nd.setText(""+Utils.foramatearMiles(String.format("%.2f", 24.39)));
        pag.setText(""+Utils.foramatearMiles(String.format("%.2f", 11.31)));
        valor_total.setText(""+Utils.roundFloat(totalPagar, 2));*/

        try {
            detalleFacturas = DataBaseHelper.getDetalleFacturasByIDFactura(DepcApplication.getApplication().getDetalleFacturasDao(), ""+factura_id);
            if(detalleFacturas != null){

                lista.setAdapter(new ListaCobrosAdapter(CobrosActivity.this, detalleFacturas));
                //scroll.smoothScrollTo(0,0);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        /*try {
            if (DataBaseHelper.getCobroByCODCliente(San32Application.getApplication().getCobroDao(),""+cliente.getCODIGO()) != null){

                cobroList = DataBaseHelper.getCobroByCODCliente(San32Application.getApplication().getCobroDao(),""+cliente.getCODIGO());

                if (cobroList != null) {
                    if (cobroList.size() > 0) {

                        allCobroList = cobroList;
                        Log.e("TAG---","cobroList.size(): > "+cobroList.size());
                        lista.setAdapter(new ListaCobrosAdapter(this));
                        scroll.smoothScrollTo(0,0);

                        double saldo_vencido = 0;
                        double saldo_por_vencer = 0;
                        double fac_ = 0;
                        double nv_ = 0;
                        double let_ = 0;
                        double nd_ = 0;
                        double pag_ = 0;

                        listaVentas = new ArrayList<String>();
                        groupCobroList = new ArrayList<List<Cobro>>();

                        for (Cobro cl : cobroList){

                            if (cl.getDias_vencido() != null){
                                if (Utils.isNumber(cl.getDias_vencido())){
                                    int dias = Integer.parseInt(cl.getDias_vencido());

                                    if (dias > 0){
                                        if(cl.getSaldo() != null){
                                            if(Utils.isNumberDecimal(cl.getSaldo())){
                                                saldo_vencido += Double.parseDouble(cl.getSaldo());

                                                if (cl.getTipo_doc() != null){
                                                    if (cl.getTipo_doc().equals(Const.FACTURA)){
                                                        fac_ += Double.parseDouble(cl.getSaldo());
                                                    }else if (cl.getTipo_doc().equals(Const.NOTA_VENTA)){
                                                        nv_ += Double.parseDouble(cl.getSaldo());
                                                    }else  if (cl.getTipo_doc().equals(Const.LETRA)){
                                                        let_ += Double.parseDouble(cl.getSaldo());
                                                    }else  if (cl.getTipo_doc().equals(Const.NOTA_DEBITO)){
                                                        nd_ += Double.parseDouble(cl.getSaldo());
                                                    }else  if (cl.getTipo_doc().equals(Const.PAGARE)){
                                                        pag_ += Double.parseDouble(cl.getSaldo());
                                                    }

                                                }
                                            }
                                        }
                                    }else{
                                            if(cl.getSaldo() != null){
                                                if(Utils.isNumberDecimal(cl.getSaldo())){
                                                    saldo_por_vencer += Double.parseDouble(cl.getSaldo());

                                                    if (cl.getTipo_doc() != null){
                                                        if (cl.getTipo_doc().equals(Const.FACTURA)){
                                                            fac_ += Double.parseDouble(cl.getSaldo());
                                                        }else if (cl.getTipo_doc().equals(Const.NOTA_VENTA)){
                                                            nv_ += Double.parseDouble(cl.getSaldo());
                                                        }else  if (cl.getTipo_doc().equals(Const.LETRA)){
                                                            let_ += Double.parseDouble(cl.getSaldo());
                                                        }else  if (cl.getTipo_doc().equals(Const.NOTA_DEBITO)){
                                                            nd_ += Double.parseDouble(cl.getSaldo());
                                                        }else  if (cl.getTipo_doc().equals(Const.PAGARE)){
                                                            pag_ += Double.parseDouble(cl.getSaldo());
                                                        }

                                                    }

                                                }
                                            }
                                    }
                                }
                            }

                            String sector = cl.getReferencia()+" "+cl.getSeq_compte();
                            if(!listaVentas.contains(sector)){
                                listaVentas.add(sector);
                            }


                        }

                        edt_vencido.setText(""+ Utils.foramatearMiles(String.format("%.2f", saldo_vencido)));
                        edt_por_vencer.setText(""+Utils.foramatearMiles(String.format("%.2f", saldo_por_vencer)));
                        edt_total.setText(""+Utils.foramatearMiles(String.format("%.2f", saldo_vencido + saldo_por_vencer)));
                        totalPagar = saldo_vencido + saldo_por_vencer;

                        fac.setText(""+Utils.foramatearMiles(String.format("%.2f", fac_)));
                        nv.setText(""+Utils.foramatearMiles(String.format("%.2f", nv_)));
                        let.setText(""+Utils.foramatearMiles(String.format("%.2f", let_)));
                        nd.setText(""+Utils.foramatearMiles(String.format("%.2f", nd_)));
                        pag.setText(""+Utils.foramatearMiles(String.format("%.2f", pag_)));



                        //----------------------------------------------------------------

                        List<Cobro> grupo = allCobroList;
                        groupCobroList.add(grupo);
                        for(String sc : listaVentas){

                            List<Cobro> grupoAux = new ArrayList<Cobro>();

                            for (Cobro cl : allCobroList){

                                String sector = cl.getReferencia()+" "+cl.getSeq_compte();
                                if(sc.equals(sector)){
                                    grupoAux.add(cl);
                                }
                            }

                            groupCobroList.add(grupoAux);

                        }

                        listaVentas.add(0,"TODOS");

                        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_1, listaVentas);
                        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adp1);



                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                Log.e("TAG---","click en el spinner: > "+position);
                                cobroList = groupCobroList.get(position);
                                lista.setAdapter(new ListaCobrosAdapter(CobrosActivity.this));
                                scroll.smoothScrollTo(0,0);
                                calculateValor(Double.parseDouble(valor_total.getText().toString()));

                                double saldo_vencido = 0;
                                double saldo_por_vencer = 0;
                                double fac_ = 0;
                                double nv_ = 0;
                                double let_ = 0;
                                double nd_ = 0;
                                double pag_ = 0;
                                double suma = 0;

                                for (Cobro cl : cobroList){

                                    suma += Double.parseDouble(cl.getSaldo());

                                    if (cl.getDias_vencido() != null){
                                        if (Utils.isNumber(cl.getDias_vencido())){
                                            int dias = Integer.parseInt(cl.getDias_vencido());

                                            if (dias > 0){
                                                if(cl.getSaldo() != null){
                                                    if(Utils.isNumberDecimal(cl.getSaldo())){
                                                        saldo_vencido += Double.parseDouble(cl.getSaldo());

                                                        if (cl.getTipo_doc() != null){
                                                            if (cl.getTipo_doc().equals(Const.FACTURA)){
                                                                fac_ += Double.parseDouble(cl.getSaldo());
                                                            }else if (cl.getTipo_doc().equals(Const.NOTA_VENTA)){
                                                                nv_ += Double.parseDouble(cl.getSaldo());
                                                            }else  if (cl.getTipo_doc().equals(Const.LETRA)){
                                                                let_ += Double.parseDouble(cl.getSaldo());
                                                            }else  if (cl.getTipo_doc().equals(Const.NOTA_DEBITO)){
                                                                nd_ += Double.parseDouble(cl.getSaldo());
                                                            }else  if (cl.getTipo_doc().equals(Const.PAGARE)){
                                                                pag_ += Double.parseDouble(cl.getSaldo());
                                                            }

                                                        }
                                                    }
                                                }else{
                                                    Log.e("lll","saldo null");
                                                }
                                            }else{

                                                if(cl.getSaldo() != null){
                                                    if(Utils.isNumberDecimal(cl.getSaldo())){
                                                        saldo_por_vencer += Double.parseDouble(cl.getSaldo());

                                                        if (cl.getTipo_doc() != null){
                                                            if (cl.getTipo_doc().equals(Const.FACTURA)){
                                                                fac_ += Double.parseDouble(cl.getSaldo());
                                                            }else if (cl.getTipo_doc().equals(Const.NOTA_VENTA)){
                                                                nv_ += Double.parseDouble(cl.getSaldo());
                                                            }else  if (cl.getTipo_doc().equals(Const.LETRA)){
                                                                let_ += Double.parseDouble(cl.getSaldo());
                                                            }else  if (cl.getTipo_doc().equals(Const.NOTA_DEBITO)){
                                                                nd_ += Double.parseDouble(cl.getSaldo());
                                                            }else  if (cl.getTipo_doc().equals(Const.PAGARE)){
                                                                pag_ += Double.parseDouble(cl.getSaldo());
                                                            }

                                                        }

                                                    }
                                                }else{
                                                    Log.e("lll","saldo null");
                                                }

                                            }
                                        }
                                    }

                                }

                                edt_vencido.setText(""+Utils.foramatearMiles( String.format("%.2f", saldo_vencido)));
                                edt_por_vencer.setText(""+Utils.foramatearMiles(String.format("%.2f", saldo_por_vencer)));
                                edt_total.setText(""+Utils.foramatearMiles(String.format("%.2f", saldo_vencido + saldo_por_vencer)));

                                fac.setText(""+Utils.foramatearMiles(String.format("%.2f", fac_)));
                                nv.setText(""+Utils.foramatearMiles(String.format("%.2f", nv_)));
                                let.setText(""+Utils.foramatearMiles(String.format("%.2f", let_)));
                                nd.setText(""+Utils.foramatearMiles(String.format("%.2f", nd_)));
                                pag.setText(""+Utils.foramatearMiles(String.format("%.2f", pag_)));

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {

                            }

                        });

                        //----------------------------------------------------------------



                        hideProgressWait();
                        getFormas();

                    }else{
                        hideProgressWait();
                        showAlert(Const.ERROR_NO_DATA_DOC);

                        lista.setAdapter(null);
                        edt_vencido.setText("$ 0.00");
                        edt_por_vencer.setText("$ 0.00");
                        edt_total.setText("$ 0.00");
                        fac.setText("$ 0.00");
                        nv.setText("$ 0.00");
                        let.setText("$ 0.00");
                        nd.setText("$ 0.00");
                        pag.setText("$ 0.00");
                        spinner.setAdapter(null);
                    }
                }else{
                    hideProgressWait();
                    showAlert(Const.ERROR_NO_DATA_DOC);
                    lista.setAdapter(null);
                    edt_vencido.setText("$ 0.00");
                    edt_por_vencer.setText("$ 0.00");
                    edt_total.setText("$ 0.00");
                    fac.setText("$ 0.00");
                    nv.setText("$ 0.00");
                    let.setText("$ 0.00");
                    nd.setText("$ 0.00");
                    pag.setText("$ 0.00");
                    spinner.setAdapter(null);
                }

            }else{
                hideProgressWait();
                showAlert(Const.ERROR_NO_DATA_DOC);
                lista.setAdapter(null);
                edt_vencido.setText("$ 0.00");
                edt_por_vencer.setText("$ 0.00");
                edt_total.setText("$ 0.00");
                fac.setText("$ 0.00");
                nv.setText("$ 0.00");
                let.setText("$ 0.00");
                nd.setText("$ 0.00");
                pag.setText("$ 0.00");
                spinner.setAdapter(null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Const.ERROR_NO_DATA_DOC);
            lista.setAdapter(null);
            edt_vencido.setText("$ 0.00");
            edt_por_vencer.setText("$ 0.00");
            edt_total.setText("$ 0.00");
            fac.setText("$ 0.00");
            nv.setText("$ 0.00");
            let.setText("$ 0.00");
            nd.setText("$ 0.00");
            pag.setText("$ 0.00");
            spinner.setAdapter(null);
        }*/

    }


    public void calculateValor(int position){

        Log.e(TAG,""+position);
        indexFactura = position;
    }


    private void showAlertPagoRapido() throws SQLException {

        String recaudadorString = "";
        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0){
                    Usuario user = usuarios.get(0);
                    if (user.getNombrescompletos() != null){
                        recaudadorString = ""+user.getNombrescompletos();
                    }
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ingreso = Double.parseDouble(valor_total.getText().toString());
        totalPagarDEbitar = totalPagar;

        LayoutInflater mInflater = LayoutInflater.from(this);
        String abonaCancela = "";

        float totalDeuda = 0;
        View detailProduct = mInflater.inflate(R.layout.cobro_rapido, null);
        final AlertDialog alertPago = new AlertDialog.Builder(this).create();
        alertPago.setView(detailProduct);

        valorTotal = 0;
        EditText recaudador = (EditText) detailProduct.findViewById(R.id.recaudador);
        TextView total_facturas = (TextView) detailProduct.findViewById(R.id.total_facturas);
        edt_referencia = (EditText) detailProduct.findViewById(R.id.edt_referencia);
        edit_numero_ingreso = (EditText) detailProduct.findViewById(R.id.edit_numero_ingreso);
        Button enviar = (Button) detailProduct.findViewById(R.id.enviar);

        total_facturas.setText(""+Utils.foramatearMiles(""+Utils.roundFloat(totalPagar, 2)));
        recaudador.setText(""+recaudadorString);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertPago.dismiss();
                pagadoPor = ""+edit_numero_ingreso.getText().toString();
                pago = null;
                EnviarFormasPago pt = new EnviarFormasPago();
                pt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        alertPago.show();


    }

    private class EnviarFormasPago extends AsyncTask<Void, Integer, Boolean> {
        String mensaje;
        boolean isError = false;

        @Override
        protected void onPreExecute() {
            hideProgressWait();
            showProgressWait("Enviando forma Pago...");
            //
            mensaje = "Envío exitoso";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            WebServices ws = new WebServices();

            try {

                JSONObject jsonRespuesta = null;

                pago = new DetalleFormaPago();

                pago.setId_viaje(""+id_vaje);
                pago.setFactura_id(""+detalleFacturas.get(indexFactura).getFactura_id());
                pago.setCuenta_id(""+cuenta_id);
                pago.setPrefijo1(""+detalleFacturas.get(indexFactura).getPrefijo1());
                pago.setPrefijo2(""+detalleFacturas.get(indexFactura).getPrefijo1());
                pago.setFactura_fiscal(""+detalleFacturas.get(indexFactura).getFactura_fiscal());
                pago.setNombre_forma_de_pago("EFECTIVO");
                pago.setNombre_corto_forma_de_pago("EFE");
                pago.setForma_pago("1");
                pago.setValor(""+detalleFacturas.get(indexFactura).getSaldo());
                pago.setBanco_origen("null");
                pago.setNum_cuenta_origen("");
                pago.setNum_documento("");
                pago.setCuenta_bancaria("null");
                pago.setUsuario_crea(""+user.getUsuario());
                pago.setNombre_persona_paga(""+pagadoPor);
                pago.setFirma_persona_paga("");
                pago.setEstado(false);
                pago.setFecha(Utils.getFecha());
                pago.setNombreCliente(nombreCliente);
                pago.setFoto_cobro("");
                pago.setMetodo("RegistrarCobro");

                DataBaseHelper.saveDetalleFormaPago(pago, DepcApplication.getApplication().getDetalleFormaPagoDao());

                final Gson gson = new Gson();
                String json = gson.toJson(pago);
                Log.e("TAG---","json: "+json);

                String respuesta = ws.makeServiceCall(URL_ENVIO,0, json);
                jsonRespuesta = new JSONObject(respuesta);
                if (jsonRespuesta.getInt("status") == 200){
                    pago.setEstado(true);
                    DataBaseHelper.updateDetalleFormaPago(pago, DepcApplication.getApplication().getDetalleFormaPagoDao());

                } else{
                    mensaje = jsonRespuesta.getString("status_message");
                    isError = true;

                }
            } catch (JSONException e) {
                e.printStackTrace();
                isError = true;

            } catch (Exception e) {
                e.printStackTrace();
                mensaje = "No se pudo realizar la sincronizacion de cobros pendientes";
                isError = true;
            }

            return true;

        }

        protected void onProgressUpdate(Integer... values){
        }

        @Override
        protected void onPostExecute(Boolean result) {
            hideProgressWait();
            if (isError) {
                showAlert("" + mensaje);
            }else {
                imprimir(generatedZPL());
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    private String generatedZPL(){

        if (detalleFacturas == null){
            return "";
        }

        if (pago == null){
            return "";
        }

        zpl = "";
        try {

            String nombreEmpresa = "DEPCONSA";
            String rucEmpresa = "";


            if (Utils.getZebra(getBaseContext())) {

                /*zpl = "^XA^CFD^POI^LH0,0 ^LL600 ^FO10,20^ADN,20,20^FD" + nombreEmpresa +
                    "^FS^FO10,45^ADN,18,12^FDRUC  " + rucEmpresa +
                    "^FS^FO10,80^ADN,18,12^FDRecibo de cobro offline"+
                    "^FS^FO10,100^ADN,26,12^FDComprobante de Cancelacion # " + comprobanteNumero +
                    "^FS ^FO10,120^ADN,18,10^FDCliente : " + cliente.getNOMBRE()+" "+ cliente.getAPELLIDO()  +
                    "^FS^FO10,150^ADN,18,10^FDFecha : " + Utils.getFecha() +
                    "^FS^FO10,170^ADN,20,20^FD-----------------------------" +
                    "^FS^FO10,190^ADN,24,10^FDDoc. FA/Cuota      F.Fvto   Valor" +
                    "^FS^FO10,210^ADN,20,20^FD-----------------------------";
            int coorY = 250;
            if (dCDeuda != null){
                boolean isDebito = false;
                float interes = 0;
                for (DocumentoDeuda dc : dCDeuda){
                    //zpl += "^FS^FO10,"+coorY+"^ADN,18,10^FD"+dc.getTipoDocumento()+"  "+dc.getNumeroCuota()+"    "+dc.getFechaVencimiento()+"  "+dc.getMontoAbonado()+"";
                    if (dc.getTipoDocumento().equals("N/D")){
                        isDebito = true;
                        if (Utils.isNumberDecimal(""+dc.getMontoAbonado())){
                            float decimal = Float.parseFloat(""+dc.getMontoAbonado());
                            interes = interes + decimal;
                        }
                    }else {
                        zpl += "^FS^FO10," + coorY + "^ADN,18,10^FD" + dc.getTipoDocumento() + "  " + dc.getReferencia() + "  " + dc.getNumeroCuota() + "/" + dc.getNumeroPago() + "    " + dc.getFechaVencimiento() + "  " + dc.getMontoAbonado() + "";
                        coorY += 20;
                    }
                }

                if (isDebito){
                    zpl += "^FS^FO10," + coorY + "^ADN,18,10^FD" + "Interes "+interes;
                    coorY += 20;
                }
            }

            coorY += 20;
            zpl += "^FS^FO10,"+coorY+"^ADN,20,20^FD-------------------------";
            coorY += 20;
            //zpl += "^FS^FO10,"+coorY+"^ADN,18,10^FDTotal Pagado : $ "+ingreso;
            zpl += "^FS^FO10,"+coorY+"^ADN,18,10^FDSaldo Anterior :  $ "+ String.format("%.2f",totalPagarDEbitar );

            coorY += 20;
            if (formasPago != null){
                for (DetallePago fp : formasPago){
                    fp.setMonto(fp.getMonto().replace(",","."));
                    zpl += "^FS^FO10,"+coorY+"^ADN,18,10^FD"+fp.getDescripcionFPago()+"     : $ "+ String.format("%.2f", Float.parseFloat(fp.getMonto()));
                    coorY += 20;
                }
            }

            coorY += 20;
            double sal = totalPagarDEbitar - ingreso;
            zpl += "^FS^FO10,"+coorY+"^ADN,18,10^FDSaldo Actual : $ " + String.format("%.2f", sal);
            coorY += 40;
            zpl +=  "^FS^FO10,"+coorY+"^ADN,18,10^FD___________________";
            coorY += 20;
            zpl +=  "^FS ^FO10,"+coorY+"^ADN,18,10^FDCliente^FS ^XZ";

                Log.e("TAG---", "zpl--- " + zpl);
                return zpl;*/
                return "";

            }else {

                String offline = "Recibo de cobro";

                zpl = nombreEmpresa + "\n" +
                        "RUC  " + rucEmpresa + "\n" +
                        "CEL  0999999999 \n" +
                        offline + "\n" +
                        "Comprobante de " + "\n" +
                        "Cancelacion # " + Calendar.getInstance().getTimeInMillis() + "\n" +
                        "Cliente : " + nombreCliente + "\n" +
                        "Fecha : " + Utils.getFecha() + "\n" +
                        "-----------------------------" + "\n" +
                        "Nro. Doc. F.Pago    Valor" + "\n" +
                        "-----------------------------" + "\n";

                try {
                    zpl += "" +truncate( pago.getFactura_id(), 8)+ "  " + truncate(pago.getNombre_corto_forma_de_pago(),9) + truncate( " $ "+ pago.getValor(),10) + "\n";
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }



                zpl += "-------------------------" + "\n";
                // zpl += "Saldo Actual : $ " + String.format("%.2f", sal) + "\n";
                //zpl += "___________________" + "\n";
                zpl += "Cliente \n";
                Log.e("TAG---", "zpl--- " + zpl);
                return zpl;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        return "";
    }


    public static String truncate(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        } else {
            int numChar = str.length();
            for (int x = 0;x  < (len - numChar); x++ ) {
                str += " ";
            }
            return str;
        }}





    private void imprimir(String texto){

        //zpl = dataPago.getResultado();
        //getCobros();
        zpl = texto;
        //Log.e("TAG---",zpl);

        new AlertDialog.Builder(CobrosActivity.this)
                .setTitle("ATENCIÓN")
                .setMessage("Pago realizado con éxito ¿Desea imprimir?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (validateStatus()) {

                            try {

                                dialog.dismiss();
                                //sendPrint();
                                if (Utils.getZebra(getBaseContext())){
                                    if (selectPrinter >= 0) {
                                        sendPrint();
                                    }else{
                                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                        mBluetoothAdapter.startDiscovery();

                                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                                        registerReceiver(mReceiver, filter);

                                        progress = ProgressDialog.show(CobrosActivity.this, "",
                                                "Buscando Impresora...", true);
                                        progress.setCancelable(true);
                                        progress.setCanceledOnTouchOutside(false);
                                        progress.setOnCancelListener(new DialogInterface.OnCancelListener(){
                                            @Override
                                            public void onCancel(DialogInterface dialog){
                                                if (mReceiver != null) {
                                                    unregisterReceiver(mReceiver);
                                                }
                                            }});
                                    }
                                }else {
                                    connect();
                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }})
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        try {

                            dialog.dismiss();
                            //getCobros();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }}).show();








        return;

        //getCobros();


    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void sendPrint(){
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                connectAndGetData();
                Looper.loop();
                Looper.myLooper().quit();
            }
        }).start();

    }

    private void connectAndGetData() {

        connection = new BluetoothConnection(mDeviceList.get(selectPrinter).getAddress());

        try {
            helper.showLoadingDialog("Conectando...");
            connection.open();
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            ZebraPrinterLinkOs linkOsPrinter = ZebraPrinterFactory.createLinkOsPrinter(printer);
            PrinterStatus printerStatus = (linkOsPrinter != null) ? linkOsPrinter.getCurrentStatus() : printer.getCurrentStatus();
            getPrinterStatus();

            if (printerStatus.isReadyToPrint) {
                testSendFile(printer);
            } else if (printerStatus.isHeadOpen) {
                helper.showErrorDialogOnGuiThread("Head Open \nPlease Close Printer Head to Print.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //getCobros();
                    }
                });
            } else if (printerStatus.isPaused) {
                helper.showErrorDialogOnGuiThread("Printer Paused.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //getCobros();
                    }
                });
            } else if (printerStatus.isPaperOut) {
                helper.showErrorDialogOnGuiThread("Media Out \nPlease Load Media to Print.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       //getCobros();
                    }
                });
            }
            connection.close();
        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //getCobros();
                }
            });
        } catch (ZebraPrinterLanguageUnknownException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //getCobros();
                }
            });
        } finally {
            helper.dismissLoadingDialog();
        }
    }



    private void testSendFile(ZebraPrinter printer) {
        try {
            File filepath = getFileStreamPath("TEST.LBL");
            createDemoFile(printer, "TEST.LBL");
            printer.sendFileContents(filepath.getAbsolutePath());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   //getCobros();
                }
            });

        } catch (ConnectionException e1) {
            helper.showErrorDialogOnGuiThread("Error sending file to printer");
            //getCobros();
        } catch (IOException e) {
            helper.showErrorDialogOnGuiThread("Error creating file");
            //getCobros();
        }
    }

    private void createDemoFile(ZebraPrinter printer, String fileName) throws IOException {

        FileOutputStream os = this.openFileOutput(fileName, Context.MODE_PRIVATE);

        byte[] configLabel = null;

        PrinterLanguage pl = printer.getPrinterControlLanguage();
        if (pl == PrinterLanguage.ZPL) {
            configLabel = zpl.getBytes();
        } else if (pl == PrinterLanguage.CPCL) {
            //String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
            //configLabel = cpclConfigLabel.getBytes();
        }
        os.write(configLabel);
        os.flush();
        os.close();
    }


    private void getPrinterStatus() throws ConnectionException {

        final String printerLanguage = SGD.GET("device.languages", connection);

        final String displayPrinterLanguage = "Printer Language is " + printerLanguage;

        SGD.SET("device.languages", "hybrid_xml_zpl", connection);

        CobrosActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(CobrosActivity.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

    }



    @Override
    public void doRetry() {

    }
}
