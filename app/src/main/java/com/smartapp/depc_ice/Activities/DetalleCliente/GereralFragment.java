package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Home.Adapter.GridAdapter;
import com.smartapp.depc_ice.Activities.Home.MainActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Fragments.BaseFragment;
import com.smartapp.depc_ice.Interface.IEstadoGabinet;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.EstadoGabinetModel;
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
 * Created by RICHARD on 18/11/2017.
 */

public class GereralFragment extends BaseFragment implements BaseFragment.BaseFragmentCallbacks {

    private GridView grid;
    private LayoutInflater layoutInflater;
    private LinearLayout linear_header;
    private View layout;
    private Clientes cliente = null;
    private View header;
    private Call<IEstadoGabinet.dataBodega> call;
    private IEstadoGabinet.dataBodega data;
    private Usuario user;
    String[] opcionesMenu = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        layout = addLayout(R.layout.general_fragment);

        layoutInflater = LayoutInflater.from(getActivity());


        cliente = ((DetalleClienteActivity) getActivity()).getCliente();
        DepcApplication.getApplication().setCliente(cliente);

        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0) {
                    user = usuarios.get(0);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        header = layoutInflater.inflate(R.layout.header_general, null);
        grid = (GridView) layout.findViewById(R.id.lista);
        linear_header = (LinearLayout) layout.findViewById(R.id.linear_header);
        linear_header.addView(header);
        //String[] opcionesMenu = getResources().getStringArray(R.array.menu_cliente_array);
        int id_personal_operaciones = user.getId_personal_operaciones();

        if (id_personal_operaciones > 0) {
            opcionesMenu = getResources().getStringArray(R.array.menu_cliente_array_despachador);
        }else {
            opcionesMenu = getResources().getStringArray(R.array.menu_cliente_array_vendedor);
        }



        grid.setAdapter(new GridAdapter(getActivity(), opcionesMenu));

        showList();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String[] fields = opcionesMenu[position].split("[\t ]");
                    if (fields.length >= 5) {
                        if (fields[4].length() > 0) {
                            Intent intent = new Intent(getActivity(), Class.forName(fields[4]));
                            startActivity(intent);
                            return;
                        }
                    }

                    showAlert(Const.EN_CONSTRUCCION);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });


        getEstadosGabinet();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void getEstadosGabinet(){

        showProgressWait();

        //JSON SEND
        EstadoGabinetModel model = new EstadoGabinetModel();
        model.setMetodo("ListarEstadoGabinet");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IEstadoGabinet request = DepcApplication.getApplication().getRestAdapter().create(IEstadoGabinet.class);
            call = request.getZonas(body);
            call.enqueue(new Callback<IEstadoGabinet.dataBodega>() {
                @Override
                public void onResponse(Call<IEstadoGabinet.dataBodega> call, Response<IEstadoGabinet.dataBodega> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListarEstadoGabinet() != null) {
                                            if (data.getData().getListarEstadoGabinet().size() > 0) {


                                                final List<EstadoGabinet> bodegas;
                                                bodegas = data.getData().getListarEstadoGabinet().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteEstadoGabinet(DepcApplication.getApplication().getEstadoGabinetDao());
                                                    DepcApplication.getApplication().getEstadoGabinetDao().callBatchTasks(new Callable<EstadoGabinet>() {
                                                        @Override
                                                        public EstadoGabinet call() throws Exception {
                                                            for (EstadoGabinet cl : bodegas) {
                                                                DataBaseHelper.saveEstadoGabinet(cl, DepcApplication.getApplication().getEstadoGabinetDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                //showListZonas();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (data.getStatus_message() != null){
                                        mensajeError = data.getStatus_message();
                                    }
                                }
                            }

                            //showListZonas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            //showListZonas();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        //showListZonas();
                    }
                }

                @Override
                public void onFailure(Call<IEstadoGabinet.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    //showListZonas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            //showListZonas();

        }
    }



    private void showList() {

        if (header != null) {


            RoundCornerProgressBar progress = (RoundCornerProgressBar) header.findViewById(R.id.progress);
            TextView saldoText = (TextView) header.findViewById(R.id.saldo);
            TextView vencer = (TextView) header.findViewById(R.id.vencer);
            TextView vencido = (TextView) header.findViewById(R.id.vencido);
            TextView porcent = (TextView) header.findViewById(R.id.porcent);
            TextView limite = (TextView) header.findViewById(R.id.limite);
            TextView disponible = (TextView) header.findViewById(R.id.disponible);

            if (cliente.getMonto_credito() != null) {

                cliente.setMonto_credito(cliente.getMonto_credito().replace(",","."));
                String cupoString = cliente.getMonto_credito();

                if (Utils.isNumber(cupoString) || Utils.isNumberDecimal(cupoString)) {
                    float cupo = Float.parseFloat(cupoString);
                    limite.setText("L??mite de Cr??dito: " + (String.format("$ %.2f", cupo)));


                    saldoText.setText("Saldo: $ 0.00");
                    disponible.setText("Disponible: " +(String.format("%.2f", cupo)));
                    porcent.setText("25%");
                    //progress.setProgress(0);

                    /*if (cliente.getSALDO() != null) {
                        cliente.setSALDO(cliente.getSALDO().replace(",", "."));
                        String saldoString = cliente.getSALDO();
                        if (Utils.isNumber(saldoString) || Utils.isNumberDecimal(saldoString)) {
                            final float saldoa = Float.parseFloat(cliente.getSALDO());
                            saldoText.setText("Saldo: " + Utils.foramatearMiles(String.format("%.2f", saldoa)));
                            disponible.setText("Disponible: " + Utils.foramatearMiles(String.format("%.2f", (cupo - saldoa))));
                            float dis = (cupo - saldoa);
                            if (dis >= 0) {

                                float perc = saldoa / cupo * 100;
                                porcent.setText("" + String.format("%.2f", perc) + "%");
                                progress.setProgress(perc);
                            }else{
                                porcent.setText("");
                                progress.setProgress(0);
                            }
                        }

                    }*/
                }

            }


            String dias = "";
            String atrasado = "";
            if (cliente.getDias_credito() != null){
                dias = cliente.getDias_credito();
            }

            if (cliente.getDias_atrasados() != null){
                atrasado = cliente.getDias_atrasados();
            }

            vencer.setText("D??as cr??dito: "+dias);
            vencido.setText("D??as atrasados: "+atrasado);


        }



    }

    @Override
    public void doRetry() {

    }

    @Override
    public void cancelLoading() {

    }
}

