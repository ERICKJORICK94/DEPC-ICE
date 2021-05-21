package com.smartapp.depc_ice.Activities.Pedido;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Pedido.Adapter.ListaPedidosAdapter;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IPedido;
import com.smartapp.depc_ice.Interface.IRemoverPedido;
import com.smartapp.depc_ice.Interface.IConsultaPedido;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.ConsultasPedidosModel;
import com.smartapp.depc_ice.Models.ListarProformas;
import com.smartapp.depc_ice.Models.RemoverPedidoModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaPredidosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private Clientes cliente;
    private ListView lista;
    private Call<IConsultaPedido.dataPedido> call;
    private IConsultaPedido.dataPedido data;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.listado_pedido_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        lista = (ListView) layout.findViewById(R.id.lista);
        cliente = DepcApplication.getApplication().getCliente();

        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0){
                    user = usuarios.get(0);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    private void fillData(){

        if (cliente != null) {
            try {
                if(DataBaseHelper.getPedidosByCliente(DepcApplication.getApplication().getPedidosDao(), cliente.getCodigo_cliente_id()) != null){
                    final List<Pedidos> pedidos = DataBaseHelper.getPedidosByCliente(DepcApplication.getApplication().getPedidosDao(), cliente.getCodigo_cliente_id());
                    Collections.reverse(pedidos);
                    lista.setAdapter(new ListaPedidosAdapter(ListaPredidosActivity.this, pedidos));

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(ListaPredidosActivity.this, DetallePedidoActivity.class);
                            intent.putExtra(Const.ID_PEDIDOS,""+pedidos.get(position).getId());
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
        getPedidos();
    }

    private void getPedidos(){

        showProgressWait();

        //JSON SEND
        ConsultasPedidosModel model = new ConsultasPedidosModel();
        model.setCliente_id(""+cliente.getCodigo_cliente_id());
        model.setMetodo("ConsultarPreventaCliente");
        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IConsultaPedido request = DepcApplication.getApplication().getRestAdapter().create(IConsultaPedido.class);
            call = request.getPedido(body);
            call.enqueue(new Callback<IConsultaPedido.dataPedido>() {
                @Override
                public void onResponse(Call<IConsultaPedido.dataPedido> call, Response<IConsultaPedido.dataPedido> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                        if (data.getData().getListarProformas() != null) {
                                            if (data.getData().getListarProformas().size() > 0) {

                                                final List<ListarProformas> proformas;
                                                proformas = data.getData().getListarProformas().get(0);

                                                if (proformas != null) {
                                                    for (ListarProformas prof : proformas) {

                                                        boolean flag = true;
                                                        List<Pedidos> ped = DataBaseHelper.getPedidosByCuenta(DepcApplication.getApplication().getPedidosDao(), ""+prof.getCuenta_id());
                                                        if (ped != null){
                                                            if (ped.size() > 0){
                                                                //update

                                                                flag = false;
                                                            }
                                                        }

                                                        if (flag){
                                                            //insert

                                                            Pedidos pedido = new Pedidos();

                                                            if (cliente != null){
                                                                if (cliente.getCodigo_cliente_id() != null){
                                                                    pedido.setCliente(""+cliente.getCodigo_cliente_id());
                                                                }

                                                                if (cliente.getNombre_comercial() != null){
                                                                    pedido.setNombreCliente(""+cliente.getNombre_comercial());
                                                                }

                                                                if (cliente.getTercero_id() != null){
                                                                    pedido.setRucCliente(""+cliente.getTercero_id());
                                                                }

                                                                if (cliente.getDireccion() != null){
                                                                    pedido.setDireccionCliente(""+cliente.getDireccion());
                                                                }

                                                                if (cliente.getTelefono1() != null){
                                                                    pedido.setTelefonoCliente(""+cliente.getTelefono1());
                                                                }
                                                            }

                                                            String fecha = "";
                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                            try {
                                                                Date date = format.parse(prof.getFecha_registro());
                                                                format= new SimpleDateFormat("dd/MM/yyyy");
                                                                fecha = format.format(date);

                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                            pedido.setFecha(""+fecha);
                                                            pedido.setVendedor(prof.getUsuario_id());
                                                            pedido.setNombreVendedor(user.getNombrescompletos());
                                                            pedido.setComentario(""+prof.getObservaciones());
                                                            pedido.setSubtotal(""+prof.getTotal_neto());
                                                            pedido.setDescuento("0.00");
                                                            pedido.setIva(""+(Float.parseFloat(prof.getTotal()) - Float.parseFloat(prof.getTotal_neto())));
                                                            pedido.setTotal(""+(Float.parseFloat(prof.getTotal())));

                                                            pedido.setForma_pago(""+prof.getForma_pago());
                                                            pedido.setForma_pago_id(""+prof.getForma_pago_id());
                                                            //pedido.setDireccionEntrega("" + direccionesList.get(indexDirecciones).getDireccion_envio());
                                                            pedido.setCodigo_direccione_entrega(""+prof.getDireccion_envio_id());
                                                            pedido.setEstadoPedido("1");
                                                            if (prof.getFoto() != null){
                                                                pedido.setFoto(""+prof.getFoto());
                                                            }
                                                            pedido.setCuenta_id(""+prof.getCuenta_id());
                                                            DataBaseHelper.savePedido(pedido,DepcApplication.getApplication().getPedidosDao());

                                                        }

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
                public void onFailure(Call<IConsultaPedido.dataPedido> call, Throwable t) {
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
