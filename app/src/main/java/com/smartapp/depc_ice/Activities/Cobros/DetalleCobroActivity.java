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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akexorcist.googledirection.model.Line;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.stmt.QueryBuilder;
import com.smartapp.depc_ice.Activities.Cobros.Adapter.FormaCobrosAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetalleFacturas;
import com.smartapp.depc_ice.Entities.DetalleFormaPago;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Models.Device;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleCobroActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private Clientes cliente;
    private FloatingActionButton fab;
    private Button enviar;
    private TextView total_facturas;
    private EditText recaudador;
    private String idPago = "";
    private EditText edt_referencia;
    private EditText edt_nro_ingreso;
    private TextView impresora;
    private String motivo = "";
    private double valorTotal = 0;
    private NonScrollListView listview;

    private androidx.appcompat.app.AlertDialog dialog;
    private Connection connection = null;
    private UIHelper helper = new UIHelper(this);
    private int selectPrinter = -1;
    private String zpl = "";
    private ArrayList<Device> mDeviceList = new ArrayList<Device>();
    private BluetoothAdapter mBluetoothAdapter;
    private double ingreso = 0;
    private double totalPagar = 0;
    private double totalPagarDEbitar = 0;
    private DetalleFacturas detalleFactura;
    private String id_vaje = "";
    private  Usuario user;
    private String cuenta_id = "";
    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    private String URL_ENVIO = "https://webserver.depconsa.com/DepWSR/application/libraries/wsapp.php";
    private String nombreCliente = "";
    private String ruc_cliente = "";
    private String direccion_cliente = "";
    private String telefono_cliente = "";
    private LinearLayout linear_recaudador;

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

        layout = addLayout(R.layout.detalle_cobro_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        impresora = (TextView) layout.findViewById(R.id.impresora);
        fab = (FloatingActionButton) layout.findViewById(R.id.fab);
        enviar = (Button) layout.findViewById(R.id.enviar);
        total_facturas = (TextView) layout.findViewById(R.id.total_facturas);
        recaudador = (EditText) layout.findViewById(R.id.recaudador);
        edt_referencia = (EditText) layout.findViewById(R.id.edt_referencia);
        edt_nro_ingreso = (EditText) layout.findViewById(R.id.edt_nro_ingreso);
        listview = (NonScrollListView) layout.findViewById(R.id.listview);
        linear_recaudador = (LinearLayout) layout.findViewById(R.id.linear_recaudador);

        if (getIntent() != null){
            detalleFactura = (DetalleFacturas) getIntent().getSerializableExtra("detalle_factura");
            id_vaje = getIntent().getStringExtra("id_vaje");
            cuenta_id = getIntent().getStringExtra("cuenta_id");
            nombreCliente = getIntent().getStringExtra("nombreCliente");
            ruc_cliente = getIntent().getStringExtra("ruc_cliente");
            direccion_cliente = getIntent().getStringExtra("direccion_cliente");
            telefono_cliente = getIntent().getStringExtra("telefono_cliente");

            if (detalleFactura != null){
                if (detalleFactura.getSaldo() != null){
                    total_facturas.setText("$ "+detalleFactura.getSaldo());
                }
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
        

        impresora.setVisibility(View.GONE);

        if (Utils.getDevices(DetalleCobroActivity.this) != null){
            if (Utils.getDevices(DetalleCobroActivity.this).size() > 0){
                mDeviceList = Utils.getDevices(DetalleCobroActivity.this);
                selectPrinter = 0;
                //buscar_impresora.setText(""+mDeviceList.get(0).getName()+" \n "+mDeviceList.get(0).getAddress());
                impresora.setVisibility(View.VISIBLE);
                impresora.setText("IMPRESORA: "+mDeviceList.get(0).getName()+"  "+mDeviceList.get(0).getAddress());
            }
        }

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

            linear_recaudador.setVisibility(View.GONE);
            if (recaudadorString.trim().length() > 0) {
                recaudador.setText(""+recaudadorString);
                linear_recaudador.setVisibility(View.VISIBLE);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    List<DetalleFormaPago> detalleFormaPagos = DataBaseHelper.getDetalleFormaPagoByFactura(DepcApplication.getApplication().getDetalleFormaPagoDao(), ""+detalleFactura.getFct_det_id());

                    if (detalleFormaPagos != null){
                        if (detalleFormaPagos.size() > 0){

                            EnviarFormasPago pt = new EnviarFormasPago();
                            pt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                           return;
                        }
                    }

                    showAlert("Ingrese al menos una forma de pago antes de continuar");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleCobroActivity.this, FormaPagoActivity.class);
                intent.putExtra("detalle_factura",detalleFactura);
                intent.putExtra("id_vaje",id_vaje);
                intent.putExtra("cuenta_id",cuenta_id);
                intent.putExtra("nombreCliente",nombreCliente);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Utils.getSinImpresora(DetalleCobroActivity.this)){
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

                        progress = ProgressDialog.show(DetalleCobroActivity.this, "",
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
            int permissionCheck = ContextCompat.checkSelfPermission(DetalleCobroActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // ask permissions here using below code
                ActivityCompat.requestPermissions(DetalleCobroActivity.this,
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
                Utils.saveDevices(auxDevice, DetalleCobroActivity.this);

                androidx.appcompat.app.AlertDialog.Builder builderInner = new androidx.appcompat.app.AlertDialog.Builder(DetalleCobroActivity.this);
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

    protected void connect() {
        enviar.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        edt_referencia.setEnabled(false);
        edt_nro_ingreso.setEnabled(false);

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
            finish();

        } catch (IOException e) {
            e.printStackTrace();
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String generatedZPL(){

       zpl = "";
        try {

            String nombreEmpresa = "";
            String rucEmpresa = "";
            String sucursal = "";
            String paisSucursal = "";
            String ciudadSucrusal = "";
            String matriz = "";
            String cuidadMatriz = "";
            String paisMatriz = "";
            String telefonoMatriz = "";
            String telefonosucursal = "";
            double pagar = 0;

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

                String recaudadorString = "";
                try {
                    List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
                    if (usuarios != null){
                        if (usuarios.size() > 0){
                            Usuario user = usuarios.get(0);
                            if (user.getNombrescompletos() != null){
                                recaudadorString = ""+user.getNombrescompletos();
                            }

                            if (user.getN_comercial() != null){
                                nombreEmpresa = user.getN_comercial();
                            }

                            if (user.getRuc() != null){
                                rucEmpresa = user.getRuc();
                            }

                            if (user.getDireccion_sucursal() != null){
                                sucursal = user.getDireccion_sucursal();
                            }
                            if (user.getCiudad_sucursal() != null){
                                ciudadSucrusal = user.getCiudad_sucursal();
                            }

                            if (user.getProvincia_sucursal() != null){
                                paisSucursal = user.getProvincia_sucursal();
                            }

                            if (user.getDireccion_matriz() != null){
                                matriz = user.getDireccion_matriz();
                            }

                            if (user.getCiudad_matriz() != null){
                                cuidadMatriz = user.getCiudad_matriz();
                            }

                            if (user.getProvincia_matriz() != null){
                                paisMatriz = user.getProvincia_matriz();
                            }

                            if (user.getTelefono_matriz() != null){
                                telefonoMatriz = user.getTelefono_matriz();
                            }
                            if (user.getTelefono_sucursal() != null){
                                telefonosucursal = user.getTelefono_sucursal();
                            }
                        }
                    }


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String offline = "Recibo de cobro";
                zpl =   nombreEmpresa + "\n" +
                        "RUC: " + rucEmpresa + "\n" +
                        "TELEFONO: " + telefonoMatriz + "\n" +
                        "DIRECCION: " + matriz + "\n" +
                        ""+cuidadMatriz+" - "+paisMatriz+"\n" +
                        "------------------------------\n" +
                        "SUCURSAL: " + sucursal + "\n" +
                        "TELEFONO: " + telefonosucursal + "\n" +
                        ""+ciudadSucrusal+" - "+paisSucursal+"\n" +
                        "------------------------------\n" +
                        "CLIENTE: " + nombreCliente + "\n" +
                        "CED/RUC: " + ruc_cliente + "\n" +
                        "DIRECCION: " + direccion_cliente + "\n" +
                        "TELEFONO: " + telefono_cliente + "\n" +
                        "FECHA: " + Utils.getFechaHora()+ "\n" +
                        "# FACTURA: "+detalleFactura.getFactura_id()+"\n" +
                        "RECAUDADOR: " + recaudadorString + "\n\n" +

                        "------------------------------" + "\n" +
                        "# DOC.    F.PAGO    VALOR" + "\n" +
                        "------------------------------" + "\n";

                float pagara = 0;
                try {
                    List<DetalleFormaPago> detalleFormaPagos = DataBaseHelper.getDetalleFormaPagoByFactura(DepcApplication.getApplication().getDetalleFormaPagoDao(), ""+detalleFactura.getFct_det_id());

                    if (detalleFormaPagos != null){
                        if (detalleFormaPagos.size() > 0){

                            for (DetalleFormaPago df : detalleFormaPagos){
                                pagara += Float.parseFloat(df.getValor());
                                zpl += "" +truncate( df.getFct_det_id(), 8)+ "  " + truncate(df.getNombre_corto_forma_de_pago(),9) + truncate( " $ "+ df.getValor(),10) + "\n";
                            }
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                zpl +=  "" + "\n";
                zpl +=  "------------------------------" + "\n" +
                        "TOTAL: $ "+ String.format("%.2f", pagara) + "\n" +
                        "------------------------------" + "\n";
                zpl +=  "" + "\n\n\n";
                zpl +=  "------------------------------" + "\n";
                zpl += "CLIENTE \n";
                zpl +=  "" + "\n\n";
                zpl =  removeAccents(zpl);
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

        zpl = texto;
        //Log.e("TAG---",zpl);

        enviar.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
        edt_referencia.setEnabled(false);
        edt_nro_ingreso.setEnabled(false);
        //listview.setAdapter(new FormaCobrosAdapter(this, idPago, false));

        new AlertDialog.Builder(DetalleCobroActivity.this)
                .setTitle("ATENCIÓN")
                .setMessage("Pago realizado con éxito ¿Desea imprimir?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (validateStatus()) {
                            try {

                                dialog.dismiss();
                                // sendPrint();

                                if (Utils.getZebra(getBaseContext())){
                                    if (selectPrinter >= 0) {
                                        sendPrint();
                                    }else{
                                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                        mBluetoothAdapter.startDiscovery();

                                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                                        registerReceiver(mReceiver, filter);

                                        //mDeviceList.clear();
                                        //selectPrinter = -1;
                                        //selectFileIndex = -1;
                                        //zpl = "";


                                        progress = ProgressDialog.show(DetalleCobroActivity.this, "",
                                                "Buscando Impresora...", true);
                                        progress.setCancelable(true);
                                        progress.setCanceledOnTouchOutside(false);
                                        progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                if (mReceiver != null) {
                                                    unregisterReceiver(mReceiver);
                                                }
                                            }
                                        });
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
                            finish();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }}).show();








        return;


    }

    private void sendPrint(){

        enviar.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
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
            } else if (printerStatus.isPaused) {
                helper.showErrorDialogOnGuiThread("Printer Paused.");
            } else if (printerStatus.isPaperOut) {
                helper.showErrorDialogOnGuiThread("Media Out \nPlease Load Media to Print.");
            }
            connection.close();
            saveSettings();
        } catch (ConnectionException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } catch (ZebraPrinterLanguageUnknownException e) {
            helper.showErrorDialogOnGuiThread(e.getMessage());
        } finally {
            helper.dismissLoadingDialog();
            finish();
        }
    }


    private void testSendFile(ZebraPrinter printer) {
        try {
            File filepath = getFileStreamPath("TEST.LBL");
            createDemoFile(printer, "TEST.LBL");
            //printer.getConnection().sendAndWaitForResponse();
            printer.sendFileContents(filepath.getAbsolutePath());

        } catch (ConnectionException e1) {
            helper.showErrorDialogOnGuiThread("Error sending file to printer");
        } catch (IOException e) {
            helper.showErrorDialogOnGuiThread("Error creating file");
        }
    }

    /**
     * This method includes the creation of test file in ZPL and CPCL formats.
     * @param printer
     * @param fileName
     * @throws IOException
     */

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

    private void saveSettings() {
        //SettingsHelper.saveBluetoothAddress(InicialActivity.this, getMacAddressFieldText());
    }


    private void getPrinterStatus() throws ConnectionException {

        final String printerLanguage = SGD.GET("device.languages", connection);

        final String displayPrinterLanguage = "Printer Language is " + printerLanguage;

        SGD.SET("device.languages", "hybrid_xml_zpl", connection);

        DetalleCobroActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(DetalleCobroActivity.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void showFormaPago(){


        if (detalleFactura != null){

            fab.setVisibility(View.VISIBLE);
            enviar.setVisibility(View.VISIBLE);
            try {
                List<DetalleFormaPago> detalleFormaPagos = DataBaseHelper.getDetalleFormaPagoByFactura(DepcApplication.getApplication().getDetalleFormaPagoDao(), ""+detalleFactura.getFct_det_id());

                if (detalleFormaPagos != null){
                    if (detalleFormaPagos.size() > 0){

                        Boolean flag = false;
                        for (DetalleFormaPago df : detalleFormaPagos){
                            if (!df.getEstado()){
                                flag = true;
                                break;
                            }
                        }

                        if (!flag){
                            fab.setVisibility(View.GONE);
                            enviar.setVisibility(View.GONE);
                        }

                        listview.setAdapter(new FormaCobrosAdapter(this,flag, detalleFormaPagos, ""+detalleFactura.getFactura_id()));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

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


            List<DetalleFormaPago> pagos;

            try {
                pagos = DataBaseHelper.getDetalleFormaPagoByFactura(DepcApplication.getApplication().getDetalleFormaPagoDao(), ""+detalleFactura.getFct_det_id());
            } catch (SQLException e) {
                pagos = null;
            }

            if (pagos.size() != 0){
                for (DetalleFormaPago pago : pagos){

                    try {

                        JSONObject jsonRespuesta = null;
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

                    } catch (SQLException e) {
                        e.printStackTrace();
                        mensaje = "No se pudo realizar la sincronizacion de cobros pendientes";
                        isError = true;
                    }

                }
            }
            else {
                mensaje = "No existen cobros pendientes por sincronizar";
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
            showFormaPago();
        }

        @Override
        protected void onCancelled() {

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        showFormaPago();
    }


    @Override
    public void doRetry() {

    }

    private Map<Character, Character> MAP_NORM;
    public String removeAccents(String value)
    {
        if (MAP_NORM == null || MAP_NORM.size() == 0)
        {
            MAP_NORM = new HashMap<Character, Character>();
            MAP_NORM.put('À', 'A');
            MAP_NORM.put('Á', 'A');
            MAP_NORM.put('Â', 'A');
            MAP_NORM.put('Ã', 'A');
            MAP_NORM.put('Ä', 'A');
            MAP_NORM.put('È', 'E');
            MAP_NORM.put('É', 'E');
            MAP_NORM.put('Ê', 'E');
            MAP_NORM.put('Ë', 'E');
            MAP_NORM.put('Í', 'I');
            MAP_NORM.put('Ì', 'I');
            MAP_NORM.put('Î', 'I');
            MAP_NORM.put('Ï', 'I');
            MAP_NORM.put('Ù', 'U');
            MAP_NORM.put('Ú', 'U');
            MAP_NORM.put('Û', 'U');
            MAP_NORM.put('Ü', 'U');
            MAP_NORM.put('Ò', 'O');
            MAP_NORM.put('Ó', 'O');
            MAP_NORM.put('Ô', 'O');
            MAP_NORM.put('Õ', 'O');
            MAP_NORM.put('Ö', 'O');
            MAP_NORM.put('Ñ', 'N');
            MAP_NORM.put('Ç', 'C');
            MAP_NORM.put('ª', 'A');
            MAP_NORM.put('º', 'O');
            MAP_NORM.put('§', 'S');
            MAP_NORM.put('³', '3');
            MAP_NORM.put('²', '2');
            MAP_NORM.put('¹', '1');
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('ä', 'a');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ë', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('î', 'i');
            MAP_NORM.put('ï', 'i');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('û', 'u');
            MAP_NORM.put('ü', 'u');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ö', 'o');
            MAP_NORM.put('ñ', 'n');
            MAP_NORM.put('ç', 'c');
        }

        if (value == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(value);

        for(int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if(c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }

        return sb.toString();

    }
}
