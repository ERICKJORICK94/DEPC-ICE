package com.smartapp.depc_ice.Activities.Gabinet;

import android.Manifest;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Pedido.Adapter.ListaPedidosAdapter;
import com.smartapp.depc_ice.Activities.Pedido.DetallePedidoActivity;
import com.smartapp.depc_ice.Activities.Pedido.RegistroPedidoActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Interface.ICrearVisitaPedidos;
import com.smartapp.depc_ice.Mapa.MapsActivity;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.Models.CrearVisitaModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.NonScrollListView;
import com.smartapp.depc_ice.Utils.PhotoViewer;
import com.smartapp.depc_ice.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

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
    private TextView ruc;

    TextView agregar,ver_foto,comentario;
    private Bitmap bitmap;
    private Bitmap bitmapFirma;
    private final static int MY_PERMISSIONS_REQUEST_CAMARA = 9991;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.detalle_gabinet_layout);
        Utils.SetStyleActionBarTitle(this);

        cliente = DepcApplication.getApplication().getCliente();

        registrar = (Button) layout.findViewById(R.id.registrar);
        clienteName = (TextView) layout.findViewById(R.id.clienteName);
        ruc = (TextView) layout.findViewById(R.id.ruc);
        lblFecha = (TextView) layout.findViewById(R.id.fecha);
        direccion = (TextView) layout.findViewById(R.id.direccion);
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


    @Override
    public void doRetry() {

    }
}
