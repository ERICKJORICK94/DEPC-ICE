package com.smartapp.depc_ice.Activities.VerDatos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
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

    private BarChart chart;

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
        chart = findViewById(R.id.chart1);

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

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(945f, 0));
        NoOfEmp.add(new BarEntry(1040f, 1));
        NoOfEmp.add(new BarEntry(1133f, 2));
        NoOfEmp.add(new BarEntry(1240f, 3));
        NoOfEmp.add(new BarEntry(1369f, 4));
        NoOfEmp.add(new BarEntry(1487f, 5));
        NoOfEmp.add(new BarEntry(1501f, 6));
        NoOfEmp.add(new BarEntry(1645f, 7));
        NoOfEmp.add(new BarEntry(1578f, 8));
        NoOfEmp.add(new BarEntry(1695f, 9));

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Indicadores de rendimientos");
        chart.animateY(2000);
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);

    }



    @Override
    public void doRetry() {

    }
}
