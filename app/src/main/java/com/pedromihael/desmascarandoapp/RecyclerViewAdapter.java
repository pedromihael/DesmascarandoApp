package com.pedromihael.desmascarandoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Post> mPosts;
    private OnPostListener mOnPostListener;

    public RecyclerViewAdapter(ArrayList<Post> posts, OnPostListener onPostListener) {
        this.mPosts = posts;
        this.mOnPostListener = onPostListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // trocar para layout de card atualizado
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_model, parent, false);
        return new ViewHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.author.setText(mPosts.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView author;
        OnPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            author = itemView.findViewById(R.id.card_text_brand);
            this.onPostListener = onPostListener;
            itemView.setOnClickListener(this); // faz com que o observer aja em toda a tela
        }

        @Override
        public void onClick(View view) {
            onPostListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostListener {
        void onPostClick(int position);
    }

}
