package com.smartapp.depc_ice.Interface;


import com.smartapp.depc_ice.Models.DataBancoModel;
import com.smartapp.depc_ice.Models.DataFormaPagoModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by GrupoLink on 09/04/2015.
 */

public interface IBancos {

    //@FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("DepWSR/application/libraries/wsapp.php")
    Call<dataBodega> getBodegas(@Body RequestBody json);

    public class dataBodega{

        private int status;
        private String status_message;
        private DataBancoModel data;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatus_message() {
            return status_message;
        }

        public void setStatus_message(String status_message) {
            this.status_message = status_message;
        }

        public DataBancoModel getData() {
            return data;
        }

        public void setData(DataBancoModel data) {
            this.data = data;
        }
    }
}

