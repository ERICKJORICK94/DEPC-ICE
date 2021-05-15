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

import com.smartapp.depc_ice.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class FormaCobrosAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isShowing = true;


    public FormaCobrosAdapter(Context c, boolean isShowing) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        this.isShowing = isShowing;

    }

    public int getCount() {

        return 2;
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

            convertView = mInflater.inflate(R.layout.custom_documento, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.close = (ImageView) convertView.findViewById(R.id.close);


            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }


        if (this.isShowing){
            viewHolder.close.setVisibility(View.VISIBLE);
        }else{
            viewHolder.close.setVisibility(View.GONE);
        }


        if (position == 0){
            viewHolder.name.setText("$ 60");
            viewHolder.status.setText("PAGO EN EFECTIVO");
        }else if (position == 1){
            viewHolder.name.setText("$ 86.30");
            viewHolder.status.setText("PAGO EN CHEQUE");
        }


        viewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*new AlertDialog.Builder(mContext)
                        .setTitle("ATENCIÓN")
                        .setMessage("¿Está seguro que desea eliminar la forma de pago?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {


                                try {
                                    DataBaseHelper.deleteDetallePagoByID(San32Application.getApplication().getDetallePagoDao(),""+item.getId());

                                    if (DataBaseHelper.getDetallePagoByIdPago(San32Application.getApplication().getDetallePagoDao(), idPago) != null){
                                        lista = DataBaseHelper.getDetallePagoByIdPago(San32Application.getApplication().getDetallePagoDao(), idPago);
                                    }

                                    notifyDataSetChanged();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }


                            }})
                        .setNegativeButton("NO", null).show();*/


            }
        });

        return convertView;
    }

    static class ViewHolder {

        TextView name;
        TextView status;
        ImageView close;


    }



}