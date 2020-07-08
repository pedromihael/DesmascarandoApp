package com.pedromihael.desmascarandoapp;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class DiffUtilsCallback extends DiffUtil.Callback {

    private ArrayList<Post> oldPostList;
    private ArrayList<Post> newPostList;

    public DiffUtilsCallback(ArrayList<Post> oldPostList, ArrayList<Post> newPostList) {
        this.oldPostList = oldPostList;
        this.newPostList = newPostList;
    }

    @Override
    public int getOldListSize() {
        return oldPostList.size();
    }

    @Override
    public int getNewListSize() {
        return newPostList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
