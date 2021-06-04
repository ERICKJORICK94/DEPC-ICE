package com.smartapp.depc_ice.Activities.Agenda.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.Agenda.PlanficadorPedidosActivity;
import com.smartapp.depc_ice.Activities.Cobros.CobrosActivity;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class PlanificadorPedidoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ClientesVisitas> clientesVisitas;

    public PlanificadorPedidoAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        this.clientesVisitas = PlanficadorPedidosActivity.clientesVisitas;
    }

    public int getCount() {
        return clientesVisitas.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.planificador_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.position = (TextView) convertView.findViewById(R.id.position);
            viewHolder.estado = (TextView) convertView.findViewById(R.id.estado);
            viewHolder.nombre = (TextView) convertView.findViewById(R.id.nombre);
            viewHolder.direccion = (TextView) convertView.findViewById(R.id.direccion);
            viewHolder.hora = (TextView) convertView.findViewById(R.id.hora);
            viewHolder.linear_first = (View) convertView.findViewById(R.id.linear_first);
            viewHolder.linear_final = (View) convertView.findViewById(R.id.linear_final);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        viewHolder.linear_first.setVisibility(View.VISIBLE);
        if (position == 0 ){
            viewHolder.linear_first.setVisibility(View.INVISIBLE);
        }

        viewHolder.linear_final.setVisibility(View.VISIBLE);
        if (position == (getCount() - 1) ){
            viewHolder.linear_final.setVisibility(View.INVISIBLE);
        }



        ClientesVisitas cl = clientesVisitas.get(position);

        viewHolder.hora.setText("");
        if (!cl.getHora().equals("00:00")){
            viewHolder.hora.setText(""+cl.getHora());
        }



        viewHolder.direccion.setText(""+cl.getDireccion());
        viewHolder.nombre.setText(""+cl.getNombre_comercial());

        if (cl.getEstado().equals("1")){
            viewHolder.estado.setText("VISITADO");
            viewHolder.estado.setVisibility(View.VISIBLE);
            viewHolder.estado.setBackgroundColor(mContext.getResources().getColor(R.color.Green));
        }else {
            viewHolder.estado.setVisibility(View.GONE);
        }

        viewHolder.position.setText(""+(position+1));
        return convertView;
    }


    static class ViewHolder {

        TextView position;
        TextView estado;
        TextView direccion;
        TextView nombre;
        TextView hora;
        View linear_first;
        View linear_final;


    }



}