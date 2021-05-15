package com.smartapp.depc_ice.Activities.Agenda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.smartapp.depc_ice.Activities.Agenda.Adapter.PlanificadorPedidoAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class PlanficadorPedidod extends BaseActitity implements OnMapReadyCallback, BaseActitity.BaseActivityCallbacks {

    private View layout;
    private String fecha;
    private GoogleMap mMap;
    private Marker startPerc = null;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.planificador_pedido_layout);
        //Utils.SetStyleActionBarTitle(this);
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

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng marker = new LatLng(Double.parseDouble(sincroDireccion.getLatitud()), Double.parseDouble(sincroDireccion.getLongitud()));
        //mMap.addMarker(new MarkerOptions().position(marker).title(""+sincroDireccion.getDireccion_envio()).snippet(""));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));
        getLocationFromGPS();
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

    @Override
    public void doRetry() {

    }
}
