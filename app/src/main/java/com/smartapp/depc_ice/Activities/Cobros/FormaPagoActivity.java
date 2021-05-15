package com.smartapp.depc_ice.Activities.Cobros;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.smartapp.depc_ice.Activities.DetalleCliente.MantDireccionActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.PhotoViewer;
import com.smartapp.depc_ice.Utils.Utils;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormaPagoActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private Spinner spinner_forma;
    private Spinner spinner_banco;
    private Spinner spinner_cuentas;

    ////Linear
    private LinearLayout linear_tarjeta;
    private LinearLayout linear_n_tarjeta;
    private LinearLayout linear_banco;
    private LinearLayout linear_cuenta_bancaria;
    private LinearLayout linear_monto_pago;
    private LinearLayout linear_vencimiento;
    private LinearLayout linear_autoriazacion;
    private LinearLayout linear_numero_cuenta;
    private LinearLayout linear_fecha_vigencia;
    private LinearLayout linear_numero_cheque;
    private LinearLayout linear_numero_deposito;
    private LinearLayout linear_numero_nc;
    private LinearLayout linear_nro_letra;
    private LinearLayout linear_cuotas;
    private LinearLayout linear_n_com;
    private LinearLayout linear_nro_voucher;
    private String idPago = "";



    //EDITTEXT
    private EditText edt_monto;
    private EditText edit_saldo;
    private EditText edt_numero_tarjeta;
    private Spinner spinner_tarjeta;
    private EditText edt_numero_voucher;
    private EditText edt_nro_com;
    private EditText edt_numero_letras;
    private EditText edt_cuotas;
    private EditText edt_numero_nc;
    private EditText edt_monto_pagar;
    private Button btn_vencimiento;
    private EditText edt_autorizacion;
    private EditText edt_numero_cuenta;
    private EditText edt_numero_cheque;
    private EditText edt_numero_deposito;
    private Button btn_fecha_vigencia;
    private EditText edt_referencia;
    private Button btn_agregar;
    private double saldo = 0;
    private int positionForma = 0;

    private String subrubro = "";
    private String Descripcionsubrubro = "";
    private String planPago = "";
    private String descripcionPlanPago = "";
    private String banco = "";
    private String descripcionbanco = "";
    private String tarjetaCredito = "";
    private String nombreTarjetaCredito = "";
    private String cuentasBancaria = "";
    private String numeroCuentaBancaria = "";
    private String nombreBanciCuentaBancaria = "";
    private String cuentaContable = "";
    private Clientes cliente;
    private TextView agregar,ver_foto;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private String fechaVencimiento = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.forma_pago_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        cliente = DepcApplication.getApplication().getCliente();

        spinner_forma = (Spinner) layout.findViewById(R.id.spinner_forma);
        spinner_banco = (Spinner) layout.findViewById(R.id.spinner_banco);
        spinner_cuentas = (Spinner) layout.findViewById(R.id.spinner_cuentas);


        linear_tarjeta = (LinearLayout) layout.findViewById(R.id.linear_tarjeta);
        linear_n_tarjeta = (LinearLayout) layout.findViewById(R.id.linear_n_tarjeta);
        linear_cuenta_bancaria = (LinearLayout) layout.findViewById(R.id.linear_cuenta_bancaria);
        linear_banco = (LinearLayout) layout.findViewById(R.id.linear_banco);
        linear_monto_pago = (LinearLayout) layout.findViewById(R.id.linear_monto_pago);
        linear_vencimiento = (LinearLayout) layout.findViewById(R.id.linear_vencimiento);
        linear_autoriazacion = (LinearLayout) layout.findViewById(R.id.linear_autoriazacion);
        linear_numero_cuenta = (LinearLayout) layout.findViewById(R.id.linear_numero_cuenta);
        linear_fecha_vigencia = (LinearLayout) layout.findViewById(R.id.linear_fecha_vigencia);
        linear_numero_cheque = (LinearLayout) layout.findViewById(R.id.linear_numero_cheque);
        linear_numero_deposito = (LinearLayout) layout.findViewById(R.id.linear_numero_deposito);
        linear_numero_nc = (LinearLayout) layout.findViewById(R.id.linear_numero_nc);
        linear_nro_letra = (LinearLayout) layout.findViewById(R.id.linear_nro_letra);
        linear_cuotas = (LinearLayout) layout.findViewById(R.id.linear_cuotas);
        linear_n_com = (LinearLayout) layout.findViewById(R.id.linear_n_com);
        linear_nro_voucher = (LinearLayout) layout.findViewById(R.id.linear_nro_voucher);
        agregar = layout.findViewById(R.id.agregar);
        ver_foto = layout.findViewById(R.id.ver_foto);


        edt_monto = (EditText) layout.findViewById(R.id.edt_monto);
        edit_saldo = (EditText) layout.findViewById(R.id.edit_saldo);
        spinner_tarjeta = (Spinner) layout.findViewById(R.id.spinner_tarjeta);
        edt_numero_tarjeta = (EditText) layout.findViewById(R.id.edt_numero_tarjeta);
        edt_numero_voucher = (EditText) layout.findViewById(R.id.edt_numero_voucher);
        edt_nro_com = (EditText) layout.findViewById(R.id.edt_nro_com);
        edt_numero_letras = (EditText) layout.findViewById(R.id.edt_numero_letras);
        edt_cuotas = (EditText) layout.findViewById(R.id.edt_cuotas);
        edt_numero_nc = (EditText) layout.findViewById(R.id.edt_numero_nc);
        edt_monto_pagar = (EditText) layout.findViewById(R.id.edt_monto_pagar);
        btn_vencimiento = (Button) layout.findViewById(R.id.btn_vencimiento);
        edt_autorizacion = (EditText) layout.findViewById(R.id.edt_autorizacion);
        edt_numero_cuenta = (EditText) layout.findViewById(R.id.edt_numero_cuenta);
        edt_numero_cheque = (EditText) layout.findViewById(R.id.edt_numero_cheque);
        edt_numero_deposito = (EditText) layout.findViewById(R.id.edt_numero_deposito);
        btn_fecha_vigencia = (Button) layout.findViewById(R.id.btn_fecha_vigencia);
        edt_referencia = (EditText) layout.findViewById(R.id.edt_referencia);
        btn_agregar = (Button) layout.findViewById(R.id.btn_agregar);

        calculateValor();

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
                        Intent intent = new Intent(FormaPagoActivity.this, PhotoViewer.class);
                        intent.putExtra("imageString64", "" +Utils.convertBase64String(bitmap));
                        startActivity(intent);
                    }

                }else{
                    showAlert("LO SENTIMOS NO TIENE FOTO ASIGNADA");
                }
            }
        });

        btn_vencimiento.setText(""+Utils.getFecha());
        fechaVencimiento = ""+Utils.getFecha();
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        btn_vencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = new Locale ( "es" , "ES" );
                Locale.setDefault(locale);
                DatePickerDialog datePickerDialog = new DatePickerDialog(FormaPagoActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
                        try {
                            Date date = format.parse(dayOfMonth+"/"+monthOfYear+"/"+year);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            btn_vencimiento.setText(sdf.format(cal.getTime()));
                            fechaVencimiento = sdf.format(cal.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, year, month, day);

                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "CANCELAR", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePickerDialog);
                datePickerDialog.show();
            }
        });


        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    validateTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        showList();
        showListBanco();
        showListTarjetas();
        showListCuenta();
    }

    private void selectPick(){
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Cámara", "Galería"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(FormaPagoActivity.this);
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
            Toast.makeText(FormaPagoActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
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

    private void calculateValor() {

        double valorTotal = 146.30;

        edt_monto.setText(""+ String.format("%.2f", Utils.roundFloat(valorTotal, 2)));

        saldo = valorTotal;
        edit_saldo.setText(""+ String.format("%.2f", saldo));
        saldo = Double.parseDouble(String.format("%.2f", saldo));
    }

    private void showList(){

        String[] FormasPago = {"EFECTIVO", "CHEQUE", "DEPÓSITO", "TRANSFERENCIA","NOTA CRÉDITO","LETRA","TARJETA DE CRÉDITO","PAGARÉ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,FormasPago);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_forma.setAdapter(adapter);
        spinner_forma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        showhide(Const.FORMA_EF);
                        break;
                    case 1:
                        showhide(Const.FORMA_CHQ);
                        break;
                    case 2:
                        showhide(Const.FORMA_DEP);
                        break;
                    case 3:
                        showhide(Const.FORMA_TRA);
                        break;
                    case 4:
                        showhide(Const.FORMA_NC);
                        break;
                    case 5:
                        showhide(Const.FORMA_LET);
                        break;
                    case 6:
                        showhide(Const.FORMA_TC);
                        break;
                    case 7:
                        showhide(Const.FORMA_PAG);
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showListBanco(){

        String[] FormasPago = {"BOLIVARIANO","PICHINCHA","PACÍFICO","INTERNACIONAL","PROINCO","GUAYAQUIL","AMAZONAS","RUMIÑAHUI"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,FormasPago);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_banco.setAdapter(adapter);
        spinner_banco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showListCuenta(){

        String[] FormasPago = {"CTE. 45XXXXXX0123","CTE. 89XXXXX0022", "AHO. 142XXX001"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,FormasPago);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cuentas.setAdapter(adapter);
        spinner_cuentas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showhide(String naturaleza){

        if(naturaleza == null){ return; }

        if(naturaleza.equals(Const.FORMA_EF)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_CHQ)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.VISIBLE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.VISIBLE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.VISIBLE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.VISIBLE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);



        }else if(naturaleza.equals(Const.FORMA_DEP)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.VISIBLE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.VISIBLE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_TRA)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.VISIBLE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.VISIBLE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);

        }else if(naturaleza.equals(Const.FORMA_NC)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.VISIBLE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.VISIBLE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_RF)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.VISIBLE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.VISIBLE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_RI)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.VISIBLE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.VISIBLE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_LET)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.VISIBLE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.VISIBLE);
            linear_cuotas.setVisibility(View.VISIBLE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_COM)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.VISIBLE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_COMF)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.VISIBLE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_TC)){

            linear_tarjeta.setVisibility(View.VISIBLE);
            linear_n_tarjeta.setVisibility(View.VISIBLE);
            linear_banco.setVisibility(View.VISIBLE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.VISIBLE);

        }else if(naturaleza.equals(Const.FORMA_PAG)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }else if(naturaleza.equals(Const.FORMA_DB)){

            linear_tarjeta.setVisibility(View.GONE);
            linear_n_tarjeta.setVisibility(View.GONE);
            linear_banco.setVisibility(View.GONE);
            linear_cuenta_bancaria.setVisibility(View.GONE);
            linear_vencimiento.setVisibility(View.GONE);
            linear_autoriazacion.setVisibility(View.GONE);
            linear_numero_cuenta.setVisibility(View.GONE);
            linear_fecha_vigencia.setVisibility(View.GONE);
            linear_numero_cheque.setVisibility(View.GONE);
            linear_numero_deposito.setVisibility(View.GONE);
            linear_numero_nc.setVisibility(View.GONE);
            linear_nro_letra.setVisibility(View.GONE);
            linear_cuotas.setVisibility(View.GONE);
            linear_n_com.setVisibility(View.GONE);
            linear_nro_voucher.setVisibility(View.GONE);


        }

    }

    private void showListTarjetas(){


        String[] FormasPago = {"VISA","MASTER CARD","DINERS","DISCOVER"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,FormasPago);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tarjeta.setAdapter(adapter);
        spinner_tarjeta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void validateTable() throws SQLException {


    }

    @Override
    public void doRetry() {

    }
}
