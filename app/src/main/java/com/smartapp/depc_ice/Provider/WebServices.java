package com.smartapp.depc_ice.Provider;
import android.util.Log;
import com.google.gson.Gson;
import com.smartapp.depc_ice.Utils.Const;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WebServices {


    public WebServices() {
        //this.activity = activity;
    }



    public String makeServiceCall(String url, int method, String params)
    {
        String response = "";

        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            //if (method == 1)
            {
                HttpPost httpPost;
                    httpPost = new HttpPost(url);

                // HttpPost httpPost = new HttpPost();

                httpPost.setHeader("Content-Type", Const.APPLICATION_JSON);
                if (params != null)
                {
                    StringEntity se = new StringEntity(params, "UTF-8");


                    // se.setContentEncoding(new BasicHeader(HTTP.CHARSET_PARAM,"UTF-8"));
                    httpPost.setEntity(se);
                }

                httpResponse = httpClient.execute(httpPost);


            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }


        return response;

    }

}

