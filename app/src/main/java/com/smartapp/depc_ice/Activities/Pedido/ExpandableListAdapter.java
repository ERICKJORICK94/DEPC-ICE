package com.smartapp.depc_ice.Activities.Pedido;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.smartapp.depc_ice.Activities.Pedido.DetallePedidoActivity;
import com.smartapp.depc_ice.Activities.Productos.Detalle.DetalleProductoActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<Productos>> _listDataChild;
    private String idPedido;
    EditText precio;
    EditText subtotalEdit;
    EditText cantidadEdit;
    EditText edt_iva ;
    EditText edt_total ;
    EditText descuentoEdit;
    private Pedidos pedido;
    private float precioProducto  = 0.0f;
    private String NIVEL_1 = "";
    private String NIVEL_2 = "";
    private String NIVEL_3 = "";
    private Clientes cliente = null;
    private boolean iscotizacion = false;


    public ExpandableListAdapter(Activity context, List<String> listDataHeader,
                                 HashMap<String, List<Productos>> listChildData, String idPedido, boolean iscotizacion) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.idPedido = idPedido;
        this.iscotizacion = iscotizacion;


        try {
            List<Pedidos> pedidos = DataBaseHelper.getPedidosByID(DepcApplication.getApplication().getPedidosDao(), idPedido);
            if (pedidos != null){
                if (pedidos.size() > 0){
                    pedido  = pedidos.get(0);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Productos getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Productos child =  getChild(groupPosition, childPosition);
        final LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_item, null);

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        TextView unidades = (TextView) convertView
                .findViewById(R.id.unidades);

        TextView subtotal = (TextView) convertView
                .findViewById(R.id.subtotal);

        TextView descuento = (TextView) convertView
                .findViewById(R.id.descuento);

        ImageView info = (ImageView) convertView
                .findViewById(R.id.info);

        try {
            if (DataBaseHelper.getDetallePedidoByIDA(DepcApplication.getApplication().getDetallePedidoDao(),child.getIdDetail()) != null){
                if (DataBaseHelper.getDetallePedidoByIDA(DepcApplication.getApplication().getDetallePedidoDao(),child.getIdDetail()).size() > 0){
                    final DetallePedido dt =  DataBaseHelper.getDetallePedidoByIDA(DepcApplication.getApplication().getDetallePedidoDao(),child.getIdDetail()).get(0);

                    if (dt.getCantidad() != null) {
                        float cant = Float.parseFloat(dt.getCantidad());
                        float x = cant - (int) cant;
                        unidades.setText((int) cant+" u.");

                    }

                    subtotal.setText("");
                    if (dt.getSubtotal() != null){
                        if (Utils.isNumberDecimal(dt.getSubtotal())) {
                            subtotal.setText("" + Utils.foramatearMiles(String.format("%.2f", Float.parseFloat(dt.getSubtotal()))));
                        }
                    }

                    if (dt.getPorcentajeDescuento() != null) {
                        float cant = Float.parseFloat(dt.getPorcentajeDescuento());

                        float x = cant - (int) cant;
                        if (x == 0){
                            descuento.setText("("+ (int) cant+"%)");
                        }else{
                            descuento.setText("("+ String.format("%.2f",cant)+"%)");
                        }

                    }

                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            View detailProduct = infalInflater.inflate(R.layout.detail_product_alert, null);
                            final AlertDialog alert = new AlertDialog.Builder(_context).create();
                            alert.setView(detailProduct);

                            ImageView back = (ImageView) detailProduct.findViewById(R.id.back);
                            ImageView info_product = (ImageView) detailProduct.findViewById(R.id.info_product);
                            ImageView borrar = (ImageView) detailProduct.findViewById(R.id.borrar);
                            ImageView edit_product = (ImageView) detailProduct.findViewById(R.id.edit_product);
                            TextView codigo= (TextView) detailProduct.findViewById(R.id.codigo);
                            TextView nombre = (TextView) detailProduct.findViewById(R.id.nombre);
                            TextView precio_unitario = (TextView) detailProduct.findViewById(R.id.precio_unitario);
                            TextView descuento = (TextView) detailProduct.findViewById(R.id.descuento);
                            TextView valor_descuento = (TextView) detailProduct.findViewById(R.id.valor_descuento);
                            TextView subtotal = (TextView) detailProduct.findViewById(R.id.subtotal);
                            TextView cantidad = (TextView) detailProduct.findViewById(R.id.cantidad);
                            Utils.imageColor(info_product, _context.getResources().getColor(R.color.White));

                            edit_product.setVisibility(View.VISIBLE);

                            codigo.setText(""+child.getCodigo_item());
                            nombre.setText(""+child.getDescripcion());
                            precio_unitario.setText(""+Utils.foramatearMiles(dt.getPrecioUnitario()) /*Utils.roundFloat(Double.parseDouble(dt.getPrecioUnitario()),2)*/);

                            if (dt.getPrecioUnitario() != null){
                                if (dt.getPrecioUnitario().length() > 0){
                                    if (Utils.isNumberDecimal(dt.getPrecioUnitario())){
                                        precioProducto = Float.parseFloat(dt.getPrecioUnitario());
                                    }else if (Utils.isNumber(dt.getPrecioUnitario())){
                                        precioProducto = Float.parseFloat(dt.getPrecioUnitario());
                                    }
                                }
                            }

                            descuento.setText("-"+dt.getPorcentajeDescuento()+"%");
                            float valor = Float.parseFloat(""+dt.getPrecioUnitario()) - (Float.parseFloat(""+dt.getPrecioUnitario()) * (Float.parseFloat(dt.getPorcentajeDescuento()) / 100));
                            valor_descuento.setText(""+Utils.foramatearMiles(""+Utils.roundFloat(valor,4)));
                            subtotal.setText(""+Utils.foramatearMiles(dt.getSubtotalNeto()));
                            cantidad.setText(""+dt.getCantidad());

                            if (dt.getCantidad() != null) {
                                if (Utils.isNumber(""+dt.getCantidad())){
                                    int cantidadNume = Integer.valueOf(""+dt.getCantidad());
                                    cantidad.setText(""+cantidadNume);
                                }else{
                                    if (Utils.isNumberDecimal(""+dt.getCantidad())){
                                        float cantidadNum = Float.valueOf(""+dt.getCantidad());
                                        cantidad.setText(""+(int)cantidadNum);
                                    }
                                }
                            }



                            float cant = Float.parseFloat(dt.getCantidad());

                            edit_product.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (alert != null){
                                        alert.dismiss();
                                        try {
                                            comprar(child, dt.getId());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });



                            try {
                                if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(),pedido.getCliente()) != null){
                                    if (DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(),pedido.getCliente()).size() > 0){
                                        cliente = DataBaseHelper.getClientesByCODCLiente(DepcApplication.getApplication().getClientesDao(),pedido.getCliente()).get(0);

                                    }

                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            info_product.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(_context, DetalleProductoActivity.class);
                                    intent.putExtra(Const.PRODUCT_DETAIL, child);
                                    _context.startActivity(intent);
                                }
                            });

                            borrar.setVisibility(View.VISIBLE);


                            borrar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    AlertDialog.Builder alertDelete = new AlertDialog.Builder(
                                            _context);
                                    alertDelete.setTitle("Alerta");
                                    alertDelete.setMessage("¿Está seguro que desea borrar el producto");
                                    alertDelete.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            try {

                                                DataBaseHelper.deleteDetallePedidoByProductoCodigo(DepcApplication.getApplication().getDetallePedidoDao(),""+dt.getId());

                                                List<Productos> productos = DataBaseHelper.getProductoByCOD(DepcApplication.getApplication().getProductosDao(), ""+child.getCodigo_item());
                                                Productos producto = productos.get(0);


                                                if (_context instanceof DetallePedidoActivity){
                                                    ((DetallePedidoActivity) _context).getPedidos();
                                                }

                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                            if (_context instanceof  DetallePedidoActivity){
                                                ((DetallePedidoActivity) _context).getPedidos();
                                            }

                                            dialog.dismiss();
                                            alert.dismiss();


                                        }
                                    });
                                    alertDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                        }
                                    });

                                    alertDelete.show();

                                }
                            });

                            back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                }
                            });


                            alert.show();




                        }
                    });

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtListChild.setText(""+child.getDescripcion());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.list_group, null);

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        TextView unidades = (TextView) convertView
                .findViewById(R.id.unidades);


        ImageView icon = (ImageView) convertView
                .findViewById(R.id.icon);

        int unidad = this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();

        if(unidad == 1) {
            unidades.setText("" + unidad+" item");
        }else{
            unidades.setText("" + unidad+" items");
        }

        if (isExpanded){
            icon.setImageDrawable(this._context.getResources().getDrawable(R.drawable.less));
        }else{
            icon.setImageDrawable(this._context.getResources().getDrawable(R.drawable.more));
        }

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }





    private void comprar(final Productos producto, int idDetallePedido) throws SQLException {


        LayoutInflater layoutInflater;
        layoutInflater = LayoutInflater.from(_context);
        View comprarProduct = layoutInflater.inflate(R.layout.pedir_product_alert, null);
        final AlertDialog alert = new AlertDialog.Builder(_context).create();
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
        Utils.imageColor(info_product,_context.getResources().getColor(R.color.White));
        alert.setCanceledOnTouchOutside(false);


        delete.setVisibility(View.GONE);
        precio.setEnabled(false);

        int id = 0;
        if (DataBaseHelper.getDetallePedidoByIDA(DepcApplication.getApplication().getDetallePedidoDao(), idDetallePedido) != null) {
            if (DataBaseHelper.getDetallePedidoByIDA(DepcApplication.getApplication().getDetallePedidoDao(), idDetallePedido).size() > 0) {

                DetallePedido detalleRetorno = DataBaseHelper.getDetallePedidoByIDA(DepcApplication.getApplication().getDetallePedidoDao(), idDetallePedido).get(0);
                id = detalleRetorno.getId();


                double cantidadPrint = Double.parseDouble(detalleRetorno.getCantidad());

                cantidadEdit.setText("" + (int) cantidadPrint);
                descuentoEdit.setText(""+detalleRetorno.getPorcentajeDescuento());
                edt_iva.setText(""+ String.format("%.2f", Float.parseFloat(detalleRetorno.getIva())));
                edt_total.setText(""+String.format("%.2f", Float.parseFloat(detalleRetorno.getTotal())));
                subtotalEdit.setText(""+String.format("%.2f", Float.parseFloat(detalleRetorno.getSubtotal())));
                delete.setVisibility(View.VISIBLE);
            }
        }

        precioProducto = Float.parseFloat(producto.getPvp());
        final float iva1 = 12;



        cantidadEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                validateCalcular(precioProducto, iva1,producto);
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
                        AlertDialog alertDialog = new AlertDialog.Builder(_context).create();
                        alertDialog.setTitle("Atención");
                        alertDialog.setMessage("El Descuento debe ser menor al 100%");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();


                        descuentoEdit.setText("");
                        return;
                    }
                }

                validateCalcular(precioProducto, iva1, producto);
            }
        });

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCalcular(precioProducto, iva1,producto);
            }
        });

        final int auxId = id;
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

                    precioFijo = precioProducto;

                    subtotal = (cantidad * precioFijo);

                    subtotalNeto = (cantidad * precioFijo) - ((cantidad * precioFijo) * (descuento / 100));


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
                    total = (subtotalNeto) + subtotalIva;
                    subtotalEdit.setText("" + subtotalNeto);

                    try {

                        DetallePedido detalle = new DetallePedido();

                        if (auxId != 0) {
                            detalle.setId(auxId);
                        }

                        detalle.setIdPedido("" + pedido.getId());
                        detalle.setNumeroItem("" + producto.getCodigo_item());
                        detalle.setCodigo("" + producto.getCodigo_item());
                        detalle.setDescripcion("" + producto.getDescripcion());
                        detalle.setTipoInventario("" );
                        detalle.setCantidad("" + cantidad);
                        detalle.setCosto(""+producto.getPvp());
                        detalle.setPrecioUnitario("" + producto.getPvp());
                        detalle.setSubtotal("" +  String.format("%.2f", subtotal));
                        detalle.setSubtotalNeto("" + String.format("%.2f", subtotalNeto));
                        detalle.setSubtotalNetoFijo("" +  String.format("%.2f", subtotalNeto));
                        detalle.setPorcentajeDescuento("" +  String.format("%.2f", descuento));
                        double ds = ((cantidad * precioFijo) * (descuento / 100));
                        //ds = Utils.roundFloat(ds,4);
                        detalle.setDescuento("" +  String.format("%.2f", ds));
                        detalle.setPorcentajeDescuentoAdicional("0");
                        detalle.setDescuentoAdicional("0");
                        detalle.setNeto("" +  String.format("%.2f", subtotalNeto));
                        detalle.setPorcentajeIva("" +  String.format("%.2f", iva )  /*Utils.roundFloat(iva,2)*/);
                        detalle.setIva("" +  String.format("%.2f", subtotalIva ) /*Utils.roundFloat(subtotalIva,2)*/ );
                        detalle.setTotal("" +  String.format("%.2f", total ) /*Utils.roundFloat(total,2)*/ );
                        detalle.setPromocion("0");
                        DataBaseHelper.saveDetallePedido(detalle, DepcApplication.getApplication().getDetallePedidoDao());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    //getPedidos();
                    if (_context instanceof  DetallePedidoActivity){
                        ((DetallePedidoActivity) _context).getPedidos();
                    }
                    alert.dismiss();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDelete = new AlertDialog.Builder(
                        _context);
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
                        if (_context instanceof  DetallePedidoActivity){
                            ((DetallePedidoActivity) _context).getPedidos();
                        }
                        alert.dismiss();

                    }
                });
                alertDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if (_context instanceof  DetallePedidoActivity){
                            ((DetallePedidoActivity) _context).getPedidos();
                        }
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
                Intent intent = new Intent(_context, DetalleProductoActivity.class);
                intent.putExtra(Const.PRODUCT_DETAIL, producto);
                _context.startActivity(intent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_context instanceof  DetallePedidoActivity){
                    ((DetallePedidoActivity) _context).getPedidos();
                }
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
                //precioFijo = Float.parseFloat(""+precio.getText().toString());
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
            //subtotalEdit.setText(""+subtotalNeto);
            subtotalEdit.setText(""+String.format("%.2f",subtotalNeto));

            subtotalIva = (subtotalNeto * (iva / 100));
            //subtotalIva = Utils.roundFloat(subtotalIva,4);
            //edt_iva.setText(""+subtotalIva);
            edt_iva.setText(""+String.format("%.2f",subtotalIva));

            total = (subtotalNeto) + subtotalIva;
            //total = Utils.roundFloat(total,4);
            //edt_total.setText(""+total);
            edt_total.setText(""+String.format("%.2f",total));

        //}
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}