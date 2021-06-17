package com.smartapp.depc_ice.Activities.Despachos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Cobros.CobrosActivity;
import com.smartapp.depc_ice.Activities.Despachos.Adapter.ListaDespachoAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Pedido.DetallePedidoActivity;
import com.smartapp.depc_ice.Activities.Productos.Detalle.DetalleProductoActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.EstadoFacturasDespacho;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IEstadoFacturaDespacho;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.Device;
import com.smartapp.depc_ice.Models.EstadoFacturaModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.BTDeviceList;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.NonScrollListView;
import com.smartapp.depc_ice.Utils.PhotoViewer;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleDespachosPlanificacionActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private String fecha;
    private Button registrar,cobrar;
    private NonScrollListView lista;
    private Spinner spinner_despacho;
    private Clientes cliente = null;
    private int indexDespachos = 0;
    String motivosDeapachos[] = {"ENTREGA TOTAL","ENTREGA PARCIAL","RECHAZADO","NO ACEPTACIÓN","CERRADO"};

    private Connection connection = null;
    private UIHelper helper = new UIHelper(this);
    private int selectPrinter = -1;
    private String zpl = "";
    private TextView impresora;
    private ArrayList<Device> mDeviceList = new ArrayList<Device>();
    private BluetoothAdapter mBluetoothAdapter;
    private androidx.appcompat.app.AlertDialog dialog;

    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    private TextView ver_mapa;
    private TextView llamar;
    private TextView btn_whatsapp;
    private TextView agregar,ver_foto;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private Call<IEstadoFacturaDespacho.dataBodega> call;
    private IEstadoFacturaDespacho.dataBodega data;

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

        layout = addLayout(R.layout.despacho_plainificacion_layout);
        spinner_despacho = (Spinner) layout.findViewById(R.id.spinner_despacho);
        impresora = (TextView) layout.findViewById(R.id.impresora);

        ver_mapa = (TextView) layout.findViewById(R.id.ver_mapa);
        llamar = (TextView) layout.findViewById(R.id.llamar);
        btn_whatsapp = (TextView) layout.findViewById(R.id.btn_whatsapp);
        cobrar = (Button) layout.findViewById(R.id.cobrar);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);

        Utils.SetStyleActionBarTitle(this);

        cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, CobrosActivity.class);
                startActivity(intent);
            }
        });


        //Demo
        try {
            List<Clientes> clientes = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), "201");
            if (clientes !=  null){
                if (clientes.size() > 0){
                    cliente = clientes.get(0);
                    DepcApplication.getApplication().setCliente(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //

        impresora.setVisibility(View.GONE);

        if (Utils.getDevices(DetalleDespachosPlanificacionActivity.this) != null){
            if (Utils.getDevices(DetalleDespachosPlanificacionActivity.this).size() > 0){
                mDeviceList = Utils.getDevices(DetalleDespachosPlanificacionActivity.this);
                selectPrinter = 0;
                //buscar_impresora.setText(""+mDeviceList.get(0).getName()+" \n "+mDeviceList.get(0).getAddress());
                impresora.setVisibility(View.VISIBLE);
                impresora.setText("IMPRESORA: "+mDeviceList.get(0).getName()+"  "+mDeviceList.get(0).getAddress());
            }
        }


        if (getIntent() != null) {
            if (getIntent().getStringExtra("fecha") != null) {
                fecha = getIntent().getStringExtra("fecha");
            }
        }


        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente.getTelefono1() != null){
                    if (cliente.getTelefono1().length() > 0) {

                        int permissionCheck = ContextCompat.checkSelfPermission(DetalleDespachosPlanificacionActivity.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    DetalleDespachosPlanificacionActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    3621);

                            return;
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                            startActivity(intent);
                            return;
                        }
                    }
                }

                showAlert("Lo sentimos no existe número al cual contactar");
            }
        });

        ver_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Direcciones direccion = new Direcciones();
                direccion.setLatitud("-2.128685");
                direccion.setLongitud("-79.89429666666666");
                direccion.setDireccion_envio("SAUCES 4 TIENDA ANGEL");
                Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, MapsActivity.class);
                intent.putExtra(Const.DETALLE_CLIENTE, direccion);
                startActivity(intent);

            }
        });

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente.getTelefono1() != null){
                    if (cliente.getTelefono1().length() > 0) {

                        try {
                            getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + cliente.getTelefono1()));
                            startActivity(intent);
                        } catch (PackageManager.NameNotFoundException e) {
                            Toast.makeText(DetalleDespachosPlanificacionActivity.this, "WhatsApp no instalado", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }
                }

                showAlert("Lo sentimos no existe número al cual contactar");
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {

                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2296);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2296);
                        }

                    }else{

                        selectPick();

                    }

                } else {

                    selectPick();

                }

            }
        });

        ver_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null){
                    if (Utils.convertBase64String(bitmap) != null) {
                        Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" +Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                }else{
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });


        registrar = (Button) layout.findViewById(R.id.registrar);
        lista = (NonScrollListView) layout.findViewById(R.id.lista);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog MyDialog = new Dialog(DetalleDespachosPlanificacionActivity.this);
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.firma_layout);
                MyDialog.setCancelable(true);
                Button limpiar = (Button) MyDialog.findViewById(R.id.limpiar);
                Button aceptar = (Button) MyDialog.findViewById(R.id.aceptar);
                SignaturePad signature_pad = (SignaturePad) MyDialog.findViewById(R.id.signature_pad);

                limpiar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signature_pad.clear();
                    }
                });

                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.dismiss();
                    }
                });


                MyDialog.show();

            }
        });

        showMotivosDespachos();
        getEstadoFacturaDespacho();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                selectPick();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

            return;
        }

        switch (requestCode) {

            case 3621:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                    startActivity(intent);
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }


    private void selectPick(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Cámara", "Galería"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this);
                builder.setTitle("Subir Foto");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Cámara")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Galería")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                //Toast.makeText(GridFotosActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                }
        } catch (Exception e) {
            Toast.makeText(DetalleDespachosPlanificacionActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Utils.getSinImpresora(DetalleDespachosPlanificacionActivity.this)){
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

                        progress = ProgressDialog.show(DetalleDespachosPlanificacionActivity.this, "",
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
            int permissionCheck = ContextCompat.checkSelfPermission(DetalleDespachosPlanificacionActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // ask permissions here using below code
                ActivityCompat.requestPermissions(DetalleDespachosPlanificacionActivity.this,
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

       if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    selectPick();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        } else if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                Log.e(TAG,"Base64: "+Utils.convertBase64String(bitmap));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                Log.e(TAG,"Base64: "+Utils.convertBase64String(bitmap));

            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

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
                Utils.saveDevices(auxDevice, DetalleDespachosPlanificacionActivity.this);

                androidx.appcompat.app.AlertDialog.Builder builderInner = new androidx.appcompat.app.AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this);
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

        zpl = "";

        /*try {

            String nombreEmpresa = "";
            String rucEmpresa = "";

            if (Utils.getZebra(getBaseContext())) {
             zpl = "^XA^CFD^POI^LH0,0 ^LL600 ^FO10,20^ADN,20,20^FD" + nombreEmpresa +
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
                return zpl;

            }else {

                    String offline = "Recibo de cobro offline";
                    if (comprobanteNumero.length() > 2){
                        offline = "";
                    }

                zpl = nombreEmpresa + "\n" +
                        "RUC  " + rucEmpresa + "\n" +
                        "CEL  0982539939 \n" +
                        offline + "\n" +
                        "Comprobante de " + "\n" +
                        "Cancelacion # " + comprobanteNumero + "\n" +
                        "Cliente : " + cliente.getNOMBRE() + " " + cliente.getAPELLIDO() + "\n" +
                        "Fecha : " + Utils.getFecha() + "\n" +
                        "-----------------------------" + "\n" +
                        "Doc. FA/Cuota     F.Fvto   Valor" + "\n" +
                        "-----------------------------" + "\n";


                int coorY = 250;
                if (dCDeuda != null) {
                    boolean isDebito = false;
                    float interes = 0;
                    for (DocumentoDeuda dc : dCDeuda) {
                        if (dc.getTipoDocumento().equals("N/D")){
                            isDebito = true;
                            if (Utils.isNumberDecimal(""+dc.getMontoAbonado())){
                                float decimal = Float.parseFloat(""+dc.getMontoAbonado());
                                interes = interes + decimal;
                            }
                        }else {

                            String cuotas = "";
                            if (dc.getNumeroCuota() != null){
                                if (!dc.getNumeroCuota().equals("0")){
                                    cuotas = ""+dc.getNumeroCuota() + "/" + dc.getNumeroPago();
                                }
                            }

                            zpl += "" + dc.getTipoDocumento() + "  " + dc.getReferencia() + "  " + cuotas + "    " + dc.getFechaVencimiento() + "  " + dc.getMontoAbonado() + "\n";
                            coorY += 20;
                        }
                    }

                    if (isDebito){
                        zpl += "Interes "+interes + "\n";
                        coorY += 20;
                    }
                }

                coorY += 20;
                zpl += "-------------------------" + "\n";
                coorY += 20;
                //zpl += "Total Pagado : $ " + ingreso + "\n";
                zpl += "Saldo Anterior : $ " + String.format("%.2f",totalPagarDEbitar ) + "\n";

                coorY += 20;
                if (formasPago != null) {
                    for (DetallePago fp : formasPago) {
                        fp.setMonto(fp.getMonto().replace(",","."));
                        zpl += "" + fp.getDescripcionFPago() + "     : $ " + String.format("%.2f", Float.parseFloat(fp.getMonto())) + "\n";
                        coorY += 20;
                    }
                }

                coorY += 20;
                double sal = totalPagarDEbitar - ingreso;
                zpl += "Saldo Actual : $ " + String.format("%.2f", sal) + "\n";
                coorY += 40;
                zpl += "___________________" + "\n";
                coorY += 20;
                zpl += "Cliente \n";

                Log.e("TAG---", "zpl--- " + zpl);
                return zpl;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }*/


        return "";
    }




    private void imprimir(String texto){

        //zpl = dataPago.getResultado();
        //getCobros();
        zpl = texto;
        Log.e("TAG---",zpl);

        if (validateStatus()) {

            new AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this)
                    .setTitle("ATENCIÓN")
                    .setMessage("¿Desea imprimir?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

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

                                        progress = ProgressDialog.show(DetalleDespachosPlanificacionActivity.this, "",
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

        }

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

        DetalleDespachosPlanificacionActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(DetalleDespachosPlanificacionActivity.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });
    }

        private void showMotivosDespachos(){

        indexDespachos = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, motivosDeapachos);
        spinner_despacho.setAdapter(adapter);
        spinner_despacho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexDespachos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fillData(){

        lista.setAdapter(new ListaDespachoAdapter(DetalleDespachosPlanificacionActivity.this));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editar();
            }
        });

        /*try {

            if (cliente != null){
                if(DataBaseHelper.getPedidosByCliente(DepcApplication.getApplication().getPedidosDao(),cliente.getCodigo_cliente_id()) != null){
                    final List<Pedidos> pedidos = DataBaseHelper.getPedidosByCliente(DepcApplication.getApplication().getPedidosDao(), cliente.getCodigo_cliente_id());
                    Collections.reverse(pedidos);
                    lista.setAdapter(new ListaPedidosAdapter(DetalleDespachosPlanificacionActivity.this, pedidos));

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, DetallePedidoActivity.class);
                            intent.putExtra(Const.ID_PEDIDOS,""+pedidos.get(position).getId());
                            startActivity(intent);


                        }
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    private void editar(){


        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(DetalleDespachosPlanificacionActivity.this);
        View comprarProduct = layoutInflater.inflate(R.layout.item_alert, null);
        final AlertDialog alert = new AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this).create();
        alert.setView(comprarProduct);

        ImageView back = (ImageView) comprarProduct.findViewById(R.id.back);
        ImageView info_product = (ImageView) comprarProduct.findViewById(R.id.info_product);
        TextView codigo = (TextView) comprarProduct.findViewById(R.id.codigo);
        TextView name = (TextView) comprarProduct.findViewById(R.id.name);
        TextView grupo = (TextView) comprarProduct.findViewById(R.id.grupo);
        AppCompatImageView agregar = comprarProduct.findViewById(R.id.agregar);
        AppCompatImageView ver_foto = comprarProduct.findViewById(R.id.ver_foto);

        codigo.setText("236454");
        name.setText("PINGUINO HEL CORNETTO HERSHEYS2");
        grupo.setText("SANTA ROSA");

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {

                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2296);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2296);
                        }

                    }else{

                        selectPick();

                    }

                } else {

                    selectPick();

                }

            }
        });

        ver_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null){
                    if (Utils.convertBase64String(bitmap) != null) {
                        Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" +Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                }else{
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

    }

    private void getEstadoFacturaDespacho(){

        showProgressWait();

        //JSON SEND
        EstadoFacturaModel model = new EstadoFacturaModel();
        model.setMetodo("ListarEstadoFacturasDespacho");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IEstadoFacturaDespacho request = DepcApplication.getApplication().getRestAdapter().create(IEstadoFacturaDespacho.class);
            call = request.getBodegas(body);
            call.enqueue(new Callback<IEstadoFacturaDespacho.dataBodega>() {
                @Override
                public void onResponse(Call<IEstadoFacturaDespacho.dataBodega> call, Response<IEstadoFacturaDespacho.dataBodega> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListarEstadoFacturasDespacho() != null) {
                                            if (data.getData().getListarEstadoFacturasDespacho().size() > 0) {


                                                final List<EstadoFacturasDespacho> bodegas;
                                                bodegas = data.getData().getListarEstadoFacturasDespacho().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteEstadoFacturasDespacho(DepcApplication.getApplication().getEstadoFacturasDespachoDao());
                                                    DepcApplication.getApplication().getEstadoFacturasDespachoDao().callBatchTasks(new Callable<EstadoFacturasDespacho>() {
                                                        @Override
                                                        public EstadoFacturasDespacho call() throws Exception {
                                                            for (EstadoFacturasDespacho cl : bodegas) {
                                                                DataBaseHelper.saveEstadoFacturasDespacho(cl, DepcApplication.getApplication().getEstadoFacturasDespachoDao());
                                                            }
                                                            return null;
                                                        }
                                                    });

                                                }

                                                showEstadoFacturaDespacho();

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

                            showEstadoFacturaDespacho();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showEstadoFacturaDespacho();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showEstadoFacturaDespacho();
                    }
                }

                @Override
                public void onFailure(Call<IEstadoFacturaDespacho.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showEstadoFacturaDespacho();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showEstadoFacturaDespacho();

        }
    }
    
    private void showEstadoFacturaDespacho(){

        try {
            List<EstadoFacturasDespacho> estadoFacturasDespachos = DataBaseHelper.getEstadoFacturasDespacho(DepcApplication.getApplication().getEstadoFacturasDespachoDao());
            if (estadoFacturasDespachos != null){
                if (estadoFacturasDespachos.size() > 0){
                    for (EstadoFacturasDespacho estado : estadoFacturasDespachos){
                        Log.e(TAG,""+estado.getDescripcion());
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    public void doRetry() {

    }
}
