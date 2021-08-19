package com.smartapp.depc_ice.Activities.GabinetGeneral;

import android.Manifest;
import android.app.Activity;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.GabinetGeneral.Adapter.ListaGabinetGeneralAdapter;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.ScannerQr.ScanQrActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.Gabinet;
import com.smartapp.depc_ice.Entities.GabinetGeneral;
import com.smartapp.depc_ice.Entities.PuntosVenta;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Interface.IClientes;
import com.smartapp.depc_ice.Interface.IEstadoGabinet;
import com.smartapp.depc_ice.Interface.IGabinetGeneral;
import com.smartapp.depc_ice.Interface.IPuntosVentas;
import com.smartapp.depc_ice.Models.ClientesModel;
import com.smartapp.depc_ice.Models.EstadoGabinetModel;
import com.smartapp.depc_ice.Models.GabinetGeneralModel;
import com.smartapp.depc_ice.Models.GabinetModel;
import com.smartapp.depc_ice.Models.PuntosVentasModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
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

/**
 * Created by RICHARD on 16/11/2017.
 */

public class GabinetGeneralActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;
    private LayoutInflater layoutInflater;
    private SearchView search;
    private ListView lista;
    private TextView num_cliente;
    private Call<IClientes.dataClientes> call;
    private IClientes.dataClientes data;
    private boolean isSearch = false;
    private String condition = "";
    private String buscar = "";
    private List<GabinetGeneral> gabinetList;

    private Call<IPuntosVentas.dataPuntos> callPuntos;
    private IPuntosVentas.dataPuntos dataPuntos;
    private Call<IEstadoGabinet.dataBodega> callEstado;
    private IEstadoGabinet.dataBodega dataEstado;
    private Call<IGabinetGeneral.dataGabinet> callGabinet;
    private IGabinetGeneral.dataGabinet dataGabinet;

    private Usuario user;
    private PuntosVenta puntosVenta;
    private List<EstadoGabinet> estadoGabinets;
    private final static int MY_PERMISSIONS_REQUEST_CAMARA = 9991;
    private String selectEstados = "0";
    private int selectedPosition = 0;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.gabinet_general_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        search = (SearchView) layout.findViewById(R.id.search);
        lista = (ListView) layout.findViewById(R.id.lista);
        num_cliente = (TextView) layout.findViewById(R.id.num_cliente);
        num_cliente.setText("");
        num_cliente.setVisibility(View.GONE);

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

        search.setActivated(true);
        search.setQueryHint("Buscar Cabinet...");
        search.onActionViewExpanded();
        search.setIconified(false);
        search.clearFocus();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                num_cliente.setText("");
                num_cliente.setVisibility(View.GONE);
                lista.setAdapter(null);
                condition(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0){
                    showList();
                }else{
                    num_cliente.setText("");
                    num_cliente.setVisibility(View.GONE);
                    lista.setAdapter(null);
                }
                if (newText.length() >= 3) {
                   // condition(newText);
                }
                buscar = newText;
                if (newText.equals("")){
                    selectedPosition = 0;
                    selectEstados = "0";
                    condition(buscar);

                }
                return false;
            }
        });

        //getClientes("");



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag){
            flag = false;
            return;
        }

        if (DepcApplication.getApplication().qrText.length() > 0){
            buscar = DepcApplication.getApplication().qrText;
            search.setQuery(buscar, true);
            DepcApplication.getApplication().qrText = "";
            return;
        }



        try {

            boolean flag = true;
            List<PuntosVenta> puntosVentas = DataBaseHelper.getPuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
            if (puntosVentas != null) {
                if (puntosVentas.size() > 0) {
                    puntosVenta = puntosVentas.get(0);
                    flag = false;
                }
            }

            if (flag){
                getPuntosVenta();
            }else {
                boolean flagEstados = true;
                estadoGabinets = DataBaseHelper.getEstadoGabinet(DepcApplication.getApplication().getEstadoGabinetDao());
                if (estadoGabinets != null) {
                    if (estadoGabinets.size() > 0) {
                        flagEstados = false;
                    }
                }

                if (flagEstados) {
                    getEstadosGabinet();
                }else{
                    getGabinetGeneral(buscar);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
        }
    }

    private void getPuntosVenta(){

        showProgressWait();

        //JSON SEND
        PuntosVentasModel model = new PuntosVentasModel();
        model.setUsuario_id(""+user.getUsuario());
        model.setMetodo("ListarPtoVenta");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IPuntosVentas request = DepcApplication.getApplication().getRestAdapter().create(IPuntosVentas.class);
            callPuntos = request.getPuntoVentas(body);
            callPuntos.enqueue(new Callback<IPuntosVentas.dataPuntos>() {
                @Override
                public void onResponse(Call<IPuntosVentas.dataPuntos> call, Response<IPuntosVentas.dataPuntos> response) {
                    if (response.isSuccessful()) {

                        dataPuntos = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataPuntos != null) {
                                if (dataPuntos.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataPuntos.getData() != null){
                                        if (dataPuntos.getData().getListarPuntos() != null) {
                                            if (dataPuntos.getData().getListarPuntos().size() > 0) {


                                                final List<PuntosVenta> puntosVentas;
                                                puntosVentas = dataPuntos.getData().getListarPuntos().get(0);

                                                if (puntosVentas != null) {
                                                    DataBaseHelper.deletePuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
                                                    DepcApplication.getApplication().getPuntosVentaDao().callBatchTasks(new Callable<PuntosVenta>() {
                                                        @Override
                                                        public PuntosVenta call() throws Exception {
                                                            for (PuntosVenta pto : puntosVentas) {
                                                                DataBaseHelper.savePuntosVenta(pto, DepcApplication.getApplication().getPuntosVentaDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showPuntosVentas();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataPuntos.getStatus_message() != null){
                                        mensajeError = dataPuntos.getStatus_message();
                                    }
                                }
                            }

                            showPuntosVentas();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showPuntosVentas();
                        }

                    } else {
                        hideProgressWait();
                        showPuntosVentas();
                    }
                }

                @Override
                public void onFailure(Call<IPuntosVentas.dataPuntos> call, Throwable t) {
                    hideProgressWait();
                    showPuntosVentas();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showPuntosVentas();

        }
    }

    private void showPuntosVentas(){

        hideProgressWait();

        try {

            List<PuntosVenta> puntosVentas = DataBaseHelper.getPuntosVenta(DepcApplication.getApplication().getPuntosVentaDao());
            if (puntosVentas != null) {
                if (puntosVentas.size() > 0) {
                    puntosVenta = puntosVentas.get(0);
                    getEstadosGabinet();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
        }
    }


    private void getEstadosGabinet(){

        showProgressWait();

        //JSON SEND
        EstadoGabinetModel model = new EstadoGabinetModel();
        model.setMetodo("ListarEstadoGabinet");


        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IEstadoGabinet request = DepcApplication.getApplication().getRestAdapter().create(IEstadoGabinet.class);
            callEstado = request.getZonas(body);
            callEstado.enqueue(new Callback<IEstadoGabinet.dataBodega>() {
                @Override
                public void onResponse(Call<IEstadoGabinet.dataBodega> call, Response<IEstadoGabinet.dataBodega> response) {
                    if (response.isSuccessful()) {

                        dataEstado = response.body();
                        try {

                            hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataEstado != null) {
                                if (dataEstado.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataEstado.getData() != null){
                                        if (dataEstado.getData().getListarEstadoGabinet() != null) {
                                            if (dataEstado.getData().getListarEstadoGabinet().size() > 0) {


                                                final List<EstadoGabinet> bodegas;
                                                bodegas = dataEstado.getData().getListarEstadoGabinet().get(0);

                                                if (bodegas != null) {
                                                    DataBaseHelper.deleteEstadoGabinet(DepcApplication.getApplication().getEstadoGabinetDao());
                                                    DepcApplication.getApplication().getEstadoGabinetDao().callBatchTasks(new Callable<EstadoGabinet>() {
                                                        @Override
                                                        public EstadoGabinet call() throws Exception {
                                                            for (EstadoGabinet cl : bodegas) {
                                                                DataBaseHelper.saveEstadoGabinet(cl, DepcApplication.getApplication().getEstadoGabinetDao());
                                                            }
                                                            return null;
                                                        }
                                                    });


                                                }

                                                showListEstado();

                                                return;
                                            }
                                        }
                                    }
                                }else{
                                    if (dataEstado.getStatus_message() != null){
                                        mensajeError = dataEstado.getStatus_message();
                                    }
                                }
                            }

                            showListEstado();

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showListEstado();
                        }

                    } else {
                        hideProgressWait();
                        //showAlert(Const.ERROR_DEFAULT);
                        showListEstado();
                    }
                }

                @Override
                public void onFailure(Call<IEstadoGabinet.dataBodega> call, Throwable t) {
                    hideProgressWait();
                    //showAlert(Const.ERROR_DEFAULT);
                    showListEstado();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            //showAlert(Const.ERROR_DEFAULT);
            showListEstado();

        }
    }

    private void showListEstado(){

        hideProgressWait();

        try {

            estadoGabinets = DataBaseHelper.getEstadoGabinet(DepcApplication.getApplication().getEstadoGabinetDao());
            if (estadoGabinets != null) {
                if (estadoGabinets.size() > 0) {
                    getGabinetGeneral(buscar);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("TAG---","error: "+e.toString());
        }

    }






    private void condition(String search){
        isSearch = true;
        search.trim();
        buscar = search;
        search = search.replace(" ","%").toUpperCase();
        condition = "and (serie.serie LIKE '%"+search+"%' or a.id_congelador LIKE '%"+search+"%')";
        getGabinetGeneral(condition);
    }

    private void getGabinetGeneral(String search){

        showProgressWait();

        lista.setAdapter(null);
        int limit = Const.PARAM_MAX_ROW;
        num_cliente.setText("");
        num_cliente.setVisibility(View.GONE);
        String estadosString = "";

        if (selectEstados.equals("0")) {
            if (estadoGabinets != null) {
                if (estadoGabinets.size() > 0) {
                    for (EstadoGabinet est : estadoGabinets) {
                        estadosString = estadosString + "" + est.getNum_estado() + ",";
                    }
                }
            }
            if (estadosString.length() > 0) {
                estadosString = removeLastChar(estadosString);
            }
        }else{
            estadosString = ""+selectEstados;
        }

        String buscar = "and a.Estado IN ("+estadosString+")";
        if (search.length() > 0){
            buscar = buscar+" "+search;
        }

        String pto_venta = "";
        if (puntosVenta.getPto_vta_id() != null){
            pto_venta = ""+puntosVenta.getPto_vta_id();
        }else{
            pto_venta = ""+user.getPto_vta_id_operaciones();
        }

        //JSON SEND
        GabinetGeneralModel model = new GabinetGeneralModel();
        model.setPto_vta_id(""+pto_venta);
        //model.setFiltro("limit "+Const.PARAM_MAX_ROW+" offset 0");
        model.setCondicion(buscar+" limit "+Const.PARAM_MAX_ROW);
        //model.setCondicion(buscar);
        model.setMetodo("ListarGabinet");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IGabinetGeneral request = DepcApplication.getApplication().getRestAdapter().create(IGabinetGeneral.class);
            callGabinet = request.getGabinet(body);
            callGabinet.enqueue(new Callback<IGabinetGeneral.dataGabinet>() {
                @Override
                public void onResponse(Call<IGabinetGeneral.dataGabinet> call, Response<IGabinetGeneral.dataGabinet> response) {
                    if (response.isSuccessful()) {

                        dataGabinet = response.body();
                        try {

                            //hideProgressWait();

                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataGabinet != null) {
                                if (dataGabinet.getStatus() == Const.COD_ERROR_SUCCESS) {
                                    if (dataGabinet.getData() != null){
                                        if (dataGabinet.getData().getListarGabinet() != null) {
                                            if (dataGabinet.getData().getListarGabinet().size() > 0) {


                                                final List<GabinetGeneral> gabinets;
                                                gabinets = dataGabinet.getData().getListarGabinet().get(0);

                                                if (gabinets != null) {
                                                    //DataBaseHelper.deleteGabinetGeneral(DepcApplication.getApplication().getGabinetGeneralDao());
                                                    DepcApplication.getApplication().getGabinetDao().callBatchTasks(new Callable<GabinetGeneral>() {
                                                        @Override
                                                        public GabinetGeneral call() throws Exception {
                                                            for (GabinetGeneral gab : gabinets) {

                                                                List<EstadoGabinet> estadoGabinets1 = DataBaseHelper.getEstadoGabinetByEstado(DepcApplication.getApplication().getEstadoGabinetDao(),""+gab.getEstado());
                                                                gab.setEstado_descripcion("");
                                                                if (estadoGabinets1 != null){
                                                                    if (estadoGabinets1.size() > 0){
                                                                        EstadoGabinet dr = estadoGabinets1.get(0);
                                                                        gab.setEstado_descripcion(""+dr.getDescripcion());
                                                                    }
                                                                }


                                                                DataBaseHelper.saveGabinetGeneral(gab, DepcApplication.getApplication().getGabinetGeneralDao());
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
                                    if (dataGabinet.getStatus_message() != null){
                                        mensajeError = dataGabinet.getStatus_message();
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
                        showList();
                    }
                }

                @Override
                public void onFailure(Call<IGabinetGeneral.dataGabinet> call, Throwable t) {
                    hideProgressWait();
                    showList();
                }
            });

        }catch (Exception e){
            hideProgressWait();
            showList();

        }

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.qr, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_qr:
                qrAction();
                break;
            case R.id.action_select:
                filter();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(){

        if (estadoGabinets != null){
            List<String> items = new ArrayList<String>();
            items.add("TODO");
            for (EstadoGabinet est : estadoGabinets){
                if (est.getDescripcion() != null){
                    items.add(""+est.getDescripcion());
                }
            }

            CharSequence[] cs = items.toArray(new CharSequence[items.size()]);

            new AlertDialog.Builder(this)
                    .setSingleChoiceItems(cs, selectedPosition, null)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                             selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                            if (selectedPosition == 0){
                                selectEstados = "0";
                                getGabinetGeneral(buscar);
                            }else{
                                selectEstados = estadoGabinets.get(selectedPosition - 1).getNum_estado();
                                getGabinetGeneral(buscar);
                            }

                        }
                    })
                    .show();
        }

    }

    private void qrAction(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {

                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    flag = true;
                    startActivityForResult(intent, 2299);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    flag = true;
                    startActivityForResult(intent, 2299);
                }

            }else{

                checkCameraPermission();

            }

        } else {

            checkCameraPermission();

        }
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                    flag = true;
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA );
                    // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
                }
            }else{ //have permissions
                iniciarScan ();
            }
        }else{ // Pre-Marshmallow
            iniciarScan ();
        }
    }

    private void iniciarScan(){

        Intent intent = new Intent(GabinetGeneralActivity.this, ScanQrActivity.class);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == 2299) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    checkCameraPermission();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMARA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarScan();
            } else {
                showAlert("Debe aceptar el permiso antes de continuar");
            }
        }
    }

    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    private void showList(){

        hideProgressWait();
        try {
            gabinetList = null;
                if (isSearch) {

                    String nuevaCondicion = "";
                    if (selectEstados == "0"){
                        nuevaCondicion = "serie LIKE '%"+buscar+"%' or id_congelador LIKE '%"+buscar+"%'";
                    }else{
                        nuevaCondicion = "(serie LIKE '%"+buscar+"%' or id_congelador LIKE '%"+buscar+"%') and estado = '"+selectEstados+"'";
                    }

                    gabinetList = DataBaseHelper.getGabinetGeneralCondicion(DepcApplication.getApplication().getGabinetGeneralDao(), "" + nuevaCondicion);
                }else{

                    if (selectEstados == "0"){
                        gabinetList = DataBaseHelper.getGabinetGeneral(DepcApplication.getApplication().getGabinetGeneralDao());
                    }else{
                        String nuevaCondicion = "estado = '"+selectEstados+"'";
                        gabinetList = DataBaseHelper.getGabinetGeneralCondicion(DepcApplication.getApplication().getGabinetGeneralDao(), "" + nuevaCondicion);
                    }

                }

                if (gabinetList != null) {
                    if (gabinetList.size() > 0) {

                        lista.setAdapter(new ListaGabinetGeneralAdapter(GabinetGeneralActivity.this, gabinetList));
                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(GabinetGeneralActivity.this, DetalleGabinetGeneralActivity.class);
                                intent.putExtra("detalle_gabinet", gabinetList.get(position));
                                startActivity(intent);
                            }
                        });

                        num_cliente.setVisibility(View.VISIBLE);
                        if (gabinetList.size() == 1) {
                            num_cliente.setText("" + gabinetList.size() + " cabinet encontrado");
                        } else {
                            num_cliente.setText("" + gabinetList.size() + " cabinet encontrados");
                        }
                    }else{
                        String error = Const.ERROR_NO_RESULT;
                        if (isSearch){ error = Const.ERROR_NO_RESULT;
                            showAlert(error);}

                    }
                }else{
                    String error = Const.ERROR_NO_RESULT;
                    if (isSearch){ error = Const.ERROR_NO_RESULT;
                        showAlert(error);}

                }

            //isSearch = false;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Const.ERROR_NO_RESULT);
        }

    }


    @Override
    public void doRetry() {

    }
}
