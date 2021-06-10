package com.smartapp.depc_ice.Activities.Gabinet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Gabinet.Adapter.ListaGabinetAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Interface.IClientesGabinet;
import com.smartapp.depc_ice.Interface.IDirecciones;
import com.smartapp.depc_ice.Models.ClientesGabinetModel;
import com.smartapp.depc_ice.Models.DireccionesModel;
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

public class GabinietActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private ListView lista;
    private Clientes cliente;
    private Call<IClientesGabinet.dataGabinet> call;
    private IClientesGabinet.dataGabinet data;
    private Call<IDirecciones.dataBodega> callDirecciones;
    private IDirecciones.dataBodega dataDirecciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.gabinet_de_layout);
        Utils.SetStyleActionBarTitle(this);

        cliente = DepcApplication.getApplication().getCliente();

        lista = (ListView) layout.findViewById(R.id.lista);


    }

    private void fillData(){

        if (cliente != null) {
            try {
                if(DataBaseHelper.getClienteGabinetByCliente(DepcApplication.getApplication().getClienteGabinetDao(), ""+cliente.getCodigo_cliente_id()) != null){
                    final List<ClienteGabinet> gabinets = DataBaseHelper.getClienteGabinetByCliente(DepcApplication.getApplication().getClienteGabinetDao(), ""+cliente.getCodigo_cliente_id());
                    lista.setAdapter(new ListaGabinetAdapter(this,gabinets));
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(GabinietActivity.this, DetalleGabinetActivity.class);
                            intent.putExtra("cliente_gabinet_id",""+gabinets.get(position).getId());
                            startActivity(intent);


                        }
                    });
                }else{
                    showAlert(Const.ERROR_DEFAULT);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        getDirecciones();
        //getGabinetCliente();
    }

    private void getDirecciones(){

        showProgressWait();

        //JSON SEND
        DireccionesModel model = new DireccionesModel();
        model.setCondicion(""+cliente.getCodigo_cliente_id());
        model.setFiltro("");
        model.setMetodo("ListaClientesDirecciones");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IDirecciones request = DepcApplication.getApplication().getRestAdapter().create(IDirecciones.class);
            callDirecciones = request.getDirecciones(body);
            callDirecciones.enqueue(new Callback<IDirecciones.dataBodega>() {
                @Override
                public void onResponse(Call<IDirecciones.dataBodega> call, Response<IDirecciones.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataDirecciones = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataDirecciones != null) {
                                if (dataDirecciones.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataDirecciones.getData() != null){
                                        if (dataDirecciones.getData().getListaClientes() != null) {
                                            if (dataDirecciones.getData().getListaClientes().size() > 0) {


                                                final List<Direcciones> bodegas;
                                                bodegas = dataDirecciones.getData().getListaClientes().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteDireccionesById(DepcApplication.getApplication().getDireccionesDao(),""+cliente.getCodigo_cliente_id());
                                                    DepcApplication.getApplication().getDireccionesDao().callBatchTasks(new Callable<Direcciones>() {
                                                        @Override
                                                        public Direcciones call() throws Exception {
                                                            for (Direcciones cl : bodegas) {
                                                                DataBaseHelper.saveDirecciones(cl, DepcApplication.getApplication().getDireccionesDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showLit();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataDirecciones.getStatus_message() != null){
                                        mensajeError = dataDirecciones.getStatus_message();
                                    }
                                }
                            }

                            showLit();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showLit();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showLit();
                    }
                }

                @Override
                public void onFailure(Call<IDirecciones.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showLit();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showLit();

        }
    }

    private void showLit(){
        hideLoading();
        getGabinetCliente();
    }

    private void getGabinetCliente(){

        showProgressWait();

        //JSON SEND
        ClientesGabinetModel model = new ClientesGabinetModel();
        model.setCliente_id(""+cliente.getCodigo_cliente_id());
        model.setMetodo("ListarGabinetCliente");
        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IClientesGabinet request = DepcApplication.getApplication().getRestAdapter().create(IClientesGabinet.class);
            call = request.getClientesGabinet(body);
            call.enqueue(new Callback<IClientesGabinet.dataGabinet>() {
                @Override
                public void onResponse(Call<IClientesGabinet.dataGabinet> call, Response<IClientesGabinet.dataGabinet> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListarGabinetCliente() != null) {
                                            if (data.getData().getListarGabinetCliente().size() > 0) {

                                                final List<ClienteGabinet> gabinets;
                                                gabinets = data.getData().getListarGabinetCliente().get(0);

                                                if (gabinets != null) {

                                                    //DataBaseHelper.deleteClienteGabinetByCliente(DepcApplication.getApplication().getClienteGabinetDao(), ""+cliente.getCodigo_cliente_id());
                                                    for (ClienteGabinet gabinet : gabinets) {
                                                        gabinet.setId_cliente(""+cliente.getCodigo_cliente_id());
                                                        gabinet.setDireccion_cliente_gabinet("");
                                                        if (gabinet.getId_direccion_cliente() != null){
                                                            List<Direcciones> direcciones = DataBaseHelper.getDireccionesBYIdCliente(DepcApplication.getApplication().getDireccionesDao(),""+gabinet.getId_direccion_cliente());
                                                            if (direcciones != null){
                                                                if (direcciones.size() > 0){
                                                                    Direcciones dr = direcciones.get(0);
                                                                    gabinet.setDireccion_cliente_gabinet(""+dr.getDireccion_envio());
                                                                }
                                                            }

                                                        }

                                                       DataBaseHelper.saveClienteGabinet(gabinet,DepcApplication.getApplication().getClienteGabinetDao());
                                                    }
                                                }

                                                fillData();

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

                            fillData();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            fillData();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        fillData();
                    }
                }

                @Override
                public void onFailure(Call<IClientesGabinet.dataGabinet> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    fillData();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            fillData();

        }
    }

    @Override
    public void doRetry() {

    }
}
