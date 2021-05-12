package com.smartapp.depc_ice.Activities.VerDatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.List;

public class VerDatosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private TextView vendedor;
    private TextView cod_vendedor;
    private TextView txt_clientes_asignados;
    private TextView txt_clientes_vendidos;
    private TextView efectividad_cobertura_cliente;
    private TextView presupuesto_galones;
    private TextView venta_galones;
    private TextView cumplimiento;
    private TextView cobranza;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.ver_datos_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        vendedor = (TextView) layout.findViewById(R.id.vendedor);
        cod_vendedor = (TextView) layout.findViewById(R.id.cod_vendedor);
        txt_clientes_asignados = (TextView) layout.findViewById(R.id.txt_clientes_asignados);
        txt_clientes_vendidos = (TextView) layout.findViewById(R.id.txt_clientes_vendidos);
        efectividad_cobertura_cliente = (TextView) layout.findViewById(R.id.efectividad_cobertura_cliente);
        presupuesto_galones = (TextView) layout.findViewById(R.id.presupuesto_galones);
        venta_galones = (TextView) layout.findViewById(R.id.venta_galones);
        cumplimiento = (TextView) layout.findViewById(R.id.cumplimiento);
        cobranza = (TextView) layout.findViewById(R.id.cobranza);

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

    }

    @Override
    public void doRetry() {

    }
}
