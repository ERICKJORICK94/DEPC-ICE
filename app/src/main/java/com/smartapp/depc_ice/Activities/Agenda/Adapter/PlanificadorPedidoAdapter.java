package com.smartapp.depc_ice.Activities.Agenda.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.Cobros.CobrosActivity;
import com.smartapp.depc_ice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class PlanificadorPedidoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> positionCheck;

    public PlanificadorPedidoAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        positionCheck = new ArrayList<Integer>();
    }

    public int getCount() {

        //return mContext.cobroList.size();

        return 20;
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
            /*viewHolder.numero = (TextView) convertView.findViewById(R.id.numero);
            viewHolder.fecha_vencida = (TextView) convertView.findViewById(R.id.fecha_vencida);
            viewHolder.valor_vendido = (TextView) convertView.findViewById(R.id.valor_vendido);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);*/


            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        if (position == 0 || position == 1){
            viewHolder.estado.setText("VISITADO");
            viewHolder.estado.setBackgroundColor(mContext.getResources().getColor(R.color.Green));
        }else {
            viewHolder.estado.setText("OENDIENTE");
            viewHolder.estado.setBackgroundColor(mContext.getResources().getColor(R.color.OrangeRed));
        }

        if (position == 1){
            viewHolder.hora.setText("09:30");
            viewHolder.direccion.setText("SAMANES 5 SOLAR 369 VILLA 5");
            viewHolder.nombre.setText("VICENTE MALDONAR ARAMBULO");
        }else if (position == 2){
            viewHolder.hora.setText("13:00");
            viewHolder.direccion.setText("GUASMO SUR CERCA DE LA PLAYITA MZ 361 SOLAR 12");
            viewHolder.nombre.setText("ANABEL MADRID VERA");
        }else if (position == 3){
            viewHolder.hora.setText("14:15");
            viewHolder.direccion.setText("DURAN CDLA EL RECREO MZ 517 SOLAR14");
            viewHolder.nombre.setText("CARLOS CARPIO PAREDES");
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


    }



}