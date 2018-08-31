package com.application.sergiomanes.ListasDeCompras.mvp.model;

import java.util.ArrayList;

public class Producto {
    private long code;
    private String name;
    private int count;
    private double price;
    private boolean isActive;

    public Producto(long code, String name, int count, double price) {
        this.code = code;
        this.name = name;
        this.count = count;
        this.price = price;
        this.isActive = true;
    }

    public Producto() {
        this.isActive = true;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static int posInArray(ArrayList<Producto> listaProd, long id)
    {
        int pos = 0;
        boolean found = false;
        while ((pos < listaProd.size()) && (!found))
        {
            if (listaProd.get(pos).getCode() == id)
            {
                found = true;
            }
            else
            {
                pos++;
            }
        }

        if (!found)
        {
            pos = -1;
        }
        return pos;
    }

}
