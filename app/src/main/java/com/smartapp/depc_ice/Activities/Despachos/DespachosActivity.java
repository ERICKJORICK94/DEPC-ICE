package com.smartapp.depc_ice.Activities.Despachos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Coordination;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Agenda.DetallePlanificacionActivity;
import com.smartapp.depc_ice.Activities.Despachos.Adapter.PlanificadorDespachosAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.DetalleFacturas;
import com.smartapp.depc_ice.Entities.DetalleViaje;
import com.smartapp.depc_ice.Entities.EstadoFacturasDespacho;
import com.smartapp.depc_ice.Entities.ListarViajesDia;
import com.smartapp.depc_ice.Entities.PuntosVenta;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IEstadoFacturaDespacho;
import com.smartapp.depc_ice.Interface.IPuntosVentas;
import com.smartapp.depc_ice.Interface.IListarDepachoDia;
import com.smartapp.depc_ice.Interface.IVisitaPedidos;
import com.smartapp.depc_ice.Models.CordenadasModel;
import com.smartapp.depc_ice.Models.DespachoDiaModelModel;
import com.smartapp.depc_ice.Models.EstadoFacturaModel;
import com.smartapp.depc_ice.Models.PuntosVentasModel;
import com.smartapp.depc_ice.Models.VisitaPedidoModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DespachosActivity extends BaseActitity implements OnMapReadyCallback,BaseActitity.BaseActivityCallbacks{

    private View layout;
    private TextView fecha,lbl_fecha;
    private GoogleMap mMap;
    private ListView lista;
    private SlidingUpPanelLayout sliding_layout;
    public static  List<DetalleViaje> detalleViajes;
    private int indexViajes = 0;
    private String fechaBuscar = "";
    private Spinner spinner_ruta;
    private List<List<CordenadasModel>> direcciones = new ArrayList<List<CordenadasModel>>();
    private final String serverKey = "AIzaSyAg_dcLJ_XnK3aVtyBiGFTxaVe-XP6zPj0";
    private Call<IPuntosVentas.dataPuntos> callPuntos;
    private LatLng origen = new LatLng(-2.128685, -79.89429666666666);
    private IPuntosVentas.dataPuntos dataPuntos;
    private Usuario user;
    private PlanificadorDespachosAdapter listaDespachoAdapter;
    private String pto_vt_id = "";
    private List<EstadoFacturasDespacho> estadosFacturas;
    private Call<IListarDepachoDia.dataClientes> call;
    private IListarDepachoDia.dataClientes data;
    private Call<IEstadoFacturaDespacho.dataBodega> callEstados;
    private IEstadoFacturaDespacho.dataBodega dataEstados;
    private Date dateChosee;
    private boolean isInitMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.despachos_layout);
        Utils.SetStyleActionBarTitle(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaBuscar = sdf.format(c.getTime());


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

        validateData();

    }

    private void validateData(){

        try {

            boolean flagPuntos = true;
            List<PuntosVenta> puntos = DataBaseHelper.getPuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
            if (puntos != null){
                if (puntos.size() > 0){
                    flagPuntos = false;
                    PuntosVenta pto = puntos.get(0);
                    pto_vt_id = pto.getPto_vta_id();

                }
            }

            if (flagPuntos){
                getPuntosVenta();
            }else{

                boolean flagEstados = true;
                estadosFacturas = DataBaseHelper.getEstadoFacturasDespacho(DepcApplication.getApplication().getEstadoFacturasDespachoDao());
                if (estadosFacturas != null){
                    if (estadosFacturas.size() > 0){
                        flagEstados = false;
                    }
                }

                if (flagEstados){
                    getEstadoFacturaDespacho();
                }else{
                    getListDeaspachosDias();
                }


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    private void initView() {

        lista = (ListView) layout.findViewById(R.id.lista);
        spinner_ruta = (Spinner) layout.findViewById(R.id.spinner_ruta);
        lbl_fecha = (TextView) layout.findViewById(R.id.lbl_fecha);
        sliding_layout = (SlidingUpPanelLayout) layout.findViewById(R.id.sliding_layout);
        lbl_fecha.setText(""+fechaBuscar);
        fecha = (TextView) layout.findViewById(R.id.fecha);
        fecha.setText(""+fechaBuscar);
        lista.setAdapter(null);
        spinner_ruta.setAdapter(null);
        indexViajes = 0;
        direcciones.clear();

        try {

            hideProgressWait();
            List<ListarViajesDia> viajes = DataBaseHelper.getListarViajesDiaByDate(DepcApplication.getApplication().getListarViajesDiaDao(),fechaBuscar);
            if (viajes != null){
                if (viajes.size() > indexViajes){
                    List<String> items= new ArrayList<String>();
                    int cont = 1;
                    for (ListarViajesDia z : viajes){
                        items.add("RUTA "+cont);
                        cont++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    spinner_ruta.setAdapter(adapter);
                    spinner_ruta.setSelection(indexViajes);
                    spinner_ruta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            indexViajes = position;
                            selection(viajes.get(indexViajes));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                }else{
                    if (!isInitMap) {
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(this);
                    }else{
                        if (mMap != null){
                            mMap.clear();
                        }
                        getLocationFromGPS();
                    }
                }
            }else{
                if (!isInitMap) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                }else{
                    if (mMap != null){
                        mMap.clear();
                    }
                    getLocationFromGPS();
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void selection(ListarViajesDia viaje){

        try{
                detalleViajes = DataBaseHelper.getDetalleViajeByViaje(DepcApplication.getApplication().getDetalleViajeDao(),""+viaje.getId_viaje());
                if (detalleViajes != null){
                    if (detalleViajes.size() > 0){

                        int max = 22;
                        int cont = 0;

                        List<CordenadasModel> dir = null;

                        for (DetalleViaje cl : detalleViajes){
                                if (cl.getLatitud() != null && cl.getLongitud() != null) {

                                    double lat = Double.parseDouble(cl.getLatitud());
                                    double lon = Double.parseDouble(cl.getLongitud());
                                    LatLng cordenadas = new LatLng(lat, lon);
                                    if (cont == 0) {
                                        dir = new ArrayList<CordenadasModel>();
                                        direcciones.add(dir);
                                    }

                                    CordenadasModel cor = new CordenadasModel();
                                    cor.setCordenadas(cordenadas);
                                    cor.setNombre("" + cl.getDireccion_envio());
                                    dir.add(cor);

                                    if (cont < max) {
                                        cont++;
                                    } else {
                                        cont = 0;
                                    }

                                }

                        }

                        listaDespachoAdapter = new PlanificadorDespachosAdapter(this, detalleViajes);
                        lista.setAdapter(listaDespachoAdapter);

                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            Intent intent = new Intent(DespachosActivity.this, DetalleDespachosPlanificacionActivity.class);
                                            intent.putExtra("detalle_viaje",detalleViajes.get(position));
                                            startActivity(intent);

                            }
                        });

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                                }
                            }
                        }, 1000);



                    }
                }

            if (!isInitMap) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }else{
                if (mMap != null){
                    mMap.clear();
                }
                getLocationFromGPS();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //initView();

        if (DepcApplication.getApplication().isReload){

            if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }

            validateData();
        }

        DepcApplication.getApplication().isReload = false;

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
            callEstados = request.getBodegas(body);
            callEstados.enqueue(new Callback<IEstadoFacturaDespacho.dataBodega>() {
                @Override
                public void onResponse(Call<IEstadoFacturaDespacho.dataBodega> call, Response<IEstadoFacturaDespacho.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataEstados = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataEstados != null) {
                                if (dataEstados.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataEstados.getData() != null){
                                        if (dataEstados.getData().getListarEstadoFacturasDespacho() != null) {
                                            if (dataEstados.getData().getListarEstadoFacturasDespacho().size() > 0) {


                                                final List<EstadoFacturasDespacho> bodegas;
                                                bodegas = dataEstados.getData().getListarEstadoFacturasDespacho().get(0);

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
                                    if (dataEstados.getStatus_message() != null){
                                        mensajeError = dataEstados.getStatus_message();
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

    private void showEstadoFacturaDespacho() {

        try {

            boolean flagEstados = true;
            estadosFacturas = DataBaseHelper.getEstadoFacturasDespacho(DepcApplication.getApplication().getEstadoFacturasDespachoDao());
            if (estadosFacturas != null){
                if (estadosFacturas.size() > 0){
                    flagEstados = false;
                }
            }

            if (flagEstados){
                initView();
            }else{
                getListDeaspachosDias();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
                                                            int contador = 0;
                                                            for (PuntosVenta pto : puntosVentas) {
                                                                if (contador == 0) {
                                                                    pto_vt_id = pto.getPto_vta_id();
                                                                }
                                                                DataBaseHelper.savePuntosVenta(pto, DepcApplication.getApplication().getPuntosVentaDao());
                                                                contador++;
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

        try {

            boolean flagPuntos = true;
            List<PuntosVenta> puntos = DataBaseHelper.getPuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
            if (puntos != null){
                if (puntos.size() > 0){
                    flagPuntos = false;
                    PuntosVenta pto = puntos.get(0);
                    pto_vt_id = pto.getPto_vta_id();

                }
            }

            if (flagPuntos){
                initView();
            }else{

                boolean flagEstados = true;
                estadosFacturas = DataBaseHelper.getEstadoFacturasDespacho(DepcApplication.getApplication().getEstadoFacturasDespachoDao());
                if (estadosFacturas != null){
                    if (estadosFacturas.size() > 0){
                        flagEstados = false;
                    }
                }

                if (flagEstados){
                    getEstadoFacturaDespacho();
                }else{
                    getListDeaspachosDias();
                }


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void getListDeaspachosDias() {

        showProgressWait();

        //JSON SEND
        DespachoDiaModelModel model = new DespachoDiaModelModel();
        model.setFecha_inicio(""+fechaBuscar);
        model.setPto_vta_id(""+pto_vt_id);
        model.setUsuario_id(""+user.getUsuario());

        /*model.setFecha_inicio("2021-07-03");
        model.setPto_vta_id("1");
        model.setUsuario_id("33");*/

        model.setMetodo("ListarViajesDia");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---", "json: " + json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IListarDepachoDia request = DepcApplication.getApplication().getRestAdapter().create(IListarDepachoDia.class);
            call = request.getClientes(body);
            call.enqueue(new Callback<IListarDepachoDia.dataClientes>() {
                @Override
                public void onResponse(Call<IListarDepachoDia.dataClientes> call, Response<IListarDepachoDia.dataClientes> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            //hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null) {
                                        if (data.getData().getListarViajesDia() != null) {
                                            if (data.getData().getListarViajesDia().size() > 0) {


                                                final List<ListarViajesDia> viajes;
                                                viajes = data.getData().getListarViajesDia();

                                                if (viajes != null) {

                                                    DataBaseHelper.deleteListarViajesDia(DepcApplication.getApplication().getListarViajesDiaDao());
                                                    DataBaseHelper.deleteDetalleViaje(DepcApplication.getApplication().getDetalleViajeDao());
                                                    DataBaseHelper.deleteDetalleFacturas(DepcApplication.getApplication().getDetalleFacturasDao());

                                                    for (ListarViajesDia cl : viajes) {

                                                        DataBaseHelper.saveListarViajesDia(cl, DepcApplication.getApplication().getListarViajesDiaDao());

                                                        if (cl.getDetalleViaje() != null){
                                                            for (DetalleViaje detalleViaje : cl.getDetalleViaje() ) {
                                                                detalleViaje.setNombre_estado("");
                                                                if (estadosFacturas != null){
                                                                    for (EstadoFacturasDespacho est : estadosFacturas){
                                                                        if (detalleViaje.getEstado() != null){
                                                                            if (est.getNum_estado().equals(detalleViaje.getEstado())){
                                                                                detalleViaje.setNombre_estado(""+est.getDescripcion());
                                                                                break;
                                                                            }
                                                                        }
                                                                    }

                                                                }

                                                                DataBaseHelper.saveDetalleViaje(detalleViaje,DepcApplication.getApplication().getDetalleViajeDao());

                                                                if (detalleViaje.getDetalleFacturas() != null){
                                                                    DataBaseHelper.deleteDetalleFacturasByIDFactura(DepcApplication.getApplication().getDetalleFacturasDao(),""+detalleViaje.getFactura_id());
                                                                    for (DetalleFacturas detalleFactura : detalleViaje.getDetalleFacturas() ) {
                                                                        DataBaseHelper.saveDetalleFacturas(detalleFactura,DepcApplication.getApplication().getDetalleFacturasDao());

                                                                    }
                                                                }

                                                            }
                                                        }

                                                    }
                                                }

                                                initView();

                                                return;
                                            }
                                        }
                                    }
                                } else {
                                    if (data.getStatus_message() != null) {
                                        mensajeError = data.getStatus_message();
                                    }
                                }
                            }

                            initView();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            initView();
                        }

                    } else {
                        hideProgressWait();
                        initView();
                    }
                }

                @Override
                public void onFailure(Call<IListarDepachoDia.dataClientes> call, Throwable t) {
                    hideProgressWait();
                    Log.e(TAG,"error: "+t.toString());
                    initView();
                }
            });

        } catch (Exception e) {
            hideProgressWait();
            initView();

        }
    }


    @Override
    public void onBackPressed() {
        if (sliding_layout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else{
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationFromGPS();
        isInitMap = true;
        //requestDirection();
    }

    @Override
    public void doRetry() {

    }

    @SuppressLint("WrongConstant")
    private void requestDirection() {

        for (List<CordenadasModel> direction : direcciones){

            int cont = 0;
            List<LatLng> loations = new ArrayList<LatLng>();
            LatLng from = null;
            LatLng to = null;
            for (CordenadasModel dir : direction ){
                if (cont == 0){
                    if (origen != null){
                        from = origen;
                        origen = null;
                    }else {
                        from = dir.getCordenadas();
                    }
                }

                if (cont == (direction.size() - 1)){
                    to = dir.getCordenadas();
                }else {
                    loations.add(dir.getCordenadas());
                }
                cont++;
            }

            GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
            GoogleDirection.withServerKey(serverKey)
                    .from(from)
                    .and(loations)
                    .to(to)
                    .language(Language.SPANISH)
                    .transportMode(TransportMode.DRIVING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(@Nullable Direction direction) {
                            onDirectionSuccessPaint(direction);
                        }

                        @Override
                        public void onDirectionFailure(@NonNull Throwable t) {
                            onDirectionFailureError(t);
                        }
                    });

        }
    }

    private void getLocationFromGPS(){

        if (    ContextCompat.checkSelfPermission(DespachosActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(DespachosActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You need to ask the user to enable the permissions
        } else {
            showProgressWait("Tomando Ubicación GPS");
            LocationTracker tracker = new LocationTracker(
                    DespachosActivity.this,
                    new TrackerSettings()
                            .setUseGPS(true)
                            .setUseNetwork(true)
                            .setUsePassive(true)
                            .setTimeout(15000)
                            .setTimeBetweenUpdates(1000)
                            .setMetersBetweenUpdates(1)
            ) {

                @SuppressLint("MissingPermission")
                @Override
                public void onLocationFound(Location location) {

                    try{
                        String latitud = String.valueOf(location.getLatitude());
                        String longitud = String.valueOf(location.getLongitude());
                        if (!latitud.equals("") && !longitud.equals("")) {

                            mMap.setMyLocationEnabled(true);

                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                            LatLng coordinate = new LatLng(lat, lng);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 18.0f));
                            origen = new LatLng(lat, lng);
                            if (direcciones.size()  > 0) {
                                requestDirection();
                            }

                        } else {
                            hideProgressWait();
                            showAlert("Baja señal con la antena GPS, por favor intente moverse a un lugar despejado para mayor cobertura");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(PlanficadorPedidod.this, "Ocurrió un problema.", Toast.LENGTH_LONG).show();

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

    private void onDirectionSuccessPaint(Direction direction) {

        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            int legCount = route.getLegList().size();
            for (int index = 0; index < legCount; index ++) {
                Leg leg = route.getLegList().get(index);
                mMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination())).setTitle(""+leg.getStartAddress());
                if (index == legCount - 1) {
                    mMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination())).setTitle(""+leg.getEndAddress());
                }
                List<Step> stepList = leg.getStepList();
                ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(
                        this,
                        stepList,
                        5,
                        Color.RED,
                        3,
                        Color.BLUE
                );
                for (PolylineOptions polylineOption : polylineOptionList) {
                    mMap.addPolyline(polylineOption);
                }
            }
            setCameraWithCoordinationBounds(route);

        } else {
            showSnackbar(direction.getStatus());
        }
    }

    private void setCameraWithCoordinationBounds(Route route) {
        Coordination southwest = route.getBound(). getSouthwestCoordination();
        Coordination northeast = route.getBound().getNortheastCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest.getCoordination(),northeast.getCoordination());
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    private void onDirectionFailureError(Throwable t) {
        showSnackbar(t.getMessage());
    }



    private void showSnackbar(String message) {
        //showAlert(message);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.buscar, menu);

        /*MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Buscar");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG,"Buscar: "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    //adapter.filter("");
                    //listView.clearTextFilter();
                } else {
                    //adapter.filter(newText);
                    //Log.e(TAG,"Buscar: "+newText);
                }
                return true;
            }
        });*/

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_calendar:
                showCalendar();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(DespachosActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
                try {
                    dateChosee = format.parse(dayOfMonth+"/"+(monthOfYear + 1)+"/"+year);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateChosee);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    fechaBuscar = sdf.format(cal.getTime());
                    fecha.setText(""+fechaBuscar);
                    lbl_fecha.setText(""+fechaBuscar);
                    validateData();

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


}
