package com.smartapp.depc_ice.Activities.Pedido.Adapter;

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

public class ListaPedidosAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Pedidos> pedidos;
    private List<alphobetics> arrays;

    public ListaPedidosAdapter(Context c, List<Pedidos> cl) {
        mContext = c;
        pedidos = cl;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return pedidos.size();
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

            convertView = mInflater.inflate(R.layout.custom_pedidos_list, null);
            viewHolder = new ViewHolder();
            viewHolder.fecha = (TextView) convertView.findViewById(R.id.fecha);
            viewHolder.cliente_name = (TextView) convertView.findViewById(R.id.cliente_name);
            viewHolder.estado = (TextView) convertView.findViewById(R.id.estado);
            viewHolder.precio = (TextView) convertView.findViewById(R.id.precio);


            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        Pedidos pedido = pedidos.get(position);
        int cantidades = 0;

        try {
            List<DetallePedido> detalles = DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pedido.getId());
            if (detalles != null){
                cantidades = detalles.size();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        viewHolder.fecha.setText(""+pedido.getFecha());
        viewHolder.cliente_name.setText("# Item: "+cantidades);
        viewHolder.precio.setText(""+pedido.getTotal());
        if (pedido.getTotal() != null){
            if (Utils.isNumberDecimal(pedido.getTotal())){
                viewHolder.precio.setText(""+Utils.foramatearMiles(String.format("%.2f", Float.parseFloat(pedido.getTotal()))));
            }
        }


        viewHolder.estado.setVisibility(View.GONE);

        if (pedido.getEstadoPedido().equals("0")){
            viewHolder.estado.setText("PEDIDO EN ESPERA");
            viewHolder.estado.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {

        TextView fecha;
        TextView cliente_name;
        TextView estado;
        TextView precio;


    }



}