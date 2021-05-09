package com.smartapp.depc_ice.Activities.Productos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Productos.Adapter.ProductoGridImagenAdapter;
import com.smartapp.depc_ice.Activities.Productos.Detalle.DetalleProductoActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Interface.IBodegas;
import com.smartapp.depc_ice.Interface.IProducto;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.ProductosModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductosActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private Call<IBodegas.dataBodega> call;
    private IBodegas.dataBodega data;

    private Call<IProducto.dataProductos> callProducto;
    private IProducto.dataProductos dataProducto;

    private TextView catalogo;
    private CheckBox check;
    private boolean isSearch = false;
    private String condition = "";
    private GridView lista;
    private String currentCodigoFamilia = "";
    private EditText search_product;
    private boolean isCatalogo = true;
    private String buscarBodega = "";
    private String search = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Richard Asencio Vargas

        layout = addLayout(R.layout.producto_catalogo);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);


        catalogo = (TextView) layout.findViewById(R.id.catalogo);
        lista = (GridView) layout.findViewById(R.id.lista);
        check = (CheckBox) layout.findViewById(R.id.check);
        search_product = (EditText) layout.findViewById(R.id.search_product);


        search_product.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                lista.setAdapter(null);
                getProducto(""+currentCodigoFamilia,""+v.getText().toString().trim());
                return false;
            }
        });

        search_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (search_product.getText().toString().trim().length() == 0){
                    // getProducto(""+currentCodigoFamilia,"");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (isCatalogo){
            getCatalogo("");
            catalogo.setVisibility(View.VISIBLE);
            catalogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCatalogo("");
                }
            });
        }else{
            catalogo.setVisibility(View.GONE);
            getProducto(""+currentCodigoFamilia,"");
        }



        /*check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                getProducto(""+currentCodigoFamilia,""+search_product.getText().toString().trim());

            }
        });*/


    }



    private void showCatalogo(final List<Bodega> catalogoList){

        View detailProduct = layoutInflater.inflate(R.layout.catalogo_search_layout, null);
        final AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setView(detailProduct);
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);

        ImageView back = (ImageView) detailProduct.findViewById(R.id.back);
        EditText search = (EditText) detailProduct.findViewById(R.id.search);
        final ListView lista = (ListView) detailProduct.findViewById(R.id.lista);
        condition = "";

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                buscarBodega = v.getText().toString().trim();
                //condition(v.getText().toString().trim());
                isSearch = true;
                showList();
                alert.dismiss();

                return false;
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });


        final List<String> nombre = new ArrayList<String>();
        int contador = 1;
        nombre.add("TODAS LAS BODEGAS");
        for (Bodega cat : catalogoList){
            nombre.add(cat.getDescripcion());
            contador++;

        }
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nombre);
        lista.setAdapter(adp);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String codigo = catalogoList.get(position-1).getBodega_id();
                    currentCodigoFamilia = "" + codigo;
                    getProducto(codigo, "");
                    catalogo.setText(""+catalogoList.get(position-1).getDescripcion());
                }else{
                    currentCodigoFamilia = "";
                    getProducto(currentCodigoFamilia, "");
                    catalogo.setText(""+nombre.get(0));
                }

                alert.dismiss();
            }
        });


        alert.show();

    }

    private void condition(String search){
        isSearch = true;
        search.trim();
        condition = "OR (bodega_id like (%"+search+"%) or descripcion like ('%"+search+"%'))";
        Log.e("TAG---","condition metoodo: "+condition);
        getCatalogo(condition);
    }


    private void getCatalogo(String search){

        showProgressWait();

        search_product.setText("");
        int limit = Const.PARAM_MAX_ROW;

        //JSON SEND
        BodegasModel model = new BodegasModel();
        model.setCondicion("");
        model.setMetodo("ListaBodegas");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IBodegas request = DepcApplication.getApplication().getRestAdapter().create(IBodegas.class);
            call = request.getBodegas(body);
            call.enqueue(new Callback<IBodegas.dataBodega>() {
                @Override
                public void onResponse(Call<IBodegas.dataBodega> call, Response<IBodegas.dataBodega> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null){
                                    if (data.getData().getListarBodegas() != null) {
                                        if (data.getData().getListarBodegas().size() > 0) {


                                            final List<Bodega> bodegas;
                                            bodegas = data.getData().getListarBodegas().get(0);

                                            if (bodegas != null) {
                                                DataBaseHelper.deleteBodega(DepcApplication.getApplication().getBodegaDao());
                                                DepcApplication.getApplication().getBodegaDao().callBatchTasks(new Callable<Bodega>() {
                                                    @Override
                                                    public Bodega call() throws Exception {
                                                        for (Bodega cl : bodegas) {
                                                            DataBaseHelper.saveBodega(cl, DepcApplication.getApplication().getBodegaDao());
                                                        }
                                                        return null;
                                                    }
                                                });


                                            }

                                            showList();

                                            return;
                                        }
                                    }
                                    }
                                }else{
                                    if (data.getStatus_message() != null){
                                        mensajeError = data.getStatus_message();
                                    }
                                }
                            }

                            showList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showList();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showList();
                    }
                }

                @Override
                public void onFailure(Call<IBodegas.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showList();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showList();

        }
    }


    private void showList(){

        hideProgressWait();

        try {

            List<Bodega> catalogoList = null;
            if (isSearch) {
                String newCondition = "(bodega_id like ('%"+buscarBodega+"%') or descripcion like ('%"+buscarBodega+"%'))";
                catalogoList = DataBaseHelper.getBodegaSearch(DepcApplication.getApplication().getBodegaDao(), "" + newCondition);
            }else{
                catalogoList = DataBaseHelper.getBodega(DepcApplication.getApplication().getBodegaDao());
            }

            isSearch = false;
            if (catalogoList != null) {
                if (catalogoList.size() > 0) {
                    showCatalogo(catalogoList);
                }else{
                    String error = Const.ERROR_DEFAULT;
                    if (isSearch){ error = Const.ERROR_NO_RESULT; }
                    showAlert(error);
                }
            }else{
                String error = Const.ERROR_DEFAULT;
                if (isSearch){ error = Const.ERROR_NO_RESULT; }
                showAlert(error);
            }

            isSearch = false;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
            showAlert(Const.ERROR_DEFAULT);
        }

    }


    private void getProducto(String codigo, String searchParameter){

        showProgressWait();

        String condicionBodega = "";
        condition = "";
        search = "";

        if (codigo.length() > 0) {
            condicionBodega = "and c.bodega_id='"+codigo+"'";
        }

        if (searchParameter.length() > 0) {
            this.search = searchParameter;
            condition = "or a.descripcion like ('%"+search+"%')  or a.codigo_item like (%"+search+"%)";
        }

        ProductosModel model = new ProductosModel();
        model.setBodega(condicionBodega);
        model.setCondicion(condition);
        model.setFiltro("limit "+Const.PARAM_MAX_ROW+" offset 0");
        model.setMetodo("ListaProductosBodegas");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json producto: ---> "+json);

        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IProducto request = DepcApplication.getApplication().getRestAdapter().create(IProducto.class);
            callProducto = request.getProductos(body);
            callProducto.enqueue(new Callback<IProducto.dataProductos>() {
                @Override
                public void onResponse(Call<IProducto.dataProductos> call, Response<IProducto.dataProductos> response) {
                    if (response.isSuccessful()) {

                        dataProducto = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataProducto != null) {
                                if (dataProducto.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataProducto.getData() != null){
                                        if (dataProducto.getData().getListarProductosBodegas()!= null) {
                                            if (dataProducto.getData().getListarProductosBodegas().size() > 0){

                                            final List<Productos> productos;
                                            productos = dataProducto.getData().getListarProductosBodegas().get(0);

                                            if (productos != null) {

                                                DepcApplication.getApplication().getProductosDao().callBatchTasks(new Callable<Productos>() {
                                                    @Override
                                                    public Productos call() throws Exception {
                                                        for (Productos pro : productos) {
                                                            DataBaseHelper.saveProduto(pro, DepcApplication.getApplication().getProductosDao());
                                                        }
                                                        return null;
                                                    }
                                                });

                                            }

                                            showListProducto();

                                            return;
                                        }
                                        }
                                    }

                                }else{
                                    if (dataProducto.getStatus_message() != null){
                                        mensajeError = dataProducto.getStatus_message();
                                    }
                                }
                            }

                            //showAlert(mensajeError);
                            showListProducto();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            //showAlert(Const.ERROR_DEFAULT);
                            showListProducto();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showListProducto();
                    }
                }

                @Override
                public void onFailure(Call<IProducto.dataProductos> call, Throwable t) {
                    hideProgressWait();
                    showListProducto();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showListProducto();

        }
    }


    private void showListProducto(){
        lista.setAdapter(null);


        try{
            List<Productos> productoList = null;
            if (condition.length() > 0) {
                String newCondicion  = "(descripcion like ('%"+search+"%')  or codigo_item like ('%"+search+"%'))";
                if (currentCodigoFamilia.length() > 0) {
                    newCondicion = newCondicion + " and bodega_id = '" + currentCodigoFamilia + "'";
                }

                productoList = DataBaseHelper.getProductoSearch(DepcApplication.getApplication().getProductosDao(),newCondicion);
            }else{

                if (currentCodigoFamilia.length() > 0) {
                    String newCondicion =  "bodega_id = '" + currentCodigoFamilia + "'";
                    productoList = DataBaseHelper.getProductoSearch(DepcApplication.getApplication().getProductosDao(),newCondicion);
                } else {
                    productoList = DataBaseHelper.getProductos(DepcApplication.getApplication().getProductosDao());
                }

            }

            if (productoList != null) {
                if (productoList.size() > 0) {


                    lista.setAdapter(new ProductoGridImagenAdapter(ProductosActivity.this, productoList));
                    final List<Productos> productoListAux = productoList;
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ProductosActivity.this, DetalleProductoActivity.class);
                            intent.putExtra(Const.PRODUCT_DETAIL, productoListAux.get(position));
                            startActivity(intent);
                        }
                    });

                }else{
                    String error = Const.ERROR_DEFAULT;
                    error = Const.ERROR_NO_RESULT;
                    showAlert(error);
                }
            }else{
                String error = Const.ERROR_DEFAULT;
                error = Const.ERROR_NO_RESULT;
                showAlert(error);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Const.ERROR_DEFAULT);
        }

    }

    @Override
    public void doRetry() {

    }

}



