package com.smartapp.depc_ice.Activities.Clientes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Clientes.Adapter.ClientesAdapter;
import com.smartapp.depc_ice.Activities.DetalleCliente.DetalleClienteActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
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

public class ClientesActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;
    private LayoutInflater layoutInflater;
    private SearchView search;
    private ListView lista;
    private TextView num_cliente;
    private Call<IClientes.dataClientes> call;
    private IClientes.dataClientes data;
    private boolean isSearch = false;
    private String condition = "";
    private String buscar = "";
    private List<Clientes> clientesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.clientes_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        search = (SearchView) layout.findViewById(R.id.search);
        lista = (ListView) layout.findViewById(R.id.lista);
        num_cliente = (TextView) layout.findViewById(R.id.num_cliente);
        num_cliente.setText("");
        num_cliente.setVisibility(View.GONE);

        search.setActivated(true);
        search.setQueryHint("Buscar cliente...");
        search.onActionViewExpanded();
        search.setIconified(false);
        search.clearFocus();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                num_cliente.setText("");
                num_cliente.setVisibility(View.GONE);
                lista.setAdapter(null);
                condition(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0){
                    showList();
                }else{
                    num_cliente.setText("");
                    num_cliente.setVisibility(View.GONE);
                    lista.setAdapter(null);
                }
                if (newText.length() >= 3) {
                   // condition(newText);
                }
                return false;
            }
        });

        getClientes("");


    }

    private void condition(String search){
        isSearch = true;
        search.trim();
        buscar = search;
        search = search.replace(" ","%").toUpperCase();
        condition = "and (b.nombre_tercero LIKE '%"+search+"%' or b.nombre_comercial LIKE '%"+search+"%' or b.direccion LIKE '%"+search+"%' or a.codigo_cliente_id LIKE '%"+search+"%')";
        getClientes(condition);
    }

    private void getClientes(String search){

        showProgressWait();

        int limit = Const.PARAM_MAX_ROW;
        num_cliente.setText("");
        num_cliente.setVisibility(View.GONE);
        String codigoCLiente = "";
        try {
            List<Usuario> usuario = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuario != null){
                if (usuario.size() > 0){
                    Usuario user = usuario.get(0);
                    codigoCLiente = user.getUsuario();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String buscar = "and d.usuario_id="+codigoCLiente;
        if (search.length() > 0){
            buscar = buscar+" "+search;
        }
        //JSON SEND
        ClientesModel model = new ClientesModel();
        model.setCondicion(buscar);
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
                            showList();
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

    private void showList(){

        hideProgressWait();
        try {
                clientesList = null;
                if (isSearch) {
                    String nuevaCondicion = "nombre_tercero LIKE '%"+buscar+"%' or nombre_comercial LIKE '%"+buscar+"%' or direccion LIKE '%"+buscar+"%' or codigo_cliente_id LIKE '%"+buscar+"%'";
                    clientesList = DataBaseHelper.getClientesSearch(DepcApplication.getApplication().getClientesDao(), "" + nuevaCondicion);
                }else{
                    clientesList = DataBaseHelper.getClientes(DepcApplication.getApplication().getClientesDao());
                }

                if (clientesList != null) {
                    if (clientesList.size() > 0) {

                        lista.setAdapter(new ClientesAdapter(ClientesActivity.this, clientesList));
                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(ClientesActivity.this, DetalleClienteActivity.class);
                                intent.putExtra(Const.DETALLE_CLIENTE, clientesList.get(position));
                                intent.putExtra("consultar", "0");
                                startActivity(intent);
                            }
                        });

                        num_cliente.setVisibility(View.VISIBLE);
                        if (clientesList.size() == 1) {
                            num_cliente.setText("" + clientesList.size() + " cliente encontrado");
                        } else {
                            num_cliente.setText("" + clientesList.size() + " clientes encontrados");
                        }
                    }else{
                        String error = Const.ERROR_DEFAULT;
                        if (isSearch){ error = Const.ERROR_NO_RESULT; }
                        showAlert(error);
                    }
                }else{
                    String error = Const.ERROR_DEFAULT;
                    if (isSearch){ error = Const.ERROR_NO_RESULT; }
                    showAlert(error);
                }

            isSearch = false;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Const.ERROR_DEFAULT);
        }

    }


    @Override
    public void doRetry() {

    }
}
