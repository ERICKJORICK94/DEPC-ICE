package com.smartapp.depc_ice.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.smartapp.depc_ice.Activities.General.BaseActitity;
import com.smartapp.depc_ice.R;

/**
 * Created by RICHARD on 02/04/2018.
 */

public class PhotoViewer extends BaseActitity implements BaseActitity.BaseActivityCallbacks{

    private View layout;
    private LayoutInflater layoutInflater;
    private String url;
    private PhotoView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = addLayout(R.layout.phot_viewer_layout);
        Utils.SetStyleActionBarTitle(this);
        layoutInflater = LayoutInflater.from(this);
        image = (PhotoView) layout.findViewById(R.id.image);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            if (b.get("url") != null) {
                url = (String) b.get("url");
                url = url.replace("\\","/");
                url = url.replace(Const.BASE_URL, Const.BASE_URL);
                Utils.setImage(url,image);
            }else if (b.get("imageString64") != null) {
                url = (String) b.get("imageString64");
                image.setImageBitmap(Utils.convert(url));

            }

        }

    }

    @Override
    public void doRetry() {

    }
}
