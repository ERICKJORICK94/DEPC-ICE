package com.smartapp.depc_ice.Activities.Productos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.Productos.Detalle.DetalleProductoActivity;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class ProductoAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Productos> productos;
    private String idPedido;

    public ProductoAdapter(Context c, List<Productos> pl, String idPedido) {
        mContext = c;
        productos = pl;
        mInflater = LayoutInflater.from(mContext);
        this.idPedido = idPedido;
    }

    public int getCount() {

        if (productos.size() > Const.PARAM_MAX_ROW){
            return Const.PARAM_MAX_ROW;
        }

        return productos.size();
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

            convertView = mInflater.inflate(R.layout.producto_list, null);
            viewHolder = new ViewHolder();

            viewHolder.list_row = (TextView) convertView.findViewById(R.id.list_row);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            viewHolder.ver_detail = (TextView) convertView.findViewById(R.id.ver_detail);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.info);
            viewHolder.precio = (TextView) convertView.findViewById(R.id.precio);
            viewHolder.cantidad = (TextView) convertView.findViewById(R.id.cantidad);
            viewHolder.precio_item = (TextView) convertView.findViewById(R.id.precio_item);
            viewHolder.linear = (LinearLayout) convertView.findViewById(R.id.linear);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        final Productos producto = productos.get(position);
        viewHolder.list_row.setText(""+producto.getDescripcion());
        viewHolder.txtDescription.setText("" + producto.getDescripcion_abrev());

        if (producto.getPvp() != null) {
            viewHolder.precio_item.setText("Precio: " + Utils.foramatearMiles(producto.getPvp().replace(",", ".")));
        }else{
            viewHolder.precio_item.setText("");
        }
        viewHolder.precio.setText("# Código: "+producto.getCodigo_item());
        if (producto.getExistencia() != null) {
            String stock = producto.getExistencia().replace(",",".");
            if (Utils.isNumber(""+stock)){
                int cantidad = Integer.valueOf(""+stock);
                viewHolder.cantidad.setText("Stock: " + cantidad);
            }else if (Utils.isNumberDecimal(""+stock)){
                float cantidad = Float.valueOf(""+stock);
                float x = cantidad - (int) cantidad;
                if (x == 0 ) {
                    viewHolder.cantidad.setText("Stock: " + (int) cantidad);
                }else{
                    viewHolder.cantidad.setText("Stock: " + String.format("%.2f",cantidad));
                }
            }

        }else{
            viewHolder.cantidad.setText("Stock: 0");
        }

        viewHolder.linear.setVisibility(View.GONE);

            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Productos pr = productos.get(position);
                    Intent intent = new Intent(mContext, DetalleProductoActivity.class);
                    intent.putExtra(Const.PRODUCT_DETAIL, pr);
                    mContext.startActivity(intent);
                }
            });

            viewHolder.ver_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Productos pr = productos.get(position);
                    Intent intent = new Intent(mContext, DetalleProductoActivity.class);
                    intent.putExtra(Const.PRODUCT_DETAIL, pr);
                    mContext.startActivity(intent);
                }
            });

        if(position%2 == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.select));
        }else{
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.not_select));
        }


        return convertView;
    }



    static class ViewHolder {

        TextView list_row;
        TextView txtDescription;
        TextView ver_detail;
        TextView precio;
        TextView cantidad;
        ImageView info;
        TextView precio_item;
        LinearLayout linear;


    }



}