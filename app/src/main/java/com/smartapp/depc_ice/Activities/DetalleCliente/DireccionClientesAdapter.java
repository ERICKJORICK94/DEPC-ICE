package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;

import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class DireccionClientesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Direcciones> cliente;
    private String nombre;


    public ProgressDialog progress;
    public DireccionClientesAdapter(Context c, List<Direcciones> cl, String nombre) {
        mContext = c;
        cliente = cl;
        this.nombre = nombre;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return cliente.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.custom_direcciones, null);
            viewHolder = new ViewHolder();
            viewHolder.nombre = (TextView) convertView.findViewById(R.id.nombre);
            viewHolder.btActualizar = (Button)convertView.findViewById(R.id.bt_actualizar);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        final Direcciones entity = cliente.get(position);
        viewHolder.nombre.setText(""+entity.getDireccion_envio());
        viewHolder.btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MantDireccionActivity.class);
                intent.putExtra(Const.DETALLE_DIRECCION, entity);
                intent.putExtra("nombre", nombre);
                intent.putExtra("cliente", entity.getCliente_id());
                //intent.putExtra("cod_ciudad", entity.getCODIGO_CIUDAD());
                intent.putExtra("parametro", "mant");
                mContext.startActivity(intent);

            }
        });


        return convertView;
    }


    public void showProgressWait2(String mensaje) {

        progress = new ProgressDialog(mContext);
        progress.setMessage(mensaje);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

    }

    public  void hideProgressWait() {
        try {

                            progress.dismiss();

        } catch (Exception e) {

        }
    }
    static class ViewHolder {

        TextView nombre;
        Button btActualizar;

    }



}