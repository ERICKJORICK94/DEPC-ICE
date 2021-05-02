package com.smartapp.depc_ice.Activities.VerDatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

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

    }

    @Override
    public void doRetry() {

    }
}
