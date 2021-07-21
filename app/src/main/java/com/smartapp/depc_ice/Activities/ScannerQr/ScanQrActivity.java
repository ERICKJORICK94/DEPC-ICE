package com.smartapp.depc_ice.Activities.ScannerQr;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.DepcApplication;
import com.smartapp.depc_ice.R;
import com.smartapp.depc_ice.Utils.Utils;

public class ScanQrActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.scan_qr_layout);
        Utils.SetStyleActionBarTitle(this);

        DepcApplication.getApplication().qrText = "";

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanQrActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        DepcApplication.getApplication().qrText =  result.getText();
                        finish();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void doRetry() {

    }
}
