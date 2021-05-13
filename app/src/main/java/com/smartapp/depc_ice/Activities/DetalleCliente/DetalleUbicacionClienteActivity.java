package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IDirecciones;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Models.BodegasModel;
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

/**
 * Created by RICHARD on 16/11/2017.
 */

public class DetalleUbicacionClienteActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;
    private LayoutInflater layoutInflater;

    private TextView nombre;
    private TextView direccion;
    private TextView codigo;
    private TextView ruc;
    private TextView cedula;
    private TextView comentario;
    private ListView lista;
    private Button btCrearDireccion;
    List<Direcciones> listaEntrega;
    private Clientes cliente;
    private Call<IDirecciones.dataBodega> call;
    private IDirecciones.dataBodega data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_ubicacion_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cliente = DepcApplication.getApplication().getCliente();

        nombre = (TextView) layout.findViewById(R.id.nombre);
        direccion = (TextView) layout.findViewById(R.id.direccion);
        codigo = (TextView) layout.findViewById(R.id.codigo);
        ruc = (TextView) layout.findViewById(R.id.ruc);
        cedula = (TextView) layout.findViewById(R.id.cedula);
        comentario = (TextView) layout.findViewById(R.id.comentario);
        lista = (ListView) layout.findViewById(R.id.lista);
        btCrearDireccion = layout.findViewById(R.id.bt_crear_direccion);

        if (cliente != null){

            nombre.setText(""+cliente.getNombre_comercial());
            String dire = "";
            if (cliente.getDireccion() != null){
                dire = cliente.getDireccion();
            }
            direccion.setText("DIRECCIÓN: "+dire);
            String cod = "";
            if (cliente.getCodigo_cliente_id() != null){
                cod = cliente.getCodigo_cliente_id();
            }
            codigo.setText("CÓDIGO: "+cod);
            ruc.setText("RUC: "+cliente.getTercero_id());
            cedula.setText("CÉDULA: "+cliente.getTercero_id());
            comentario.setText("");


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        getDirecciones();
    }

    @Override
    public void doRetry() {

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
            call = request.getDirecciones(body);
            call.enqueue(new Callback<IDirecciones.dataBodega>() {
                @Override
                public void onResponse(Call<IDirecciones.dataBodega> call, Response<IDirecciones.dataBodega> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListaClientes() != null) {
                                            if (data.getData().getListaClientes().size() > 0) {


                                                final List<Direcciones> bodegas;
                                                bodegas = data.getData().getListaClientes().get(0);

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
                                    if (data.getStatus_message() != null){
                                        mensajeError = data.getStatus_message();
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

        try {
            if( DataBaseHelper.getDireccionesBYIdClienteDESC(DepcApplication.getApplication().getDireccionesDao(), ""+ cliente.getCodigo_cliente_id()) != null){
                listaEntrega = DataBaseHelper.getDireccionesBYIdClienteDESC(DepcApplication.getApplication().getDireccionesDao(), ""+ cliente.getCodigo_cliente_id());
                lista.setAdapter(new DireccionClientesAdapter(this, listaEntrega, cliente.getNombre_comercial()));

                Utils.setListViewHeightBasedOnChildren(lista);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        btCrearDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetalleUbicacionClienteActivity.this , MantDireccionActivity.class);
                intent.putExtra("parametro", "nuevo");
                intent.putExtra("nombre", cliente.getNombre_comercial());
                intent.putExtra("cliente", cliente.getCodigo_cliente_id());
                startActivity(intent);

            }
        });


    }
}
