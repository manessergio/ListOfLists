package com.application.sergiomanes.ListasDeCompras;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.sergiomanes.ListasDeCompras.mvp.model.DatabaseHelper;
import com.application.sergiomanes.ListasDeCompras.mvp.model.Lista;
import com.application.sergiomanes.ListasDeCompras.mvp.model.Producto;

import java.util.ArrayList;
import java.util.Date;

public class ABMCompras extends AppCompatActivity {

    EditText nameProduct,countProduct,priceProduct;
    TextView idProduct,total;
    RecyclerView recyclerView;
    Button addProductbtn,updateProductbtn,deleteProductbtn;
    Lista list;
    DatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.application.sergiomanes.ListasDeCompras.R.layout.activity_abml);

        nameProduct = (EditText) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.productNameEditText);
        countProduct = (EditText) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.countEditText);
        priceProduct = (EditText) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.priceEditText);
        addProductbtn = (Button) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.addButton);
        updateProductbtn = (Button) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.updateButton);
        deleteProductbtn = (Button) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.deleteButton);
        idProduct = (TextView) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.idProductTextView);
        total = (TextView) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.totalTextView);
        recyclerView = (RecyclerView) findViewById(com.application.sergiomanes.ListasDeCompras.R.id.recyclerList);

        DB = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) //Caso mostrar Lista con o sin contenido
        {
            list = DB.getList(bundle.getLong("listID"));
            DB.getAllProductsFromListID(list);
        }
        else // Caso crear Lista nueva
        {
            list = new Lista(new ArrayList<Producto>(), new Date());
            DB.addList(list);

            if (list.getId()==-1){
                Toast.makeText(getApplicationContext(),getResources().
                                                       getString(com.application.sergiomanes.ListasDeCompras.R.string.error_list_insertion)
                                                      ,Toast.LENGTH_SHORT).show();
                return;
            }
        }

        total.setText(String.format("%.2f", list.getSubtotal()));

        final DetailAdapter adapter = new DetailAdapter(list.getListProducts(), com.application.sergiomanes.ListasDeCompras.R.layout.itemrecyclerview, this, new DetailAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int pos) {
                Producto producto = DB.getProduct(list.getListProducts().get(pos));
                idProduct.setText(String.valueOf(producto.getCode()));
                nameProduct.setText(String.valueOf(producto.getName()));
                countProduct.setText(String.valueOf(producto.getCount()));
                priceProduct.setText(String.valueOf(producto.getPrice()));
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);


        addProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProductProcess(adapter);
            }
        });

        updateProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductProcess(adapter);
            }
        });

        deleteProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!idProduct.getText().toString().equals("")) && (checkFields()))
                {
                    long id = Long.valueOf(idProduct.getText().toString());

                    int posInArray = Producto.posInArray(list.getListProducts(),id);
                    Producto producto = list.getListProducts().get(posInArray);

                    DB.deleteProduct(id);
                    list.getListProducts().remove(posInArray);
                    adapter.notifyItemRemoved(posInArray);

                    list.setSubtotal(Double.parseDouble(total.getText().toString().replace(",", "."))-(producto.getPrice()*producto.getCount()));
                    total.setText(String.format("%.2f", list.getSubtotal()));
                    DB.updateSubTotalList(list);
                    Toast.makeText(getApplicationContext(),getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.product_deleted),Toast.LENGTH_SHORT).show();

                    clearFields();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.error_empty_fields),Toast.LENGTH_SHORT).show();
                }
            }
        });

        priceProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT)) {

                        if (idProduct.getText().toString().equals(""))
                        {addProductProcess(adapter);}
                        else
                        {updateProductProcess(adapter);}

                    }

                return true;
            }

        });
    }

    public void addProductProcess (DetailAdapter adapter)
    {
        if (checkFields()) {
            Producto producto = new Producto();
            producto.setName(nameProduct.getText().toString());
            producto.setCount(Integer.valueOf(countProduct.getText().toString()));
            producto.setPrice(Double.valueOf(priceProduct.getText().toString()));

            long code = DB.addProduct(producto, list);

            if (code != -1) {
                producto.setCode(code);
                idProduct.setText(String.valueOf(code));
                list.getListProducts().add(producto);
                int posInArray = Producto.posInArray(list.getListProducts(), code);
                adapter.notifyItemInserted(posInArray);
                Toast.makeText(getApplicationContext(), getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.product_inserted), Toast.LENGTH_SHORT).show();
                list.setSubtotal(list.getSubtotal()+(producto.getPrice()*producto.getCount()));
                total.setText(String.format("%.2f", list.getSubtotal()));
                DB.updateSubTotalList(list);
                clearFields();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.error_product_insertion), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.error_empty_fields),Toast.LENGTH_SHORT).show();
        }
    }

    public void updateProductProcess(DetailAdapter adapter)
    {
        if ((!idProduct.getText().toString().equals("")) && (checkFields())) {

            int posInArray = Producto.posInArray(list.getListProducts(),Long.valueOf(idProduct.getText().toString()));

            Producto oldProduct = list.getListProducts().get(posInArray);

            list.setSubtotal(list.getSubtotal()-(oldProduct.getPrice()*oldProduct.getCount()));
            double subTotalBefore = list.getSubtotal();
            total.setText(String.format("%.2f", list.getSubtotal()));

            Producto newProducto = new Producto();
            newProducto.setName(nameProduct.getText().toString());
            newProducto.setCount(Integer.valueOf(countProduct.getText().toString()));
            newProducto.setPrice(Double.valueOf(priceProduct.getText().toString()));
            newProducto.setCode(Long.valueOf(idProduct.getText().toString()));


            DB.updateProduct(newProducto);
            list.getListProducts().set(posInArray, newProducto);
            adapter.notifyItemChanged(posInArray);

            Toast.makeText(getApplicationContext(), getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.product_updated), Toast.LENGTH_SHORT).show();

            list.setSubtotal(subTotalBefore+(newProducto.getPrice()*newProducto.getCount()));
            total.setText(String.format("%.2f", list.getSubtotal()));
            DB.updateSubTotalList(list);
            clearFields();
        }
        else
        {
            Toast.makeText(getApplicationContext(),getResources().getString(com.application.sergiomanes.ListasDeCompras.R.string.error_empty_fields),Toast.LENGTH_SHORT).show();
        }
    }

    public void clearFields(){
        idProduct.setText(String.valueOf(""));
        nameProduct.setText(String.valueOf(""));
        nameProduct.requestFocus();
        countProduct.setText(String.valueOf(""));
        priceProduct.setText(String.valueOf(""));
    }

    public boolean checkFields(){
        boolean check = true;
        if ((nameProduct.getText().toString().equals("")) || (countProduct.getText().toString().equals("")) || (priceProduct.getText().toString().equals("")))
        {
            check = false;
        }

        return  check;
    }




}
