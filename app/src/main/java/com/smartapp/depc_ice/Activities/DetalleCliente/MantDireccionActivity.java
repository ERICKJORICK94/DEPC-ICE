package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;
import com.schibstedspain.leku.LocationPickerActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.Adapter.ListaGabinetClienteAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.ScannerQr.ScanQrActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.Gabinet;
import com.smartapp.depc_ice.Entities.PuntosVenta;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IClientesGabinet;
import com.smartapp.depc_ice.Interface.ICrearDireccion;
import com.smartapp.depc_ice.Interface.ICrearDireccion;
import com.smartapp.depc_ice.Interface.IGabinet;
import com.smartapp.depc_ice.Interface.IPuntosVentas;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.ClientesGabinetModel;
import com.smartapp.depc_ice.Models.GabinetModel;
import com.smartapp.depc_ice.Models.PuntosVentasModel;
import com.smartapp.depc_ice.Models.RegistrarDireccionClienteModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.NonScrollListView;
import com.smartapp.depc_ice.Utils.PhotoViewer;
import com.smartapp.depc_ice.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MantDireccionActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks   {
    List<String> desc = new ArrayList<String>();;
    List<String> id = new ArrayList<String>();;
    EditText etDireccion;
    EditText correo;
    EditText celular;
    EditText dias;
    EditText nombre_foto;
    ImageButton ibTomarGPS;
    TextView ibVerMapa;
    LinearLayout llLatLon, llNOLatLon;
    TextView tvCodCli, tvNombre, tvSecuencia, tvLat, tvLon, tvRuta, agregar,ver_foto,lista_gabinets;
    Button btActualizar;
    private Direcciones direccion;
    private View layout;
    private Spinner spinnerCongelador;
    private LayoutInflater layoutInflater;
    LocationListener mlocListener2;
    LocationManager mlocManager2;
    Direcciones temp;
    String cod_cliente = "";
    int idActulizar = 0;
    private LinearLayout btn_whatsapp;
    private Spinner zona;
    private Call<IZonas.dataBodega> call;
    private IZonas.dataBodega data;
    String[] semanas = { "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES","SÁBADO","DOMINGO"};

    private Call<IPuntosVentas.dataPuntos> callPuntos;
    private IPuntosVentas.dataPuntos dataPuntos;

    private Call<IGabinet.dataGabinet> callGabinet;
    private IGabinet.dataGabinet dataGabinet;

    private int indexZonas = -1;
    private int indexGabinets = -1;
    private List<Zonas> zonas;
    private List<Gabinet> gabinets;
    private List<PuntosVenta> puntosVentas;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private boolean isActualizar = false;
    private Clientes cliente;
    private TextView qr;
    private Spinner spinner_dias;
    private final static int MY_PERMISSIONS_REQUEST_CAMARA = 9991;
    private final static int MAP_BUTTON_REQUEST_CODE = 6324;
    private String congeladorID = "null";

    private Call<ICrearDireccion.dataUsuario> callRegistro;
    private ICrearDireccion.dataUsuario dataRegistro;
    private Usuario user;
    private NonScrollListView listaGabinet;

    private Call<IClientesGabinet.dataGabinet> callGabinetCliente;
    private IClientesGabinet.dataGabinet dataGabinetCliente;
    private List<ClienteGabinet> gabinetsClientes;
    private int indexSemana = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.activity_mant_direccio);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

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

        cliente = DepcApplication.getApplication().getCliente();

        ibTomarGPS = layout.findViewById(R.id.ib_tomar_gps);
        spinner_dias = layout.findViewById(R.id.spinner_dias);
        ibVerMapa = layout.findViewById(R.id.ib_ver_mapa);
        btActualizar = layout.findViewById(R.id.bt_actualizar);
        llLatLon = layout.findViewById(R.id.ll_lat_lon);
        llNOLatLon = layout.findViewById(R.id.ll_no_lat_lon);
        tvCodCli = layout.findViewById(R.id.tv_cliente);
        lista_gabinets = layout.findViewById(R.id.lista_gabinets);
        correo = layout.findViewById(R.id.correo);
        celular = layout.findViewById(R.id.celular);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);
        dias = layout.findViewById(R.id.dias);
        nombre_foto = layout.findViewById(R.id.nombre_foto);
        tvNombre = layout.findViewById(R.id.tv_nombre);
        tvSecuencia = layout.findViewById(R.id.tv_secuencia);
        tvLat = layout.findViewById(R.id.tv_lat);
        tvLon = layout.findViewById(R.id.tv_lon);
        tvRuta = layout.findViewById(R.id.tv_ruta);
        listaGabinet = (NonScrollListView) layout.findViewById(R.id.listaGabinet);
        spinnerCongelador = layout.findViewById(R.id.spinnerCongelador);
        qr = layout.findViewById(R.id.qr);
        etDireccion = layout.findViewById(R.id.et_direccion);
        zona = (Spinner) layout.findViewById(R.id.zona);
        btn_whatsapp = layout.findViewById(R.id.btn_whatsapp);

        String parametro = getIntent().getStringExtra("parametro");
        String nombre = getIntent().getStringExtra("nombre");
        temp = new Direcciones();

        tvNombre.setText(nombre);
        tvCodCli.setText(getIntent().getStringExtra("cliente"));


        if (getIntent().getStringExtra("cliente") != null){
            cod_cliente = getIntent().getStringExtra("cliente");

        }

        listaGabinet.setVisibility(View.GONE);
        lista_gabinets.setVisibility(View.GONE);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,semanas);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dias.setAdapter(aa);
        spinner_dias.setSelection(indexSemana);
        spinner_dias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indexSemana = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(parametro.equals("mant")) {
            direccion = (Direcciones) getIntent().getSerializableExtra(Const.DETALLE_DIRECCION);
            btActualizar.setText("ACTUALIZAR");
            isActualizar = true;
            //Joseph
            //Log.e("==>",direccion.toString());
            temp = direccion;

            if (temp.getLongitud() == null){
                temp.setLongitud("");
            }

            if (temp.getLatitud() == null){
                temp.setLatitud("");
            }



            if (temp.getLatitud().length() == 0 && temp.getLongitud().length() == 0) {
                llNOLatLon.setVisibility(View.VISIBLE);
                llLatLon.setVisibility(View.GONE);
            }

            //tvSecuencia.setText(temp.getSECUENCIA());

            tvLat.setText(""+temp.getLatitud());
            tvLon.setText(""+temp.getLongitud());
            etDireccion.setText(""+temp.getDireccion_envio());

            if (temp.getEmail_contacto() != null){
                correo.setText(""+temp.getEmail_contacto());
            }

            if (temp.getTelefono_contacto() != null){
                celular.setText(""+temp.getTelefono_contacto());
            }

            if (temp.getDia_visita() != null){
                dias.setText(""+temp.getDia_visita());
                if (Utils.isNumber(temp.getDia_visita())){
                    int in = Integer.parseInt(temp.getDia_visita());
                    if (in > 0 && in <= 7){
                        indexSemana = in - 1;
                        spinner_dias.setSelection(indexSemana);
                    }
                }
            }

            if (temp.getCodigo() != null){
                congeladorID = ""+temp.getCodigo();
            }

            if (temp.getFoto() != null){
                if (temp.getFoto().length() > 0){
                    if (Utils.convert(temp.getFoto()) != null){
                        bitmap = Utils.convert(temp.getFoto());
                    }
                }
            }


        } else {
            //NUEVA DIRECCION
            direccion = new Direcciones();

            if (cliente != null){

                if (cliente.getTelefono1() != null){
                    celular.setText(""+cliente.getTelefono1());
                }

                if (cliente.getEmail() != null){
                    correo.setText(""+cliente.getEmail());
                }

                /*if (cliente.getDias_credito() != null){
                    dias.setText(""+cliente.getDias_credito());
                }*/

            }


        }
        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isActualizar){
                    //showAlert(Const.EN_CONSTRUCCION);
                    sendCrear();
                }else{
                    sendCrear();
                }


            }
        });

        ibVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = true;
                if( direccion == null){
                    //Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
                    //return;
                    flag = false;
                }

                if( direccion != null) {
                    if (direccion.getLatitud() == null) {
                        //Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
                        //return;
                        flag = false;
                    }
                }

                if( direccion != null) {
                    if (direccion.getLongitud() == null) {
                        //Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
                        //return;
                        flag = false;
                    }
                }

                if( direccion != null) {
                    if (direccion.getLatitud() != null) {
                        if (direccion.getLatitud().length() == 0 && direccion.getLongitud().length() == 0) {
                                /*Intent intent = new Intent(MantDireccionActivity.this, MapsActivity.class);
                                intent.putExtra(Const.DETALLE_CLIENTE, direccion);
                                startActivity(intent);*/
                            flag = false;

                        }
                    }
                }



                    if (flag){

                        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                                .withLocation(Double.parseDouble(direccion.getLatitud()), Double.parseDouble(direccion.getLongitud()))
                                .withGeolocApiKey(getResources().getString(R.string.YOUR_API_KEY))
                                .withSearchZone("ec_EC")
                                //.withSearchZone(new SearchZoneRect(new LatLng(26.525467, -18.910366), new LatLng(43.906271, 5.394197)))
                                .withDefaultLocaleSearchZone()
                                .shouldReturnOkOnBackPressed()
                                .withStreetHidden()
                                .withCityHidden()
                                .withZipCodeHidden()
                                .withSatelliteViewHidden()
                                //.withGooglePlacesEnabled()
                                .withGooglePlacesApiKey(getResources().getString(R.string.YOUR_API_KEY))
                                .withGoogleTimeZoneEnabled()
                                .withVoiceSearchHidden()
                                .withUnnamedRoadHidden()
                                .build(MantDireccionActivity.this);

                        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);

                    }else {

                        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                                //.withLocation(Double.parseDouble(direccion.getLatitud()), Double.parseDouble(direccion.getLongitud()))
                                .withGeolocApiKey(getResources().getString(R.string.YOUR_API_KEY))
                                .withSearchZone("ec_EC")
                                //.withSearchZone(new SearchZoneRect(new LatLng(26.525467, -18.910366), new LatLng(43.906271, 5.394197)))
                                .withDefaultLocaleSearchZone()
                                .shouldReturnOkOnBackPressed()
                                .withStreetHidden()
                                .withCityHidden()
                                .withZipCodeHidden()
                                .withSatelliteViewHidden()
                                //.withGooglePlacesEnabled()
                                .withGooglePlacesApiKey(getResources().getString(R.string.YOUR_API_KEY))
                                .withGoogleTimeZoneEnabled()
                                .withVoiceSearchHidden()
                                .withUnnamedRoadHidden()
                                .build(MantDireccionActivity.this);

                        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
                    }
            }


        });
        
        ibTomarGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MantDireccionActivity.this);
                                builder.setMessage("¿Seguro que desea tomar la ubicación GPS ahora? \n Si ya tiene datos previos se sobreescribiran...")
                                        .setCancelable(false)
                                        .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                if (DepcApplication.getApplication() != null){
                                                    if (DepcApplication.getApplication().getLatitud() != null){
                                                        if (DepcApplication.getApplication().getLongitud() != null){
                                                            if (DepcApplication.getApplication().getLatitud().length() > 0){
                                                                if (DepcApplication.getApplication().getLongitud().length() > 0){
                                                                    //code
                                                                    tvLat.setText(DepcApplication.getApplication().getLatitud());
                                                                    tvLon.setText(DepcApplication.getApplication().getLongitud());
                                                                    direccion.setLatitud(DepcApplication.getApplication().getLatitud());
                                                                    direccion.setLatitud(DepcApplication.getApplication().getLongitud());

                                                                    Toast.makeText(MantDireccionActivity.this,"Test Ruta Latitud"+ DepcApplication.getApplication().getLatitud().toString(), Toast.LENGTH_LONG).show();

                                                                    llLatLon.setVisibility(View.VISIBLE);
                                                                    llNOLatLon.setVisibility(View.GONE);
                                                                    //Toast.makeText(MantDireccionActivity.this, "Senso exitoso.", Toast.LENGTH_LONG).show();
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                    }

                                                }

                                                if (    ContextCompat.checkSelfPermission(MantDireccionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                                        && ContextCompat.checkSelfPermission(MantDireccionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                    // You need to ask the user to enable the permissions
                                                } else {
                                                    showProgressWait("Tomando Ubicación GPS");
                                                    LocationTracker tracker = new LocationTracker(
                                                            MantDireccionActivity.this,
                                                            new TrackerSettings()
                                                                    .setUseGPS(true)
                                                                    .setUseNetwork(true)
                                                                    .setUsePassive(true)
                                                                    .setTimeout(15000)
                                                                    .setTimeBetweenUpdates(1000)
                                                                    .setMetersBetweenUpdates(1)
                                                    ) {

                                                        @Override
                                                        public void onLocationFound(Location location) {

                                                            try{
                                                                String latitud = String.valueOf(location.getLatitude());
                                                                String longitud = String.valueOf(location.getLongitude());
                                                                if (!latitud.equals("") && !longitud.equals("")) {
                                                                    tvLat.setText(latitud);
                                                                    tvLon.setText(longitud);
                                                                    direccion.setLatitud(latitud);
                                                                    direccion.setLongitud(longitud);

                                                                    llLatLon.setVisibility(View.VISIBLE);
                                                                    llNOLatLon.setVisibility(View.GONE);
                                                                    //Toast.makeText(MantDireccionActivity.this, "Senso exitoso.", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    hideProgressWait();
                                                                    showAlert("Baja señal con la antena GPS, por favor intente moverse a un lugar despejado para mayor cobertura");
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                //Toast.makeText(MantDireccionActivity.this, "Ocurrió un problema.", Toast.LENGTH_LONG).show();

                                                            } finally {
                                                                this.stopListening();
                                                                hideProgressWait();

                                                            }
                                                        }

                                                        @Override
                                                        public void onTimeout() {
                                                            this.stopListening();
                                                            hideProgressWait();
                                                            showAlert("Baja señal con la antena GPS, por favor intente moverse a un lugar despejado para mayor cobertura");

                                                        }
                                                    };
                                                    tracker.startListening();
                                                }

                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {

                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2299);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2299);
                        }

                    }else{

                        checkCameraPermission();

                    }

                } else {

                    checkCameraPermission();

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
                        Intent intent = new Intent(MantDireccionActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" +Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                }else{
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });


        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvLat.getText().toString().length() > 0 && tvLon.getText().toString().length() > 0){

                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.google.com/maps?q="+tvLat.getText().toString()+","+tvLon.getText().toString());
                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        showAlert("Instale Whatsapp antes de continuar");
                    }

                }else{
                    showAlert("Por favor cense antes de continuar");
                }
            }
        });


        getZonas();


    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA );
                    // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
                }
            }else{ //have permissions
                iniciarScan ();
            }
        }else{ // Pre-Marshmallow
            iniciarScan ();
        }
    }

    private void iniciarScan(){

        Intent intent = new Intent(MantDireccionActivity.this, ScanQrActivity.class);
        startActivity(intent);

    }


    private void selectPick(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Cámara", "Galería"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MantDireccionActivity.this);
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
            Toast.makeText(MantDireccionActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_BUTTON_REQUEST_CODE && data != null) {



            if (resultCode == Activity.RESULT_OK) {
                double latitud = data.getDoubleExtra("latitude", 0.0);
                double longitud = data.getDoubleExtra("longitude", 0.0);

                tvLat.setText(""+latitud);
                tvLon.setText(""+longitud);
                direccion.setLatitud(""+latitud);
                direccion.setLongitud(""+longitud);

                llLatLon.setVisibility(View.VISIBLE);
                llNOLatLon.setVisibility(View.GONE);
                //Toast.makeText(MantDireccionActivity.this, "Senso exitoso.", Toast.LENGTH_LONG).show();


            } else if (resultCode == 2) {
                double latitude = data.getDoubleExtra("latitude", 0.0);
                double longitude = data.getDoubleExtra("longitude", 0.0);


            }



        }else if (requestCode == 2299) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    checkCameraPermission();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    selectPick();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                selectPick();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == MY_PERMISSIONS_REQUEST_CAMARA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarScan();
            } else {
                showAlert("Debe aceptar el permiso antes de continuar");
            }
        }
    }

    private void showListZonas(){

        hideProgressWait();

        try {

            zonas = DataBaseHelper.getZonas(DepcApplication.getApplication().getZonasDao());
            if (zonas != null) {
                if (zonas.size() > 0) {

                    int index = 0;
                    int contador = 0;
                    List<String> items= new ArrayList<String>();
                    for (Zonas z : zonas){
                        items.add(z.getDescripcion());
                        if (z.getZona_id() != null){
                            if (direccion != null) {
                                if (direccion.getZona_id() != null) {
                                    if (direccion.getZona_id().equals("" + z.getZona_id())) {
                                        index = contador;
                                    }
                                }
                            }else{
                                if (cliente != null) {
                                    if (cliente.getZona_id() != null) {
                                        if (cliente.getZona_id().equals("" + z.getZona_id())) {
                                            index = contador;
                                        }
                                    }
                                }
                            }
                        }
                        contador++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    zona.setAdapter(adapter);
                    zona.setSelection(index);
                    indexZonas = index;
                    zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            indexZonas = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
            }

            //getPuntosVenta();
            if (isActualizar) {
                getGabinetCliente();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
        }
    }

    private void getZonas(){

        showProgressWait();

        //JSON SEND
        BodegasModel model = new BodegasModel();
        model.setCondicion("");
        model.setMetodo("ListarZonas");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IZonas request = DepcApplication.getApplication().getRestAdapter().create(IZonas.class);
            call = request.getZonas(body);
            call.enqueue(new Callback<IZonas.dataBodega>() {
                @Override
                public void onResponse(Call<IZonas.dataBodega> call, Response<IZonas.dataBodega> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListarBodegas() != null) {
                                            if (data.getData().getListarBodegas().size() > 0) {


                                                final List<Zonas> bodegas;
                                                bodegas = data.getData().getListarBodegas().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteZonas(DepcApplication.getApplication().getZonasDao());
                                                    DepcApplication.getApplication().getZonasDao().callBatchTasks(new Callable<Zonas>() {
                                                        @Override
                                                        public Zonas call() throws Exception {
                                                            for (Zonas cl : bodegas) {
                                                                DataBaseHelper.saveZonas(cl, DepcApplication.getApplication().getZonasDao());
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
                        showListZonas();
                    }
                }

                @Override
                public void onFailure(Call<IZonas.dataBodega> call, Throwable t) {
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



    private void getPuntosVenta(){

        showProgressWait();

        //JSON SEND
        PuntosVentasModel model = new PuntosVentasModel();
        model.setUsuario_id(""+user.getUsuario());
        model.setMetodo("ListarPtoVenta");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IPuntosVentas request = DepcApplication.getApplication().getRestAdapter().create(IPuntosVentas.class);
            callPuntos = request.getPuntoVentas(body);
            callPuntos.enqueue(new Callback<IPuntosVentas.dataPuntos>() {
                @Override
                public void onResponse(Call<IPuntosVentas.dataPuntos> call, Response<IPuntosVentas.dataPuntos> response) {
                    if (response.isSuccessful()) {

                        dataPuntos = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataPuntos != null) {
                                if (dataPuntos.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataPuntos.getData() != null){
                                        if (dataPuntos.getData().getListarPuntos() != null) {
                                            if (dataPuntos.getData().getListarPuntos().size() > 0) {


                                                final List<PuntosVenta> puntosVentas;
                                                puntosVentas = dataPuntos.getData().getListarPuntos().get(0);

                                                if (puntosVentas != null) {
                                                    DataBaseHelper.deletePuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
                                                    DepcApplication.getApplication().getPuntosVentaDao().callBatchTasks(new Callable<PuntosVenta>() {
                                                        @Override
                                                        public PuntosVenta call() throws Exception {
                                                            for (PuntosVenta pto : puntosVentas) {
                                                                DataBaseHelper.savePuntosVenta(pto, DepcApplication.getApplication().getPuntosVentaDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showPuntosVentas();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataPuntos.getStatus_message() != null){
                                        mensajeError = dataPuntos.getStatus_message();
                                    }
                                }
                            }

                            showPuntosVentas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showPuntosVentas();
                        }

                    } else {
                        hideProgressWait();
                        showPuntosVentas();
                    }
                }

                @Override
                public void onFailure(Call<IPuntosVentas.dataPuntos> call, Throwable t) {
                    hideProgressWait();
                    showPuntosVentas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showPuntosVentas();

        }
    }

    private void showPuntosVentas(){

        hideProgressWait();

        try {

            puntosVentas = DataBaseHelper.getPuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
            if (puntosVentas != null) {
                if (puntosVentas.size() > 0) {
                    //aqui
                    PuntosVenta pt = puntosVentas.get(0);
                    getGabinets(pt.getPto_vta_id());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
        }
    }

    private void getGabinets(String idPuntoVenta){

        showProgressWait();

        //JSON SEND
        GabinetModel model = new GabinetModel();
        model.setPto_vta_id(""+idPuntoVenta);
        model.setMetodo("ListarGabinet");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IGabinet request = DepcApplication.getApplication().getRestAdapter().create(IGabinet.class);
            callGabinet = request.getGabinet(body);
            callGabinet.enqueue(new Callback<IGabinet.dataGabinet>() {
                @Override
                public void onResponse(Call<IGabinet.dataGabinet> call, Response<IGabinet.dataGabinet> response) {
                    if (response.isSuccessful()) {

                        dataGabinet = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataGabinet != null) {
                                if (dataGabinet.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataGabinet.getData() != null){
                                        if (dataGabinet.getData().getListarGabinet() != null) {
                                            if (dataGabinet.getData().getListarGabinet().size() > 0) {


                                                final List<Gabinet> gabinets;
                                                gabinets = dataGabinet.getData().getListarGabinet().get(0);

                                                if (gabinets != null) {
                                                    DataBaseHelper.deleteGabinet(DepcApplication.getApplication().getGabinetDao());
                                                    DepcApplication.getApplication().getGabinetDao().callBatchTasks(new Callable<Gabinet>() {
                                                        @Override
                                                        public Gabinet call() throws Exception {
                                                            for (Gabinet gab : gabinets) {
                                                                DataBaseHelper.saveGabinet(gab, DepcApplication.getApplication().getGabinetDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showListGabinet();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataGabinet.getStatus_message() != null){
                                        mensajeError = dataGabinet.getStatus_message();
                                    }
                                }
                            }

                            showListGabinet();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showListGabinet();
                        }

                    } else {
                        hideProgressWait();
                        showListGabinet();
                    }
                }

                @Override
                public void onFailure(Call<IGabinet.dataGabinet> call, Throwable t) {
                    hideProgressWait();
                    showListGabinet();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showListGabinet();

        }
    }

    private void showListGabinet(){

        hideProgressWait();

        try {

            gabinets = DataBaseHelper.getGabinet(DepcApplication.getApplication().getGabinetDao());
            if (gabinets != null) {
                if (gabinets.size() > 0) {




                    List<String> items= new ArrayList<String>();
                    items.add("SELECCIONE");
                    int index = 0;
                    int contador = 1;
                    for (Gabinet z : gabinets){

                        if (z.getCodigo() != null) {
                            items.add("" + z.getCodigo());
                        }else{
                            items.add("SIN CODIGO");
                        }

                        if (z.getCodigo() != null){
                            if (congeladorID.equals("" + z.getCodigo())) {
                                index = contador;
                            }
                        }
                        contador++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    spinnerCongelador.setAdapter(adapter);
                    spinnerCongelador.setSelection(index);
                    indexGabinets = index;
                    spinnerCongelador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            indexGabinets = position;
                            if (position == 0){
                                congeladorID = "null";
                            }else{
                                congeladorID = ""+items.get(position);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
        }
    }


    private void sendCrear(){

        try {
                    if(etDireccion.getText().length() > 0) {

                        String alertaMensaje = "Antes de continuar, por favor cense las coordenadas de la dirección indicada";
                        if (tvLat.getText().toString().length() == 0) {
                            showAlert(alertaMensaje);
                            return;
                        }

                        if (tvLon.getText().toString().length() == 0) {
                            showAlert(alertaMensaje);
                            return;
                        }

                        if (bitmap == null){
                            showAlert("NECESITA CARGAR UNA FOTO");
                            return;
                        }

                        /*if (congelador.getText().length() == 0){
                            congeladorID = "null";
                        }else{
                            congeladorID = congelador.getText().toString();
                        }*/

                        if (isActualizar){

                            direccion.setLatitud(tvLat.getText().toString());
                            direccion.setLongitud(tvLon.getText().toString());
                            direccion.setDireccion_envio(etDireccion.getText().toString().toUpperCase());
                            if (bitmap != null){
                                if (Utils.convertBase64String(bitmap) != null){
                                    direccion.setFoto(""+Utils.convertBase64String(bitmap));
                                }
                            }
                            direccion.setCliente_id(""+getIntent().getStringExtra("cliente"));
                            direccion.setCodigo(congeladorID);
                            //direccion.setDia_visita(""+dias.getText().toString());
                            direccion.setDia_visita(""+(indexSemana + 1 ));
                            direccion.setEmail_contacto(""+correo.getText().toString());
                            direccion.setExtension_contacto("");
                            direccion.setTelefono_contacto(""+celular.getText().toString());
                            direccion.setNombre_contacto(""+tvNombre.getText().toString());
                            direccion.setPais("ECUADOR");
                            direccion.setParroquia("");
                            direccion.setProvincia("");
                            direccion.setZona_id(""+zonas.get(indexZonas).getZona_id());;
                            DataBaseHelper.updateDirecciones(direccion, DepcApplication.getApplication().getDireccionesDao());


                        }else{


                            direccion.setLatitud(tvLat.getText().toString());
                            direccion.setLongitud(tvLon.getText().toString());
                            direccion.setDireccion_envio(etDireccion.getText().toString().toUpperCase());
                            if (bitmap != null){
                                if (Utils.convertBase64String(bitmap) != null){
                                    direccion.setFoto(""+Utils.convertBase64String(bitmap));
                                }
                            }
                            direccion.setCliente_id(""+getIntent().getStringExtra("cliente"));
                            direccion.setCodigo(congeladorID);
                            //direccion.setDia_visita(""+dias.getText().toString());
                            direccion.setDia_visita(""+(indexSemana + 1));
                            direccion.setEmail_contacto(""+correo.getText().toString());
                            direccion.setExtension_contacto("");
                            direccion.setTelefono_contacto(""+celular.getText().toString());
                            direccion.setNombre_contacto(""+tvNombre.getText().toString());
                            direccion.setPais("ECUADOR");
                            direccion.setParroquia("");
                            direccion.setId("");
                            direccion.setProvincia("");
                            direccion.setZona_id(""+zonas.get(indexZonas).getZona_id());;
                            DataBaseHelper.saveDirecciones(direccion, DepcApplication.getApplication().getDireccionesDao());
                        }


                        showProgressWait();


                        //JSON SEND
                        RegistrarDireccionClienteModel model = new RegistrarDireccionClienteModel();
                        model.setCliente_id(""+direccion.getCliente_id());
                        model.setCongelador_id(""+congeladorID);
                        model.setDias_visita(""+direccion.getDia_visita());
                        model.setDireccion_envio(""+direccion.getDireccion_envio());
                        model.setEmail(""+direccion.getEmail_contacto());
                        model.setFoto(""+direccion.getFoto());
                        model.setLatitud(""+direccion.getLatitud());
                        model.setLongitud(""+direccion.getLongitud());
                        model.setTelefono_contacto(""+direccion.getTelefono_contacto());
                        model.setZona_id(""+direccion.getZona_id());
                        model.setVendedor_id(""+user.getUsuario());
                        model.setDireccion_id("");
                        model.setMetodo("CrearClientesDirecciones");
                        if (isActualizar){
                            model.setDireccion_id(""+direccion.getId());
                            model.setMetodo("ActualizarClientesDirecciones");
                        }


                        final Gson gson = new Gson();
                        String json = gson.toJson(model);
                        Log.e("TAG---","json: "+json);

                        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

                        try {

                            ICrearDireccion request = DepcApplication.getApplication().getRestAdapter().create(ICrearDireccion.class);
                            callRegistro = request.getCrearDireccion(body);
                            callRegistro.enqueue(new Callback<ICrearDireccion.dataUsuario>() {
                                @Override
                                public void onResponse(Call<ICrearDireccion.dataUsuario> call, Response<ICrearDireccion.dataUsuario> response) {
                                    if (response.isSuccessful()) {

                                        dataRegistro = response.body();
                                        try {

                                            hideProgressWait();

                                            String mensajeError = Const.ERROR_DEFAULT;

                                            if (dataRegistro != null) {
                                                if (dataRegistro.getStatus() == Const.COD_ERROR_SUCCESS) {

                                                    new AlertDialog.Builder(MantDireccionActivity.this)
                                                            .setTitle(getResources().getString(R.string.app_name))
                                                            .setMessage("Dirección cliente creado con éxito")
                                                            .setCancelable(false)
                                                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.dismiss();
                                                                    finish();
                                                                }
                                                            })
                                                            .show();

                                                    return;
                                                }else{
                                                    if (dataRegistro.getStatus_message() != null){
                                                        mensajeError = dataRegistro.getStatus_message();
                                                    }
                                                }
                                            }

                                            showAlert(mensajeError);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            hideProgressWait();
                                            showAlert( Const.ERROR_DEFAULT);
                                        }

                                    } else {
                                        hideProgressWait();
                                        showAlert( Const.ERROR_DEFAULT);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ICrearDireccion.dataUsuario> call, Throwable t) {
                                    hideProgressWait();
                                    Log.e("Error",""+t.toString());
                                    showAlert(Const.ERROR_COBERTURA);

                                }
                            });

                        }catch (Exception e){
                            hideProgressWait();
                            showAlert( Const.ERROR_DEFAULT);

                        }

                    }else{
                        showAlert("INGRES NOMBRE DIRECCIÖN");
                    }




        } catch (SQLException e) {
            Toast.makeText(MantDireccionActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    @Override
    public void doRetry() {

    }

    private void getGabinetCliente(){

        showProgressWait();

        //JSON SEND
        ClientesGabinetModel model = new ClientesGabinetModel();
        model.setCliente_id(""+cliente.getCodigo_cliente_id());
        model.setMetodo("ListarGabinetCliente");
        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IClientesGabinet request = DepcApplication.getApplication().getRestAdapter().create(IClientesGabinet.class);
            callGabinetCliente = request.getClientesGabinet(body);
            callGabinetCliente.enqueue(new Callback<IClientesGabinet.dataGabinet>() {
                @Override
                public void onResponse(Call<IClientesGabinet.dataGabinet> call, Response<IClientesGabinet.dataGabinet> response) {
                    if (response.isSuccessful()) {

                        dataGabinetCliente = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (dataGabinetCliente.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataGabinetCliente.getData() != null){
                                        if (dataGabinetCliente.getData().getListarGabinetCliente() != null) {
                                            if (dataGabinetCliente.getData().getListarGabinetCliente().size() > 0) {

                                                final List<ClienteGabinet> gabinets;
                                                gabinets = dataGabinetCliente.getData().getListarGabinetCliente().get(0);

                                                if (gabinets != null) {

                                                    //DataBaseHelper.deleteClienteGabinetByCliente(DepcApplication.getApplication().getClienteGabinetDao(), ""+cliente.getCodigo_cliente_id());
                                                    for (ClienteGabinet gabinet : gabinets) {
                                                        gabinet.setId_cliente(""+cliente.getCodigo_cliente_id());
                                                        gabinet.setDireccion_cliente_gabinet("");
                                                        if (gabinet.getId_direccion_cliente() != null){
                                                            List<Direcciones> direcciones = DataBaseHelper.getDireccionesBYIdCliente(DepcApplication.getApplication().getDireccionesDao(),""+gabinet.getId_direccion_cliente());
                                                            if (direcciones != null){
                                                                if (direcciones.size() > 0){
                                                                    Direcciones dr = direcciones.get(0);
                                                                    gabinet.setDireccion_cliente_gabinet(""+dr.getDireccion_envio());
                                                                }
                                                            }


                                                            List<EstadoGabinet> estadoGabinets = DataBaseHelper.getEstadoGabinetByEstado(DepcApplication.getApplication().getEstadoGabinetDao(),""+gabinet.getEstado());
                                                            gabinet.setEstado_descripcion("");
                                                            if (estadoGabinets != null){
                                                                if (estadoGabinets.size() > 0){
                                                                    EstadoGabinet dr = estadoGabinets.get(0);
                                                                    gabinet.setEstado_descripcion(""+dr.getDescripcion());
                                                                }
                                                            }

                                                        }

                                                        DataBaseHelper.saveClienteGabinet(gabinet,DepcApplication.getApplication().getClienteGabinetDao());
                                                    }
                                                }

                                                fillData();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataGabinetCliente.getStatus_message() != null){
                                        mensajeError = dataGabinetCliente.getStatus_message();
                                    }
                                }
                            }

                            fillData();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            fillData();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        fillData();
                    }
                }

                @Override
                public void onFailure(Call<IClientesGabinet.dataGabinet> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    fillData();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            fillData();

        }
    }

    private void fillData(){

        try {
            gabinetsClientes = DataBaseHelper.getClienteGabinetByClienteAndDireccion(DepcApplication.getApplication().getClienteGabinetDao(),""+cliente.getCodigo_cliente_id(), ""+direccion.getId());

            if (gabinetsClientes != null){
                if (gabinetsClientes.size() > 0){
                    listaGabinet.setVisibility(View.VISIBLE);
                    lista_gabinets.setVisibility(View.VISIBLE);
                    listaGabinet.setAdapter(new ListaGabinetClienteAdapter(MantDireccionActivity.this,gabinetsClientes));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
