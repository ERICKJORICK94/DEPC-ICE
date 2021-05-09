package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

import androidx.core.content.ContextCompat;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;
import java.util.ArrayList;
import java.util.List;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;


public class MantDireccionActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks   {
    List<String> desc = new ArrayList<String>();;
    List<String> id = new ArrayList<String>();;
    EditText etDireccion;
    ImageButton ibTomarGPS;
    TextView ibVerMapa;
    LinearLayout llLatLon, llNOLatLon, llRutaCiudad, llEscRutaCiudad;
    TextView tvCodCli, tvNombre, tvSecuencia, tvLat, tvLon, tvRuta, tvCiudad;
    Spinner spCiudad;
    Button btActualizar;
    private Direcciones direccion;
    //private List<Ciudades> ciudades;
    private View layout;
    private LayoutInflater layoutInflater;
    LocationListener mlocListener2;
    LocationManager mlocManager2;
    Direcciones temp;
    int currentCiudades = 0;
    String cod_ciudad = "";
    String cod_cliente = "";
    int idActulizar = 0;
    private LinearLayout btn_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.activity_mant_direccio);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        ibTomarGPS = layout.findViewById(R.id.ib_tomar_gps);
        ibVerMapa = layout.findViewById(R.id.ib_ver_mapa);
        btActualizar = layout.findViewById(R.id.bt_actualizar);
        llLatLon = layout.findViewById(R.id.ll_lat_lon);
        llNOLatLon = layout.findViewById(R.id.ll_no_lat_lon);
        llRutaCiudad = layout.findViewById(R.id.ll_ruta_ciudad);
        tvCodCli = layout.findViewById(R.id.tv_cliente);
        tvNombre = layout.findViewById(R.id.tv_nombre);
        tvSecuencia = layout.findViewById(R.id.tv_secuencia);
        tvLat = layout.findViewById(R.id.tv_lat);
        tvLon = layout.findViewById(R.id.tv_lon);
        tvRuta = layout.findViewById(R.id.tv_ruta);
        etDireccion = layout.findViewById(R.id.et_direccion);
        spCiudad = layout.findViewById(R.id.sp_ciudad);
        btn_whatsapp = layout.findViewById(R.id.btn_whatsapp);

        String parametro = getIntent().getStringExtra("parametro");
        String nombre = getIntent().getStringExtra("nombre");
        temp = new Direcciones();

        tvNombre.setText(nombre);
        tvCodCli.setText(getIntent().getStringExtra("cliente"));


        if (getIntent().getStringExtra("cliente") != null){
            cod_cliente = getIntent().getStringExtra("cliente");

        }

        if (getIntent().getStringExtra("cod_ciudad") != null){
            cod_ciudad = getIntent().getStringExtra("cod_ciudad");

        }


        if(parametro.equals("mant")) {
            direccion = (Direcciones) getIntent().getSerializableExtra(Const.DETALLE_DIRECCION);
            btActualizar.setText("ACTUALIZAR");
            //Joseph
            //Log.e("==>",direccion.toString());
            temp = direccion;

            if (temp.getLatitud().length() == 0 && temp.getLongitud().length() == 0) {
                llNOLatLon.setVisibility(View.VISIBLE);
                llLatLon.setVisibility(View.GONE);
            }

            //tvSecuencia.setText(temp.getSECUENCIA());

            tvLat.setText(temp.getLatitud());
            tvLon.setText(temp.getLongitud());
            etDireccion.setText(temp.getDireccion_envio());
            tvRuta.setText("");
            if (temp.getCiudad() != null) {
                for (int cont = 0; cont < id.size(); cont++) {
                    if (id.get(cont).equals(temp.getCiudad())) {
                        spCiudad.setSelection(cont);
                    }

                }
            }

        } else {
            //NUEVA DIRECCION
            direccion = new Direcciones();


        }
        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert(Const.EN_CONSTRUCCION);

                /*if(etDireccion.getText().length() > 3){
                    //temp.setSECUENCIA(tvSecuencia.getText().toString());
                    //temp.setCLIENTE(tvCodCli.getText().toString());
                    String alertaMensaje = "Antes de continuar, por favor cense las coordenadas de la dirección indicada";
                    if (tvLat.getText().toString().length() == 0){
                        showAlert(alertaMensaje);
                        return;
                    }

                    if (tvLon.getText().toString().length() == 0){
                        showAlert(alertaMensaje);
                        return;
                    }

                    temp.setLATITUD(tvLat.getText().toString());
                    temp.setLONGITUD(tvLon.getText().toString());
                    temp.setDIRECCION(etDireccion.getText().toString().toUpperCase());

                    if (ciudades == null){
                        showAlert("Por favor es necesario seleccionar una ciudad antes de conitnuar");
                        return;
                    }

                    if (ciudades.size() == 0){
                        showAlert("Por favor es necesario seleccionar una ciudad antes de conitnuar");
                        return;
                    }

                    //temp.setCODIGO_CIUDAD(id.get(spCiudad.getSelectedItemPosition()));
                    temp.setCODIGO_CIUDAD(ciudades.get(currentCiudades).getCODIGO());
                    //temp.setCODIGO_RUTA(""+cod_ruta);
                    temp.setESTADO_SINCRO("N");

                    try {
                        if(tvSecuencia.getText().toString().contains("NEW")){
                            temp.setCLIENTE(""+cod_cliente);
                            temp.setESTADO_SINCRO("C");
                            temp.setSECUENCIA(tvSecuencia.getText().toString());
                            idActulizar = DataBaseHelper.saveSincroDireccionUpdate(temp, San32Application.getApplication().getSincroDireccionDao());
                            temp.setId(idActulizar);
                            CrearDireccionTask edt = new CrearDireccionTask();
                            edt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else {
                            DataBaseHelper.updateSincroDireccion(temp, San32Application.getApplication().getSincroDireccionDao());
                            idActulizar = temp.getId();
                            EnvioDireccionesTask edt = new EnvioDireccionesTask();
                            edt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }

                        //Toast.makeText(MantDireccionActivity.this, "Dirección Grabada con éxito.", Toast.LENGTH_LONG).show();
                        //finish();

                    } catch (SQLException e) {
                        Toast.makeText(MantDireccionActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }*/

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





        /*try {
            ciudades = DataBaseHelper.getCiudades(San32Application.getApplication().getCiudadesDao());
            if (ciudades == null){
                CiudadesGetTask pt = new CiudadesGetTask();
                pt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }else {
                if (ciudades.size() == 0){
                    CiudadesGetTask pt = new CiudadesGetTask();
                    pt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }else{
                    showSpinner();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/



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




    }

    @Override
    public void doRetry() {

    }

}
