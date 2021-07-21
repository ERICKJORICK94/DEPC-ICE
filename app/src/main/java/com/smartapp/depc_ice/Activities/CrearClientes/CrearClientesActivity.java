package com.smartapp.depc_ice.Activities.CrearClientes;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.Clientes.ClientesActivity;
import com.smartapp.depc_ice.Activities.DetalleCliente.DetalleClienteActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IRegistrarCliente;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.RegistrarClienteModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;

public class CrearClientesActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;

    private EditText cedula;
    private EditText nombre;
    private EditText direccion;
    private EditText direccion_cobro;
    private EditText dias;
    private EditText monto;
    private EditText celular;
    private EditText correo;
    private Spinner forma_pago;
    private Spinner contribuyente;
    private Spinner tipo_cliente;
    private Spinner zona;
    private RadioButton ci;
    private RadioButton ruc;
    private RadioButton pas;
    private RadioButton nacional;
    private RadioButton extranjero;
    private String tipo_id_tercero = "CI";
    private Button pdf;
    private Button registrar;

    private Call<IZonas.dataBodega> call;
    private IZonas.dataBodega data;
    private Call<IRegistrarCliente.dataBodega> callRegistro;
    private IRegistrarCliente.dataBodega dataRegistro;
    private int indexFormaPago = 0;
    private int indexContribuyente = 0;
    private int indexTipoCliente = 0;
    private int indexZonas = -1;
    private boolean nacionalidad = true;
    private List<Zonas> zonas;
    String PDFbase64String = "null";
    private Usuario user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.crear_clientes_layout);
        Utils.SetStyleActionBarTitle(this);


        cedula = (EditText) layout.findViewById(R.id.cedula);
        nombre = (EditText) layout.findViewById(R.id.nombre);
        direccion = (EditText) layout.findViewById(R.id.direccion);
        direccion_cobro = (EditText) layout.findViewById(R.id.direccion_cobro);
        dias = (EditText) layout.findViewById(R.id.dias);
        monto = (EditText) layout.findViewById(R.id.monto);
        celular = (EditText) layout.findViewById(R.id.celular);
        correo = (EditText) layout.findViewById(R.id.correo);
        forma_pago = (Spinner) layout.findViewById(R.id.forma_pago);
        contribuyente = (Spinner) layout.findViewById(R.id.contribuyente);
        tipo_cliente = (Spinner) layout.findViewById(R.id.tipo_cliente);
        zona = (Spinner) layout.findViewById(R.id.zona);
        ci = layout.findViewById(R.id.ci);
        ruc = layout.findViewById(R.id.ruc);
        pas = layout.findViewById(R.id.pas);
        nacional = layout.findViewById(R.id.nacional);
        extranjero = layout.findViewById(R.id.extranjero);
        registrar = (Button) layout.findViewById(R.id.registrar);
        pdf = (Button) layout.findViewById(R.id.pdf);

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


        ci.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cedula.setHint("Cédula");
                    tipo_id_tercero = "CI";
                }

            }
        });

        ruc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cedula.setHint("Ruc");
                    tipo_id_tercero = "RUC";
                }

            }
        });

        pas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    cedula.setHint("Pasaporte");
                    tipo_id_tercero = "PAS";
                }

            }
        });

        nacional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    nacionalidad = true;
                }


            }
        });

        extranjero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    nacionalidad = false;
                }


            }
        });


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        checkPermissionForReadExtertalStorage();

                    }

                } else {

                    checkPermissionForReadExtertalStorage();

                }

            }
        });

        String[] itemsFormaPago = new String[]{"CONTADO", "CRÉDITO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsFormaPago);
        forma_pago.setAdapter(adapter);
        forma_pago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexFormaPago = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] itemsContribuyente = new String[]{"NO", "SÍ"};
        ArrayAdapter<String> adapterContribuyente = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsContribuyente);
        contribuyente.setAdapter(adapterContribuyente);
        contribuyente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexContribuyente = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] itemsTipoCliente = new String[]{"PRECONTADO", "CONTADO"};
        ArrayAdapter<String> adapterTipoCliente= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsTipoCliente);
        tipo_cliente.setAdapter(adapterTipoCliente);
        tipo_cliente.setEnabled(false);
        tipo_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexTipoCliente = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(cedula.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un Identificación para poder continuar", Toast.LENGTH_LONG).show();
                    return;
                }
                if(direccion.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese una dirección válida", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(direccion_cobro.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese una dirección de cobro válida", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if(nombre.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un nombre válido", Toast.LENGTH_LONG).show();
                    return;
                }
                if(dias.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese días de crédito", Toast.LENGTH_LONG).show();
                    return;
                }else if (Integer.parseInt(dias.getText().toString()) == 0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese días de crédito mayor a cero", Toast.LENGTH_LONG).show();
                    return;
                }
                if(monto.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese monto de crédito", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(celular.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese una teléfono válido", Toast.LENGTH_LONG).show();
                    return;
                }*/
                /*if(correo.getText().length()==0){
                    Toast.makeText(CrearClientesActivity.this, "Ingrese un correo", Toast.LENGTH_LONG).show();
                    return;
                }*/

                if(indexZonas < 0 ){
                    Toast.makeText(CrearClientesActivity.this, "Seleccione una zona", Toast.LENGTH_LONG).show();
                    return;
                }

                /*if(PDFbase64String.length() == 0 ){
                    Toast.makeText(CrearClientesActivity.this, "Añada pdf antes de continuar", Toast.LENGTH_LONG).show();
                    return;
                }*/

                String mensaje = "¿Seguro que desea continuar con el registro?";
                new AlertDialog.Builder(CrearClientesActivity.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendRegistrar();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .show();
            }
        });

        getZonas();

    }

    private void iniciarCarga(){

        Intent intent4 = new Intent(CrearClientesActivity.this, NormalFilePickActivity.class);
        intent4.putExtra(Constant.MAX_NUMBER, 1);
        intent4.putExtra(IS_NEED_FOLDER_LIST, false);
        intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"pdf"});
        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);


    }

    public void checkPermissionForReadExtertalStorage() {

        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3102 );
                    // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
                }
            }else{ //have permissions
                iniciarCarga ();
            }
        }else{ // Pre-Marshmallow
            iniciarCarga ();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarCarga();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2296) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    checkPermissionForReadExtertalStorage();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }

            return;
        }

        switch (requestCode){
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    if (list != null){
                        for (NormalFile f: list){
                            File file = new File(f.getPath());
                            byte[] b = new byte[(int) file.length()];
                            try {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                fileInputStream.read(b);
                                for (int j = 0; j < b.length; j++) {
                                    System.out.print((char) b[j]);
                                }
                            } catch (FileNotFoundException e) {
                                System.out.println("File Not Found.");
                                e.printStackTrace();
                            } catch (IOException e1) {
                                System.out.println("Error Reading The File.");
                                e1.printStackTrace();
                            }

                            byte[] byteFileArray = new byte[0];
                            try {
                                byteFileArray = FileUtils.readFileToByteArray(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (byteFileArray.length > 0) {
                                PDFbase64String = android.util.Base64.encodeToString(byteFileArray, android.util.Base64.NO_WRAP);
                                //Log.e("File Base64 string", "IMAGE PARSE ==>" + base64String);
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showListZonas(){

        hideProgressWait();

        try {

            zonas = DataBaseHelper.getZonas(DepcApplication.getApplication().getZonasDao());
            if (zonas != null) {
                if (zonas.size() > 0) {

                   List<String> items= new ArrayList<String>();
                    for (Zonas z : zonas){
                        items.add(z.getDescripcion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    zona.setAdapter(adapter);
                    zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            indexZonas = position;
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

    private void getZonas(){

        showProgressWait();

        //JSON SEND
        BodegasModel model = new BodegasModel();
        model.setCondicion("");
        model.setMetodo("ListarZonas");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IZonas request = DepcApplication.getApplication().getRestAdapter().create(IZonas.class);
            call = request.getZonas(body);
            call.enqueue(new Callback<IZonas.dataBodega>() {
                @Override
                public void onResponse(Call<IZonas.dataBodega> call, Response<IZonas.dataBodega> response) {
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


                                                final List<Zonas> bodegas;
                                                bodegas = data.getData().getListarBodegas().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteZonas(DepcApplication.getApplication().getZonasDao());
                                                    DepcApplication.getApplication().getZonasDao().callBatchTasks(new Callable<Zonas>() {
                                                        @Override
                                                        public Zonas call() throws Exception {
                                                            for (Zonas cl : bodegas) {
                                                                DataBaseHelper.saveZonas(cl, DepcApplication.getApplication().getZonasDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showListZonas();

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

                            showListZonas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showListZonas();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showListZonas();
                    }
                }

                @Override
                public void onFailure(Call<IZonas.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showListZonas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showListZonas();

        }
    }



    private void sendRegistrar(){

        showProgressWait();
        String formaPago = "4";
        if (indexFormaPago == 0){
            formaPago = "4";
        }else{
            formaPago = "5";
        }

        String contribuyenteEspecial = "0";
        if (indexContribuyente == 0){
            contribuyenteEspecial = "0";
        }else{
            contribuyenteEspecial = "1";
        }

        String claseCliente = "1";
        if (!nacionalidad){
            claseCliente = "2";
        }

        String tipoCliente = "0";
        if (indexTipoCliente == 0){
            tipoCliente = "0";
        }else{
            tipoCliente = "1";
        }

        //JSON SEND
        RegistrarClienteModel model = new RegistrarClienteModel();
        model.setTipo_id_tercero(""+tipo_id_tercero);
        model.setTercero_id(""+cedula.getText().toString().trim());
        model.setDias_credito(""+dias.getText().toString().trim());
        model.setMonto_credito(""+monto.getText().toString().trim());
        model.setForma_pago(""+formaPago);
        model.setContribuyente_especial(""+contribuyenteEspecial);
        model.setClase_cliente(""+claseCliente);
        model.setExportador("0");
        model.setVerificado("0");
        model.setNo_aplica_ret("0");
        model.setSociedad("0");
        model.setDia_max("");
        model.setDireccion(""+direccion.getText().toString().trim());
        model.setTelefono1(""+celular.getText().toString().trim());
        model.setEmail(""+correo.getText().toString().trim());
        model.setNombre_tercero(""+nombre.getText().toString().trim());
        model.setDireccion_cobro(""+direccion_cobro.getText().toString().trim());
        model.setNombre_tercero(""+nombre.getText().toString().trim());
        model.setSw_persona_juridica("1");
        model.setTipo_cliente(""+tipoCliente);
        model.setZona_id(""+zonas.get(indexZonas).getZona_id());
        model.setDocumentopdf(PDFbase64String);
        model.setVendedor_id(""+user.getUsuario());
        model.setMetodo("CrearCliente");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);

        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IRegistrarCliente request = DepcApplication.getApplication().getRestAdapter().create(IRegistrarCliente.class);
            callRegistro = request.getRegistrarCliente(body);
            callRegistro.enqueue(new Callback<IRegistrarCliente.dataBodega>() {
                @Override
                public void onResponse(Call<IRegistrarCliente.dataBodega> call, Response<IRegistrarCliente.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataRegistro = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataRegistro != null) {
                                if (dataRegistro.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataRegistro.getData() != null){

                                        Clientes cl = dataRegistro.getData();
                                        if (cl.getCliente_id() != null){
                                            cl.setCodigo_cliente_id(cl.getCliente_id());
                                        }

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

                                        DataBaseHelper.saveClientes(cl, DepcApplication.getApplication().getClientesDao());
                                        new AlertDialog.Builder(CrearClientesActivity.this)
                                                .setTitle(getResources().getString(R.string.app_name))
                                                .setMessage("Cliente registrado con éxito")
                                                .setCancelable(false)
                                                .setPositiveButton("VER CLIENTE", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                        Intent intent = new Intent(CrearClientesActivity.this, DetalleClienteActivity.class);
                                                        intent.putExtra(Const.DETALLE_CLIENTE, dataRegistro.getData());
                                                        intent.putExtra("consultar", "1");
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                })
                                                .setNegativeButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                })
                                                .show();



                                        return;
                                    }
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
                public void onFailure(Call<IRegistrarCliente.dataBodega> call, Throwable t) {
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

}
