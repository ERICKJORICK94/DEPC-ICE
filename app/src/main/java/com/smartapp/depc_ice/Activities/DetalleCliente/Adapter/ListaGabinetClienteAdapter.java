package com.smartapp.depc_ice.Activities.DetalleCliente.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smartapp.depc_ice.Activities.GabinetClientes.DetalleGabinetActivity;
import com.smartapp.depc_ice.Activities.GabinetClientes.GabinietClientesActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
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

public class ListaGabinetClienteAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ClienteGabinet> gabinets;
    private List<alphobetics> arrays;
    private int ID_VER = 546;

    public ListaGabinetClienteAdapter(Context c, List<ClienteGabinet> cl) {
        mContext = c;
        gabinets = cl;
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

            convertView = mInflater.inflate(R.layout.custom_gabninet_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.serie = (TextView) convertView.findViewById(R.id.serie);
            viewHolder.ver = (Button) convertView.findViewById(R.id.ver);



            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        ClienteGabinet gabinet = gabinets.get(position);

        viewHolder.serie.setText("SERIE: ");
        if (gabinet.getSerie() != null){
            viewHolder.serie.setText("SERIE: "+gabinet.getSerie());
        }

        viewHolder.ver.setId(ID_VER+position);

        viewHolder.ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = v.getId() - ID_VER;
                ClienteGabinet gabinet = gabinets.get(pos);

                Intent intent = new Intent(mContext, DetalleGabinetActivity.class);
                intent.putExtra("cliente_gabinet_id",""+gabinet.getId());
                mContext.startActivity(intent);
            }
        });





        return convertView;
    }

    static class ViewHolder {

        TextView serie;
        Button ver;



    }



}