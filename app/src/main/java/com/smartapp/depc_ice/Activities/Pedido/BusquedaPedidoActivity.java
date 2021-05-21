package com.smartapp.depc_ice.Activities.Pedido;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Productos.Adapter.ProductoAdapter;
import com.smartapp.depc_ice.Activities.Productos.Adapter.ProductoGridImagenAdapter;
import com.smartapp.depc_ice.Activities.Productos.Detalle.DetalleProductoActivity;
import com.smartapp.depc_ice.Activities.Productos.ProductosActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Interface.IProducto;
import com.smartapp.depc_ice.Models.ProductosModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusquedaPedidoActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;

    private Call<IProducto.dataProductos> callProducto;
    private IProducto.dataProductos dataProducto;

    private boolean isSearch = false;
    private String condition = "";
    private ListView lista;
    private EditText search_product;
    private Pedidos pedido;
    private String tipoInventario = "G";
    EditText precio;
    EditText subtotalEdit;
    EditText cantidadEdit;
    EditText edt_iva ;
     EditText edt_total ;
    EditText descuentoEdit;
    float precioProducto = 0.0f;
    private TextView cantidades_pedidos;
    private TextView total_pedido;
    private String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Richard Asencio Vargas

        layout = addLayout(R.layout.busqueda_producto);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        lista = (ListView) layout.findViewById(R.id.lista);
        search_product = (EditText) layout.findViewById(R.id.search_product);
        cantidades_pedidos = (TextView) layout.findViewById(R.id.cantidades_pedidos);
        total_pedido = (TextView) layout.findViewById(R.id.total_pedido);

        if(getIntent().getExtras() != null){
            pedido = (Pedidos) getIntent().getExtras().getSerializable(Const.PEDIDO_PASS);
        }


        if (pedido == null){
            showAlert(Const.ERROR_DEFAULT);
            return;
        }

        getPedidos();

        search_product.clearFocus();

        search_product.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                lista.setAdapter(null);
                getProducto(pedido.getBodega(),""+v.getText().toString().trim());
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


        getProducto(pedido.getBodega(),"");

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
            this.search = searchParameter.toUpperCase();
            condition = "and a.descripcion like ('%"+search+"%')  or a.codigo_item like '%"+search+"%'";
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
                    String newCondicion  = "descripcion like ('%"+search+"%')  or codigo_item like '%"+search+"%'";
                    if (pedido.getBodega().length() > 0) {
                        newCondicion = newCondicion + " and bodega_id = '" + pedido.getBodega() + "'";
                    }

                    productoList = DataBaseHelper.getProductoSearch(DepcApplication.getApplication().getProductosDao(),newCondicion);
                }else{

                    if (pedido.getBodega().length() > 0) {
                        String newCondicion =  "bodega_id = '" + pedido.getBodega() + "'";
                        productoList = DataBaseHelper.getProductoSearch(DepcApplication.getApplication().getProductosDao(),newCondicion);
                    } else {
                        productoList = DataBaseHelper.getProductos(DepcApplication.getApplication().getProductosDao());
                    }

                }

                if (productoList != null) {
                    if (productoList.size() > 0) {


                        lista.setAdapter(new ProductoAdapter(BusquedaPedidoActivity.this, productoList, ""+pedido.getId()));
                        final List<Productos> productoListAux = productoList;
                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    comprar(productoListAux.get(position));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
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


    private void comprar(final Productos producto) throws SQLException {


        if ( producto.getPvp() == null){
            producto.setPvp("0.0");
        }

        precioProducto = Float.parseFloat(producto.getPvp());

        if ( producto.getExistencia() == null){  producto.setExistencia("0"); }
        DataBaseHelper.saveProduto(producto ,DepcApplication.getApplication().getProductosDao());


        View comprarProduct = layoutInflater.inflate(R.layout.pedir_product_alert, null);
        final AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setView(comprarProduct);

        ImageView back = (ImageView) comprarProduct.findViewById(R.id.back);
        ImageView info_product = (ImageView) comprarProduct.findViewById(R.id.info_product);
        TextView codigo = (TextView) comprarProduct.findViewById(R.id.codigo);
        TextView name = (TextView) comprarProduct.findViewById(R.id.name);
        TextView grupo = (TextView) comprarProduct.findViewById(R.id.grupo);
        TextView txtD1 = (TextView) comprarProduct.findViewById(R.id.txtD1);
        TextView stock = (TextView) comprarProduct.findViewById(R.id.stock);
        precio = (EditText) comprarProduct.findViewById(R.id.precio);
        subtotalEdit  = (EditText) comprarProduct.findViewById(R.id.subtotal);
        cantidadEdit = (EditText) comprarProduct.findViewById(R.id.cantidad);

        edt_iva = (EditText) comprarProduct.findViewById(R.id.edt_iva);
        edt_total = (EditText) comprarProduct.findViewById(R.id.edt_total);
        descuentoEdit = (EditText) comprarProduct.findViewById(R.id.descuento);

        Button calcular = (Button) comprarProduct.findViewById(R.id.calcular);
        AppCompatImageView save = (AppCompatImageView) comprarProduct.findViewById(R.id.save);
        final AppCompatImageView delete = (AppCompatImageView) comprarProduct.findViewById(R.id.delete);
        Utils.imageColor(info_product,getResources().getColor(R.color.White));
        alert.setCanceledOnTouchOutside(false);


        delete.setVisibility(View.GONE);

        final float iva1 = 12;

        cantidadEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCalcular(precioProducto, iva1,producto);
            }

        });


        precio.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                validateCalcular(precioProducto, iva1, producto);
            }
        });



        descuentoEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(Utils.isNumber(descuentoEdit.getText().toString())){
                    int number = Integer.parseInt(descuentoEdit.getText().toString());
                    if (number > 100){
                        showAlert("El Descuento debe ser menor al 100%");
                        descuentoEdit.setText("");
                        return;
                    }
                }

                validateCalcular(precioProducto, iva1,producto);
            }
        });

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCalcular(precioProducto, iva1,producto);
            }
        });

        //final int auxId = id;
        final int auxId = 0;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float descuento = 0;
                float cantidad = 0;
                double subtotal = 0;
                double subtotalNeto = 0;
                double total = 0;
                double iva = iva1;
                double subtotalIva = 0;
                double precioFijo = 0;


                if (cantidadEdit.getText().toString().length() > 0) {


                    if (descuentoEdit.getText().toString().length() > 0) {
                        descuento = Float.parseFloat("" + descuentoEdit.getText().toString());
                    }

                    if (cantidadEdit.getText().toString().length() > 0) {
                        cantidad = Float.parseFloat("" + cantidadEdit.getText().toString());

                    }

                    if (precio.getText().toString().length() > 0) {
                        //precioFijo = Float.parseFloat("" + precio.getText().toString());
                        String precioConversion = precio.getText().toString().replace(",",".");
                        precioFijo = Float.parseFloat(""+precioConversion);
                    }else{
                        precioFijo = precioProducto;
                    }

                    subtotal = (cantidad * precioFijo);
                    //subtotal = Utils.roundFloat(subtotal,4);

                    subtotalNeto = (cantidad * precioFijo) - ((cantidad * precioFijo) * (descuento / 100));
                    //subtotalNeto = Utils.roundFloat(subtotalNeto,4);



                    try {
                        if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()) != null){
                            if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).size() > 0) {
                                Clientes cl = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).get(0);
                                if (cl.getCobrar_iva() != null){
                                    if (cl.getCobrar_iva().toLowerCase().equals("0")){
                                        iva = 0;
                                    }
                                }

                            }

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    subtotalIva = (subtotalNeto * (iva / 100));
                    //subtotalIva = Utils.roundFloat(subtotalIva,4);
                    total = (subtotalNeto) + subtotalIva;
                    //total = Utils.roundFloat(total,4);
                    subtotalEdit.setText("" + subtotalNeto);

                    try {

                        DetallePedido detalle = new DetallePedido();

                        detalle.setIdPedido("" + pedido.getId());
                        detalle.setNumeroItem("" + producto.getCodigo_item());
                        detalle.setCodigo("" + producto.getCodigo_item());
                        detalle.setDescripcion("" + producto.getDescripcion());
                        detalle.setTipoInventario("");
                        detalle.setCantidad("" + cantidad);
                        detalle.setCosto(""+producto.getCosto());
                        detalle.setPrecioUnitario("" + producto.getPvp());
                        detalle.setSubtotal("" + String.format("%.2f",subtotal));
                        detalle.setSubtotalNeto("" + String.format("%.2f", subtotalNeto));
                        detalle.setSubtotalNetoFijo("" + String.format("%.2f" , subtotalNeto));
                        detalle.setPorcentajeDescuento("" + String.format("%.2f", descuento));
                        double ds = ((cantidad * precioFijo) * (descuento / 100));
                        //ds = Utils.roundFloat(ds,4);
                        detalle.setDescuento("" + String.format("%.2f", ds ));
                        detalle.setPorcentajeDescuentoAdicional("0");
                        detalle.setDescuentoAdicional("0");
                        detalle.setNeto("" + String.format("%.2f", subtotalNeto));
                        detalle.setPorcentajeIva("" + String.format("%.2f", iva)  /*Utils.roundFloat(iva,2)*/);
                        detalle.setIva("" + String.format("%.2f" , subtotalIva ) /*Utils.roundFloat(subtotalIva,2)*/ );
                        detalle.setTotal("" + String.format("%.2f", total ) /*Utils.roundFloat(total,2)*/ );
                        detalle.setPromocion("0");

                        DataBaseHelper.saveDetallePedidoCreate(detalle, DepcApplication.getApplication().getDetallePedidoDao());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    getPedidos();
                    alert.dismiss();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDelete = new AlertDialog.Builder(
                        BusquedaPedidoActivity.this);
                alertDelete.setTitle("Alerta");
                alertDelete.setMessage("¿Está seguro que desea borrar el producto");
                alertDelete.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            DataBaseHelper.deleteDetallePedidoByProductoCodigo(DepcApplication.getApplication().getDetallePedidoDao(),producto.getCodigo_item(), ""+pedido.getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                        getPedidos();
                        alert.dismiss();

                    }
                });
                alertDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        getPedidos();
                        alert.dismiss();
                    }
                });

                alertDelete.show();



            }
        });


        precio.setText(""+precioProducto);

        codigo.setText(""+producto.getCodigo_item());
        name.setText(""+producto.getDescripcion());
        grupo.setText("BODEGA: "+producto.getDescripcion_bodega());


        if (producto.getExistencia() != null) {
            if (Utils.isNumber(""+producto.getExistencia())){
                int cantidad = Integer.valueOf(""+producto.getExistencia());
                stock.setText(""+cantidad+"\nUNIDADES");
            }else{
                if (Utils.isNumberDecimal(""+producto.getExistencia())){
                    float cantidad = Float.valueOf(""+producto.getExistencia());
                    stock.setText(""+(int)cantidad+"\nUNIDADES");
                }
            }

        }

        info_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusquedaPedidoActivity.this, DetalleProductoActivity.class);
                intent.putExtra(Const.PRODUCT_DETAIL, producto);
                startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPedidos();
                alert.dismiss();
            }
        });

        alert.show();

     }

    private void validateCalcular(float precioProducto, float iva1, Productos producto){
        //if (cantidadEdit.getText().toString().length() > 0){
            float descuento = 0;
            float cantidad = 0;
            double subtotal = 0;
            double subtotalNeto = 0;
            double subtotalIva = 0;
            double total = 0;
            double iva = iva1;
            double precioFijo = 0;

            if (descuentoEdit.getText().toString().length() > 0){
                descuento = Float.parseFloat(""+descuentoEdit.getText().toString());
            }

            if (cantidadEdit.getText().toString().length() > 0){
                cantidad = Float.parseFloat(""+cantidadEdit.getText().toString());

            }

            if (precio.getText().toString().length() > 0){
                String precioConversion = precio.getText().toString().replace(",",".");
                precioFijo = Float.parseFloat(""+precioConversion);
            }else{
                precioFijo = precioProducto;
            }

            try {
                if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()) != null){
                    if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).size() > 0) {
                        Clientes cl = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(), pedido.getCliente()).get(0);
                        if (cl.getCobrar_iva() != null){
                            if (cl.getCobrar_iva().toLowerCase().equals("0")){
                                iva = 0;
                            }
                        }

                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


            subtotalNeto = (cantidad * precioFijo) - ((cantidad * precioFijo) * (descuento / 100));
            subtotal = (cantidad * precioFijo);
            //subtotal = Utils.roundFloat(subtotal,4);
            //subtotalEdit.setText(""+subtotal);
            //subtotalEdit.setText(""+Utils.roundFloat(subtotalNeto,4));
            subtotalEdit.setText(""+ String.format("%.2f",subtotalNeto));

            subtotalIva = (subtotalNeto * (iva / 100));
            //subtotalIva = Utils.roundFloat(subtotalIva,4);
            edt_iva.setText(""+ String.format("%.2f",subtotalIva));

            total = (subtotalNeto) + subtotalIva;
            //total = Utils.roundFloat(total,4);
            edt_total.setText(""+ String.format("%.2f",total));

        //}
    }

    private void getPedidos(){

        try {
            if(DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(),""+pedido.getId()) != null){
                List<Pedidos> pedidos = DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(),""+pedido.getId());
                float cantidad = 0;
                double subtotal = 0;
                for(Pedidos pd : pedidos){

                    if (DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pd.getId()) != null){
                        List<DetallePedido> detalles = DataBaseHelper.getDetallePedidoByID(DepcApplication.getApplication().getDetallePedidoDao(), ""+pd.getId());
                        for(DetallePedido dt : detalles){
                            if (dt.getCantidad() != null) {
                                cantidad += Float.parseFloat(""+dt.getCantidad());
                                if (dt.getSubtotal() != null)
                                subtotal += Double.parseDouble(""+dt.getSubtotal());
                            }
                        }

                    }
                }

                float x = cantidad - (int) cantidad;
                if (x == 0){
                    cantidades_pedidos.setText(""+ (int) cantidad);
                }else{
                    cantidades_pedidos.setText(""+ String.format("%.2f",cantidad));
                }

                double x1 = subtotal - (int) subtotal;
                if (x1 == 0){
                    total_pedido.setText(""+ Utils.foramatearMiles(""+((int) subtotal)));
                }else{
                    total_pedido.setText(""+ Utils.foramatearMiles(String.format("%.2f",subtotal)));
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void doRetry() {

    }






}



