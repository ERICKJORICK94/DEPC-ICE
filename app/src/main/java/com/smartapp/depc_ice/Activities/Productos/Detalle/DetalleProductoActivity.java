package com.smartapp.depc_ice.Activities.Productos.Detalle;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by macmini on 24/4/18.
 */

public class DetalleProductoActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private Productos producto;
    private int selectItem = -1;
    private TextView descripcion;
    private TextView inventario;
    private TextView transito;
    private TextView lista_precio;
    private TextView presentaciones;
    private TextView precio;
    private TextView comentario;
    private TextView especificaciones;
    private TextView codigo;
    private TextView name;
    private ImageView image;
    private ImageView share;
    private TextView share_precio;
    private LinearLayout linear_bodega;
    private ArrayList<String> urlString = null;
    private ArrayList<String> listPrecio = new ArrayList<String>();
    private ArrayList<String> listPrecioAux = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        layout = addLayout(R.layout.detalle_producto_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            producto = (Productos) b.getSerializable(Const.PRODUCT_DETAIL);
        }

        codigo = (TextView) layout.findViewById(R.id.codigo);
        name = (TextView) layout.findViewById(R.id.name);
        descripcion = (TextView) layout.findViewById(R.id.descripcion);
        inventario = (TextView) layout.findViewById(R.id.inventario);
        transito = (TextView) layout.findViewById(R.id.transito);
        precio = (TextView) layout.findViewById(R.id.precio);
        linear_bodega = (LinearLayout) layout.findViewById(R.id.linear_bodega);
        comentario = (TextView) layout.findViewById(R.id.comentario);
        lista_precio = (TextView) layout.findViewById(R.id.lista_precio);
        presentaciones = (TextView) layout.findViewById(R.id.presentaciones);
        especificaciones = (TextView) layout.findViewById(R.id.especificaciones);
        image = (ImageView) layout.findViewById(R.id.image);
        share = (ImageView) layout.findViewById(R.id.share);
        share_precio = (TextView) layout.findViewById(R.id.share_precio);

        if (Utils.sImageLoader != null){
            Utils.sImageLoader.clearMemoryCache();
            Utils.sImageLoader.clearDiskCache();
            Utils.sImageLoader.clearDiscCache();
        }

        linear_bodega.setVisibility(View.GONE);

        if (producto != null){
            codigo.setText("COD: "+producto.getCodigo_item());
            name.setText(""+producto.getDescripcion());

            if (producto.getDescripcion_abrev() != null) {
                comentario.setText(" " + producto.getDescripcion_abrev());
            }


            if (producto.getPvp() != null) {
                String campo = producto.getPvp().replace(",",".");
                precio.setText("" + String.format("$ %.2f", Float.parseFloat(campo)));
            }

            String bodega = "";
            if (producto.getDescripcion_bodega() != null){
                bodega = producto.getDescripcion_bodega();
            }

            String codBodega = "";
            if (producto.getBodega_id() != null){
                codBodega = producto.getBodega_id();
            }

            String responsableBodega = "";
            try {
                List<Bodega> bg = DataBaseHelper.getBodegaSearch(DepcApplication.getApplication().getBodegaDao(),"bodega_id = '"+codBodega+"'");
                if (bg != null){
                    if (bg.size() > 0){
                        Bodega ba = bg.get(0);
                        if (ba.getResponsable() != null){
                            responsableBodega = ba.getResponsable();
                        }
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            final String bodyData = "<b>BODEGA:</b>  "+bodega+"<br>"
                                +"<b>RESPONSABLE:</b>  "+responsableBodega+"<br>";


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                descripcion.setText(Html.fromHtml(bodyData, Html.FROM_HTML_MODE_LEGACY));
            } else {
                descripcion.setText(Html.fromHtml(bodyData));
            }

            if (producto.getExistencia() != null) {
                inventario.setText("" + producto.getExistencia());
            }

            transito.setText("NO ESPECIFICADO");
            lista_precio.setText("NO ESPECIFICADO");

            presentaciones.setText("NO ESPECIFICADO");

            especificaciones.setText("NO ESPECIFICADO");


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent(com.app.san_32.Activities.DetalleProductoActivity.this, PhotoViewer.class);
                    intent.putExtra("url",""+producto.getRUTA_IMAGEN());
                    startActivity(intent);*/
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if (producto.getRUTA_IMAGEN() != null){
                        if(producto.getRUTA_IMAGEN().length() > 0){

                            Glide.with(com.app.san_32.Activities.DetalleProductoActivity.this)
                                    .asBitmap()
                                    .load(""+producto.getRUTA_IMAGEN())
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            hideProgressWait();
                                            showListPrecio(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }


                                        @Override
                                        public void onLoadStarted(@Nullable Drawable placeholder) {
                                            super.onLoadStarted(placeholder);
                                            showProgressWait();
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            super.onLoadFailed(errorDrawable);
                                            hideProgressWait();
                                        }
                                    });


                        }
                    }*/
                }
            });

            share_precio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*String campo = producto.getPRECIO().replace(",",".");

                    Intent txtIntent = new Intent(Intent.ACTION_SEND);
                    txtIntent .setType("text/plain");
                    txtIntent .putExtra(Intent.EXTRA_SUBJECT, "San32");
                    txtIntent .putExtra(Intent.EXTRA_TEXT, producto.getNOMBRE()+" ---->  "+Utils.foramatearMiles(campo));
                    startActivity(Intent.createChooser(txtIntent ,"Share"));*/

                }
            });
        }

    }
    @Override
    public void doRetry() {

    }
}
