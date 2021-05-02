package com.smartapp.depc_ice.Activities.General;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;
import java.util.Locale;

public class BaseActitity extends AppCompatActivity implements DialogInterface.OnCancelListener{

    public ProgressBar progressBar;
    public TextView txt_espere;
    public LinearLayout linear_process;
    public LinearLayout linear_container;
    private BaseActivityCallbacks callBack;
    public LayoutInflater inflater;
    public ProgressDialog progress;
    private TelephonyManager mTelephonyManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2983;
    boolean mBound = false;
    public String CodigoPadre = "";
    public static final String TAG = "DEPC-ICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_base);

        Locale locale = new Locale("us");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        callBack = (BaseActivityCallbacks)this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt_espere = (TextView) findViewById(R.id.txt_espere);
        linear_process = (LinearLayout) findViewById(R.id.linear_process);
        linear_container = (LinearLayout) findViewById(R.id.linear_container);

        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.Red),
                android.graphics.PorterDuff.Mode.SRC_IN);

        txt_espere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(callBack != null)
                        callBack.doRetry();

                }catch (Exception e){
                    Log.e(TAG, "Error: "+e.toString());
                }
            }
        });

        hideLoading();

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View addLayout(int id){
        inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(id, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linear_container.addView(view);

        return view;
    }


    public void showAlert(String mensaje){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Atención");
        alertDialog.setMessage(""+mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    public void showAlertHTML(String title, String mensaje){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(Html.fromHtml(mensaje));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void showLoading(){
        linear_process.setVisibility(View.VISIBLE);
        linear_container.setVisibility(View.GONE);
        txt_espere.setText("" + getResources().getString(R.string.lbl_procesando));
        txt_espere.setBackgroundColor(getResources().getColor(R.color.transparent));
        txt_espere.setPadding(0, 0, 0, 0);
        txt_espere.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void hideLoading(){
        linear_process.setVisibility(View.GONE);
        linear_container.setVisibility(View.VISIBLE);
    }

    public void setTextLoading(String title){
        linear_process.setVisibility(View.VISIBLE);
        linear_container.setVisibility(View.GONE);

        if(title !=  null){
            txt_espere.setText(""+title);
        }else {
            txt_espere.setText("");
        }

        txt_espere.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public void showRetry(){

        final float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (15 * scale + 0.5f);

        linear_process.setVisibility(View.VISIBLE);
        linear_container.setVisibility(View.GONE);
        txt_espere.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        txt_espere.setText("" + getResources().getString(R.string.lbl_reintentar));
        txt_espere.setBackgroundColor(getResources().getColor(R.color.transparent));
        txt_espere.setPadding(padding, padding, padding, padding);

    }


    public void showProgressWait() {

        progress = new ProgressDialog(this);
        progress.setMessage(getResources().getString(R.string.lbl_procesando));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setOnCancelListener(this);
        progress.show();

    }

    public void showProgressWait(String mensaje) {

        if (progress == null){
            progress = new ProgressDialog(this);
            progress.setMessage(mensaje);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setOnCancelListener(this);
        }

        progress.show();

    }

    public  void hideProgressWait() {

        if (progress != null){
            if (progress.isShowing()){
                progress.dismiss();;
            }
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    public static interface BaseActivityCallbacks {
        void doRetry();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.getImei(this).length() == 0){
            getUniqueIMEIId();
        }

        validate();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    void validate(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE,},
                    LOCATION_PERMISSION_REQUEST_CODE);

        } else  {
            validateGPS();
        }

    }

    void validateGPS(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(""+getResources().getString(R.string.lbl_active_gps))
                    .setCancelable(false)
                    .setPositiveButton(""+getResources().getString(R.string.lbl_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
        }else{
            Log.e(TAG,"EL GPS ESTA ACTIVADO ");
            if (getLocation()!= null ){
                Location location = getLocation();
                Log.e(TAG,"latitud----->>>> "+location.getLatitude());
                Log.e(TAG,"longitud----->>>> "+location.getLongitude());
            }
        }

    }

    public Location getLocation()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(bestProvider);
        try {
            return location;
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }


    public void getUniqueIMEIId() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        Const.PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getDeviceImei();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == Const.PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }else if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                validateGPS();

            } else {
                Toast.makeText(this, ""+getResources().getString(R.string.lbl_error_gps),
                        Toast.LENGTH_LONG).show();
            }
            return;

        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceImei() {

        String deviceid = "";
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        else
        {
            deviceid = telephonyManager.getDeviceId();
        }

        Utils.setImei(this, deviceid);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
