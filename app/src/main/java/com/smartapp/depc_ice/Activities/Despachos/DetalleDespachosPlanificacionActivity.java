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
import com.smartapp.depc_ice.Entities.DetalleFacturas;
import com.smartapp.depc_ice.Entities.DetalleFormaPago;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.DetalleViaje;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.EstadoFacturasDespacho;
import com.smartapp.depc_ice.Entities.MotivosNoEntrega;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Entities.PuntosVenta;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Interface.IDespachoItemFactura;
import com.smartapp.depc_ice.Interface.IEstadoFacturaDespacho;
import com.smartapp.depc_ice.Interface.IMotivosNoEntrega;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.Models.DespachoItemFactura;
import com.smartapp.depc_ice.Models.Device;
import com.smartapp.depc_ice.Models.EstadoFacturaModel;
import com.smartapp.depc_ice.Models.MotivoNoEntregaModel;
import com.smartapp.depc_ice.Models.RegistrarDespachoModel;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private int indexEstadoFacturaDespachos = 0;
    private List<DetalleFacturas> detalleFacturas;
    float cant = 0;
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
    private TextView llamar,clienteName,ruc,lbl_fecha, direccion;
    private TextView btn_whatsapp;
    private TextView agregar,ver_foto;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private Call<IEstadoFacturaDespacho.dataBodega> call;
    private IEstadoFacturaDespacho.dataBodega data;
    private DetalleViaje detalleViaje;
    private Call<IClientes.dataClientes> callCliente;
    private IClientes.dataClientes dataCliente;
    private Clientes cliente;
    private Spinner spinner_noentrega;
    private int indexMotivoNoEntrega = -1;
    Bitmap fotItem = null;
    boolean fotoItem = false;
    private Bitmap bitmapFirma;
    private EditText recibe;
    private Usuario user;
    private Call<IMotivosNoEntrega.dataBodega> callMotivos;
    private IMotivosNoEntrega.dataBodega dataMotvos;
    private Call<IDespachoItemFactura.dataBodega> callItem;
    private IDespachoItemFactura.dataBodega dataItem;
    private List<MotivosNoEntrega> motivosNoEntrega;
    private EditText observacio;
    List<EstadoFacturasDespacho> estadoFacturasDespachos;

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

        DepcApplication.getApplication().isReload = false;

        layout = addLayout(R.layout.despacho_plainificacion_layout);
        spinner_despacho = (Spinner) layout.findViewById(R.id.spinner_despacho);
        impresora = (TextView) layout.findViewById(R.id.impresora);

        ver_mapa = (TextView) layout.findViewById(R.id.ver_mapa);
        llamar = (TextView) layout.findViewById(R.id.llamar);
        clienteName = (TextView) layout.findViewById(R.id.clienteName);
        ruc = (TextView) layout.findViewById(R.id.ruc);
        lbl_fecha = (TextView) layout.findViewById(R.id.lbl_fecha);
        direccion = (TextView) layout.findViewById(R.id.direccion);
        recibe = (EditText) layout.findViewById(R.id.recibe);
        observacio = (EditText) layout.findViewById(R.id.comentario);
        spinner_noentrega = (Spinner) layout.findViewById(R.id.spinner_noentrega);
        btn_whatsapp = (TextView) layout.findViewById(R.id.btn_whatsapp);
        cobrar = (Button) layout.findViewById(R.id.cobrar);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);

        Utils.SetStyleActionBarTitle(this);

        if (getIntent() != null){
            detalleViaje = (DetalleViaje) getIntent().getSerializableExtra("detalle_viaje");
        }

        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0){
                    user = usuarios.get(0);
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detalleViaje != null){
                    if (detalleViaje.getFactura_id() != null){
                        Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, CobrosActivity.class);
                        intent.putExtra("factura_id",detalleViaje.getFactura_id());
                        intent.putExtra("id_vaje",detalleViaje.getId_viaje());
                        intent.putExtra("cuenta_id",detalleViaje.getCuenta_id());
                        String nombreCliente = "";
                        String ruc_cliente = "";
                        String direccion_cliente = "";
                        String telefono_cliente = "";
                        if (cliente != null){
                            if (cliente.getNombre_comercial() != null){
                                nombreCliente =  cliente.getNombre_comercial();
                            }
                            if (cliente.getTercero_id() != null){
                                ruc_cliente =  cliente.getTercero_id();
                            }
                            if (cliente.getDireccion() != null){
                                direccion_cliente =  cliente.getDireccion();
                            }

                            if (cliente.getTelefono() != null){
                                telefono_cliente =  cliente.getTelefono();
                            }
                        }
                        intent.putExtra("nombreCliente",nombreCliente);
                        intent.putExtra("ruc_cliente",ruc_cliente);
                        intent.putExtra("direccion_cliente",direccion_cliente);
                        intent.putExtra("telefono_cliente",telefono_cliente);
                        startActivity(intent);
                    }
                }

            }
        });

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
                if (cliente != null){
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

            }
        });

        ver_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detalleViaje != null){
                    if (detalleViaje.getLatitud() != null && detalleViaje.getLongitud() != null){
                        Direcciones direccion = new Direcciones();
                        direccion.setLatitud(detalleViaje.getLatitud());
                        direccion.setLongitud(detalleViaje.getLongitud());
                        direccion.setDireccion_envio(""+detalleViaje.getDireccion_envio());
                        Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, MapsActivity.class);
                        intent.putExtra(Const.DETALLE_CLIENTE, direccion);
                        startActivity(intent);
                    }
                }




            }
        });

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente != null){
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
                        bitmapFirma = signature_pad.getSignatureBitmap();
                        registrarDespacho();
                        MyDialog.dismiss();
                    }
                });


                MyDialog.show();

            }
        });

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
                    /*Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                    startActivity(intent);*/
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

        if (detalleFacturas != null){
            generatedZPL();
        }

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


                if (fotoItem) {

                    fotItem = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    fotItem.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    Log.e(TAG, "Base64: " + Utils.convertBase64String(fotItem));
                }else{
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    Log.e(TAG, "Base64: " + Utils.convertBase64String(bitmap));
                }

                fotoItem = false;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {

                if (fotoItem) {
                    fotItem = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    fotItem.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    Log.e(TAG,"Base64: "+Utils.convertBase64String(fotItem));
                }else{
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    Log.e(TAG,"Base64: "+Utils.convertBase64String(bitmap));
                }
                fotoItem = false;

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
        if (cliente == null){
            return "";
        }


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

                String offline = "";

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

                String nombreCliente = "";
                String ruc_cliente = "";
                String direccion_cliente = "";
                String telefono_cliente = "";
                if (cliente != null){

                    if (cliente.getNombre_comercial() != null){
                        nombreCliente = ""+cliente.getNombre_comercial();
                    }

                    if (cliente.getTercero_id() != null){
                        ruc_cliente = ""+cliente.getTercero_id();
                    }

                    if (cliente.getDireccion() != null){
                        direccion_cliente = ""+cliente.getDireccion();
                    }

                    if (cliente.getTelefono() != null){
                        telefono_cliente = ""+cliente.getTelefono();
                    }

                }

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
                        "RECAUDADOR: " + recaudadorString + "\n\n" +

                        "------------------------------" + "\n" +
                        "COD.   DESCRIP.       CANT" + "\n" +
                        "------------------------------" + "\n";

                try {


                    if (detalleFacturas != null){
                        if (detalleFacturas.size() > 0){

                            for (DetalleFacturas df : detalleFacturas){
                                zpl += "" +truncate( df.getCodigo_item(), 5)+ "  " + truncate(df.getDescripcion(),17) + truncate( ""+ df.getCantidad(),8) + "\n";
                            }
                        }
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }



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
        //getCobros();
        zpl = texto;
        //Log.e("TAG---",zpl);

        new AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this)
                .setTitle("ATENCIÓN")
                .setMessage("¿Desea imprimir items de Despacho?")
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

        DetalleDespachosPlanificacionActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Toast.makeText(DetalleDespachosPlanificacionActivity.this, displayPrinterLanguage + "\n" + "Language set to ZPL", Toast.LENGTH_LONG).show();

            }
        });
    }



    private void fillData(){

        if (cliente != null){
            if (cliente.getNombre_tercero() != null){
                clienteName.setText(""+cliente.getNombre_tercero());
            }

            if (detalleViaje != null){
                if (detalleViaje.getFecha_crea() != null){
                    lbl_fecha.setText(""+detalleViaje.getFecha_crea());
                }
            }

            if (cliente.getTercero_id() != null){
                ruc.setText(""+cliente.getTercero_id());
            }

            if (cliente.getDireccion() != null){
                direccion.setText(""+cliente.getDireccion());
            }
            
        }


        try {


            if (detalleViaje != null){
                if (detalleViaje.getFactura_id() != null){

                    detalleFacturas = DataBaseHelper.getDetalleFacturasByIDFactura(DepcApplication.getApplication().getDetalleFacturasDao(), ""+detalleViaje.getFactura_id());
                    if (cliente != null){
                        if(detalleFacturas != null){

                            lista.setAdapter(new ListaDespachoAdapter(DetalleDespachosPlanificacionActivity.this, detalleFacturas));
                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    editar(detalleFacturas.get(position));
                                }
                            });

                            getMotivosNoEntrega();

                        }
                    }

                }
            }




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void getMotivosNoEntrega(){

        showProgressWait();

        //JSON SEND
        MotivoNoEntregaModel model = new MotivoNoEntregaModel();
        model.setMetodo("ObtenerMotivosNoEntrega");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IMotivosNoEntrega request = DepcApplication.getApplication().getRestAdapter().create(IMotivosNoEntrega.class);
            callMotivos = request.getBodegas(body);
            callMotivos.enqueue(new Callback<IMotivosNoEntrega.dataBodega>() {
                @Override
                public void onResponse(Call<IMotivosNoEntrega.dataBodega> call, Response<IMotivosNoEntrega.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataMotvos = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataMotvos != null) {
                                if (dataMotvos.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataMotvos.getData() != null){
                                        if (dataMotvos.getData().getListarMotivosNoEntrega() != null) {
                                            if (dataMotvos.getData().getListarMotivosNoEntrega().size() > 0) {


                                                final List<MotivosNoEntrega> bodegas;
                                                bodegas = dataMotvos.getData().getListarMotivosNoEntrega().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteMotivosNoEntrega(DepcApplication.getApplication().getMotivosNoEntregaDao());
                                                    DepcApplication.getApplication().getMotivosNoEntregaDao().callBatchTasks(new Callable<MotivosNoEntrega>() {
                                                        @Override
                                                        public MotivosNoEntrega call() throws Exception {
                                                            for (MotivosNoEntrega cl : bodegas) {
                                                                DataBaseHelper.saveMotivosNoEntrega(cl, DepcApplication.getApplication().getMotivosNoEntregaDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showMotivosNoentrega();

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

                            showMotivosNoentrega();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showMotivosNoentrega();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showMotivosNoentrega();
                    }
                }

                @Override
                public void onFailure(Call<IMotivosNoEntrega.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showMotivosNoentrega();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showMotivosNoentrega();

        }
    }

    private void showMotivosNoentrega(){

        indexMotivoNoEntrega = -1;
        try {

            motivosNoEntrega = DataBaseHelper.getMotivosNoEntrega(DepcApplication.getApplication().getMotivosNoEntregaDao());

             if (motivosNoEntrega != null) {
            if (motivosNoEntrega.size() > 0) {

                List<String> items= new ArrayList<String>();
                int indexMoti = -1;
                int contador = 0;
                for (MotivosNoEntrega z : motivosNoEntrega){
                    if (z.getDescripcion() != null) {
                        if (detalleViaje.getId_motivo_noentrega() != null){
                            if (z.getNum_motivo().equals(detalleViaje.getId_motivo_noentrega())){
                                indexMoti = contador;
                            }
                        }
                        items.add(z.getDescripcion());
                    }else{
                        items.add("SIN DESCRIPCIÓN");
                    }

                    contador++;
                }
                items.add("SELECCIONE MOTIVO NO ENTREGA");
                final int listsize = items.size() - 1;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, items) {
                    @Override
                    public int getCount() {
                        return(listsize); // Truncate the list
                    }
                };
                spinner_noentrega.setAdapter(adapter);
                if (indexMoti != -1){
                    spinner_noentrega.setSelection(indexMoti);
                }else{
                    spinner_noentrega.setSelection(listsize);
                }

                spinner_noentrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        indexMotivoNoEntrega = position;
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

    private void editar(DetalleFacturas df){

        String pto_vt = "";
        try {
            List<PuntosVenta> puntos = DataBaseHelper.getPuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
            if (puntos != null){
                if (puntos.size() > 0){
                    pto_vt = puntos.get(0).getNombre();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(DetalleDespachosPlanificacionActivity.this);
        View comprarProduct = layoutInflater.inflate(R.layout.item_alert, null);
        final AlertDialog alert = new AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this).create();
        alert.setView(comprarProduct);

        ImageView back = (ImageView) comprarProduct.findViewById(R.id.back);
        ImageView info_product = (ImageView) comprarProduct.findViewById(R.id.info_product);
        //Spinner spinner_noentrega = (Spinner) comprarProduct.findViewById(R.id.spinner_noentrega);
        TextView codigo = (TextView) comprarProduct.findViewById(R.id.codigo);
        TextView cantidad = (TextView) comprarProduct.findViewById(R.id.cantidad);
        TextView name = (TextView) comprarProduct.findViewById(R.id.name);
        TextView comentario = (TextView) comprarProduct.findViewById(R.id.comentario);
        TextView grupo = (TextView) comprarProduct.findViewById(R.id.grupo);
        ImageView menos = (ImageView) comprarProduct.findViewById(R.id.menos);
        ImageView mas = (ImageView) comprarProduct.findViewById(R.id.mas);
        AppCompatImageView agregar = comprarProduct.findViewById(R.id.agregar);
        AppCompatImageView ver_foto = comprarProduct.findViewById(R.id.ver_foto);
        Button guardar = comprarProduct.findViewById(R.id.guardar);
        fotItem = null;
        fotoItem = false;

        codigo.setText(""+df.getCodigo_item());
        name.setText(""+df.getDescripcion());
        grupo.setText(""+pto_vt);
        cantidad.setText(""+df.getCantidad()+" / "+df.getCantidad());

        if (df.getObservacion_noentrega() != null){
            comentario.setText(""+df.getObservacion_noentrega());
        }

        if (df.getFoto_noentrega() != null){
            if (df.getFoto_noentrega().length() > 0) {
                if (!df.getFoto_noentrega().equals("null")) {
                    fotItem = Utils.convert(df.getFoto_noentrega());
                }
            }
        }

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fotoItem = true;
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

        cant = 0;
        if (df.getCantidad() != null){
            cant = Float.parseFloat(df.getCantidad());
        }
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cant > 0){
                    cant--;
                    cantidad.setText(""+cant+" / "+df.getCantidad());
                }

            }
        });

        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cant < Float.parseFloat(df.getCantidad())){
                    cant++;
                    cantidad.setText(""+cant+" / "+df.getCantidad());
                }

            }
        });

        ver_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fotItem != null){
                    if (Utils.convertBase64String(fotItem) != null) {
                        Intent intent = new Intent(DetalleDespachosPlanificacionActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" +Utils.convertBase64String(fotItem));
                        startActivity(intent);
                    }

                }else{
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiaestoItemDespacho(alert, comentario.getText().toString(),""+df.getFct_det_id());
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


    private void cambiaestoItemDespacho(AlertDialog alert, String comentario, String fct_det_id){

        showProgressWait();

        //JSON SEND
        DespachoItemFactura model = new DespachoItemFactura();
        model.setObservacion_noentrega(comentario);
        model.setFoto_noentrega("");
        if (fotItem != null){
            model.setFoto_noentrega(""+Utils.convertBase64String(fotItem));
        }
        model.setCntdespacho(""+cant);
        model.setFct_det_id(fct_det_id);
        model.setMetodo("RegistrarItemDespacho");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IDespachoItemFactura request = DepcApplication.getApplication().getRestAdapter().create(IDespachoItemFactura.class);
            callItem = request.getBodegas(body);
            callItem.enqueue(new Callback<IDespachoItemFactura.dataBodega>() {
                @Override
                public void onResponse(Call<IDespachoItemFactura.dataBodega> call, Response<IDespachoItemFactura.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataItem = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataItem != null) {
                                if (dataItem.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alert.dismiss();

                                            showAlert("ACTUALIZACIÓN REALIZADA");
                                            return;
                                        }
                                    });

                                    return;
                                }else{
                                    if (dataItem.getStatus_message() != null){
                                        mensajeError = dataItem.getStatus_message();
                                    }

                                }
                            }

                            showAlert(mensajeError);


                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showAlert(Const.ERROR_DEFAULT);
                        }

                    } else {
                        hideProgressWait();
                        showAlert(Const.ERROR_DEFAULT);

                    }
                }

                @Override
                public void onFailure(Call<IDespachoItemFactura.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    showAlert(Const.ERROR_DEFAULT);

                }
            });

        }catch (Exception e){
            hideProgressWait();
            showAlert(Const.ERROR_DEFAULT);

        }
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
            estadoFacturasDespachos = DataBaseHelper.getEstadoFacturasDespacho(DepcApplication.getApplication().getEstadoFacturasDespachoDao());
            if (estadoFacturasDespachos != null){
                if (estadoFacturasDespachos.size() > 0){

                    indexEstadoFacturaDespachos = 0;
                    int contador = 0;
                    List<String> items= new ArrayList<String>();
                    for (EstadoFacturasDespacho z : estadoFacturasDespachos){
                        items.add(z.getDescripcion());
                        if (z.getNum_estado() != null){
                            if (detalleViaje != null){
                            if (detalleViaje.getEstado() != null) {
                                if (detalleViaje.getEstado().equals("" + z.getNum_estado())) {
                                    indexEstadoFacturaDespachos = contador;
                                }
                            }
                            }
                        }
                        contador++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    spinner_despacho.setAdapter(adapter);
                    spinner_despacho.setSelection(indexEstadoFacturaDespachos);
                    spinner_despacho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            indexEstadoFacturaDespachos = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            }

            /*if (detalleViaje != null) {
                if (detalleViaje.getCuenta_id() != null) {
                    condition(""+detalleViaje.getCuenta_id());
                }
            }*/

            if (detalleViaje != null){
                if (detalleViaje.getFactura_id() != null){
                    String cliente_id = "";
                    detalleFacturas = DataBaseHelper.getDetalleFacturasByIDFactura(DepcApplication.getApplication().getDetalleFacturasDao(), ""+detalleViaje.getFactura_id());
                    if(detalleFacturas != null){
                        for (DetalleFacturas df: detalleFacturas) {
                            cliente_id = df.getCliente_id();
                        }
                    }

                    condition(""+cliente_id);

                }

                if (detalleViaje.getFoto_entrega() != null){
                    if (detalleViaje.getFoto_entrega().length() > 0) {
                        if (!detalleViaje.getFoto_entrega().equals("null")) {
                            bitmap = Utils.convert(detalleViaje.getFoto_entrega());
                        }
                    }
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    private void condition(String search){
        search.trim();
        search = search.replace(" ","%").toUpperCase();
        String condition = "and a.codigo_cliente_id = '"+search+"'";
        getClientes(condition);
    }

    private void getClientes(String search){

        showProgressWait();

        int limit = Const.PARAM_MAX_ROW;

        String buscar = "";
        if (search.length() > 0){
            buscar = buscar+" "+search;
        }
        //JSON SEND
        ClientesModel model = new ClientesModel();
        model.setCondicion(buscar);
        model.setFiltro("limit "+Const.PARAM_MAX_ROW+" offset 0");
        model.setMetodo("ListaClientes");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);
        try {
            IClientes request = DepcApplication.getApplication().getRestAdapter().create(IClientes.class);
            callCliente = request.getClientes(body);
            callCliente.enqueue(new Callback<IClientes.dataClientes>() {
                @Override
                public void onResponse(Call<IClientes.dataClientes> call, Response<IClientes.dataClientes> response) {
                    if (response.isSuccessful()) {

                        dataCliente = response.body();
                        try {

                            hideProgressWait();

                            if (dataCliente != null) {
                                if (dataCliente.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataCliente.getData() != null)
                                        if (dataCliente.getData().getListaClientes() != null){
                                            if (dataCliente.getData().getListaClientes().size() > 0){

                                                final List<Clientes> clientes;
                                                clientes = dataCliente.getData().getListaClientes().get(0);

                                                if (clientes != null){
                                                    DepcApplication.getApplication().getClientesDao().callBatchTasks(new Callable<Clientes>() {
                                                        @Override
                                                        public Clientes call() throws Exception {

                                                            for (Clientes cl : clientes){
                                                                boolean isFlag = true;
                                                                if (cl.getNombre_comercial() != null){
                                                                    if (cl.getNombre_comercial().length() > 0) {
                                                                        if (cl.getNombre_tercero() == null) {
                                                                            cl.setNombre_tercero("" + cl.getNombre_comercial());
                                                                            isFlag = false;
                                                                        }else if (cl.getNombre_tercero().length() == 0) {
                                                                            cl.setNombre_tercero("" + cl.getNombre_comercial());
                                                                            isFlag = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (cl.getNombre_tercero() != null){
                                                                    if (cl.getNombre_tercero().length() > 0) {
                                                                        if (cl.getNombre_comercial() == null) {
                                                                            cl.setNombre_comercial("" + cl.getNombre_tercero());
                                                                            isFlag = false;
                                                                        }else if (cl.getNombre_comercial().length() == 0) {
                                                                            cl.setNombre_comercial("" + cl.getNombre_tercero());
                                                                            isFlag = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (cl.getNombre_comercial() != null && cl.getNombre_tercero() != null){
                                                                    isFlag = false;
                                                                }

                                                                if (isFlag) {
                                                                    cl.setNombre_tercero("SIN NOMBRE");
                                                                    cl.setNombre_comercial("SIN NOMBRE ASIGNADO");
                                                                }

                                                                cliente = cl;
                                                            }

                                                            return null;
                                                        }
                                                    });


                                                }

                                                fillData();

                                                return;
                                            }
                                        }
                                }else{
                                    //Error
                                }
                            }
                            fillData();

                        } catch (Exception e) {
                            e.printStackTrace();
                            fillData();
                        }

                    } else {
                        fillData();
                    }
                }

                @Override
                public void onFailure(Call<IClientes.dataClientes> call, Throwable t) {
                    fillData();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            fillData();

        }

    }




    private void registrarDespacho(){

        /*if (indexMotivoNoEntrega == -1){
            showAlert("Seleccione motivo de no entrega");
            return;
        }

        if (motivosNoEntrega.size() == indexMotivoNoEntrega){
            showAlert("Seleccione motivo de no entrega");
            return;
        }*/

        if (indexEstadoFacturaDespachos == -1){
            showAlert("Seleccione estado de factura");
            return;
        }

        showProgressWait();


        //JSON SEND
        RegistrarDespachoModel model = new RegistrarDespachoModel();
        model.setFactura_id(""+detalleViaje.getFactura_id());
        model.setFoto_entrega("null");
        if (bitmap != null){
            model.setFoto_entrega(""+Utils.convertBase64String(bitmap));
        }
        model.setId_motivo_noentrega("null");
        if (motivosNoEntrega != null) {
            if (indexMotivoNoEntrega != -1) {
                if (motivosNoEntrega.size() != indexMotivoNoEntrega){
                    model.setId_motivo_noentrega("" + motivosNoEntrega.get(indexMotivoNoEntrega).getNum_motivo());
                }
            }
        }


        model.setFirma_persona_recibe("null");
        if (bitmapFirma != null){
            model.setFirma_persona_recibe(""+Utils.convertBase64String(bitmapFirma));
        }
        model.setNombre_persona_recibe(""+recibe.getText().toString());
        model.setUsuario_procesa(""+user.getUsuario());
        model.setObservacion_entrega(""+observacio.getText().toString());
        model.setEstado(""+estadoFacturasDespachos.get(indexEstadoFacturaDespachos).getNum_estado());
        model.setMetodo("RegistrarDespachoFactura");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IDespachoItemFactura request = DepcApplication.getApplication().getRestAdapter().create(IDespachoItemFactura.class);
            callItem = request.getBodegas(body);
            callItem.enqueue(new Callback<IDespachoItemFactura.dataBodega>() {
                @Override
                public void onResponse(Call<IDespachoItemFactura.dataBodega> call, Response<IDespachoItemFactura.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataItem = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataItem != null) {
                                if (dataItem.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            AlertDialog alertDialog = new AlertDialog.Builder(DetalleDespachosPlanificacionActivity.this).create();
                                            alertDialog.setTitle("Atención");
                                            alertDialog.setMessage("ACTUALIZACIÓN REALIZADA");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            DepcApplication.getApplication().isReload = true;
                                                            finish();
                                                        }
                                                    });
                                            alertDialog.show();

                                            return;
                                        }
                                    });

                                    return;
                                }else{
                                    if (dataItem.getStatus_message() != null){
                                        mensajeError = dataItem.getStatus_message();
                                    }

                                }
                            }

                            showAlert(mensajeError);


                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showAlert(Const.ERROR_DEFAULT);
                        }

                    } else {
                        hideProgressWait();
                        showAlert(Const.ERROR_DEFAULT);

                    }
                }

                @Override
                public void onFailure(Call<IDespachoItemFactura.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    showAlert(Const.ERROR_DEFAULT);

                }
            });

        }catch (Exception e){
            hideProgressWait();
            showAlert(Const.ERROR_DEFAULT);

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
