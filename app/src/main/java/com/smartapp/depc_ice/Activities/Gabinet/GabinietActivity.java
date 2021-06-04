package com.smartapp.depc_ice.Activities.Gabinet;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.Gabinet.Adapter.ListaGabinetAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

public class GabinietActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.gabinet_de_layout);
        Utils.SetStyleActionBarTitle(this);

        lista = (ListView) layout.findViewById(R.id.lista);
        lista.setAdapter(new ListaGabinetAdapter(this));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GabinietActivity.this, DetalleGabinetActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void doRetry() {

    }
}
