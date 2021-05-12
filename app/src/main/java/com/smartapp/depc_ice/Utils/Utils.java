package com.smartapp.depc_ice.Utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.R;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {

    public static final ImageLoader sImageLoader;
    private static ImageLoadingListener animateFirstListener;
    public static final String PREFERENCE_DEVICE = "preference_device";

    static {

        sImageLoader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DepcApplication.getApplication().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        sImageLoader.init(config);
    }

    public static String convertBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public static String getFechaHora(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(c.getTime());
    }

    public static void setListViewHeightBasedOnChildren2(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            //pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public static boolean getSinImpresora(Context ctx) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(Const.IS_SIN_IMPRESORA, false);
    }


    public static void setSinImpresora(Context ctx, boolean value){
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =      preferences.edit();
        editor.putBoolean(Const.IS_SIN_IMPRESORA, value);
        editor.commit();
    }

    public static boolean getZebra(Context ctx) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(Const.IS_ZEBRA, false);
    }


    public static void setZebra(Context ctx, boolean value){
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =      preferences.edit();
        editor.putBoolean(Const.IS_ZEBRA, value);
        editor.commit();
    }


    public static boolean getSeleccioImpresora(Context ctx) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(Const.IS_SELECCT, false);
    }


    public static void setSeleccioImpresora(Context ctx, boolean value){
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =      preferences.edit();
        editor.putBoolean(Const.IS_SELECCT, value);
        editor.commit();
    }


    public static void setImei(Context ctx, String value){
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =      preferences.edit();
        editor.putString(Const.IS_IMEI, value);
        editor.commit();
    }

    public static String getImei(Context ctx) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(Const.IS_IMEI, "");
    }

    public static int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date createdConvertedDate = null;
        Date expireCovertedDate = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar start = new GregorianCalendar();
        start.setTime(createdConvertedDate);

        Calendar end = new GregorianCalendar();
        end.setTime(expireCovertedDate);

        long diff = end.getTimeInMillis() - start.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);


        return (int) (dayCount);
    }

    public static void SetStyleActionBar(final AppCompatActivity act) {

        ActionBar actionBar = act.getSupportActionBar();

        View custom_bar = act.getLayoutInflater().inflate(R.layout.actionbar_custom, null);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(custom_bar,layout);
    }

    public static String getHour() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm");
        return dateFormatter.format(calendar.getTime());
    }


    public static void SetStyleActionBarTitle(final AppCompatActivity act) {

        ActionBar actionBar = act.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public static Boolean isBluetoothActivate(){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                return false;
            }else{
                return true;
            }
        }

    }

    public static String getFecha(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }

    public static void imageColor(ImageView image, int color){
        image.setColorFilter(color);

    }

    public static boolean isNumber(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isNumberDecimal(String text) {
        try {
            if(text.matches("\\d+(?:\\.\\d+)?")) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public static void setImage(String url, ImageView img){

        if (url != null) {
            if (animateFirstListener == null) {
                animateFirstListener = new AnimateFirstDisplayListener();
            }
            sImageLoader.displayImage(url, img, animateFirstListener);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            //pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 50;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

}
