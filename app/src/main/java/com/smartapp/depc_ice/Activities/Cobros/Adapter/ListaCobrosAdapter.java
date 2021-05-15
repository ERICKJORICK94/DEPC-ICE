package com.smartapp.depc_ice.Activities.Cobros.Adapter;

import android.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.smartapp.depc_ice.Activities.Cobros.CobrosActivity;
import com.smartapp.depc_ice.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RICHARD on 18/11/2017.
 */

public class ListaCobrosAdapter extends BaseAdapter {

    private CobrosActivity mContext;
    private LayoutInflater mInflater;
    private List<Integer> positionCheck;

    public ListaCobrosAdapter(CobrosActivity c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        positionCheck = new ArrayList<Integer>();
    }

    public int getCount() {

        //return mContext.cobroList.size();

        return 20;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.custom_cobro, null);
            viewHolder = new ViewHolder();
            viewHolder.cuota = (TextView) convertView.findViewById(R.id.cuota);
            viewHolder.numero = (TextView) convertView.findViewById(R.id.numero);
            viewHolder.fecha_vencida = (TextView) convertView.findViewById(R.id.fecha_vencida);
            viewHolder.valor_vendido = (TextView) convertView.findViewById(R.id.valor_vendido);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);


            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }

        //Cobro item = mContext.cobroList.get(position);

        viewHolder.cuota.setText(""+(position + 1));

        viewHolder.numero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickDetail(mContext.cobroList.get(position));
            }
        });

        viewHolder.fecha_vencida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clickDetail(mContext.cobroList.get(position));
            }
        });


        viewHolder.valor_vendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.check.isChecked()) {
                    //showAlertEditValor(position);
                }
            }
        });


        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    positionCheck.add(position);
                }else{
                    if(positionCheck.contains(position)){
                        int index = 0;
                        for(int p : positionCheck){
                            if(p == position){
                                positionCheck.remove(index);
                                break;
                            }
                            index += 1;
                        }
                    }
                }

                mContext.calculateValor(positionCheck);
            }
        });


        int fact = 3569;
        viewHolder.numero.setText("FAC #"+(3569+position)+"  8/12");
        viewHolder.fecha_vencida.setText("20/05/2021");
        viewHolder.valor_vendido.setText("$ 16.36");

        if (position%2 == 0){
            viewHolder.numero.setTextColor(mContext.getResources().getColor(R.color.Red));
            viewHolder.fecha_vencida.setTextColor(mContext.getResources().getColor(R.color.Red));
            viewHolder.valor_vendido.setTextColor(mContext.getResources().getColor(R.color.Red));
        }else{
            viewHolder.numero.setTextColor(mContext.getResources().getColor(R.color.Gray));
            viewHolder.fecha_vencida.setTextColor(mContext.getResources().getColor(R.color.Gray));
            viewHolder.valor_vendido.setTextColor(mContext.getResources().getColor(R.color.Black));
        }


        return convertView;
    }

    /*private void clickDetail(Cobro co){

                View detailProduct = mInflater.inflate(R.layout.detail_cobro, null);
                final AlertDialog alert = new AlertDialog.Builder(mContext).create();
                alert.setView(detailProduct);

                ImageView back = (ImageView) detailProduct.findViewById(R.id.back);
                TextView codigo= (TextView) detailProduct.findViewById(R.id.codigo);
                TextView nombre = (TextView) detailProduct.findViewById(R.id.nombre);
                TextView resumen = (TextView) detailProduct.findViewById(R.id.resumen);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });


                codigo.setText(""+co.getTipo_doc()+"# "+co.getNum_compte());
                nombre.setText(""+co.getNom_vendedor());

                resumen.setText("Tipo: "+co.getTipo_doc()+"\n"+
                                "Fecha ingreso: "+co.getFECHA_INGRESO()+"\n"+
                                "Fecha Vencimiento: "+co.getFECHA_VENCIMIENTO()+"\n"+
                                "Motivo: "+co.getMotivo()+"\n"+
                                "Bodega: "+co.getNom_bodega()+"\n"+
                                "Días vencidos: "+co.getDias_vencido()+"\n"+
                                "Valor abono: "+Utils.foramatearMiles(co.getValor_abono())+"\n"+
                                "Valor Comprobante: "+Utils.foramatearMiles(co.getValor_compte())+"\n"+
                                "Interés: "+"\n"+
                                 "Total a pagar: "+Utils.foramatearMiles(co.getSaldo()));

            alert.show();
    }*/

    /*private void showAlertEditValor(final int position){

        String valor = ""+mContext.cobroList.get(position).getSaldo();

        if(mContext.cobroList.get(position).getValorUsuario() != null){
            if(mContext.cobroList.get(position).getValorUsuario().length() > 0){
                valor = mContext.cobroList.get(position).getValorUsuario();
            }

        }

        new MaterialDialog.Builder(mContext)
                .title("Atención")
                .content("Abono parcial")
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .input(""+ String.format("%.2f", Double.parseDouble(valor)), ""+ String.format("%.2f", Double.parseDouble(valor)), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        mContext.cobroList.get(position).setValor_compte(mContext.cobroList.get(position).getValor_compte().replace(",","."));

                        if (input.length() == 0) {
                            dialog.dismiss();
                            mContext.cobroList.get(position).setValorUsuario(null);
                            notifyDataSetChanged();
                            mContext.calculateValor(positionCheck);

                            return;
                        }else{
                            if (Float.parseFloat(""+input) > Float.parseFloat(mContext.cobroList.get(position).getSaldo()) ){
                                dialog.dismiss();
                                mContext.cobroList.get(position).setValorUsuario(null);
                                notifyDataSetChanged();
                                mContext.calculateValor(positionCheck);
                                Toast.makeText(mContext, "El valor ingresado no puede ser mayor al valor del documento seleccionado",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            mContext.cobroList.get(position).setValorUsuario(""+input);
                        }
                        notifyDataSetChanged();
                        mContext.calculateValor(positionCheck);
                    }
                }).show();

    }*/


    static class ViewHolder {

        TextView cuota;
        TextView numero;
        TextView fecha_vencida;
        TextView valor_vendido;
        CheckBox check;


    }



}