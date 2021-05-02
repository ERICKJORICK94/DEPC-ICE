package com.smartapp.depc_ice.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.smartapp.depc_ice.R;

/**
 * Created by Pavilion on 27/09/2016.
 */
public class BaseFragment extends Fragment implements DialogInterface.OnCancelListener {

    public ProgressBar progressBar;
    public TextView txt_espere;
    public LinearLayout linear_process;
    public LinearLayout linear_container;
    public LayoutInflater inflater;
    public ProgressDialog progress;
    private BaseFragmentCallbacks callBack;
    public View parent;
    ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        parent = (View) inflater.inflate(R.layout.activity_base, container, false);
        this.container = container;

        try {
            callBack = (BaseFragmentCallbacks) this;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error: "+e.toString());
        }

        progressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        txt_espere = (TextView) parent.findViewById(R.id.txt_espere);
        linear_process = (LinearLayout) parent.findViewById(R.id.linear_process);
        linear_container = (LinearLayout) parent.findViewById(R.id.linear_container);


        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.Red),
                android.graphics.PorterDuff.Mode.SRC_IN);

        txt_espere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (callBack != null)
                        callBack.doRetry();

                } catch (Exception e) {

                }
            }
        });

        hideLoading();

        return parent;
    }

   public void showLoading(){
        linear_process.setVisibility(View.VISIBLE);
        linear_container.setVisibility(View.GONE);
        txt_espere.setText("" + getResources().getString(R.string.lbl_procesando));
        txt_espere.setBackgroundColor(getResources().getColor(R.color.transparent));
        txt_espere.setPadding(0, 0, 0, 0);
        txt_espere.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void showAlert(String mensaje){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Atención");
        alertDialog.setMessage(""+mensaje);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void hideLoading(){
        linear_process.setVisibility(View.GONE);
        linear_container.setVisibility(View.VISIBLE);
    }

    public void setTextLoading(String title){
        linear_process.setVisibility(View.VISIBLE);
        linear_container.setVisibility(View.GONE);

        if(title !=  null){
            txt_espere.setText(""+title);
        }else {
            txt_espere.setText("");
        }

        txt_espere.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    public View addLayout(int id){

        View view = (View) inflater.inflate(id, this.container, false);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linear_container.addView(view);

        return view;
    }

    public void showProgressWait(String msm){

        String message = getResources().getString(R.string.lbl_procesando);
        if(msm != null)
            message = msm;

        progress = new ProgressDialog(getActivity());
        progress.setMessage(msm);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.setOnCancelListener(this);
        progress.show();
    }

    public void showProgressWait() {

        progress = new ProgressDialog(getActivity());
        progress.setMessage(getResources().getString(R.string.lbl_procesando));
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.setOnCancelListener(this);
        progress.show();

    }

    public void showRetry(){

        final float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (15 * scale + 0.5f);

        linear_process.setVisibility(View.VISIBLE);
        linear_container.setVisibility(View.GONE);
        txt_espere.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        txt_espere.setText("" + getResources().getString(R.string.lbl_reintentar));
        txt_espere.setBackgroundColor(getResources().getColor(R.color.transparent));
        txt_espere.setPadding(padding, padding, padding, padding);

    }

    public  void hideProgressWait() {
        try {
            if (progress != null) {
                if (progress.isShowing()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                }
                else {
                }
            } else {
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        try {
            if(callBack != null)
                callBack.cancelLoading();

        }catch (Exception e){

        }
    }



    public static interface BaseFragmentCallbacks {

        void doRetry();
        void cancelLoading();
    }
}

