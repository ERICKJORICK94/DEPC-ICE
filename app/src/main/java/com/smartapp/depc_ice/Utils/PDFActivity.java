package com.smartapp.depc_ice.Utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PDFActivity extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private GridViewWithHeaderAndFooter grid;
    private ListView lista;
    private LayoutInflater layoutInflater;
    private String numDocumento = null;
    private String codBodega = null;
    private PDFView pdfView;
    private File fileShare = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.pdf_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);

        pdfView = layout.findViewById(R.id.pdfView);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null) {
            codBodega = b.getString("direccion");
            fileShare = new File(codBodega);
            OpenPdfActivity(fileShare);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compartir, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_compartir) {
            descargar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void descargar(){

        if (fileShare != null) {
            Uri uri = Uri.fromFile(fileShare);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Compartir"));
        }

    }


    private void OpenPdfActivity(File file) {

        /*PDFView.with(this)
                .fromfilepath(absolutePath)
                .swipeHorizontal(false)
                .start();

        finish();*/
        pdfView.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .spacing(0)
                .load();
    }

    @Override
    public void doRetry() {

    }
}
