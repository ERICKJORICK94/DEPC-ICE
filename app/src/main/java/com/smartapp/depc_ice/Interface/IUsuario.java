package com.smartapp.depc_ice.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IUsuario {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("v1/api/Login")
    Call<dataUsuario> getLogin(@Field("usuario") String usuario,
                               @Field("clave") String clave);

    public class dataUsuario{

        private String CodigoError;
        private String MensajeError;

        public String getCodigoError() {
            return CodigoError;
        }

        public void setCodigoError(String codigoError) {
            CodigoError = codigoError;
        }

        public String getMensajeError() {
            return MensajeError;
        }

        public void setMensajeError(String mensajeError) {
            MensajeError = mensajeError;
        }
    }
}

