package com.smartapp.depc_ice.Interface;


import com.smartapp.depc_ice.Models.DataGabinetModel;
import com.smartapp.depc_ice.Models.DataPuntosVentasModel;
import com.smartapp.depc_ice.Utils.Const;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by GrupoLink on 09/04/2015.
 */

public interface IGabinet {

    //@FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST(Const.WS)
    Call<dataGabinet> getGabinet(@Body RequestBody json);

    public class dataGabinet{

        private int status;
        private String status_message;
        private DataGabinetModel data;

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

        public DataGabinetModel getData() {
            return data;
        }

        public void setData(DataGabinetModel data) {
            this.data = data;
        }
    }
}

