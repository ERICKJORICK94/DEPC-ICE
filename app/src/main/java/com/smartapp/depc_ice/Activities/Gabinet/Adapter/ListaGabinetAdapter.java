package com.smartapp.depc_ice.Activities.Gabinet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Models.alphobetics;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class ListaGabinetAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ClienteGabinet> gabinets;

    public ListaGabinetAdapter(Context c, List<ClienteGabinet> gabinets) {
        mContext = c;
        this.gabinets = gabinets;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return gabinets.size();
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

            convertView = mInflater.inflate(R.layout.custom_gabinet_list, null);
            viewHolder = new ViewHolder();
            viewHolder.codigo = (TextView) convertView.findViewById(R.id.codigo);
            viewHolder.estado = (TextView) convertView.findViewById(R.id.estado);
            viewHolder.direccion = (TextView) convertView.findViewById(R.id.direccion);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        ClienteGabinet gab = gabinets.get(position);
        viewHolder.codigo.setText("COD:");
        if (gab.getCodigo() != null) {
            viewHolder.codigo.setText("COD: " + gab.getCodigo());
        }


        viewHolder.direccion.setText("");
        if (gab.getDireccion_cliente_gabinet() != null) {
            viewHolder.direccion.setText("" + gab.getDireccion_cliente_gabinet());
        }

        viewHolder.estado.setText("");
        if (gab.getEstado() != null){
            if (gab.getEstado().equals(Const.ESTADO_0)){
                viewHolder.estado.setText(""+Const.ESTADO_DES_0);
            }else if (gab.getEstado().equals(Const.ESTADO_1)){
                viewHolder.estado.setText(""+Const.ESTADO_DES_1);
            }else if (gab.getEstado().equals(Const.ESTADO_2)){
                viewHolder.estado.setText(""+Const.ESTADO_DES_2);
            }else if (gab.getEstado().equals(Const.ESTADO_3)){
                viewHolder.estado.setText(""+Const.ESTADO_DES_3);
            }else if (gab.getEstado().equals(Const.ESTADO_4)){
                viewHolder.estado.setText(""+Const.ESTADO_DES_4);
            }else if (gab.getEstado().equals(Const.ESTADO_5)){
                viewHolder.estado.setText(""+Const.ESTADO_DES_5);
            }
        }


        return convertView;
    }

    static class ViewHolder {

        TextView codigo;
        TextView estado;
        TextView direccion;


    }



}