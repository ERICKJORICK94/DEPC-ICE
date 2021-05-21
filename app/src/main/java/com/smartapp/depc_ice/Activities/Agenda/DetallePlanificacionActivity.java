package com.smartapp.depc_ice.Activities.Agenda;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartapp.depc_ice.Activities.AcercaDe.AcercaDeActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Home.MainActivity;
import com.smartapp.depc_ice.Activities.Pedido.Adapter.ListaPedidosAdapter;
import com.smartapp.depc_ice.Activities.Pedido.DetallePedidoActivity;
import com.smartapp.depc_ice.Activities.Pedido.ListaPredidosActivity;
import com.smartapp.depc_ice.Activities.Pedido.RegistroPedidoActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.NonScrollListView;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DetallePlanificacionActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private String fecha;
    private Button registrar;
    private NonScrollListView lista;
    private FloatingActionButton fab;
    private Clientes cliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_plainificacion_layout);
        Utils.SetStyleActionBarTitle(this);


        //Demo
        try {
            List<Clientes> clientes = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), "201");
            if (clientes !=  null){
                if (clientes.size() > 0){
                    cliente = clientes.get(0);
                    DepcApplication.getApplication().setCliente(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //

        if (getIntent() != null) {
            if (getIntent().getStringExtra("fecha") != null) {
                fecha = getIntent().getStringExtra("fecha");
            }
        }

        registrar = (Button) layout.findViewById(R.id.registrar);
        lista = (NonScrollListView) layout.findViewById(R.id.lista);
        fab = (FloatingActionButton) layout.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallePlanificacionActivity.this, RegistroPedidoActivity.class);
                startActivity(intent);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog MyDialog = new Dialog(DetallePlanificacionActivity.this);
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.firma_layout);
                MyDialog.setCancelable(true);
                Button limpiar = (Button) MyDialog.findViewById(R.id.limpiar);
                Button aceptar = (Button) MyDialog.findViewById(R.id.aceptar);
                SignaturePad signature_pad = (SignaturePad) MyDialog.findViewById(R.id.signature_pad);

                limpiar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signature_pad.clear();
                    }
                });

                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.dismiss();
                    }
                });


                MyDialog.show();

            }
        });
    }

    private void fillData(){

        try {

            if (cliente != null){
                if(DataBaseHelper.getPedidosByCliente(DepcApplication.getApplication().getPedidosDao(),cliente.getCodigo_cliente_id()) != null){
                    final List<Pedidos> pedidos = DataBaseHelper.getPedidosByCliente(DepcApplication.getApplication().getPedidosDao(), cliente.getCodigo_cliente_id());
                    Collections.reverse(pedidos);
                    lista.setAdapter(new ListaPedidosAdapter(DetallePlanificacionActivity.this, pedidos));

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(DetallePlanificacionActivity.this, DetallePedidoActivity.class);
                            intent.putExtra(Const.ID_PEDIDOS,""+pedidos.get(position).getId());
                            startActivity(intent);


                        }
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    public void doRetry() {

    }
}
