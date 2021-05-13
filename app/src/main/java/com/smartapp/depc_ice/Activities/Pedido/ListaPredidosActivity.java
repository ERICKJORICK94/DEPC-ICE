package com.smartapp.depc_ice.Activities.Pedido;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Pedido.Adapter.ListaPedidosAdapter;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ListaPredidosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private Clientes cliente;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.listado_pedido_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        lista = (ListView) layout.findViewById(R.id.lista);
        cliente = DepcApplication.getApplication().getCliente();


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
        fillData();
    }

    @Override
    public void doRetry() {

    }
}
