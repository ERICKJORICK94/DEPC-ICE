package com.smartapp.depc_ice.Activities.Agenda.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartapp.depc_ice.Fragments.BaseFragment;
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

        /*try {
            if (DataBaseHelper.getUsuario(San32Application.getApplication().getUsuarioDao()) != null){
                if (DataBaseHelper.getUsuario(San32Application.getApplication().getUsuarioDao()).size() > 0) {
                    Usuario user = DataBaseHelper.getUsuario(San32Application.getApplication().getUsuarioDao()).get(0);
                    vendedor.setText(""+user.getVendedorNombre());
                    cod_vendedor.setText(""+user.getVendedorCodigo());
                    codVendedor = ""+user.getVendedorCodigo();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


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
                    //getCobros(finalMes, finalYear);
                    showList();
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
                    //getCobros(finalMes, finalYear);
                    showList();
                }
                isFirst = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getCobros(finalMes,finalYear);

    }


    /*private void getCobros(final int mes, final int yearParam){

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

        if (Utils.isTablet(getActivity())){
            if (isFlag){
                DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(yearParam, mes,calendar.get(Calendar.DAY_OF_MONTH));
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String[] days = new String[7];
                for (int i = 0; i < 7; i++)
                {
                    days[i] = format1.format(calendar.getTime());
                    Log.e("Fechas:",""+days[i]);
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                desde = ""+days[0];
                hasta = ""+days[6];

                isFlag = false;
            }
        }


        ClienteVisitaModel model = new ClienteVisitaModel();
        SesionModel session = Utils.getSession();
        model.setParametrosSession(session);

        model.setSucursal(session.getCodigoSucursal());
        model.setBodega(session.getCodigoBodega());
        model.setCampos(Const.FIELDS_CLIENTES_VISITA);
        model.setModelo(Const.MODEL_CLIENTE_VISITA);
        String condicion = "FECHA BETWEEN '"+desde+"' AND '"+hasta+"'";
        model.setCondicion(condicion);
        model.setLimite("");
        model.setId("");
        model.setFiltro("VENDEDOR = '"+codVendedor+"'");
        model.setQrderBy("");
        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IClienteVisita request = San32Application.getApplication().getRestAdapter().create(IClienteVisita.class);
            call = request.getClienteVisita(body);
            call.enqueue(new Callback<IClienteVisita.dataClienteVisita>() {
                @Override
                public void onResponse(Call<IClienteVisita.dataClienteVisita> call, Response<IClienteVisita.dataClienteVisita> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            String mensajeError = Const.ERROR_DEFAULT;


                            if (data != null) {
                                if (data.getCodigoError().equals(Const.COD_ERROR_SUCCESS)) {
                                    if (data.getResultado() != null){
                                        if (data.getResultado().length() > 0){

                                            final ArrayList<ClientesVisitas> clientes;
                                            clientes = gson.fromJson(data.getResultado(), new TypeToken<ArrayList<ClientesVisitas>>() {}.getType());

                                            //DataBaseHelper.deleteClientesVisitas(San32Application.getApplication().getClientesVisitasDao());

                                            final List<Integer> ids = new ArrayList<Integer>();
                                            if (clientes != null){


                                                San32Application.getApplication().getClientesVisitasDao().callBatchTasks(new Callable<ClientesVisitas>() {
                                                    @Override
                                                    public ClientesVisitas call() throws Exception {
                                                        for (ClientesVisitas cl : clientes){

                                                            if(!validateDate(cl.getHORA_INICIO())){
                                                                cl.setHORA_INICIO("00:00");
                                                            }

                                                            if(!validateDate(cl.getHORA_FIN())){
                                                                cl.setHORA_FIN("00:00");
                                                            }

                                                            int id = DataBaseHelper.saveClientesVisitaByIDs(cl,San32Application.getApplication().getClientesVisitasDao());
                                                            ids.add(id);
                                                        }
                                                        return null;
                                                    }
                                                });

                                                if (!Utils.isTablet(getActivity())){
                                                    String month = ""+(mes+1);
                                                    if ((mes+1) <= 9){
                                                        month = "0"+(mes+1);
                                                    }


                                                    List<ClientesVisitas> cls = DataBaseHelper.getClienteVisitaByMes(San32Application.getApplication().getClientesVisitasDao(), month,""+yearParam);

                                                    if (cls != null) {
                                                        final List<Integer> idsBorrar = new ArrayList<Integer>();
                                                        for (ClientesVisitas cl : cls) {
                                                            String initial = cl.getCODIGO().substring(0,2);
                                                            if (!ids.contains(cl.getId()) && !initial.equals("CN") && cl.getPENDIENTE() == 0 && cl.getPENDIENTE_REAGENDAR() == 0 && cl.getIdAgendar() == 0){
                                                                idsBorrar.add(cl.getId());
                                                            }
                                                        }

                                                        San32Application.getApplication().getClientesVisitasDao().callBatchTasks(new Callable<Void>() {
                                                            @Override
                                                            public Void call() throws Exception {
                                                                for (int d : idsBorrar){
                                                                    DataBaseHelper.deleteClientesVisitas(San32Application.getApplication().getClientesVisitasDao(),d);
                                                                }
                                                                return null;
                                                            }
                                                        });


                                                    }
                                                }

                                            }


                                            hideProgressWait();
                                            showList();

                                            return;
                                        }
                                    }
                                }else{
                                    if (data.getMensajeError() != null){
                                        mensajeError = data.getMensajeError();
                                    }
                                }
                            }

                            //showAlert(mensajeError);
                            showList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            //showAlert(Const.ERROR_DEFAULT);
                            showList();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showList();
                    }
                }

                @Override
                public void onFailure(Call<IClienteVisita.dataClienteVisita> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showList();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showList();

        }
    }*/

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
        showList();

    }
}

