package com.pedromihael.desmascarandoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Post> mPosts;
    private OnPostListener mOnPostListener;

    public RecyclerViewAdapter(ArrayList<Post> posts, OnPostListener onPostListener) {
        this.mPosts = posts;
        this.mOnPostListener = onPostListener;
    }

    public RecyclerViewAdapter(ArrayList<Post> posts) {
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view, mOnPostListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.author.setText(mPosts.get(position).getAuthor());
        holder.time.setText(mPosts.get(position).getTime());

//        File imgFile = new  File(mPosts.get(position).getFilePath());
//
//        if(imgFile.exists()){
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            holder.img.setImageBitmap(myBitmap);
//        }

    }

    public void updateAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void insertData(ArrayList<Post> newPostsList) {
        DiffUtilsCallback diffUtilsCallback = new DiffUtilsCallback(mPosts, newPostsList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilsCallback);
        mPosts.add(newPostsList.get(0));
        diffResult.dispatchUpdatesTo(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView author;
        TextView time;
        ImageView img;
        OnPostListener onPostListener;

        public ViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);
            author = itemView.findViewById(R.id.card_text_author);
            time = itemView.findViewById(R.id.card_text_time);
            img = itemView.findViewById(R.id.card_image);
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
