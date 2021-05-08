package com.smartapp.depc_ice.Activities.Productos.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.util.List;


/**
 * Created by RICHARD on 18/11/2017.
 */

public class ProductoGridImagenAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Productos> productoList;

    public ProductoGridImagenAdapter(Context c, List<Productos> productoList) {
        mContext = c;
        this.productoList = productoList;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return this.productoList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.custom_category, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView precio = (TextView) convertView.findViewById(R.id.precio);

        /*Glide.with(mContext)
                .load(""+productoList.get(position).getRUTA_IMAGEN())
                .override(100, Target.SIZE_ORIGINAL)
                .skipMemoryCache(true)
                .centerCrop()
                .dontAnimate()
                .dontTransform()
                .priority(Priority.IMMEDIATE)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .format(DecodeFormat.DEFAULT)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(new RequestOptions().placeholder(R.drawable.not_image).error(R.drawable.not_image))//this line optional - you can skip this line
                .into(image);*/


        title.setText(""+productoList.get(position).getDescripcion());
        if (productoList.get(position).getPvp() != null){

            String pre = productoList.get(position).getPvp().replace(",",".");
            if (Utils.isNumber(pre)){
                pre = ""+ Integer.parseInt(pre);
            }else if (Utils.isNumberDecimal(pre)){
                pre = ""+ String.format("%.2f", Float.parseFloat(pre));
            }
            precio.setText("$ "+pre);

        }

        description.setText("COD# "+productoList.get(position).getCodigo_item());


        return convertView;
    }

}