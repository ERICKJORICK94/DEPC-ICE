package com.smartapp.depc_ice.Activities.Despachos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Models.alphobetics;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class ListaDespachoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    //private List<Pedidos> pedidos;

    public ListaDespachoAdapter(Context c /*,List<Pedidos> cl*/) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return 3;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.custom_despacho_list, null);
            viewHolder = new ViewHolder();
            viewHolder.item = (TextView) convertView.findViewById(R.id.item);
            viewHolder.cantidad = (TextView) convertView.findViewById(R.id.cantidad);


            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        if (position == 0){
            viewHolder.item.setText("PINGUINO HEL CORNETTO HERSHEYS2");
            viewHolder.cantidad.setText("5/5");
        } else if (position == 1){
            viewHolder.item.setText("PINGUINO POLITO VAINILLA 75X55ML");
            viewHolder.cantidad.setText("3/6");
        }else if (position == 2){
            viewHolder.item.setText("PINGUINO CASERO BANANO CHOCOLATE");
            viewHolder.cantidad.setText("8/9");
        }


        return convertView;
    }

    static class ViewHolder {

        TextView item;
        TextView cantidad;


    }



}