package com.smartapp.depc_ice.Activities.AcercaDe;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class AcercaDeActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.acerca_de_layout);
        Utils.SetStyleActionBarTitle(this);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView) layout.findViewById(R.id.version)).setText("version instalada: "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doRetry() {

    }
}
