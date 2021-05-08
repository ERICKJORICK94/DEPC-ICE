package com.smartapp.depc_ice.Interface;

import com.smartapp.depc_ice.Entities.Usuario;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IUsuario {

    @Headers("Content-Type: application/json")
    @POST("DepWSR/application/libraries/wsapp.php")
    Call<dataUsuario> getLogin(@Body RequestBody json);

    public class dataUsuario{

        private int status;
        private String status_message;
        private Usuario data;

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

        public Usuario getData() {
            return data;
        }

        public void setData(Usuario data) {
            this.data = data;
        }
    }
}

