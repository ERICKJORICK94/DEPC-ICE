package com.smartapp.depc_ice.Activities.Despachos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartapp.depc_ice.Entities.DetalleViaje;
import com.smartapp.depc_ice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class PlanificadorDespachosAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DetalleViaje> detalleViajes;

    public PlanificadorDespachosAdapter(Context c, List<DetalleViaje> detalleViajes) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        this.detalleViajes = detalleViajes;
    }

    public int getCount() {

        return detalleViajes.size();
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
            /*viewHolder.numero = (TextView) convertView.findViewById(R.id.numero);
            viewHolder.fecha_vencida = (TextView) convertView.findViewById(R.id.fecha_vencida);
            viewHolder.valor_vendido = (TextView) convertView.findViewById(R.id.valor_vendido);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);*/


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

        DetalleViaje item = detalleViajes.get(position);

        viewHolder.estado.setText("");
        //viewHolder.estado.setVisibility(View.GONE);
        viewHolder.estado.setBackgroundColor(mContext.getResources().getColor(R.color.Green));
        if (item.getNombre_estado() != null){
            viewHolder.estado.setText(""+item.getNombre_estado());
        }

        viewHolder.hora.setText("00:00");
        viewHolder.direccion.setText(""+item.getDireccion_envio());
        viewHolder.nombre.setText("");

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