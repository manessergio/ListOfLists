package com.application.sergiomanes.ListasDeCompras;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.application.sergiomanes.ListasDeCompras.mvp.model.DatabaseHelper;
import com.application.sergiomanes.ListasDeCompras.mvp.model.Lista;
import com.application.sergiomanes.ListasDeCompras.mvp.model.Producto;

import java.util.ArrayList;
import java.util.Date;

import static com.application.sergiomanes.ListasDeCompras.mvp.model.Lista.posInArray;

public class ActivityLists extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addListButton;
    DatabaseHelper DB;
    ArrayList<Lista> arrayListLists = new ArrayList<>();
    int eventPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.application.sergiomanes.ListasDeCompras.R.layout.activity_lists);

        recyclerView = findViewById(com.application.sergiomanes.ListasDeCompras.R.id.recyclerAllLists);
        addListButton = findViewById(com.application.sergiomanes.ListasDeCompras.R.id.addList) ;
        DB = new DatabaseHelper(this);

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLists.this,ABMCompras.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayListLists = DB.getAllLists();
        final ListsAdapter adapter = new ListsAdapter(arrayListLists, com.application.sergiomanes.ListasDeCompras.R.layout.listsrecyclerview, this, new ListsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int pos) {

                Intent intent = new Intent(ActivityLists.this, ABMCompras.class);
                Bundle bundle = new Bundle();
                bundle.putLong("listID", arrayListLists.get(pos).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new ListsAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(int pos) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLists.this);
                builder.setTitle(com.application.sergiomanes.ListasDeCompras.R.string.borrar_listado);
                builder.setMessage(com.application.sergiomanes.ListasDeCompras.R.string.re_preguntar);
                eventPosition = pos;

                builder.setPositiveButton(com.application.sergiomanes.ListasDeCompras.R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Lista list = arrayListLists.get(eventPosition);
                        DB.deleteList(list);
                        arrayListLists.remove(eventPosition);
                        recyclerView.getAdapter().notifyItemRemoved(eventPosition);
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton(com.application.sergiomanes.ListasDeCompras.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton(R.string.duplicarLista, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Lista list = arrayListLists.get(eventPosition);
                        Lista newList = new Lista(new ArrayList<Producto>(),new Date());
                        Producto product;
                        DB.addList(newList);
                        arrayListLists.add(newList);
                        DB.getAllProductsFromListID(list);
                        for (int i=0;i<list.getListProducts().size();i++)
                        {
                            product = list.getListProducts().get(i);
                            product.setPrice(0);
                            DB.addProduct(product, newList);
                        }

                        eventPosition = posInArray(arrayListLists,newList.getId());
                        Intent intent = new Intent(ActivityLists.this, ABMCompras.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("listID", arrayListLists.get(eventPosition).getId());
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();

            }
        });

        recyclerView.setAdapter(adapter);
    }


}

