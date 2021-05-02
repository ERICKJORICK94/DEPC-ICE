package com.smartapp.depc_ice.Activities.Agenda;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.tabs.TabLayout;
import com.smartapp.depc_ice.Activities.Agenda.Adapter.TabAdapter;
import com.smartapp.depc_ice.Activities.Agenda.Fragments.CalendarioFragment;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.util.ArrayList;

public class AgendaActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;
    private LayoutInflater layoutInflater;

    private TabLayout tablayout;
    private ViewPager pager;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.agenda_activity_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        tablayout = (TabLayout) layout.findViewById(R.id.tablayout);
        pager = (ViewPager) layout.findViewById(R.id.pager);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new CalendarioFragment(), "Calendario");
        pager.setAdapter(adapter);
        tablayout.setupWithViewPager(pager);

    }


    @Override
    public void doRetry() {

    }

}


