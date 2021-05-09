package com.smartapp.depc_ice.Mapa;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

public class MapsActivity extends BaseActitity implements OnMapReadyCallback, BaseActitity.BaseActivityCallbacks {

    private GoogleMap mMap;
    private Direcciones sincroDireccion;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        layout = addLayout(R.layout.activity_maps);
        Utils.SetStyleActionBarTitle(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        sincroDireccion = (Direcciones) getIntent().getSerializableExtra(Const.DETALLE_CLIENTE);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
            LatLng marker = new LatLng(Double.parseDouble(sincroDireccion.getLatitud()), Double.parseDouble(sincroDireccion.getLongitud()));
            mMap.addMarker(new MarkerOptions().position(marker).title(""+sincroDireccion.getDireccion_envio()).snippet(""));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15));


    }

    @Override
    public void doRetry() {

    }
}
