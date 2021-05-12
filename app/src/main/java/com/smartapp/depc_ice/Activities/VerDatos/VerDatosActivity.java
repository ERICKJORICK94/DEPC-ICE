package com.smartapp.depc_ice.Activities.VerDatos;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    private PieChart chart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;

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

        getEntries();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        chart.setData(pieData);
        chart.setCenterText("Efectividad");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);

    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(7f, "Clientes"));
        pieEntries.add(new PieEntry(4f, "Vendidos"));
        pieEntries.add(new PieEntry(6f, "Presupuesto"));
        pieEntries.add(new PieEntry(8f, "Cumplimiento"));
        pieEntries.add(new PieEntry(7f, "Cobranzas"));
    }

    @Override
    public void doRetry() {

    }
}
