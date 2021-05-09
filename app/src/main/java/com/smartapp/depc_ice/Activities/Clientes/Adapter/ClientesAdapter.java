package com.smartapp.depc_ice.Activities.Clientes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Models.alphobetics;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class ClientesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Clientes> cliente;
    private List<alphobetics> arrays;

    public ClientesAdapter(Context c, List<Clientes> cl) {
        mContext = c;
        cliente = cl;
        mInflater = LayoutInflater.from(mContext);
        arrays = new ArrayList<alphobetics>();
    }

    public int getCount() {

        if (cliente.size() > Const.PARAM_MAX_ROW){
            return Const.PARAM_MAX_ROW;
        }

        return cliente.size();
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

            convertView = mInflater.inflate(R.layout.custom_lista_name, null);
            viewHolder = new ViewHolder();
            viewHolder.letter = (TextView) convertView.findViewById(R.id.letter);
            viewHolder.codigo = (TextView) convertView.findViewById(R.id.codigo);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.ruc = (TextView) convertView.findViewById(R.id.ruc);
            viewHolder.direccion = (TextView) convertView.findViewById(R.id.direccion);
            viewHolder.estatus = (TextView) convertView.findViewById(R.id.estatus);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        Clientes entity = cliente.get(position);
        String codigo = "";
        if (entity.getCodigo_cliente_id() != null){
            codigo = entity.getCodigo_cliente_id();
        }
        viewHolder.codigo.setText("COD: "+codigo);
        viewHolder.name.setText("");
        if (entity.getNombre_comercial() != null) {
            viewHolder.name.setText("" + entity.getNombre_comercial());
        }
        viewHolder.ruc.setText(""+entity.getTipo_id_tercero()+" "+entity.getTercero_id());
        viewHolder.direccion.setText(""+entity.getDireccion());
        viewHolder.letter.setVisibility(View.INVISIBLE);

        if(entity.getNombre_comercial() != null) {
            if(entity.getNombre_comercial().length() > 0) {
                String fisrCharacter = ""+entity.getNombre_comercial().charAt(0);
                fisrCharacter.toUpperCase();
                boolean contains = false;
                int index = 0;
                for(alphobetics a: arrays){
                    if (a.getLetter().equals(fisrCharacter)){
                        contains = true;
                        index = a.getPosition();
                        break;
                    }
                }

                if (!contains) {
                    alphobetics al = new alphobetics();
                    al.setLetter(fisrCharacter);
                    al.setPosition(position);
                    arrays.add(al);
                    viewHolder.letter.setVisibility(View.VISIBLE);
                    viewHolder.letter.setText(fisrCharacter);
                }else{
                    if (index == position){
                        viewHolder.letter.setVisibility(View.VISIBLE);
                        viewHolder.letter.setText(fisrCharacter);
                    }
                }
            }
        }

        viewHolder.estatus.setVisibility(View.GONE);
        if (entity.getEstado() != null){
            if (entity.getEstado().equals("1")){
                viewHolder.estatus.setText("ACTIVO");
                viewHolder.estatus.setTextColor(mContext.getResources().getColor(R.color.Green));
                viewHolder.estatus.setVisibility(View.VISIBLE);
            }else{
                viewHolder.estatus.setText("INACTIVO");
                viewHolder.estatus.setTextColor(mContext.getResources().getColor(R.color.Red));
                viewHolder.estatus.setVisibility(View.VISIBLE);
            }
        }



        return convertView;
    }



    static class ViewHolder {

        TextView letter;
        TextView codigo;
        TextView name;
        TextView ruc;
        TextView direccion;
        TextView estatus;


    }



}