package com.smartapp.depc_ice.Activities.Agenda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Agenda.Adapter.PlanificadorPedidoAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IVisitaPedidos;
import com.smartapp.depc_ice.Models.CordenadasModel;
import com.smartapp.depc_ice.Models.VisitaPedidoModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class PlanficadorPedidosActivity extends BaseActitity implements OnMapReadyCallback, BaseActitity.BaseActivityCallbacks {

    private View layout;
    private String fecha;
    private int dia;
    private GoogleMap mMap;
    private Marker startPerc = null;
    private ListView lista;
    private TextView lbl_fecha;

    private final String serverKey = "AIzaSyAg_dcLJ_XnK3aVtyBiGFTxaVe-XP6zPj0";
    private LatLng origen = new LatLng(-2.128685, -79.89429666666666);
    private List<List<CordenadasModel>> direcciones = new ArrayList<List<CordenadasModel>>();
    private boolean flag  = true;
    private Call<IVisitaPedidos.dataClientes> call;
    private IVisitaPedidos.dataClientes data;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.planificador_pedido_layout);
        Utils.SetStyleActionBarTitle(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        Calendar calendar = Calendar.getInstance(Locale.US);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                dia = 1;
                break;
            case Calendar.TUESDAY:
                dia = 2;
                break;
            case Calendar.WEDNESDAY:
                dia = 3;
                break;
            case Calendar.THURSDAY:
                dia = 4;
                break;
            case Calendar.FRIDAY:
                dia = 5;
                break;
            case Calendar.SATURDAY:
                dia = 6;
                break;
            case Calendar.SUNDAY:
                dia = 7;
                break;
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

        fecha = Utils.getFecha();
        Log.e(TAG,"curret day = "+dia);
        getVisitaPedidos();



        if (getIntent() != null) {
            if (getIntent().getStringExtra("fecha") != null) {
                //fecha = getIntent().getStringExtra("fecha");
                //dia = getIntent().getStringExtra("dia");
                initView();
            }
        }
    }

    private void initView() {

        lista = (ListView) layout.findViewById(R.id.lista);
        lbl_fecha = (TextView) layout.findViewById(R.id.lbl_fecha);
        lbl_fecha.setText(""+fecha);

        try {
            List<ClientesVisitas> clientesVisitas = DataBaseHelper.getClienteVisitaDiaVisita(DepcApplication.getApplication().getClientesVisitasDao(),""+dia);
            if (clientesVisitas != null){
                if (clientesVisitas.size() > 0){

                    int max = 22;
                    int cont = 0;

                    List<CordenadasModel> dir = null;
                    for (ClientesVisitas cl : clientesVisitas){

                        if (cl.getLatitud() != null && cl.getLongitud() != null){

                            double lat = Double.parseDouble(cl.getLatitud());
                            double lon = Double.parseDouble(cl.getLongitud());
                            //LatLng cordenadas = new LatLng(lat,lon);
                            LatLng cordenadas = new LatLng(lon,lat);//ESTAN AL REVEZ

                            if (cont == 0){
                              dir = new ArrayList<CordenadasModel>();
                              direcciones.add(dir);
                            }

                            CordenadasModel cor = new CordenadasModel();
                            cor.setCordenadas(cordenadas);
                            cor.setNombre(""+cl.getDireccion());
                            dir.add(cor);

                            if (cont < max) {
                                cont++;
                            }else {
                                cont = 0;
                            }

                        }


                    }

                    lista.setAdapter(new PlanificadorPedidoAdapter(this, clientesVisitas));
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(PlanficadorPedidosActivity.this, DetallePlanificacionActivity.class);
                            intent.putExtra("fecha",fecha);
                            intent.putExtra("latitud",""+clientesVisitas.get(position).getLatitud());
                            intent.putExtra("longitud",""+clientesVisitas.get(position).getLongitud());
                            intent.putExtra("direccionRuta",""+clientesVisitas.get(position).getDireccion());
                            intent.putExtra("cliente_id",""+clientesVisitas.get(position).getCliente_id());
                            startActivity(intent);

                        }
                    });

                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getVisitaPedidos() {

        showProgressWait();


        //JSON SEND
        VisitaPedidoModel model = new VisitaPedidoModel();
        model.setCondicion("c.dia_visita IN (" + dia + ")  and e.usuario_id = " + user.getUsuario());
        model.setFiltro("");
        model.setMetodo("ListaClientesDireccionesVisita");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---", "json: " + json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IVisitaPedidos request = DepcApplication.getApplication().getRestAdapter().create(IVisitaPedidos.class);
            call = request.getVisitas(body);
            call.enqueue(new Callback<IVisitaPedidos.dataClientes>() {
                @Override
                public void onResponse(Call<IVisitaPedidos.dataClientes> call, Response<IVisitaPedidos.dataClientes> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            //hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null) {
                                        if (data.getData().getListaClientes() != null) {
                                            if (data.getData().getListaClientes().size() > 0) {


                                                final List<ClientesVisitas> clientesVisitas;
                                                clientesVisitas = data.getData().getListaClientes().get(0);

                                                if (clientesVisitas != null) {
                                                    DepcApplication.getApplication().getClientesVisitasDao().callBatchTasks(new Callable<ClientesVisitas>() {
                                                        @Override
                                                        public ClientesVisitas call() throws Exception {
                                                            for (ClientesVisitas cl : clientesVisitas) {
                                                                cl.setHora("00:00");
                                                                cl.setEstado("0");
                                                                cl.setFecha(Utils.getFecha());
                                                                DataBaseHelper.saveClientesVisitas(cl, DepcApplication.getApplication().getClientesVisitasDao());
                                                            }
                                                            return null;
                                                        }
                                                    });
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
                public void onFailure(Call<IVisitaPedidos.dataClientes> call, Throwable t) {
                    hideProgressWait();
                    initView();
                }
            });

        } catch (Exception e) {
            hideProgressWait();
            initView();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationFromGPS();
        //requestDirection();
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
        if (    ContextCompat.checkSelfPermission(PlanficadorPedidosActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(PlanficadorPedidosActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You need to ask the user to enable the permissions
        } else {
            showProgressWait("Tomando Ubicación GPS");
            LocationTracker tracker = new LocationTracker(
                    PlanficadorPedidosActivity.this,
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
    public void doRetry() {

    }
}
