package com.smartapp.depc_ice.Activities.GabinetClientes;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IRegistrarEmergencia;
import com.smartapp.depc_ice.Models.RegistrarEmergenciaModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.PhotoViewer;
import com.smartapp.depc_ice.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleGabinetActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;

    private Button registrar;

    private Clientes cliente = null;
    private TextView clienteName;
    private TextView lblFecha;
    private TextView direccion;
    private TextView ruc,codigo,ingresado,fecha_ingreso,serie,direccion_cliente;
    private ClienteGabinet clienteGabinet;
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

    private Call<IRegistrarEmergencia.dataBodega> callRegistro;
    private IRegistrarEmergencia.dataBodega dataRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_gabinet_layout);
        Utils.SetStyleActionBarTitle(this);

        cliente = DepcApplication.getApplication().getCliente();

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
            String id = getIntent().getStringExtra("cliente_gabinet_id");
            try {
                List<ClienteGabinet> clienteGabinets = DataBaseHelper.getClienteGabinetById(DepcApplication.getApplication().getClienteGabinetDao(), ""+id);
                if (clienteGabinets != null){
                    if (clienteGabinets.size() > 0 ){
                        clienteGabinet = clienteGabinets.get(0);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        registrar = (Button) layout.findViewById(R.id.registrar);
        clienteName = (TextView) layout.findViewById(R.id.clienteName);
        ruc = (TextView) layout.findViewById(R.id.ruc);
        lblFecha = (TextView) layout.findViewById(R.id.fecha);
        direccion = (TextView) layout.findViewById(R.id.direccion);
        codigo = (TextView) layout.findViewById(R.id.codigo);
        ingresado = (TextView) layout.findViewById(R.id.ingresado);
        ingresado = (TextView) layout.findViewById(R.id.ingresado);
        spinner = (Spinner) layout.findViewById(R.id.spinner);
        serie = (TextView) layout.findViewById(R.id.serie);
        direccion_cliente = (TextView) layout.findViewById(R.id.direccion_cliente);
        registrar_emergencia = (Button) layout.findViewById(R.id.registrar_emergencia);
        fecha_ingreso = (TextView) layout.findViewById(R.id.fecha_ingreso);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);
        comentario = layout.findViewById(R.id.comentario);

        if (cliente != null){

            if (cliente.getNombre_comercial() != null){
                clienteName.setText(""+cliente.getNombre_comercial());
            }

            if (cliente.getDireccion() != null){
                direccion.setText(""+cliente.getDireccion());
            }

            if (cliente.getTercero_id() != null){
                ruc.setText(""+cliente.getTercero_id());
            }

            lblFecha.setText(""+Utils.getFecha());

        }

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

        if (clienteGabinet != null){

            codigo.setText("");
            if (clienteGabinet.getCodigo_tipo_congelador() != null){
                codigo.setText(""+clienteGabinet.getCodigo_tipo_congelador());
            }

            ingresado.setText("");
            if (clienteGabinet.getUsuario_crea() != null){
                ingresado.setText(""+clienteGabinet.getUsuario_crea());
            }

            fecha_ingreso.setText("");
            if (clienteGabinet.getFecha_crea() != null){
                fecha_ingreso.setText(""+clienteGabinet.getFecha_crea());
            }

            serie.setText("");
            if (clienteGabinet.getSerie() != null){
                serie.setText(""+clienteGabinet.getSerie());
            }

            direccion_cliente.setText("");
            if (clienteGabinet.getDireccion_cliente_gabinet() != null){
                direccion_cliente.setText(""+clienteGabinet.getDireccion_cliente_gabinet());
            }

            if (clienteGabinet.getFoto() != null){
                if (clienteGabinet.getFoto().length() > 0){
                    if (Utils.convert(clienteGabinet.getFoto()) != null){
                        bitmap = Utils.convert(clienteGabinet.getFoto());
                    }
                }
            }

            comentario.setText("");
            if (clienteGabinet.getObservacion() != null){
                comentario.setText(""+clienteGabinet.getObservacion());
            }

            if (clienteGabinet.getEstado() != null){


                if (estadosGabinets != null){
                    int contador = 0;
                    for (EstadoGabinet es : estadosGabinets){
                        if (es.getNum_estado().equals(clienteGabinet.getEstado())){
                            indexEstados = contador;
                        }

                        contador++;
                    }
                    if (indexEstados >= 0) {
                        spinner.setSelection(indexEstados);
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
                        Intent intent = new Intent(DetalleGabinetActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" + Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                } else {
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });


    }

    private void selectPick(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Cámara", "Galería"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DetalleGabinetActivity.this);
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
            Toast.makeText(DetalleGabinetActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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
                    if (cliente != null) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cliente.getTelefono1()));
                        startActivity(intent);
                    }
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
        model.setPto_vta_id(""+clienteGabinet.getPto_vta_id());
        model.setBodega(""+clienteGabinet.getBodega());
        model.setId_direccion_cliente(""+clienteGabinet.getId_direccion_cliente());
        model.setEstado(estado);
        model.setCongelador_id(""+clienteGabinet.getId_congelador());
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


                                    clienteGabinet.setEstado(""+model.getEstado());
                                    clienteGabinet.setObservacion(""+model.getObservacion());
                                    clienteGabinet.setFoto(""+model.getFoto());

                                    DataBaseHelper.updateClienteGabinet(clienteGabinet, DepcApplication.getApplication().getClienteGabinetDao());


                                    new AlertDialog.Builder(DetalleGabinetActivity.this)
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
}
