package com.application.sergiomanes.ListasDeCompras.mvp.model;

import android.icu.util.Calendar;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;


public class Lista {

    private ArrayList<Producto> list;
    private long id;
    private Date createdDate;
    private Double subtotal;

    public Lista(ArrayList<Producto> list, long id, Date date) {
        this.list = list;
        this.id = id;
        this.createdDate = date;
        this.subtotal = 0.0;
    }

    public Lista(ArrayList<Producto> list, Date date) {
        this.list = list;
        this.createdDate = date;
        this.subtotal = 0.0;
    }

    public Lista() {
        this.subtotal = 0.0;
    }

    public ArrayList<Producto> getList() {
        return list;
    }

    public void setList(ArrayList<Producto> list) {
        this.list = list;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
