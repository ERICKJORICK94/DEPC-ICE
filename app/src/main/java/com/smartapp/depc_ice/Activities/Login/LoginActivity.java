package com.smartapp.depc_ice.Activities.Login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Activities.AcercaDe.AcercaDeActivity;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.Activities.Home.MainActivity;
import com.smartapp.depc_ice.Database.DataBaseHelper;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.Interface.IUsuario;
import com.smartapp.depc_ice.Models.LoginModel;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Const;
import com.smartapp.depc_ice.Utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private EditText edt_user;
    private TextInputEditText edt_pass;
    private Button login;
    private View layout;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    private Call<IUsuario.dataUsuario> callUsuario;
    private IUsuario.dataUsuario dataUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        layout = addLayout(R.layout.login_layout);

        inputLayoutName = (TextInputLayout) layout.findViewById(R.id.input_layout_name);
        inputLayoutPassword = (TextInputLayout) layout.findViewById(R.id.input_layout_password);
        edt_user = (EditText) layout.findViewById(R.id.edt_user);
        edt_pass = (TextInputEditText) layout.findViewById(R.id.edt_pass);
        login = (Button) layout.findViewById(R.id.login);


        layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout.findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AcercaDeActivity.class);
                startActivity(intent);
            }
        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView) layout.findViewById(R.id.version)).setText("v"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        edt_user.addTextChangedListener(new MyTextWatcher(edt_user));
        edt_pass.addTextChangedListener(new MyTextWatcher(edt_pass));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        login();

    }


    private void login(){

        showProgressWait();

        LoginModel model = new LoginModel();
        model.setUsuario(""+edt_user.getText().toString().trim());
        model.setPassword(""+edt_pass.getText().toString().trim());
        model.setMetodo("Login");

        final Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("TAG---","json: "+json);
        RequestBody body = RequestBody.create(MediaType.parse(Const.APPLICATION_JSON), json);

        try {

            IUsuario request = DepcApplication.getApplication().getRestAdapter().create(IUsuario.class);
            callUsuario = request.getLogin(body);
            callUsuario.enqueue(new Callback<IUsuario.dataUsuario>() {
                @Override
                public void onResponse(Call<IUsuario.dataUsuario> call, Response<IUsuario.dataUsuario> response) {
                    if (response.isSuccessful()) {

                        dataUsuario = response.body();

                        try {

                            hideProgressWait();
                            String mensajeError = Const.ERROR_DEFAULT;

                            if (dataUsuario != null) {
                                if (dataUsuario.getStatus() == Const.COD_ERROR_SUCCESS) {

                                    if (dataUsuario.getData() != null){


                                        if (dataUsuario.getData().getPerfil() == -1){
                                            showAlert("USUARIO NO HABILITADO PARA EL USO DE LA APP");
                                            return;

                                        }else {
                                            DataBaseHelper.deleteUUsuario(DepcApplication.getApplication().getUsuarioDao());
                                            DataBaseHelper.saveUsuario(dataUsuario.getData(), DepcApplication.getApplication().getUsuarioDao());
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                    }

                                }else{
                                    if (dataUsuario.getStatus_message() != null){
                                        mensajeError = dataUsuario.getStatus_message();
                                    }
                                }
                            }

                            showAlert(""+mensajeError);


                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressWait();
                            showAlert(""+Const.ERROR_DEFAULT);
                        }

                    } else {
                        hideProgressWait();
                        showAlert(""+Const.ERROR_DEFAULT);
                    }
                }

                @Override
                public void onFailure(Call<IUsuario.dataUsuario> call, Throwable t) {
                    hideProgressWait();
                    showAlert(""+Const.ERROR_DEFAULT);
                }
            });

        }catch (Exception e){
            Log.e(TAG,"Error: "+e.toString());
            showAlert(""+Const.ERROR_DEFAULT);
        }


    }

    private boolean validateName() {
        if (edt_user.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(edt_user);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (edt_pass.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(edt_pass);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void doRetry() {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edt_user:
                    validateName();
                    break;
                case R.id.edt_pass:
                    validatePassword();
                    break;
            }
        }
    }
}
