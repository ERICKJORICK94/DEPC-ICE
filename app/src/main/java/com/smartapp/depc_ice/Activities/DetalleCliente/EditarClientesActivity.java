package com.smartapp.depc_ice.Activities.DetalleCliente;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.CrearClientes.CrearClientesActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Interface.IActualizarCliente;
import com.smartapp.depc_ice.Interface.IZonas;
import com.smartapp.depc_ice.Models.ActualizarClienteModel;
import com.smartapp.depc_ice.Models.BodegasModel;
import com.smartapp.depc_ice.Models.RegistrarClienteModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.PDFActivity;
import com.smartapp.depc_ice.Utils.Utils;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;

public class EditarClientesActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

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
    private Button actualizar;
    private Button eliminar;
    private TextView ver_pdf;
    private TextView pdf;

    private Call<IZonas.dataBodega> call;
    private IZonas.dataBodega data;
    private Call<IActualizarCliente.dataBodega> callRegistro;
    private IActualizarCliente.dataBodega dataRegistro;
    private int indexFormaPago = 0;
    private int indexContribuyente = 0;
    private int indexTipoCliente = 0;
    private int indexZonas = -1;
    private boolean nacionalidad = true;
    private List<Zonas> zonas;
    private Clientes cliente = null;
    String PDFbase64String = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.editar_clientes_layout);
        Utils.SetStyleActionBarTitle(this);

        cliente = DepcApplication.getApplication().getCliente();


        cedula = (EditText) layout.findViewById(R.id.cedula);
        pdf = (TextView) layout.findViewById(R.id.pdf);
        ver_pdf = (TextView) layout.findViewById(R.id.ver_pdf);
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
        actualizar = (Button) layout.findViewById(R.id.actualizar);
        eliminar = (Button) layout.findViewById(R.id.eliminar);

        if (cliente != null){
            if (cliente.getTipo_id_tercero() != null){
                if (cliente.getTipo_id_tercero().equals("CI")){
                    ci.setChecked(true);
                    tipo_id_tercero = "CI";
                }else{
                    ci.setChecked(false);
                }

                if (cliente.getTipo_id_tercero().equals("RUC")){
                    ruc.setChecked(true);
                    tipo_id_tercero = "RUC";
                }else{
                    ruc.setChecked(false);
                }

                if (cliente.getTipo_id_tercero().equals("PAS")){
                    pas.setChecked(true);
                    tipo_id_tercero = "PAS";
                }else{
                    pas.setChecked(false);
                }
            }

            if (cliente.getTercero_id() != null){
                cedula.setText(""+cliente.getTercero_id());
            }

            if (cliente.getNombre_comercial() != null){
                nombre.setText(""+cliente.getNombre_comercial());
            }

            if (cliente.getDireccion() != null){
                direccion.setText(""+cliente.getDireccion());
            }

            if (cliente.getDireccion_cobro() != null){
                direccion_cobro.setText(""+cliente.getDireccion_cobro());
            }

            if (cliente.getDias_credito() != null){
                dias.setText(""+cliente.getDias_credito());
            }

            if (cliente.getMonto_credito() != null){
                monto.setText(""+cliente.getMonto_credito());
            }

            if (cliente.getTelefono1() != null){
                celular.setText(""+cliente.getTelefono1());
            }

            if (cliente.getEmail() != null){
                correo.setText(""+cliente.getEmail());
            }

            if (cliente.getClase_cliente() != null){
                if (cliente.getClase_cliente().equals("1")){
                    nacional.setChecked(true);
                    extranjero.setChecked(false);
                    nacionalidad = true;
                }else{
                    nacional.setChecked(false);
                    extranjero.setChecked(true);
                    nacionalidad = false;
                }
            }


        }

        ver_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cliente != null){
                    if (cliente.getDocumentopdf() != null){
                    if (cliente.getDocumentopdf().length() > 0) {
                        if (!cliente.getDocumentopdf().equals("null")) {
                            storetoPdfandOpen(EditarClientesActivity.this, cliente.getDocumentopdf());
                            return;
                        }
                    }
                    }
                }

                if (PDFbase64String != null){
                    if (PDFbase64String.length() > 0){
                        if (!PDFbase64String.equals("null")) {
                            storetoPdfandOpen(EditarClientesActivity.this, PDFbase64String);
                            return;
                        }
                    }
                }

                showAlert("NO EXISTE ARCHIVO");
            }
        });


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(EditarClientesActivity.this, NormalFilePickActivity.class);
                intent4.putExtra(Constant.MAX_NUMBER, 1);
                intent4.putExtra(IS_NEED_FOLDER_LIST, false);
                intent4.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"pdf"});
                startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
            }
        });




        ci.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    cedula.setHint("Cédula");
                    tipo_id_tercero = "CI";

            }
        });

        ruc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    cedula.setHint("Ruc");
                     tipo_id_tercero = "RUC";

            }
        });

        pas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    cedula.setHint("Pasaporte");
                tipo_id_tercero = "PAS";

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


        String[] itemsFormaPago = new String[]{"CONTADO"};

        int index = 0;
        if (cliente != null){
            if (cliente.getForma_pago() != null){
                if (cliente.getForma_pago().equals("4")){
                    index = 0;
                }else if (cliente.getForma_pago().equals("5")){
                    index = 1;
                }
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsFormaPago);
        forma_pago.setAdapter(adapter);
        forma_pago.setSelection(index);
        indexFormaPago = index;
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


        int indexCon = 0;
        if (cliente != null){
            if (cliente.getContribuyente_especial() != null){
                if (cliente.getContribuyente_especial().equals("0")){
                    indexCon = 0;
                }else if (cliente.getContribuyente_especial().equals("1")){
                    indexCon = 1;
                }
            }
        }


        ArrayAdapter<String> adapterContribuyente = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsContribuyente);
        contribuyente.setAdapter(adapterContribuyente);
        contribuyente.setSelection(indexCon);
        indexContribuyente = indexCon;
        contribuyente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexContribuyente = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] itemsTipoCliente = new String[]{"PRECONTACTO"};

        int indexpre = 0;
        if (cliente != null){
            if (cliente.getTipo_cliente() != null){
                if (cliente.getTipo_cliente().equals("0")){
                    indexpre = 0;
                }else if (cliente.getTipo_cliente().equals("1")){
                    indexpre = 1;
                }
            }
        }
        ArrayAdapter<String> adapterTipoCliente= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsTipoCliente);
        tipo_cliente.setAdapter(adapterTipoCliente);
        tipo_cliente.setSelection(indexpre);
        tipo_cliente.setEnabled(false);
        indexTipoCliente = indexpre;
        tipo_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexTipoCliente = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(cedula.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese un Identificación para poder continuar", Toast.LENGTH_LONG).show();
                    return;
                }
                if(direccion.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese una dirección válida", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(direccion_cobro.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese una dirección de cobro válida", Toast.LENGTH_LONG).show();
                    return;
                }*/
                if(nombre.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese un nombre válido", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(dias.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese días de crédito", Toast.LENGTH_LONG).show();
                    return;
                }
                if(monto.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese monto de crédito", Toast.LENGTH_LONG).show();
                    return;
                }if (Integer.parseInt(dias.getText().toString()) == 0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese días de crédito mayor a cero", Toast.LENGTH_LONG).show();
                    return;
                }*/
                /*if(celular.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese una teléfono válido", Toast.LENGTH_LONG).show();
                    return;
                }*/

                /*if(correo.getText().length()==0){
                    Toast.makeText(EditarClientesActivity.this, "Ingrese un correo", Toast.LENGTH_LONG).show();
                    return;
                }*/

                if(indexZonas < 0 ){
                    Toast.makeText(EditarClientesActivity.this, "Seleccione una zona", Toast.LENGTH_LONG).show();
                    return;
                }

                String mensaje = "¿Seguro que desea continuar la actualización?";
                new AlertDialog.Builder(EditarClientesActivity.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendActualizar();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .show();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mensaje = "¿Seguro que desea eliminar el cliente?";
                new AlertDialog.Builder(EditarClientesActivity.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setMessage(mensaje)
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendEliminar();
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

    public void storetoPdfandOpen(Context context, String base) {


        String root = null;
        try {
            root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

        Log.d("ResponseEnv",root);

        File myDir = new File(root + "/DEPCONSA");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        String fname = "Aechivos-" + n + ".pdf";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {

            FileOutputStream out = new FileOutputStream(file);
            byte[] pdfAsBytes = android.util.Base64.decode(base , android.util.Base64.DEFAULT);
            out.write(pdfAsBytes);
            out.flush();
            out.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), "DEPCONSA");
        File imgFile = new File(dir, fname);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);

        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = Uri.parse("file://" + imgFile); // My work-around for new SDKs, causes ActivityNotFoundException in API 10.
        }

        /*sendIntent.setDataAndType(uri, "application/pdf");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(sendIntent);*/
        Intent intent = new Intent(EditarClientesActivity.this, PDFActivity.class);
        intent.putExtra("direccion",uri.getPath());
        startActivity(intent);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

                    int index = 0;
                    int contador = 0;
                   List<String> items= new ArrayList<String>();
                    for (Zonas z : zonas){
                        items.add(z.getDescripcion());
                        if (z.getZona_id() != null){
                            if (cliente.getZona_id() != null){
                                if (cliente.getZona_id().equals(""+z.getZona_id())){
                                    index = contador;
                                }
                            }
                        }
                        contador++;
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
                    zona.setAdapter(adapter);
                    zona.setSelection(index);
                    indexZonas = index;
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



    private void sendActualizar(){

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
        ActualizarClienteModel model = new ActualizarClienteModel();
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
        model.setNombre_comercial(""+nombre.getText().toString().trim());
        model.setSw_persona_juridica("1");
        model.setTipo_cliente(""+tipoCliente);
        model.setZona_id(""+zonas.get(indexZonas).getZona_id());
        model.setCliente_id(""+cliente.getCodigo_cliente_id());
        model.setDocumentopdf(PDFbase64String);
        model.setMetodo("ActualizarCliente");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);

        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IActualizarCliente request = DepcApplication.getApplication().getRestAdapter().create(IActualizarCliente.class);
            callRegistro = request.getRegistrarCliente(body);
            callRegistro.enqueue(new Callback<IActualizarCliente.dataBodega>() {
                @Override
                public void onResponse(Call<IActualizarCliente.dataBodega> call, Response<IActualizarCliente.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataRegistro = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataRegistro != null) {
                                if (dataRegistro.getStatus() == Const.COD_ERROR_SUCCESS) {

                                    cliente.setTipo_id_tercero(""+model.getTipo_id_tercero());
                                    cliente.setTercero_id(""+model.getTercero_id());
                                    cliente.setDias_credito(""+model.getDias_credito());
                                    cliente.setMonto_credito(""+model.getMonto_credito());
                                    cliente.setMonto_credito(""+model.getMonto_credito());
                                    cliente.setForma_pago(""+model.getForma_pago());
                                    cliente.setContribuyente_especial(""+model.getContribuyente_especial());
                                    cliente.setClase_cliente(""+model.getClase_cliente());
                                    cliente.setDireccion(""+model.getDireccion());
                                    cliente.setTelefono1(""+model.getTelefono1());
                                    cliente.setEmail(""+model.getEmail());
                                    cliente.setNombre_tercero(""+model.getNombre_tercero());
                                    cliente.setNombre_comercial(""+model.getNombre_comercial());
                                    cliente.setDireccion_cobro(""+model.getDireccion_cobro());
                                    cliente.setTipo_cliente(""+model.getTipo_cliente());
                                    cliente.setZona(""+model.getZona_id());
                                    cliente.setDocumentopdf(""+model.getDocumentopdf());

                                    DataBaseHelper.saveClientes(cliente, DepcApplication.getApplication().getClientesDao());

                                    new AlertDialog.Builder(EditarClientesActivity.this)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("Cliente actualizado con éxito")
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
                public void onFailure(Call<IActualizarCliente.dataBodega> call, Throwable t) {
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

    private void sendEliminar(){


    }

    @Override
    public void doRetry() {

    }

}
