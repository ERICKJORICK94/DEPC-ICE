package com.smartapp.depc_ice.Interface;


import com.smartapp.depc_ice.Models.DataClientesGabinetModel;
import com.smartapp.depc_ice.Models.DataPedidosModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by GrupoLink on 09/04/2015.
 */

public interface IClientesGabinet {

    //@FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("DepWSR/application/libraries/wsapp.php")
    Call<dataGabinet> getClientesGabinet(@Body RequestBody json);

    public class dataGabinet{

        private int status;
        private String status_message;
        private DataClientesGabinetModel data;

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

        public DataClientesGabinetModel getData() {
            return data;
        }

        public void setData(DataClientesGabinetModel data) {
            this.data = data;
        }
    }
}

