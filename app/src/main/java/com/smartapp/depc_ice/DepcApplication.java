package com.smartapp.depc_ice;

import android.app.Application;
import android.os.StrictMode;
import com.j256.ormlite.dao.Dao;
import com.smartapp.depc_ice.Database.Database;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Utils.Const;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DepcApplication extends Application {

    private static DepcApplication instance;
    private Retrofit mRestAdapter;
    private Database databaseHelper = null;
    private Dao<Usuario, Integer> usuario = null;
    private static String latitud = "";
    private static String longitud = "";

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        instance = this;
        databaseHelper = new Database(this);

    }

    public Retrofit getRestAdapter() {
        return this.mRestAdapter;
    }

    public Dao<Usuario, Integer> getUsuarioDao() throws SQLException {
        if (usuario == null) {
            usuario = databaseHelper.getDao(Usuario.class);
        }
        return usuario;
    }

    public void setLatitud(String latitud){
        this.latitud =  latitud;
    }

    public String getLatitud(){

        if (latitud != null){
            return latitud;
        }
            return "";
    }

    public void setLongitud(String longitud){
        this.longitud = longitud;
    }

    public String getLongitud(){

        if (longitud != null){
            return longitud;
        }
        return "";
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static DepcApplication getApplication() {
        return instance;
    }
}

