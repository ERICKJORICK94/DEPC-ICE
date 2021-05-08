package com.smartapp.depc_ice.Activities.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.smartapp.depc_ice.Activities.AcercaDe.AcercaDeActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Home.Adapter.GridAdapter;
import com.smartapp.depc_ice.Activities.Login.LoginActivity;
import com.smartapp.depc_ice.Activities.Novedades.NovedadesActivity;
import com.smartapp.depc_ice.Activities.VerDatos.VerDatosActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;


public class MainActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks {

    private View layout;
    private GridViewWithHeaderAndFooter grid;
    private LayoutInflater layoutInflater;
    private View header;
    private TextView tvCantSincro;
    private ImageView refrescar_efectividad;
    private TextView ventas;
    private TextView presupuesto;
    private TextView cobranza;
    private TextView txt_porcentaje;
    private RoundCornerProgressBar progress_ventas;
    int cont=0;
    private LinearLayout linear_ver_datos;
    private Usuario user;

    public void validaSincroPendientes(){

        try {
            cont=0;
            /*List<String> fechas = new ArrayList<String>();
            try {
                final List<ClientesAgendar> clientes = DataBaseHelper.getClienteAgendar(San32Application.getApplication().getClientesAgendarDao());
                if (clientes != null) {
                    for (ClientesAgendar ca : clientes){
                        if (!fechas.contains(ca.getFechaRealizada())){
                            fechas.add(ca.getFechaRealizada());
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            cont += fechas.size();*/

            tvCantSincro.setText(""+cont);
            if(cont != 0)
                tvCantSincro.setVisibility(View.VISIBLE);

            else
                tvCantSincro.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = addLayout(R.layout.activity_main);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        header = layoutInflater.inflate(R.layout.header_home, null);
        ((LinearLayout) header.findViewById(R.id.about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AcercaDeActivity.class);
                startActivity(intent);
            }
        });
        tvCantSincro = header.findViewById(R.id.cant_sincro);
        linear_ver_datos =  (LinearLayout) header.findViewById(R.id.linear_ver_datos);
        refrescar_efectividad = (ImageView) header.findViewById(R.id.refrescar_efectividad);
        ventas  = (TextView) header.findViewById(R.id.ventas);
        cobranza = (TextView) header.findViewById(R.id.cobranza);
        presupuesto = (TextView) header.findViewById(R.id.presupuesto);
        txt_porcentaje = (TextView) header.findViewById(R.id.txt_porcentaje);
        progress_ventas = (RoundCornerProgressBar) header.findViewById(R.id.progress_ventas);

        Button configurar_impresora = (Button) header.findViewById(R.id.configurar_impresora);
        configurar_impresora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateImpresora();
            }
        });

        refrescar_efectividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getEfectividadCliente();
            }
        });

        linear_ver_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, VerDatosActivity.class);
                startActivity(intent);


            }
        });

        TextView vendedor = (TextView) header.findViewById(R.id.vendedor);

        vendedor.setText("");
        try {
             List<Usuario> usuarios = DataBaseHelper.getUsuario(DepcApplication.getApplication().getUsuarioDao());
            if (usuarios != null){
                if (usuarios.size() > 0) {
                    user = usuarios.get(0);
                    if (user.getNombrescompletos() != null){
                        vendedor.setText(""+user.getNombrescompletos());
                    }
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ((LinearLayout) header.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("¿Está seguro que desea cerrar sesión?")
                        .setCancelable(false)
                        .setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                try {
                                    DataBaseHelper.deleteUUsuario(DepcApplication.getApplication().getUsuarioDao());
                                    DataBaseHelper.deleteClientes(DepcApplication.getApplication().getClientesDao());
                                    DataBaseHelper.deleteBodega(DepcApplication.getApplication().getBodegaDao());
                                    DataBaseHelper.deleteProducto(DepcApplication.getApplication().getProductosDao());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        ((LinearLayout) header.findViewById(R.id.novedades)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NovedadesActivity.class);
                startActivity(intent);
            }
        });


        ((RelativeLayout) header.findViewById(R.id.sincronizar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog MyDialog = new Dialog(MainActivity.this);
                MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                MyDialog.setContentView(R.layout.sync_layout);
                MyDialog.setCancelable(true);
                TextView lbl_fecha = (TextView) MyDialog.findViewById(R.id.lbl_fecha);
                TextView lbl_sincronizaciones = (TextView) MyDialog.findViewById(R.id.lbl_sincronizaciones);
                Button btn_ok = (Button) MyDialog.findViewById(R.id.sync);
                Button btn_desc = (Button) MyDialog.findViewById(R.id.desc);
                lbl_sincronizaciones.setGravity(Gravity.CENTER_HORIZONTAL);
                //lbl_fecha.setText(""+Utils.getDateSync(MainActivity.this));
                lbl_sincronizaciones.setText("");

                /*if (contadorUbicaciones > 0){
                    if (contadorUbicaciones == 1) {
                        lbl_sincronizaciones.setText(""+contadorUbicaciones+ " Actualización de ubicación ");
                    }else{
                        lbl_sincronizaciones.setText(""+contadorUbicaciones+ " Actualizaciones de ubicación ");
                    }
                }*/


                if (cont  > 0){
                    btn_ok.setEnabled(true);
                }else{
                    btn_ok.setEnabled(false);
                }

                btn_desc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.cancel();

                        //ClienteAgendaTasl pt = new ClienteAgendaTasl();
                        //pt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog.cancel();

                        //EnvioClientesNuevosTask pt = new EnvioClientesNuevosTask();
                        //pt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                });

                MyDialog.show();

            }
        });


        ((TextView) header.findViewById(R.id.txt_imei)).setText("IMEI: " + Utils.getImei(this));

        if (layout.findViewById(R.id.lista) instanceof GridViewWithHeaderAndFooter) {
            grid = (GridViewWithHeaderAndFooter) layout.findViewById(R.id.lista);
            grid.addHeaderView(header);
            String[] opcionesMenu = getResources().getStringArray(R.array.menu_array);
            grid.setAdapter(new GridAdapter(this, opcionesMenu));

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //menuGo(position);

                    try {
                        String[] fields = opcionesMenu[position].split("[\t ]");
                        if (fields.length >= 5) {
                            if (fields[4].length() > 0) {
                                Intent intent = new Intent(MainActivity.this, Class.forName(fields[4]));
                                startActivity(intent);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


    }

    @Override
    public void doRetry() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        validaSincroPendientes();
    }

    private void validateImpresora(){


        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Por favor, seleccione impresora a utilizar");

        // add a list
        String[] animals = {"Impresora Zebra", "Impresora Bluetooth", "Sin Impresora"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0){
                    Utils.setSinImpresora(MainActivity.this, false);
                    Utils.setZebra(MainActivity.this, true);
                }else if (which == 1){
                    Utils.setSinImpresora(MainActivity.this, false);
                    Utils.setZebra(MainActivity.this, false);
                }else{
                    Utils.setZebra(MainActivity.this, false);
                    Utils.setSinImpresora(MainActivity.this, true);
                }

                Utils.setSeleccioImpresora(MainActivity.this, true);
            }
        });


        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

}
