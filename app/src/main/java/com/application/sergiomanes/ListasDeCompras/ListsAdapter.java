package com.application.sergiomanes.ListasDeCompras;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.sergiomanes.ListasDeCompras.mvp.model.Lista;

import java.util.ArrayList;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ItemHolder> {
    private ArrayList<Lista> list;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    int resource;
    Activity activity;

    public ListsAdapter (ArrayList<Lista> list, int resource, Activity activity, OnItemClickListener item, OnItemLongClickListener itemlong) {
        this.list = list;
        this.resource = resource;
        this.activity = activity;
        this.onItemClickListener = item;
        this.onItemLongClickListener = itemlong;
    }

    @Override
    public ListsAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        return new ListsAdapter.ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListsAdapter.ItemHolder holder, final int position) {
        Lista lista = list.get(position);
        holder.paid.setText(String.valueOf(lista.getSubtotal()));
        holder.date.setText(String.valueOf(lista.getCreatedDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.OnItemLongClick(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        private TextView paid, date;

        public ItemHolder(View itemView) {
            super(itemView);
            paid = (TextView) itemView.findViewById(com.application.sergiomanes.ListasDeCompras.R.id.numeroTextView);
            date = (TextView) itemView.findViewById(com.application.sergiomanes.ListasDeCompras.R.id.fechaTextView);
        }
    }


    public interface OnItemClickListener{
        void OnItemClick(int pos);
    }

    public interface OnItemLongClickListener{
        void OnItemLongClick(int pos);
    }
}
