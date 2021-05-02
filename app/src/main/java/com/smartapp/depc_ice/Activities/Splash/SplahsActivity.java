package com.smartapp.depc_ice.Activities.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Home.MainActivity;
import com.smartapp.depc_ice.Activities.Login.LoginActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.R;

import java.sql.SQLException;
import java.util.List;

public class SplahsActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private static int SPLASH_TIME_OUT = 900;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        try {

            List<Usuario> users =  DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (users != null){
                if (users.size() > 0) {
                    Intent intent = new Intent(SplahsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }

            //Intent intent = new Intent(SplahsActivity.this, LoginActivity.class);
            Intent intent = new Intent(SplahsActivity.this, MainActivity.class);
            startActivity(intent);

        } catch (SQLException e) {
            e.printStackTrace();
        }

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        getSupportActionBar().hide();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable,SPLASH_TIME_OUT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(handler != null) {
            handler.removeCallbacks(runnable);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(handler != null)
            handler.removeCallbacks(runnable);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(handler != null)
            handler.removeCallbacks(runnable);

    }

    @Override
    public void doRetry() {

    }
}
