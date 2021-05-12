package com.smartapp.depc_ice.Activities.Pedido;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.stmt.QueryBuilder;
import com.smartapp.depc_ice.Activities.DetalleCliente.DetalleUbicacionClienteActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class RegistroPedidoActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{


    //UbicacionClienteTask
    //DireccionEntrega    --- tabla
    private View layout;
    private LayoutInflater layoutInflater;
    private Clientes cliente;

    private LinearLayout linear_fondo;
    private EditText cliente_name;
    private EditText cupo;
    private EditText cupo_mes;
    private Spinner spinner_bodega;
    private EditText ruc;
    private EditText direccion;
    private EditText telefono;
    private EditText vendedor;
    private EditText comentario;
    private Button crear;
    private int currentBodega = -1;
    private int currentDirecciones = 0;
    private List<Bodega> bodegas;
    private Spinner spinner_direccion;
    private ImageButton crear_ubicacion;
    private Pedidos pedido = null;
    private boolean isActualizar = false;
    private Usuario user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.registro_pedido);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cliente_name = (EditText) layout.findViewById(R.id.cliente_name);
        cupo = (EditText) layout.findViewById(R.id.cupo);
        cupo_mes = (EditText) layout.findViewById(R.id.cupo_mes);
        spinner_bodega = (Spinner) layout.findViewById(R.id.spinner_bodega);
        ruc = (EditText) layout.findViewById(R.id.ruc);
        direccion = (EditText) layout.findViewById(R.id.direccion);
        linear_fondo = (LinearLayout) layout.findViewById(R.id.linear_fondo);
        telefono = (EditText) layout.findViewById(R.id.telefono);
        vendedor = (EditText) layout.findViewById(R.id.vendedor);
        comentario = (EditText) layout.findViewById(R.id.comentario);
        spinner_direccion = (Spinner) layout.findViewById(R.id.spinner_direccion);
        crear = (Button) layout.findViewById(R.id.crear);
        crear_ubicacion = (ImageButton) layout.findViewById(R.id.crear_ubicacion);

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

        cliente = DepcApplication.getApplication().getCliente();

            if (getIntent().getSerializableExtra(Const.ID_PEDIDO) != null) {
                pedido = (Pedidos) getIntent().getSerializableExtra(Const.ID_PEDIDO);
                crear.setText("ACTUALIZAR");
                isActualizar = true;
            }

        if (cliente != null){

            if (cliente.getNombre_comercial() != null){
                cliente_name.setText(""+cliente.getNombre_comercial());
            }

            if (cliente.getMonto_credito() != null){
                cupo.setText(""+cliente.getMonto_credito());
            }

            if (cliente.getTercero_id() != null){
                ruc.setText(""+cliente.getTercero_id());
            }

            if (cliente.getDireccion() != null){
                direccion.setText(""+cliente.getDireccion());
            }

            if (cliente.getTelefono1() != null){
                telefono.setText(""+cliente.getTelefono1());
            }

            if(user != null){
                if (user.getNombrescompletos() != null){
                    vendedor.setText(""+user.getNombrescompletos());
                }
            }

            crear_ubicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(RegistroPedidoActivity.this, DetalleUbicacionClienteActivity.class);
                    startActivity(intent);
                }
            });

        }

    }


    @Override
    public void doRetry() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
