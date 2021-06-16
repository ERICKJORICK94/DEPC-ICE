package com.smartapp.depc_ice.Activities.GabinetGeneral;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Clientes.Adapter.ClientesAdapter;
import com.smartapp.depc_ice.Activities.Clientes.ClientesActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.DetalleClienteActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.DetalleUbicacionClienteActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.DireccionClientesAdapter;
import com.smartapp.depc_ice.Activities.DetalleCliente.MantDireccionActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.GabinetGeneral;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IAsignarGabinet;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Interface.IDirecciones;
import com.smartapp.depc_ice.Interface.IRegistrarEmergencia;
import com.smartapp.depc_ice.Models.AsignarGabinetModel;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.Models.DireccionesModel;
import com.smartapp.depc_ice.Models.RegistrarEmergenciaModel;
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

public class DetalleGabinetGeneralActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;

    private Button registrar;
    private Call<IClientes.dataClientes> call;
    private IClientes.dataClientes data;


    private TextView codigo,ingresado,fecha_ingreso,serie,txt_select_cliente,txt_select_direccion;
    TextView agregar,ver_foto,comentario;
    private Bitmap bitmap;
    private Spinner spinner;
    private Bitmap bitmapFirma;
    private Button registrar_emergencia;
    private final static int MY_PERMISSIONS_REQUEST_CAMARA = 9991;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private int indexEstados = -1;
    private Usuario user;
    private List<EstadoGabinet> estadosGabinets;
    private Spinner spinnerCliente;
    private LayoutInflater layoutInflater;

    private Call<IRegistrarEmergencia.dataBodega> callRegistro;
    private IRegistrarEmergencia.dataBodega dataRegistro;
    private GabinetGeneral gabinetGeneral;
    private CardView card_cliente;
    private boolean isSearch = false;
    private String condition = "";
    private String buscar = "";
    private List<Clientes> clientesList;
    private LinearLayout linear_direccion;
    private String cliente_id = "-1";
    private String cliente_direcciones_id = "-1";
    private Button asignar;

    private Call<IDirecciones.dataBodega> callDirecciones;
    private IDirecciones.dataBodega dataDirecciones;

    private Call<IAsignarGabinet.dataBodega> callAsignar;
    private IAsignarGabinet.dataBodega dataAsignar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_gabinet_general_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

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

        if (getIntent() != null) {
            gabinetGeneral = (GabinetGeneral) getIntent().getSerializableExtra("detalle_gabinet");

        }

        registrar = (Button) layout.findViewById(R.id.registrar);
        codigo = (TextView) layout.findViewById(R.id.codigo);
        ingresado = (TextView) layout.findViewById(R.id.ingresado);
        ingresado = (TextView) layout.findViewById(R.id.ingresado);
        spinner = (Spinner) layout.findViewById(R.id.spinner);
        spinnerCliente = (Spinner) layout.findViewById(R.id.spinnerCliente);
        card_cliente = (CardView) layout.findViewById(R.id.card_cliente);
        serie = (TextView) layout.findViewById(R.id.serie);
        txt_select_cliente = (TextView) layout.findViewById(R.id.txt_select_cliente);
        txt_select_direccion = (TextView) layout.findViewById(R.id.txt_select_direccion);
        registrar_emergencia = (Button) layout.findViewById(R.id.registrar_emergencia);
        fecha_ingreso = (TextView) layout.findViewById(R.id.fecha_ingreso);
        asignar = (Button) layout.findViewById(R.id.asignar);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);
        comentario = layout.findViewById(R.id.comentario);
        linear_direccion = layout.findViewById(R.id.linear_direccion);

        card_cliente.setVisibility(View.GONE);
        linear_direccion.setVisibility(View.GONE);

        try {
            estadosGabinets = DataBaseHelper.getEstadoGabinet(DepcApplication.getApplication().getEstadoGabinetDao());

            if (estadosGabinets != null) {
                if (estadosGabinets.size() > 0) {

                    indexEstados = 0;
                    List<String> items= new ArrayList<String>();
                    for (EstadoGabinet z : estadosGabinets){
                        items.add(z.getDescripcion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    spinner.setAdapter(adapter);
                    spinner.setSelection(indexEstados);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            indexEstados = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (gabinetGeneral != null){

            codigo.setText("");
            if (gabinetGeneral.getCodigo_tipo_congelador() != null){
                codigo.setText(""+gabinetGeneral.getCodigo_tipo_congelador());
            }

            ingresado.setText("");
            if (gabinetGeneral.getUsuario_crea() != null){
                ingresado.setText(""+gabinetGeneral.getUsuario_crea());
            }

            fecha_ingreso.setText("");
            if (gabinetGeneral.getFecha_crea() != null){
                fecha_ingreso.setText(""+gabinetGeneral.getFecha_crea());
            }

            serie.setText("");
            if (gabinetGeneral.getSerie() != null){
                serie.setText(""+gabinetGeneral.getSerie());
            }

            /*direccion_cliente.setText("");
            if (gabinetGeneral.getDireccion_cliente_gabinet() != null){
                direccion_cliente.setText(""+gabinetGeneral.getDireccion_cliente_gabinet());
            }*/

            if (gabinetGeneral.getFoto() != null){
                if (gabinetGeneral.getFoto().length() > 0){
                    if (Utils.convert(gabinetGeneral.getFoto()) != null){
                        bitmap = Utils.convert(gabinetGeneral.getFoto());
                    }
                }
            }

            comentario.setText("");
            if (gabinetGeneral.getObservacion() != null){
                comentario.setText(""+gabinetGeneral.getObservacion());
            }

            if (gabinetGeneral.getEstado() != null){


                String estado = "";
                if (estadosGabinets != null){
                    int contador = 0;
                    for (EstadoGabinet es : estadosGabinets){
                        if (es.getNum_estado().equals(gabinetGeneral.getEstado())){
                            indexEstados = contador;
                            estado = es.getNum_estado();
                        }

                        contador++;
                    }
                    if (indexEstados >= 0) {
                        spinner.setSelection(indexEstados);
                        if (estado.equals("1")){
                            card_cliente.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            registrar_emergencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indexEstados >= 0) {
                        if (estadosGabinets != null) {
                            sendRegistrarEmergencia();
                        }
                        return;
                    }

                    showAlert("Seleccione estado antes de continuar");
                }
            });


        }

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
                if (bitmap != null) {
                    if (Utils.convertBase64String(bitmap) != null) {
                        Intent intent = new Intent(DetalleGabinetGeneralActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" + Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                } else {
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });


        txt_select_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cliente_direcciones_id = "-1";
                txt_select_direccion.setText("SELECCIONE...");
                linear_direccion.setVisibility(View.GONE);

                getClientes("");
            }
        });

        txt_select_direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cliente_id.equals("-1")){
                    getDirecciones(cliente_id);
                }
            }
        });


        asignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cliente_direcciones_id.equals("-1")){
                    asignarGabinet();
                    return;
                }

                showAlert("SELECCIONE DIRECCIÓN ANTES DE CONTINUAR");
            }
        });

    }

    private void selectPick(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Cámara", "Galería"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetalleGabinetGeneralActivity.this);
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
            Toast.makeText(DetalleGabinetGeneralActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                selectPick();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

            return;
        }

        switch (requestCode) {

            case 3621:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    /*if (cliente != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                        startActivity(intent);
                    }*/
                } else {
                    Log.d("TAG", "Call Permission Not Granted");
                }
                break;

            default:
                break;
        }
    }


    private void sendRegistrarEmergencia(){

        showProgressWait();
        String estado = ""+estadosGabinets.get(indexEstados).getNum_estado();

        //JSON SEND
        RegistrarEmergenciaModel model = new RegistrarEmergenciaModel();
        model.setPto_vta_id(""+gabinetGeneral.getPto_vta_id());
        model.setBodega(""+gabinetGeneral.getBodega());
        model.setId_direccion_cliente(""+gabinetGeneral.getId_direccion_cliente());
        model.setEstado(estado);
        model.setCongelador_id(""+gabinetGeneral.getId_congelador());
        model.setObservacion(""+comentario.getText().toString());
        model.setUsuario_id(""+user.getUsuario());
        model.setFoto("");
        if (bitmap != null){
            if (Utils.convertBase64String(bitmap) != null){
                model.setFoto(""+Utils.convertBase64String(bitmap));
            }
        }
        model.setMetodo("RegistrarEventoCabinet");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);

        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IRegistrarEmergencia request = DepcApplication.getApplication().getRestAdapter().create(IRegistrarEmergencia.class);
            callRegistro = request.getBodegas(body);
            callRegistro.enqueue(new Callback<IRegistrarEmergencia.dataBodega>() {
                @Override
                public void onResponse(Call<IRegistrarEmergencia.dataBodega> call, Response<IRegistrarEmergencia.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataRegistro = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataRegistro != null) {
                                if (dataRegistro.getStatus() == Const.COD_ERROR_SUCCESS) {


                                    gabinetGeneral.setEstado(""+model.getEstado());
                                    gabinetGeneral.setObservacion(""+model.getObservacion());
                                    gabinetGeneral.setFoto(""+model.getFoto());

                                    DataBaseHelper.updateGabinetGeneral(gabinetGeneral, DepcApplication.getApplication().getGabinetGeneralDao());


                                    new AlertDialog.Builder(DetalleGabinetGeneralActivity.this)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("Se cambio el estado con éxito")
                                            .setCancelable(false)
                                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            })
                                            .show();



                                    return;
                                }else{
                                    if (dataRegistro.getStatus_message() != null){
                                        mensajeError = dataRegistro.getStatus_message();
                                    }
                                }
                            }

                            showAlert(mensajeError);

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showAlert( Const.ERROR_DEFAULT);
                        }

                    } else {
                        hideProgressWait();
                        showAlert( Const.ERROR_DEFAULT);
                    }
                }

                @Override
                public void onFailure(Call<IRegistrarEmergencia.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    Log.e("Error",""+t.toString());
                    showAlert(Const.ERROR_COBERTURA);

                }
            });

        }catch (Exception e){
            hideProgressWait();
            showAlert( Const.ERROR_DEFAULT);

        }


    }


    @Override
    public void doRetry() {

    }



    private void condition(String search){
        isSearch = true;
        search.trim();
        buscar = search;
        search = search.replace(" ","%").toUpperCase();
        condition = "and (b.nombre_tercero LIKE '%"+search+"%' or b.nombre_comercial LIKE '%"+search+"%' or b.direccion LIKE '%"+search+"%' or a.codigo_cliente_id LIKE '%"+search+"%')";
        getClientes(condition);
    }

    private void getClientes(String search){

        showProgressWait();

        int limit = Const.PARAM_MAX_ROW;
        String codigoCLiente = "";
        try {
            List<Usuario> usuario = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuario != null){
                if (usuario.size() > 0){
                    Usuario user = usuario.get(0);
                    codigoCLiente = user.getUsuario();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String buscar = "and d.usuario_id="+codigoCLiente;
        if (search.length() > 0){
            buscar = buscar+" "+search;
        }
        //JSON SEND
        ClientesModel model = new ClientesModel();
        model.setCondicion(buscar);
        model.setFiltro("limit "+Const.PARAM_MAX_ROW+" offset 0");
        model.setMetodo("ListaClientes");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);
        try {
            IClientes request = DepcApplication.getApplication().getRestAdapter().create(IClientes.class);
            call = request.getClientes(body);
            call.enqueue(new Callback<IClientes.dataClientes>() {
                @Override
                public void onResponse(Call<IClientes.dataClientes> call, Response<IClientes.dataClientes> response) {
                    if (response.isSuccessful()) {

                        data = response.body();
                        try {

                            hideProgressWait();

                            if (data != null) {
                                if (data.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (data.getData() != null)
                                        if (data.getData().getListaClientes() != null){
                                            if (data.getData().getListaClientes().size() > 0){

                                                final List<Clientes> clientes;
                                                clientes = data.getData().getListaClientes().get(0);

                                                if (clientes != null){
                                                    DepcApplication.getApplication().getClientesDao().callBatchTasks(new Callable<Clientes>() {
                                                        @Override
                                                        public Clientes call() throws Exception {

                                                            for (Clientes cl : clientes){


                                                                boolean isFlag = true;
                                                                if (cl.getNombre_comercial() != null){
                                                                    if (cl.getNombre_comercial().length() > 0) {
                                                                        if (cl.getNombre_tercero() == null) {
                                                                            cl.setNombre_tercero("" + cl.getNombre_comercial());
                                                                            isFlag = false;
                                                                        }else if (cl.getNombre_tercero().length() == 0) {
                                                                            cl.setNombre_tercero("" + cl.getNombre_comercial());
                                                                            isFlag = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (cl.getNombre_tercero() != null){
                                                                    if (cl.getNombre_tercero().length() > 0) {
                                                                        if (cl.getNombre_comercial() == null) {
                                                                            cl.setNombre_comercial("" + cl.getNombre_tercero());
                                                                            isFlag = false;
                                                                        }else if (cl.getNombre_comercial().length() == 0) {
                                                                            cl.setNombre_comercial("" + cl.getNombre_tercero());
                                                                            isFlag = false;
                                                                        }
                                                                    }
                                                                }

                                                                if (cl.getNombre_comercial() != null && cl.getNombre_tercero() != null){
                                                                    isFlag = false;
                                                                }

                                                                if (isFlag) {
                                                                    cl.setNombre_tercero("SIN NOMBRE");
                                                                    cl.setNombre_comercial("SIN NOMBRE ASIGNADO");
                                                                }

                                                                DataBaseHelper.saveClientes(cl,DepcApplication.getApplication().getClientesDao());
                                                            }

                                                            return null;
                                                        }
                                                    });


                                                }

                                                showList();

                                                return;
                                            }
                                        }
                                }else{
                                    //Error
                                }
                            }
                            showList();

                        } catch (Exception e) {
                            e.printStackTrace();
                            showList();
                        }

                    } else {
                        showList();
                    }
                }

                @Override
                public void onFailure(Call<IClientes.dataClientes> call, Throwable t) {
                    showList();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showList();

        }

    }

    private void showList(){

        hideProgressWait();
        try {
            clientesList = null;
            if (isSearch) {
                String nuevaCondicion = "nombre_tercero LIKE '%"+buscar+"%' or nombre_comercial LIKE '%"+buscar+"%' or direccion LIKE '%"+buscar+"%' or codigo_cliente_id LIKE '%"+buscar+"%'";
                clientesList = DataBaseHelper.getClientesSearch(DepcApplication.getApplication().getClientesDao(), "" + nuevaCondicion);
            }else{
                clientesList = DataBaseHelper.getClientes(DepcApplication.getApplication().getClientesDao());
            }

            if (clientesList != null) {
                if (clientesList.size() > 0) {

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

                            //buscarBodega = v.getText().toString().trim();
                            condition(v.getText().toString().trim());
                            //isSearch = true;
                            //showList();
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

                    for (Clientes cat : clientesList){
                        nombre.add(cat.getNombre_comercial());
                        contador++;

                    }
                    ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nombre);
                    lista.setAdapter(adp);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            cliente_id = ""+clientesList.get(position).getCodigo_cliente_id();
                            txt_select_cliente.setText(""+clientesList.get(position).getNombre_comercial());
                            linear_direccion.setVisibility(View.VISIBLE);
                            //getDirecciones(cliente_id);
                            /*currentCodigoFamilia = "";
                            getProducto(currentCodigoFamilia, "");
                            catalogo.setText(""+nombre.get(0));*/

                            alert.dismiss();
                        }
                    });


                    alert.show();


                }else{
                    String error = Const.ERROR_NO_RESULT;
                    if (isSearch){ error = Const.ERROR_NO_RESULT; }
                    showAlert(error);
                }
            }else{
                String error = Const.ERROR_NO_RESULT;
                if (isSearch){ error = Const.ERROR_NO_RESULT; }
                showAlert(error);
            }

            isSearch = false;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Const.ERROR_NO_RESULT);
        }

    }

    private void getDirecciones(String cliente_id){

        showProgressWait();

        //JSON SEND
        DireccionesModel model = new DireccionesModel();
        model.setCondicion(""+cliente_id);
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
                                                    DataBaseHelper.deleteDireccionesById(DepcApplication.getApplication().getDireccionesDao(),""+cliente_id);
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

                                                showLit();

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

                            showLit();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showLit();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showLit();
                    }
                }

                @Override
                public void onFailure(Call<IDirecciones.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showLit();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showLit();

        }
    }

    private void showLit(){

        try {
            if( DataBaseHelper.getDireccionesBYIdClienteDESC(DepcApplication.getApplication().getDireccionesDao(), ""+cliente_id) != null){
                List<Direcciones> direcciones = DataBaseHelper.getDireccionesBYIdClienteDESC(DepcApplication.getApplication().getDireccionesDao(), ""+ cliente_id);


                View detailProduct = layoutInflater.inflate(R.layout.catalogo_search_layout, null);
                final AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setView(detailProduct);
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);

                ImageView back = (ImageView) detailProduct.findViewById(R.id.back);
                EditText search = (EditText) detailProduct.findViewById(R.id.search);
                final ListView lista = (ListView) detailProduct.findViewById(R.id.lista);
                condition = "";
                search.setVisibility(View.GONE);

                search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        //condition(v.getText().toString().trim());
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

                for (Direcciones cat : direcciones){
                    nombre.add(cat.getDireccion_envio());
                    contador++;

                }
                ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nombre);
                lista.setAdapter(adp);

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        /*cliente_id = ""+clientesList.get(position).getCodigo_cliente_id();
                        txt_select_cliente.setText(""+clientesList.get(position).getNombre_comercial());
                        getDirecciones(cliente_id);*/

                        cliente_direcciones_id = ""+direcciones.get(position).getId();
                        txt_select_direccion.setText(""+direcciones.get(position).getDireccion_envio());


                        alert.dismiss();
                    }
                });


                alert.show();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }






    private void asignarGabinet(){

        showProgressWait();

        //JSON SEND
        AsignarGabinetModel model = new AsignarGabinetModel();
        model.setId_congelador(""+gabinetGeneral.getId_congelador());
        model.setId_direccion_cliente(cliente_direcciones_id);
        model.setMetodo("RegistarGabinetDireccion");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IAsignarGabinet request = DepcApplication.getApplication().getRestAdapter().create(IAsignarGabinet.class);
            callAsignar = request.getBodegas(body);
            callAsignar.enqueue(new Callback<IAsignarGabinet.dataBodega>() {
                @Override
                public void onResponse(Call<IAsignarGabinet.dataBodega> call, Response<IAsignarGabinet.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataAsignar = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataAsignar != null) {
                                if (dataAsignar.getStatus() == Const.COD_ERROR_SUCCESS) {

                                    new AlertDialog.Builder(DetalleGabinetGeneralActivity.this)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("Gabinet asignado con éxito")
                                            .setCancelable(false)
                                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            })
                                            .show();
                                    return;
                                }else{
                                    if (dataDirecciones.getStatus_message() != null){
                                        mensajeError = dataDirecciones.getStatus_message();
                                    }
                                }
                            }

                            showAlert(mensajeError);

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showAlert(Const.ERROR_DEFAULT);
                        }

                    } else {
                        hideProgressWait();
                        showAlert(Const.ERROR_DEFAULT);
                    }
                }

                @Override
                public void onFailure(Call<IAsignarGabinet.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    showAlert(Const.ERROR_DEFAULT);

                }
            });

        }catch (Exception e){
            hideProgressWait();
            showAlert(Const.ERROR_DEFAULT);

        }
    }
}
