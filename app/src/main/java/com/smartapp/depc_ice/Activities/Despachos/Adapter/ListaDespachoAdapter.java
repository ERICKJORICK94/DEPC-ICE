package com.smartapp.depc_ice.Activities.Despachos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.DetalleFacturas;
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
    List<DetalleFacturas> detalleFacturas;

    public ListaDespachoAdapter(Context c , List<DetalleFacturas> detalleFacturas) {
        mContext = c;
        this.detalleFacturas = detalleFacturas;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return this.detalleFacturas.size();
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


        DetalleFacturas df = this.detalleFacturas.get(position);

        viewHolder.item.setText("");
        if (df.getDescripcion() != null){
            viewHolder.item.setText(""+df.getDescripcion());
        }

        viewHolder.cantidad.setText("");
        if (df.getCantidad() != null){
            viewHolder.cantidad.setText(""+df.getCantidad());
        }


        return convertView;
    }

    static class ViewHolder {

        TextView item;
        TextView cantidad;


    }



}