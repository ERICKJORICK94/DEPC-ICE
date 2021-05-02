package com.smartapp.depc_ice.Activities.Novedades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

public class NovedadesActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.layout_novedades);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

    }

    @Override
    public void doRetry() {

    }
}
