package com.pedromihael.desmascarandoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<Cellphone> mModelsList;
    List<Cellphone> mBrandsList;
    String mFragmentTitle;

    public RecyclerViewAdapter(Context mContext, List<Cellphone> mData, String mFragmentTitle) {
        this.mFragmentTitle = mFragmentTitle;
        this.mContext = mContext;

        if (mFragmentTitle.equals("Modelos")) {
            this.mModelsList = mData;
        } else {
            this.mBrandsList = mData;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(mFragmentTitle.equals("Modelos")) {
            view = LayoutInflater.from(mContext).inflate(R.layout.card_item_model, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.card_item_brand, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       if (mFragmentTitle.equals("Modelos")) {
           holder.tv_model.setText(mModelsList.get(position).getName());
           holder.tv_brand.setText("/ " + mModelsList.get(position).getBrand());
       } else {
           holder.tv_brand.setText(mBrandsList.get(position).getName());
       }

    }

    @Override
    public int getItemCount() {
        if (mFragmentTitle.equals("Modelos")) {
            return mModelsList.size();
        } else {
            return mBrandsList.size();
        }
    }

    public void updateView() {
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        private TextView tv_model;
        private TextView tv_brand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (mFragmentTitle.equals("Modelos")) {
                tv_brand = itemView.findViewById(R.id.card_text_brand);
                tv_model = itemView.findViewById(R.id.card_text_model);
            } else {
                tv_brand = itemView.findViewById(R.id.brand_card_text_brand);
            }

        }
    }

}
