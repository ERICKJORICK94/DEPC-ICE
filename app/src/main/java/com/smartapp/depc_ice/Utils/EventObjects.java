package com.smartapp.depc_ice.Utils;

import java.util.Date;

public class EventObjects {
    private int id;
    private String message;
    private Date date;
    private String fecha;
    private int contador;

    public EventObjects() {
    }

    public EventObjects(String message, Date date, String fecha, int contador) {
        this.message = message;
        this.date = date;
        this.fecha = fecha;
        this.contador = contador;
    }

    public EventObjects(int id, String message, Date date, String fecha, int contador) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.fecha = fecha;
        this.contador = contador;
    }

    public EventObjects(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }
    public String getMessage() {
        return message;
    }
    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    @Override
    public boolean equals(Object object) {

        if (object != null && object instanceof EventObjects) {
            EventObjects thing = (EventObjects) object;
           return  this.fecha.equals(thing.fecha);
        }

        return false;
    }


}