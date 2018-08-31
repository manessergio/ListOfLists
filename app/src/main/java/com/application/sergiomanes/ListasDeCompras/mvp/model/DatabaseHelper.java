package com.application.sergiomanes.ListasDeCompras.mvp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mysqlitedatabase";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlLists = "CREATE TABLE Lists " +
                "(listID integer primary key autoincrement," +
                "createdDate integer," +
                "subTotal real)";
        String sqlProducts = "CREATE TABLE Products" +
                "(productID integer primary key autoincrement," +
                "productName varchar," +
                "productCount varchar," +
                "productPrice double," +
                "isActive bit," +
                "listID integer," +
                "FOREIGN KEY(listID) REFERENCES Lists(listID))";

        db.execSQL(sqlLists);
        db.execSQL(sqlProducts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sqlLists = "DROP TABLE IF EXISTS Lists";
            String sqlProducts = "DROP TABLE IF EXISTS Products";

            db.execSQL(sqlProducts);
            db.execSQL(sqlLists);
    }

    public long addProduct(Producto producto, Lista lista)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("productName",producto.getName());
        contentValues.put("productCount",producto.getCount());
        contentValues.put("productPrice",producto.getPrice());
        contentValues.put("isActive",producto.isActive());
        contentValues.put("listID",lista.getId());

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("Products",null,contentValues);
        db.close();

        return result;
    }

    public void deleteProduct(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Products","productID = " + String.valueOf(id),null);
        db.close();
    }

    public  void updateProduct(Producto producto)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("productName",producto.getName());
        contentValues.put("productCount",producto.getCount());
        contentValues.put("productPrice",producto.getPrice());

        SQLiteDatabase db = getWritableDatabase();
        db.update("Products",contentValues,"productID = " + String.valueOf(producto.getCode()),null);
        db.close();
    }

    public Producto getProduct(Producto id)
    {
        Producto producto = new Producto();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT productID, productName, productCount, productPrice " +
                                        "FROM Products " +
                                        "WHERE isActive = 1 AND productID = " + String.valueOf(id.getCode()),null);

        if (cursor.moveToFirst())
        {
                producto.setCode(cursor.getLong(0));
                producto.setName(cursor.getString(1));
                producto.setCount(cursor.getInt(2));
                producto.setPrice(cursor.getDouble(3));
        }

        db.close();
        return producto;
    }

    public ArrayList<Producto> getAllProductsFromListID(Lista list)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Producto> arrayListProducts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT productID, productName, productCount, productPrice " +
                "FROM Products " +
                "WHERE isActive = 1 AND listID ="+ list.getId(),null);

        if (cursor.moveToFirst())
        {
            do{
                Producto producto = new Producto();
                producto.setCode(cursor.getLong(0));
                producto.setName(cursor.getString(1));
                producto.setCount(cursor.getInt(2));
                producto.setPrice(cursor.getDouble(3));
                arrayListProducts.add(producto);
            }while (cursor.moveToNext());

        }

        db.close();
        return arrayListProducts ;
    }

    public ArrayList<Lista> getAllLists()
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Lista> arrayListLists = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT listID, createdDate , subTotal " +
                "FROM Lists ORDER BY listID DESC",null);

        if (cursor.moveToFirst())
        {
            do{
                Lista lista = new Lista();
                lista.setId(cursor.getInt(0));
                lista.setCreatedDate(new Date(cursor.getLong(1)));
                lista.setSubtotal(cursor.getDouble(2));
                arrayListLists.add(lista);
            }while (cursor.moveToNext());

        }

        db.close();
        return arrayListLists ;
    }

    public long addList(Lista lista)
    {
        ContentValues contentValues = new ContentValues();
        //Para dale el formato de fecha que quiero
        /*SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = (Date)formatter.parse("12-December-2012"); */
        contentValues.put("createdDate",lista.getCreatedDate().getTime());

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert("Lists",null,contentValues);
        db.close();
        return result;
    }

    public Lista getList(long id)
    {
        Lista list = new Lista();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT listID, createdDate, subTotal " +
            "FROM Lists WHERE listID= "+id,null);

        if (cursor.moveToFirst())
        {
            list.setId(cursor.getLong(0));
            list.setCreatedDate(new Date(cursor.getLong(1)));
            list.setSubtotal(cursor.getDouble(2));
        }

        db.close();
        return list;
    }


    public void deleteList(Lista list)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Products","listID="+list.getId(),null);
        db.delete("Lists","listID="+list.getId(),null);
        db.close();
    }

    public  void updateSubTotalList(Lista list)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("listID",list.getId());
        contentValues.put("createdDate",list.getCreatedDate().getTime());
        contentValues.put("subTotal",list.getSubtotal());

        SQLiteDatabase db = getWritableDatabase();
        db.update("Lists",contentValues,"listID = " + String.valueOf(list.getId()),null);
        db.close();
    }



}
