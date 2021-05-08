package com.smartapp.depc_ice.Activities.Despachos;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.lang.reflect.Field;
import java.util.List;

public class DespachosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private TextView num_rutas;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.despachos_layout);
        Utils.SetStyleActionBarTitle(this);
        num_rutas = (TextView) layout.findViewById(R.id.num_rutas);
        lista = (ListView) layout.findViewById(R.id.lista);

    }

    @Override
    public void doRetry() {

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
}
