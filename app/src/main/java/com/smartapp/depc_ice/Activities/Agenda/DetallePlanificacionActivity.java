package com.smartapp.depc_ice.Activities.Agenda;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Home.MainActivity;
import com.smartapp.depc_ice.Activities.Pedido.Adapter.ListaPedidosAdapter;
import com.smartapp.depc_ice.Activities.Pedido.DetallePedidoActivity;
import com.smartapp.depc_ice.Activities.Pedido.ListaPredidosActivity;
import com.smartapp.depc_ice.Activities.Pedido.RegistroPedidoActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.NonScrollListView;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePlanificacionActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private String fecha;
    private String latitud;
    private String longitud;
    private String direccionRuta;

    private Button registrar;
    private NonScrollListView lista;
    private FloatingActionButton fab;
    private Clientes cliente = null;
    private TextView clienteName;
    private TextView lblFecha;
    private TextView direccion;

    private TextView ver_mapa;
    private TextView llamar;
    private TextView btn_whatsapp;
    private TextView ruc;
    private String clienteId = "";
    private Call<IClientes.dataClientes> call;
    private IClientes.dataClientes data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_plainificacion_layout);
        Utils.SetStyleActionBarTitle(this);


        if (getIntent() != null) {
            if (getIntent().getStringExtra("fecha") != null) {
                fecha = getIntent().getStringExtra("fecha");
                clienteId = getIntent().getStringExtra("cliente_id");
                latitud = getIntent().getStringExtra("latitud");
                longitud = getIntent().getStringExtra("longitud");
                direccionRuta = getIntent().getStringExtra("direccionRuta");
            }
        }

       // cliente = DepcApplication.getApplication().getCliente();

        registrar = (Button) layout.findViewById(R.id.registrar);
        lista = (NonScrollListView) layout.findViewById(R.id.lista);
        fab = (FloatingActionButton) layout.findViewById(R.id.fab);
        clienteName = (TextView) layout.findViewById(R.id.clienteName);
        ruc = (TextView) layout.findViewById(R.id.ruc);
        lblFecha = (TextView) layout.findViewById(R.id.fecha);
        direccion = (TextView) layout.findViewById(R.id.direccion);

        ver_mapa = (TextView) layout.findViewById(R.id.ver_mapa);
        llamar = (TextView) layout.findViewById(R.id.llamar);
        btn_whatsapp = (TextView) layout.findViewById(R.id.btn_whatsapp);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente != null) {
                    Intent intent = new Intent(DetallePlanificacionActivity.this, RegistroPedidoActivity.class);
                    startActivity(intent);
                }
            }
        });




        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente != null) {
                    if (cliente.getTelefono1() != null) {
                        if (cliente.getTelefono1().length() > 0) {

                            int permissionCheck = ContextCompat.checkSelfPermission(DetallePlanificacionActivity.this, Manifest.permission.CALL_PHONE);

                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(
                                        DetallePlanificacionActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        3621);

                                return;
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                                startActivity(intent);
                                return;
                            }
                        }
                    }
                }

                showAlert("Lo sentimos no existe número al cual contactar");
            }
        });

        ver_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Direcciones direccion = new Direcciones();
                direccion.setLatitud(longitud);//ESTA AL REVES
                direccion.setLongitud(latitud);//ESTA AL REVES
                direccion.setDireccion_envio(direccionRuta);
                Intent intent = new Intent(DetallePlanificacionActivity.this, MapsActivity.class);
                intent.putExtra(Const.DETALLE_CLIENTE, direccion);
                startActivity(intent);

            }
        });

        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cliente != null) {
                    if (cliente.getTelefono1() != null) {
                        if (cliente.getTelefono1().length() > 0) {

                            try {
                                getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + cliente.getTelefono1()));
                                startActivity(intent);
                            } catch (PackageManager.NameNotFoundException e) {
                                Toast.makeText(DetallePlanificacionActivity.this, "WhatsApp no instalado", Toast.LENGTH_SHORT).show();
                            }

                            return;
                        }
                    }
                }

                showAlert("Lo sentimos no existe número al cual contactar");
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

        getClientes();
    }

    private void fillData(){

        try {

           List<Clientes> clientes = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), ""+clienteId);

           if (clientes != null){
               if (clientes.size() > 0){
                   cliente = clientes.get(0);
               }
           }

            if (cliente != null){
                DepcApplication.getApplication().setCliente(cliente);


                clienteName.setText(""+cliente.getNombre_comercial());
                ruc.setText(""+cliente.getTercero_id());
                lblFecha.setText(""+fecha);
                direccion.setText(""+direccionRuta);


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 3621:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (cliente != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                        startActivity(intent);
                    }
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }

    private void getClientes(){

        showProgressWait();
        //JSON SEND
        ClientesModel model = new ClientesModel();
        model.setCondicion("and a.codigo_cliente_id = "+clienteId);
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

                                                fillData();

                                                return;
                                            }
                                        }
                                }else{
                                    //Error
                                }
                            }
                            fillData();

                        } catch (Exception e) {
                            e.printStackTrace();
                            fillData();
                        }

                    } else {
                        fillData();
                    }
                }

                @Override
                public void onFailure(Call<IClientes.dataClientes> call, Throwable t) {
                    fillData();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            fillData();

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
