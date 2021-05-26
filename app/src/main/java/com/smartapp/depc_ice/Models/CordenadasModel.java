package com.smartapp.depc_ice.Models;

import com.google.android.gms.maps.model.LatLng;

public class CordenadasModel {

    private LatLng cordenadas;
    private String nombre;

    public LatLng getCordenadas() {
        return cordenadas;
    }

    public void setCordenadas(LatLng cordenadas) {
        this.cordenadas = cordenadas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
