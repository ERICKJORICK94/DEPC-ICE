package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.Manifest;
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
import android.os.StrictMode;
import android.provider.MediaStore;
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
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.ScannerQr.ScanQrActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.ICrearDireccion;
import com.smartapp.depc_ice.Interface.ICrearDireccion;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.RegistrarDireccionClienteModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
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
    TextView tvCodCli, tvNombre, tvSecuencia, tvLat, tvLon, tvRuta, agregar,ver_foto;
    Button btActualizar;
    private Direcciones direccion;
    private View layout;
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
    private int indexZonas = -1;
    private List<Zonas> zonas;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private boolean isActualizar = false;
    private Clientes cliente;
    private TextView qr;
    private final static int MY_PERMISSIONS_REQUEST_CAMARA = 9991;
    private String congeladorID = "null";

    private Call<ICrearDireccion.dataUsuario> callRegistro;
    private ICrearDireccion.dataUsuario dataRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.activity_mant_direccio);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cliente = DepcApplication.getApplication().getCliente();

        ibTomarGPS = layout.findViewById(R.id.ib_tomar_gps);
        ibVerMapa = layout.findViewById(R.id.ib_ver_mapa);
        btActualizar = layout.findViewById(R.id.bt_actualizar);
        llLatLon = layout.findViewById(R.id.ll_lat_lon);
        llNOLatLon = layout.findViewById(R.id.ll_no_lat_lon);
        tvCodCli = layout.findViewById(R.id.tv_cliente);
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

                if (cliente.getDias_credito() != null){
                    dias.setText(""+cliente.getDias_credito());
                }

            }


        }
        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isActualizar){
                    showAlert(Const.EN_CONSTRUCCION);
                }else{
                    sendCrear();
                }


            }
        });

        ibVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( direccion == null){
                    Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
                    return;
                }

                if( direccion.getLatitud() == null){
                    Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
                    return;
                }

                if( direccion.getLongitud() == null){
                    Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
                    return;
                }

                    if( direccion.getLatitud().length() != 0 && direccion.getLongitud().length() != 0){
                                Intent intent = new Intent(MantDireccionActivity.this, MapsActivity.class);
                                intent.putExtra(Const.DETALLE_CLIENTE, direccion);
                                startActivity(intent);
                    }
                    else
                        Toast.makeText(MantDireccionActivity.this, "Dirección no está sensada aún.", Toast.LENGTH_LONG).show();
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
                                                                    Toast.makeText(MantDireccionActivity.this, "Senso exitoso.", Toast.LENGTH_LONG).show();
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
                                                                    Toast.makeText(MantDireccionActivity.this, "Senso exitoso.", Toast.LENGTH_LONG).show();
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

                checkCameraPermission();
            }
        });


        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPick();
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
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                }
        } catch (Exception e) {
            Toast.makeText(MantDireccionActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_CAMERA) {
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

                        direccion.setLatitud(tvLat.getText().toString());
                        direccion.setLongitud(tvLon.getText().toString());
                        direccion.setDireccion_envio(etDireccion.getText().toString().toUpperCase());
                        if (bitmap != null){
                            if (Utils.convertBase64String(bitmap) != null){
                                direccion.setFoto(""+Utils.convertBase64String(bitmap));
                            }
                        }
                        direccion.setCliente_id(""+getIntent().getStringExtra("cliente"));
                        direccion.setCongelador_id(congeladorID);
                        direccion.setDia_visita(""+dias.getText().toString());
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

                        showProgressWait();


                        //JSON SEND
                        RegistrarDireccionClienteModel model = new RegistrarDireccionClienteModel();
                        model.setCliente_id(""+direccion.getCliente_id());
                        model.setCongelador(""+direccion.getCongelador_id());
                        model.setDias_visita(""+direccion.getDia_visita());
                        model.setDireccion_envio(""+direccion.getDireccion_envio());
                        model.setEmail(""+direccion.getEmail_contacto());
                        model.setFoto(""+direccion.getFoto());
                        model.setLatitud(""+direccion.getLatitud());
                        model.setLongitud(""+direccion.getLongitud());
                        model.setTelefono_contacto(""+direccion.getTelefono_contacto());
                        model.setZona_id(""+direccion.getZona_id());
                        model.setMetodo("CrearClientesDirecciones");

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

}
