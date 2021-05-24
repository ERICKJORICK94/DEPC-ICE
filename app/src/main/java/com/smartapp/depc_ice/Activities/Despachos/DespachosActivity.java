package com.smartapp.depc_ice.Activities.Despachos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smartapp.depc_ice.Activities.Agenda.Adapter.PlanificadorPedidoAdapter;
import com.smartapp.depc_ice.Activities.Agenda.DetallePlanificacionActivity;
import com.smartapp.depc_ice.Activities.Agenda.PlanficadorPedidod;
import com.smartapp.depc_ice.Activities.Cobros.FormaPagoActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class DespachosActivity extends BaseActitity implements OnMapReadyCallback,BaseActitity.BaseActivityCallbacks{

    private View layout;
    private TextView fecha;
    private GoogleMap mMap;
    private ListView lista;

    private final String serverKey = "AIzaSyAg_dcLJ_XnK3aVtyBiGFTxaVe-XP6zPj0";
    private LatLng park = new LatLng(-2.128685, -79.89429666666666);
    private LatLng shopping = new LatLng(-2.09513578088982, -79.91638178353094);
    private LatLng dinner = new LatLng(-2.1078659777639466, -79.94509791683966);
    private LatLng gallery = new LatLng(-2.1298057177493757, -79.91702239587498);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.despachos_layout);
        Utils.SetStyleActionBarTitle(this);
        fecha = (TextView) layout.findViewById(R.id.fecha);
        fecha.setText(""+Utils.getFecha());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lista = (ListView) layout.findViewById(R.id.lista);
        lista.setAdapter(new PlanificadorPedidoAdapter(this));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Intent intent = new Intent(DespachosActivity.this, DetallePlanificacionActivity.class);
                intent.putExtra("fecha",fecha.getText());
                startActivity(intent);*/

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationFromGPS();
        //requestDirection();
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

    @Override
    public void doRetry() {

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
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.buscar, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
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
        });

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

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Locale locale = new Locale ( "es" , "ES" );
        Locale.setDefault(locale);
        DatePickerDialog datePickerDialog = new DatePickerDialog(DespachosActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
                try {
                    Date date = format.parse(dayOfMonth+"/"+monthOfYear+"/"+year);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    fecha.setText(sdf.format(cal.getTime()));
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
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "CANCELAR", datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePickerDialog);
        datePickerDialog.show();

    }


}
