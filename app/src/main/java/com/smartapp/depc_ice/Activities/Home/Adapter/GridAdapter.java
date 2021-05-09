package com.smartapp.depc_ice.Activities.Home.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.smartapp.depc_ice.R;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    String[] opcionesMenu;

    public GridAdapter(Context c, String[] opcionesMenu) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        this.opcionesMenu = opcionesMenu;
    }

    public int getCount() {
        if (opcionesMenu != null){
            return opcionesMenu.length;
        }

        return 0;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.custom_list_main, null);
        ImageView imagen = (ImageView) convertView.findViewById(R.id.imagen);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        String[] fields = opcionesMenu[position].split("[\t ]");
        title.setText(fields[2]);
        imagen.setImageResource(getImageId(mContext, fields[3]));

        return convertView;
    }

    private int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier( imageName, null, context.getPackageName());
    }


}