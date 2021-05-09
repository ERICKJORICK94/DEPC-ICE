package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RICHARD on 16/11/2017.
 */

public class DetalleClienteActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private static final int NUM_PAGES = 1;
    private ViewPager pager;
    private View layout;
    private LayoutInflater layoutInflater;
    private Clientes cliente;

    private TextView cliente_name;
    private TextView direccion;
    private TextView codigo;
    private LinearLayout perfil;
    private String isConsultar = "0";
    private Call<IClientes.dataClientes> call;
    private IClientes.dataClientes data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detail_cliente_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        if (getIntent() != null){
            cliente = (Clientes) getIntent().getSerializableExtra(Const.DETALLE_CLIENTE);
            isConsultar = getIntent().getStringExtra("consultar");
        }

        cliente_name = (TextView) layout.findViewById(R.id.cliente_name);
        direccion = (TextView) layout.findViewById(R.id.direccion);
        codigo = (TextView) layout.findViewById(R.id.codigo);
        perfil = (LinearLayout) layout.findViewById(R.id.perfil);

        if (cliente != null){

            cliente_name.setText(""+cliente.getNombre_comercial());
            direccion.setText(""+cliente.getDireccion());
            codigo.setText("COD: "+cliente.getCodigo_cliente_id());

            perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(DetalleClienteActivity.this, PerfilClienteActivity.class);
                    intent.putExtra(Const.DETALLE_CLIENTE, cliente);
                    startActivity(intent);*/
                }
            });

            if (isConsultar.equals("1")){
                getClientes();
            }else{
                showList();
            }

        }

    }

    private void showList(){

        List<Clientes> cls = null;
        try {
            cls = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), ""+cliente.getCodigo_cliente_id());
            if (cls != null){
                if (cls.size() > 0){
                    cliente = cls.get(0);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        pager = (ViewPager) layout.findViewById(R.id.pager);
        pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) layout.findViewById(R.id.tabs);
        tabsStrip.setAllCaps(true);
        tabsStrip.setShouldExpand(true);
        tabsStrip.setIndicatorColor(getResources().getColor(R.color.colorNaranja));
        tabsStrip.setViewPager(pager);

    }

    private void getClientes(){

        showProgressWait();

        //JSON SEND
        ClientesModel model = new ClientesModel();
        model.setCondicion("and a.codigo_cliente_id = "+cliente.getCodigo_cliente_id());
        model.setFiltro("limit "+Const.PARAM_MAX_ROW+" offset 0");
        model.setMetodo("ListaClientes");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);
        try {
            IClientes request = DepcApplication.getApplication().getRestAdapter().create(IClientes.class);
            call = request.getClientes(body);
            call.enqueue(new Callback<IClientes.dataClientes>() {
                @Override
                public void onResponse(Call<IClientes.dataClientes> call, Response<IClientes.dataClientes> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null)
                                        if (data.getData().getListaClientes() != null){
                                            if (data.getData().getListaClientes().size() > 0){

                                                final List<Clientes> clientes;
                                                clientes = data.getData().getListaClientes().get(0);

                                                if (clientes != null){
                                                    DepcApplication.getApplication().getClientesDao().callBatchTasks(new Callable<Clientes>() {
                                                        @Override
                                                        public Clientes call() throws Exception {

                                                            for (Clientes cl : clientes){


                                                                boolean isFlag = true;
                                                                if (cl.getNombre_comercial() != null){
                                                                    if (cl.getNombre_comercial().length() > 0) {
                                                                        if (cl.getNombre_tercero() == null) {
                                                                            cl.setNombre_tercero("" + cl.getNombre_comercial());
                                                                            isFlag = false;
                                                                        }else if (cl.getNombre_tercero().length() == 0) {
                                                                            cl.setNombre_tercero("" + cl.getNombre_comercial());
                                                                            isFlag = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (cl.getNombre_tercero() != null){
                                                                    if (cl.getNombre_tercero().length() > 0) {
                                                                        if (cl.getNombre_comercial() == null) {
                                                                            cl.setNombre_comercial("" + cl.getNombre_tercero());
                                                                            isFlag = false;
                                                                        }else if (cl.getNombre_comercial().length() == 0) {
                                                                            cl.setNombre_comercial("" + cl.getNombre_tercero());
                                                                            isFlag = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (cl.getNombre_comercial() != null && cl.getNombre_tercero() != null){
                                                                    isFlag = false;
                                                                }

                                                                if (isFlag) {
                                                                    cl.setNombre_tercero("SIN NOMBRE");
                                                                    cl.setNombre_comercial("SIN NOMBRE ASIGNADO");
                                                                }

                                                                DataBaseHelper.saveClientes(cl,DepcApplication.getApplication().getClientesDao());
                                                            }

                                                            return null;
                                                        }
                                                    });


                                                }

                                                showList();

                                                return;
                                            }
                                        }
                                }else{
                                    //Error
                                }
                            }
                            showList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            //showList();
                        }

                    } else {
                        showList();
                    }
                }

                @Override
                public void onFailure(Call<IClientes.dataClientes> call, Throwable t) {
                    showList();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showList();

        }

    }

    @Override
    public void doRetry() {

    }

    public Clientes getCliente() {
        return cliente;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[] { "GENERAL", "CARTERA"};

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new GereralFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

}
