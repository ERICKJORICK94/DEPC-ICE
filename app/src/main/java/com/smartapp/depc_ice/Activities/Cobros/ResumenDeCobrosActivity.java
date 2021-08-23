package com.smartapp.depc_ice.Activities.Cobros;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Cobros.Adapter.ResumenFormaCobrosAdapter;
import com.smartapp.depc_ice.Activities.Despachos.DespachosActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.DetalleFormaPago;
import com.smartapp.depc_ice.Entities.FormaPago;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IFormaPago;
import com.smartapp.depc_ice.Models.Device;
import com.smartapp.depc_ice.Models.EstadoGabinetModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.BTDeviceList;
import com.smartapp.depc_ice.Utils.Const;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumenDeCobrosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private Spinner spinner;
    private Call<IFormaPago.dataBodega> call;
    private IFormaPago.dataBodega data;
    private int indexFormaPago = 0;
    private List<FormaPago> formaPagos;
    private ListView lista;

    private TextView impresora;
    private Connection connection = null;
    private UIHelper helper = new UIHelper(this);
    private int selectPrinter = -1;
    private String zpl = "";
    private ArrayList<Device> mDeviceList = new ArrayList<Device>();
    private BluetoothAdapter mBluetoothAdapter;
    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    int positionSelectFactura = - 1;
    private Date dateChosee;
    private String fechaBuscar;
    private TextView fechaSeleccionada,total;
    private List<DetalleFormaPago> pagos = null;
    private androidx.appcompat.app.AlertDialog dialog;
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

        layout = addLayout(R.layout.resumen_de_cobros_layout);
        Utils.SetStyleActionBarTitle(this);

        spinner = (Spinner) layout.findViewById(R.id.spinner);
        lista = (ListView) layout.findViewById(R.id.lista);
        impresora = (TextView) layout.findViewById(R.id.impresora);
        fechaSeleccionada = (TextView) layout.findViewById(R.id.fechaSeleccionada);
        total = (TextView) layout.findViewById(R.id.total);

        impresora.setVisibility(View.GONE);
        total.setVisibility(View.GONE);
        fechaBuscar = Utils.getFecha();
        fechaSeleccionada.setText(""+fechaBuscar);

        if (Utils.getDevices(ResumenDeCobrosActivity.this) != null){
            if (Utils.getDevices(ResumenDeCobrosActivity.this).size() > 0){
                mDeviceList = Utils.getDevices(ResumenDeCobrosActivity.this);
                selectPrinter = 0;
                //buscar_impresora.setText(""+mDeviceList.get(0).getName()+" \n "+mDeviceList.get(0).getAddress());
                impresora.setVisibility(View.VISIBLE);
                impresora.setText("IMPRESORA: "+mDeviceList.get(0).getName()+"  "+mDeviceList.get(0).getAddress());
            }
        }


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

                                                showListZonas();

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

                            showListZonas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showListZonas();
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
                    showListZonas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showListZonas();

        }
    }

    private void showListZonas(){

        try {
            formaPagos = DataBaseHelper.getFormaPago(DepcApplication.getApplication().getFormaPagoDao());

            if (formaPagos != null){
                if (formaPagos.size() > 0){

                    List<String> items= new ArrayList<String>();
                    items.add("TODOS");
                    for (FormaPago z : formaPagos){
                        items.add(z.getDescripcion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                indexFormaPago = position;
                                showformas();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Utils.getSinImpresora(ResumenDeCobrosActivity.this)){
            getMenuInflater().inflate(R.menu.resumen, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_print:{

                if (pagos == null){
                    showAlert("NO DISPONE DE DATOS A IMPRIMIR");
                    return true;
                }
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

                generatedZPL();
                if (validateStatus()){

                    if (Utils.getZebra(getBaseContext())) {

                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        mBluetoothAdapter.startDiscovery();

                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                        registerReceiver(mReceiver, filter);

                        progress = ProgressDialog.show(ResumenDeCobrosActivity.this, "",
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
            case R.id.action_calendar: {
                showCalendar();
                return true;
            }

                //return true;
        }

        finish();

        return true;
    }

    private void showCalendar(){

        if (dateChosee == null) {
            dateChosee = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateChosee);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Locale locale = new Locale ( "es" , "ES" );
        Locale.setDefault(locale);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ResumenDeCobrosActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
                try {
                    dateChosee = format.parse(dayOfMonth+"/"+(monthOfYear + 1)+"/"+year);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateChosee);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaBuscar = sdf.format(cal.getTime());
                    fechaSeleccionada.setText(""+fechaBuscar);
                    showformas();

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }, year, month, day);

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        // datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "CANCELAR", datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePickerDialog);
        datePickerDialog.show();

    }


    private void showformas(){

        lista.setAdapter(null);
        total.setVisibility(View.GONE);
        if (formaPagos != null){
            try {
                 pagos = null;
                if (indexFormaPago == 0){
                    pagos = DataBaseHelper.getDetalleFormaPagoByFacturaFilterAll(DepcApplication.getApplication().getDetalleFormaPagoDao(), fechaBuscar);
                }else {
                    pagos = DataBaseHelper.getDetalleFormaPagoByFacturaFilter(DepcApplication.getApplication().getDetalleFormaPagoDao(), formaPagos.get(indexFormaPago - 1).getId_forma_pago(), fechaBuscar);
                }


                if (pagos != null){
                    if (pagos.size() > 0){
                        lista.setAdapter(new ResumenFormaCobrosAdapter(ResumenDeCobrosActivity.this,pagos));

                        float acum = 0;
                        for (DetalleFormaPago df : pagos){
                            if (df.getValor() != null){
                                float valor = Float.parseFloat(df.getValor());
                                acum = acum + valor;
                            }
                        }

                        total.setVisibility(View.VISIBLE);
                        total.setText(String.format("TOTAL: $ %.2f",acum));
                    }

                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    private boolean validateStatus(){

        hideProgressWait();
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(ResumenDeCobrosActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // ask permissions here using below code
                ActivityCompat.requestPermissions(ResumenDeCobrosActivity.this,
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
                Utils.saveDevices(auxDevice, ResumenDeCobrosActivity.this);

                androidx.appcompat.app.AlertDialog.Builder builderInner = new androidx.appcompat.app.AlertDialog.Builder(ResumenDeCobrosActivity.this);
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

    private String generatedZPL(){

        if (formaPagos == null){
            return "";
        }

        if (pagos == null){
            return "";
        }

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
                float acum = 0;
                String recaudadorString = "";
                Usuario user;
                try {
                    List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
                    if (usuarios != null){
                        if (usuarios.size() > 0){
                            user = usuarios.get(0);
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


                String offline = "Resumen de cobros";


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
                        "FECHA: " + Utils.getFechaHora()+ "\n" +
                        "RECAUDADOR: " + recaudadorString + "\n\n" +

                        "------------------------------" + "\n" +
                        "# DOC.    F.PAGO    VALOR" + "\n" +
                        "------------------------------" + "\n";


                try {

                    for (DetalleFormaPago df : pagos) {
                        zpl += "" + truncate(df.getFct_det_id(), 8) + "  " + truncate(df.getNombre_corto_forma_de_pago(), 9) + truncate(" $ " + df.getValor(), 10) + "\n";
                        if (df.getValor() != null){
                            float valor = Float.parseFloat(df.getValor());
                            acum = acum + valor;
                        }
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }



                zpl +=  "------------------------------" + "\n";
                zpl += "TOTAL: " +String.format("$ %.2f",acum)+ "\n";
                 zpl +=  "" + "\n\n";
                Log.e("TAG---", "zpl--- " + zpl);

                zpl =  removeAccents(zpl);
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

        new AlertDialog.Builder(ResumenDeCobrosActivity.this)
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

                                        progress = ProgressDialog.show(ResumenDeCobrosActivity.this, "",
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

        ResumenDeCobrosActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(CobrosActivity.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });

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
