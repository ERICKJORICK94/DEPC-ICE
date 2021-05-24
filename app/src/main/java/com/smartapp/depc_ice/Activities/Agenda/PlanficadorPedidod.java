package com.smartapp.depc_ice.Activities.Agenda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akexorcist.googledirection.BuildConfig;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.config.GoogleDirectionConfiguration;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smartapp.depc_ice.Activities.Agenda.Adapter.PlanificadorPedidoAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class PlanficadorPedidod extends BaseActitity implements OnMapReadyCallback, BaseActitity.BaseActivityCallbacks {

    private View layout;
    private String fecha;
    private GoogleMap mMap;
    private Marker startPerc = null;
    private ListView lista;

    private final String serverKey = "AIzaSyAg_dcLJ_XnK3aVtyBiGFTxaVe-XP6zPj0";
    private LatLng park = new LatLng(-2.128685, -79.89429666666666);
    private LatLng shopping = new LatLng(-2.09513578088982, -79.91638178353094);
    private LatLng dinner = new LatLng(-2.1078659777639466, -79.94509791683966);
    private LatLng gallery = new LatLng(-2.1298057177493757, -79.91702239587498);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.planificador_pedido_layout);
        Utils.SetStyleActionBarTitle(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        if (getIntent() != null) {
            if (getIntent().getStringExtra("fecha") != null) {
                fecha = getIntent().getStringExtra("fecha");
                initView();
            }
        }
    }

    private void initView() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lista = (ListView) layout.findViewById(R.id.lista);
        lista.setAdapter(new PlanificadorPedidoAdapter(this));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PlanficadorPedidod.this, DetallePlanificacionActivity.class);
                intent.putExtra("fecha",fecha);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationFromGPS();
        //requestDirection();
    }

    private void requestDirection() {
        GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
        GoogleDirection.withServerKey(serverKey)
                .from(park)
                .and(shopping)
                .and(dinner)
                .to(park)
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
    
    private void getLocationFromGPS(){
        if (    ContextCompat.checkSelfPermission(PlanficadorPedidod.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(PlanficadorPedidod.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You need to ask the user to enable the permissions
        } else {
            showProgressWait("Tomando Ubicación GPS");
            LocationTracker tracker = new LocationTracker(
                    PlanficadorPedidod.this,
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

                            /*startPerc = mMap.addMarker(new MarkerOptions()
                                    .position(coordinate)
                                    .title("Posición actual")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));*/

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 18.0f));
                            requestDirection();

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
                    mMap.addMarker(new MarkerOptions().position(leg.getStartLocation().getCoordination())).setTitle(""+index);
                    if (index == legCount - 1) {
                        mMap.addMarker(new MarkerOptions().position(leg.getEndLocation().getCoordination())).setTitle(""+index);
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
        showAlert(message);
    }

    @Override
    public void doRetry() {

    }
}
