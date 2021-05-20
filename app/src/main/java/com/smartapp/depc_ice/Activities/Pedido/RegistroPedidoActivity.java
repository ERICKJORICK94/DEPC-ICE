package com.smartapp.depc_ice.Activities.Pedido;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.DetalleCliente.DetalleUbicacionClienteActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.EditarClientesActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.MantDireccionActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IBodegas;
import com.smartapp.depc_ice.Interface.IDirecciones;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.DireccionesModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.PhotoViewer;
import com.smartapp.depc_ice.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistroPedidoActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{


    //UbicacionClienteTask
    //DireccionEntrega    --- tabla
    private View layout;
    private LayoutInflater layoutInflater;
    private Clientes cliente;

    private LinearLayout linear_fondo;
    private EditText cliente_name;
    private EditText cupo;
    private EditText cupo_mes;
    private Spinner spinner_bodega;
    private Spinner spinner_forma_pago;
    private EditText ruc;
    private EditText persona_recibe;
    private EditText direccion;
    private EditText telefono;
    private EditText vendedor;
    private EditText comentario;
    private Button crear;
    private TextView crear_pedido, agregar,ver_foto;
    private List<Bodega> bodegas;
    private Spinner spinner_direccion;
    private ImageButton crear_ubicacion;
    private Pedidos pedido = null;
    private boolean isActualizar = false;
    private Usuario user ;
    private Call<IBodegas.dataBodega> call;
    private IBodegas.dataBodega data;
    private int indexBodega = -1;
    boolean flagBodega = false;
    boolean flagDirecciones = false;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private Call<IDirecciones.dataBodega> callDirecciones;
    private IDirecciones.dataBodega dataDirecciones;
    private int indexDirecciones = -1;
    private int indexFormaPago = 0;
    private List<Direcciones> direccionesList;
    private Bitmap bitmap;

    String FormaPago[] = {"CONTADO","CRÉDITO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.registro_pedido);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cliente_name = (EditText) layout.findViewById(R.id.cliente_name);
        cupo = (EditText) layout.findViewById(R.id.cupo);
        cupo_mes = (EditText) layout.findViewById(R.id.cupo_mes);
        cupo_mes = (EditText) layout.findViewById(R.id.cupo_mes);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);
        persona_recibe = (EditText) layout.findViewById(R.id.persona_recibe);
        spinner_bodega = (Spinner) layout.findViewById(R.id.spinner_bodega);
        ruc = (EditText) layout.findViewById(R.id.ruc);
        crear_pedido = (TextView) layout.findViewById(R.id.crear_pedido);
        direccion = (EditText) layout.findViewById(R.id.direccion);
        linear_fondo = (LinearLayout) layout.findViewById(R.id.linear_fondo);
        telefono = (EditText) layout.findViewById(R.id.telefono);
        vendedor = (EditText) layout.findViewById(R.id.vendedor);
        comentario = (EditText) layout.findViewById(R.id.comentario);
        spinner_direccion = (Spinner) layout.findViewById(R.id.spinner_direccion);
        spinner_forma_pago = (Spinner) layout.findViewById(R.id.spinner_forma_pago);
        crear = (Button) layout.findViewById(R.id.crear);
        crear_ubicacion = (ImageButton) layout.findViewById(R.id.crear_ubicacion);

        try {
            List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0){
                    user = usuarios.get(0);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        cliente = DepcApplication.getApplication().getCliente();

            if (getIntent().getSerializableExtra(Const.ID_PEDIDO) != null) {
                pedido = (Pedidos) getIntent().getSerializableExtra(Const.ID_PEDIDO);
                crear.setText("ACTUALIZAR");
                isActualizar = true;
                crear_pedido.setVisibility(View.GONE);

                if (pedido != null){
                    if (pedido.getComentario() != null){
                        comentario.setText(""+pedido.getComentario());
                    }
                }
            }

        if (cliente != null){

            if (cliente.getNombre_comercial() != null){
                cliente_name.setText(""+cliente.getNombre_comercial());
            }

            if (cliente.getMonto_credito() != null){
                cupo.setText(""+cliente.getMonto_credito());
            }

            if (cliente.getTercero_id() != null){
                ruc.setText(""+cliente.getTercero_id());
            }

            if (cliente.getDireccion() != null){
                direccion.setText(""+cliente.getDireccion());
            }

            if (cliente.getTelefono1() != null){
                telefono.setText(""+cliente.getTelefono1());
            }

            if(user != null){
                if (user.getNombrescompletos() != null){
                    vendedor.setText(""+user.getNombrescompletos());
                }
            }

            crear_ubicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(RegistroPedidoActivity.this, DetalleUbicacionClienteActivity.class);
                    startActivity(intent);
                }
            });

        }


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isActualizar){

                    if (indexBodega == -1){
                        Toast.makeText(RegistroPedidoActivity.this, "Seleccione una bodega para continuar", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (indexDirecciones == -1){
                        Toast.makeText(RegistroPedidoActivity.this, "Seleccione una Dirección para continuar, caso contrario cree una.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    pedido.setComentario(""+comentario.getText().toString());
                    if (bodegas != null) {
                        if (bodegas.size() > 0) {
                            pedido.setBodega("" + bodegas.get(indexBodega).getBodega_id());
                            pedido.setNombreBodega("" + bodegas.get(indexBodega).getDescripcion());
                        }
                    }

                    if (direccionesList != null){
                        if (direccionesList.size() > 0) {

                            pedido.setDireccionEntrega("" + direccionesList.get(indexDirecciones).getDireccion_envio());
                            pedido.setCodigo_direccione_entrega(""+direccionesList.get(indexDirecciones).getId());
                        }
                    }

                    pedido.setForma_pago("" + FormaPago[indexFormaPago]);
                    pedido.setForma_pago_id("4");
                    if (indexFormaPago == 1) {
                        pedido.setForma_pago_id("5");
                    }

                    try {
                        DataBaseHelper.updatePedido(pedido,DepcApplication.getApplication().getPedidosDao());
                        finish();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }



                }else{
                    //CREAR
                    if (indexBodega == -1){
                        Toast.makeText(RegistroPedidoActivity.this, "Seleccione una bodega para continuar", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (indexDirecciones == -1){
                        Toast.makeText(RegistroPedidoActivity.this, "Seleccione una Dirección para continuar, caso contrario cree una.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (pedido == null) {
                        pedido = new Pedidos();

                        if (bodegas != null) {
                            if (bodegas.size() > 0) {
                                pedido.setBodega("" + bodegas.get(indexBodega).getBodega_id());
                                pedido.setNombreBodega("" + bodegas.get(indexBodega).getDescripcion());
                            }
                        }

                        if (cliente != null){
                            if (cliente.getCodigo_cliente_id() != null){
                                pedido.setCliente(""+cliente.getCodigo_cliente_id());
                            }

                            if (cliente.getNombre_comercial() != null){
                                pedido.setNombreCliente(""+cliente.getNombre_comercial());
                            }

                            if (cliente.getTercero_id() != null){
                                pedido.setRucCliente(""+cliente.getTercero_id());
                            }

                            if (cliente.getDireccion() != null){
                                pedido.setDireccionCliente(""+cliente.getDireccion());
                            }

                            if (cliente.getTelefono1() != null){
                                pedido.setTelefonoCliente(""+cliente.getTelefono1());
                            }
                        }

                        pedido.setFecha(""+Utils.getFecha());
                        pedido.setVendedor(user.getUsuario());
                        pedido.setNombreVendedor(user.getNombrescompletos());
                        pedido.setComentario(""+comentario.getText().toString());
                        pedido.setSubtotal("0.00");
                        pedido.setDescuento("0.00");
                        pedido.setIva("0.00");
                        pedido.setTotal("0.00");

                        pedido.setForma_pago("" + FormaPago[indexFormaPago]);
                        pedido.setForma_pago_id("4");
                        if (indexFormaPago == 1) {
                            pedido.setForma_pago_id("5");
                        }

                        if (direccionesList != null){
                            if (direccionesList.size() > 0) {

                                pedido.setDireccionEntrega("" + direccionesList.get(indexDirecciones).getDireccion_envio());
                                pedido.setCodigo_direccione_entrega(""+direccionesList.get(indexDirecciones).getId());
                            }
                        }

                        try {
                            int id = DataBaseHelper.savePedidoID(pedido,DepcApplication.getApplication().getPedidosDao());
                            pedido.setId(id);

                            Intent intent = new Intent(RegistroPedidoActivity.this, DetallePedidoActivity.class);
                            intent.putExtra(Const.ID_PEDIDOS,""+id);
                            startActivity(intent);
                            finish();

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                    //CREAR
                }

            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {

                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2296);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2296);
                        }

                    }else{

                        selectPick();

                    }

                } else {

                    selectPick();

                }



            }
        });

        ver_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap != null){
                    if (Utils.convertBase64String(bitmap) != null) {
                        Intent intent = new Intent(RegistroPedidoActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" +Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                }else{
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });

        getBodegas();

        showFormaPago();

    }

    private void selectPick(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Cámara", "Galería"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RegistroPedidoActivity.this);
                builder.setTitle("Subir Foto");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Cámara")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Galería")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                //Toast.makeText(GridFotosActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
                }
        } catch (Exception e) {
            Toast.makeText(RegistroPedidoActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    selectPick();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                Log.e(TAG,"Base64: "+Utils.convertBase64String(bitmap));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                Log.e(TAG,"Base64: "+Utils.convertBase64String(bitmap));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                selectPick();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showbodegas(){

        hideProgressWait();

        try {

            bodegas  = DataBaseHelper.getBodega(DepcApplication.getApplication().getBodegaDao());


            indexBodega = -1;


            int contador = 0;
            if (bodegas != null) {
                if (bodegas.size() > 0) {
                    List<String> items= new ArrayList<String>();
                    for (Bodega z : bodegas){
                        items.add(z.getDescripcion());

                        if (pedido != null){
                            if (pedido.getBodega() != null){
                                if (pedido.getBodega().equals(""+z.getBodega_id())){
                                    indexBodega = contador;
                                }
                            }
                        }
                        contador++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    spinner_bodega.setAdapter(adapter);
                    if (indexBodega == -1) {
                        indexBodega = 0;
                    }else{
                        spinner_bodega.setSelection(indexBodega);
                    }
                    spinner_bodega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (flagBodega) {
                                indexBodega = position;
                            }

                            flagBodega = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());

        }

        getDirecciones();

    }


    private void showDirecciones(){

        hideProgressWait();

        try {

            direccionesList  = DataBaseHelper.getDireccionesBYIdClienteOrder(DepcApplication.getApplication().getDireccionesDao(),""+cliente.getCodigo_cliente_id());

            indexDirecciones = -1;
            int contador = 0;
            if (direccionesList != null) {
                if (direccionesList.size() > 0) {
                    List<String> items= new ArrayList<String>();
                    for (Direcciones z : direccionesList){
                        items.add(z.getDireccion_envio());

                        if (pedido != null){
                            if (pedido.getCodigo_direccione_entrega() != null){
                                if (pedido.getCodigo_direccione_entrega().equals(""+z.getId())){
                                    indexDirecciones = contador;
                                }
                            }
                        }
                        contador++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    spinner_direccion.setAdapter(adapter);
                    if (indexDirecciones == -1) {
                        indexDirecciones = 0;
                    }else{
                        spinner_direccion.setSelection(indexDirecciones);
                    }
                    spinner_direccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (flagDirecciones) {
                                indexDirecciones = position;
                            }

                            flagDirecciones = true;


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());

        }

    }


    private void showFormaPago(){

        indexFormaPago = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, FormaPago);
        spinner_forma_pago.setAdapter(adapter);
        if (pedido != null){
            if (pedido.getForma_pago_id() != null){
                if (pedido.getForma_pago_id().equals("5")){
                    indexFormaPago = 1;
                }
            }
        }

        spinner_forma_pago.setSelection(indexFormaPago);
        spinner_forma_pago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexFormaPago = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void getDirecciones(){

        showProgressWait();

        //JSON SEND
        DireccionesModel model = new DireccionesModel();
        model.setCondicion(""+cliente.getCodigo_cliente_id());
        model.setFiltro("");
        model.setMetodo("ListaClientesDirecciones");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IDirecciones request = DepcApplication.getApplication().getRestAdapter().create(IDirecciones.class);
            callDirecciones = request.getDirecciones(body);
            callDirecciones.enqueue(new Callback<IDirecciones.dataBodega>() {
                @Override
                public void onResponse(Call<IDirecciones.dataBodega> call, Response<IDirecciones.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataDirecciones = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataDirecciones != null) {
                                if (dataDirecciones.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataDirecciones.getData() != null){
                                        if (dataDirecciones.getData().getListaClientes() != null) {
                                            if (dataDirecciones.getData().getListaClientes().size() > 0) {


                                                final List<Direcciones> bodegas;
                                                bodegas = dataDirecciones.getData().getListaClientes().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteDireccionesById(DepcApplication.getApplication().getDireccionesDao(),""+cliente.getCodigo_cliente_id());
                                                    DepcApplication.getApplication().getDireccionesDao().callBatchTasks(new Callable<Direcciones>() {
                                                        @Override
                                                        public Direcciones call() throws Exception {
                                                            for (Direcciones cl : bodegas) {
                                                                DataBaseHelper.saveDirecciones(cl, DepcApplication.getApplication().getDireccionesDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showDirecciones();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataDirecciones.getStatus_message() != null){
                                        mensajeError = dataDirecciones.getStatus_message();
                                    }
                                }
                            }

                            showDirecciones();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showDirecciones();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showDirecciones();
                    }
                }

                @Override
                public void onFailure(Call<IDirecciones.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showDirecciones();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showDirecciones();

        }
    }

    private void getBodegas(){

        showProgressWait();

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

                                                showbodegas();

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

                            showbodegas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showbodegas();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showbodegas();
                    }
                }

                @Override
                public void onFailure(Call<IBodegas.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showbodegas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showbodegas();

        }
    }


    @Override
    public void doRetry() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        showDirecciones();
    }
}
