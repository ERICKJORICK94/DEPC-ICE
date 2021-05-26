package com.smartapp.depc_ice.Activities.Agenda.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Fragments.BaseFragment;
import com.smartapp.depc_ice.Interface.IVisitaPedidos;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.VisitaPedidoModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.CalendarCustomView;
import com.smartapp.depc_ice.Utils.Const;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarioFragment extends BaseFragment implements BaseFragment.BaseFragmentCallbacks {

    private View layout;
    private CalendarCustomView calendView;
    private TextView vendedor;
    private TextView cod_vendedor;
    private Spinner spinner_mes;
    private Spinner spinner_anio;
    String[] MONTHS = new String[] {"ENERO", "FEBRERO", "MARZO","ABRIL", "MAYO", "JUNIO","JULIO", "AGOSTO", "SEPTIEMBRE","OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
    String[] YEARS = new String[] {"2015", "2016", "2017","2018", "2019", "2020","2021","2022","2023","2024"};
    int finalMes = 0;
    int finalYear = 0;
    boolean isFirst = false;
    String codVendedor = "";
    boolean isFlag =  true;
    int currentMes  = 0;
    private Usuario user;

    private Call<IVisitaPedidos.dataClientes> call;
    private IVisitaPedidos.dataClientes data;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        layout = addLayout(R.layout.calendario_layout);
        isFirst = false;

        spinner_mes = (Spinner) layout.findViewById(R.id.spinner_mes);
        spinner_anio = (Spinner) layout.findViewById(R.id.spinner_anio);
        calendView = (CalendarCustomView) layout.findViewById(R.id.custom_calendar);
        vendedor = (TextView) layout.findViewById(R.id.vendedor);
        cod_vendedor = (TextView) layout.findViewById(R.id.cod_vendedor);

        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0){
                    user = usuarios.get(0);
                    if (user.getNombrescompletos() != null){
                        vendedor.setText(""+user.getNombrescompletos());
                    }

                    if (user.getUsuario() != null){
                        cod_vendedor.setText(""+user.getUsuario());
                    }
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year =  calendar.get(Calendar.YEAR);
        finalMes = month;
        finalYear = year;
        currentMes = month;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, MONTHS);
        spinner_mes.setAdapter(adapter);
        spinner_mes.setSelection(month);
        spinner_mes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    finalMes = position;
                    getVisitaPedidos(finalMes, finalYear);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, YEARS);
        spinner_anio.setAdapter(adapter2);

        int contador = 0;
        for (String y : YEARS){

            if (y.equals(""+year)){
                break;
            }
            contador++;
        }
        spinner_anio.setSelection(contador);
        spinner_anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    finalYear = Integer.parseInt(YEARS[position].trim());
                    getVisitaPedidos(finalMes, finalYear);
                }
                isFirst = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getVisitaPedidos(finalMes,finalYear);

    }


    private void getVisitaPedidos(final int mes, final int yearParam){

        showProgressWait();
        Calendar cal = Calendar.getInstance();
        cal.set(yearParam, mes, 1);
        int MAX_DAY = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        final Calendar c1 = Calendar.getInstance();
        c1.set(yearParam, mes, 1);
        Calendar c2 = Calendar.getInstance();
        c2.set(yearParam, mes, MAX_DAY);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String desde = ""+format.format(c1.getTime());
        String hasta = ""+format.format(c2.getTime());

        //JSON SEND
        VisitaPedidoModel model = new VisitaPedidoModel();
        model.setCondicion("c.dia_visita IN (1,2,3,4,5,6)  and e.usuario_id = "+user.getUsuario());
        model.setFiltro("");
        model.setMetodo("ListaClientesDireccionesVisita");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IVisitaPedidos request = DepcApplication.getApplication().getRestAdapter().create(IVisitaPedidos.class);
            call = request.getVisitas(body);
            call.enqueue(new Callback<IVisitaPedidos.dataClientes>() {
                @Override
                public void onResponse(Call<IVisitaPedidos.dataClientes> call, Response<IVisitaPedidos.dataClientes> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            //hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListaClientes() != null) {
                                            if (data.getData().getListaClientes().size() > 0) {


                                                final List<ClientesVisitas> clientesVisitas;
                                                clientesVisitas = data.getData().getListaClientes().get(0);

                                                if (clientesVisitas != null) {
                                                    DepcApplication.getApplication().getClientesVisitasDao().callBatchTasks(new Callable<ClientesVisitas>() {
                                                        @Override
                                                        public ClientesVisitas call() throws Exception {
                                                            for (ClientesVisitas cl : clientesVisitas) {
                                                                DataBaseHelper.saveClientesVisitas(cl, DepcApplication.getApplication().getClientesVisitasDao());
                                                            }
                                                            return null;
                                                        }
                                                    });
                                                }

                                                showList();

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

                            showList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showList();
                        }

                    } else {
                        hideProgressWait();
                        showList();
                    }
                }

                @Override
                public void onFailure(Call<IVisitaPedidos.dataClientes> call, Throwable t) {
                    hideProgressWait();
                    showList();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showList();

        }




    }

    private boolean validateDate(String date){
        boolean retorno = false;

        if (date == null){
            return false;
        }

        if(!date.matches("(?:[0-1][0-9]|2[0-4]):[0-5]\\d")){
            retorno = false;
        }else{
            retorno = true;
        }

        return retorno;
    }


    private void showList(){

        try {
            hideProgressWait();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    calendView.setUpCalendarAdapter(finalMes+1, finalYear);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Const.ERROR_DEFAULT);
        }

    }




    @Override
    public void doRetry() {

    }

    @Override
    public void cancelLoading() {

    }


    @Override
    public void onResume() {
        super.onResume();
        //showList();

    }
}

