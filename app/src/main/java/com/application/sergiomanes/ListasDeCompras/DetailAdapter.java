package com.application.sergiomanes.ListasDeCompras;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.sergiomanes.ListasDeCompras.mvp.model.Producto;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.Holder> {

    private ArrayList<Producto> list;
    private OnItemClickListener onItemClickListener;
    int resource;
    Activity activity;

    public DetailAdapter(ArrayList<Producto> list, int resource, Activity activity, OnItemClickListener item) {
        this.list = list;
        this.resource = resource;
        this.activity = activity;
        this.onItemClickListener = item;
    }

    @Override
    public Holder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        Producto producto = list.get(position);
        holder.id.setText(String.valueOf(producto.getCode()));
        holder.name.setText(producto.getName());
        holder.count.setText(String.valueOf(producto.getCount()));
        holder.price.setText(String.valueOf(producto.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView id, name, count, price;

        public Holder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(com.application.sergiomanes.ListasDeCompras.R.id.productIDTextView);
            name = (TextView) itemView.findViewById(com.application.sergiomanes.ListasDeCompras.R.id.productNameEditText);
            count = (TextView) itemView.findViewById(com.application.sergiomanes.ListasDeCompras.R.id.productCountTextView);
            price = (TextView) itemView.findViewById(com.application.sergiomanes.ListasDeCompras.R.id.productPriceTextView);
        }
    }


    public interface OnItemClickListener{
        void OnItemClick(int pos);
    }
}
