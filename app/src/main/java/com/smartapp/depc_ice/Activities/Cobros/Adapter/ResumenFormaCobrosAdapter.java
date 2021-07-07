package com.smartapp.depc_ice.Activities.Cobros.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.DetalleFormaPago;
import com.smartapp.depc_ice.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class ResumenFormaCobrosAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    List<DetalleFormaPago> detalleFormaPagos;


    public ResumenFormaCobrosAdapter(Context c, List<DetalleFormaPago> detalleFormaPagos) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        this.detalleFormaPagos = detalleFormaPagos;

    }

    public int getCount() {

        return detalleFormaPagos.size();
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

            convertView = mInflater.inflate(R.layout.custom_resumen, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.forma_pago = (TextView) convertView.findViewById(R.id.forma_pago);
            viewHolder.valor = (TextView) convertView.findViewById(R.id.valor);


            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }



        DetalleFormaPago df = detalleFormaPagos.get(position);
        viewHolder.name.setText(""+df.getNombreCliente());
        viewHolder.forma_pago.setText(""+df.getNombre_forma_de_pago());
        viewHolder.valor.setText("$ "+df.getValor());

        return convertView;
    }

    static class ViewHolder {

        TextView name;
        TextView forma_pago;
        TextView valor;


    }



}