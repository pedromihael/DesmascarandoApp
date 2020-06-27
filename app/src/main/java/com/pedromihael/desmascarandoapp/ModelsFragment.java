package com.pedromihael.desmascarandoapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelsFragment extends Fragment implements RecyclerViewAdapter.OnPostListener{

    View mView;
    private RecyclerView mRecyclerView;
    private ArrayList<Post> mPostsList;

    public ArrayList<Post> getmPostsList() {
        return mPostsList;
    }

    public void setmPostsList(ArrayList<Post> mPostsList) {
        this.mPostsList = mPostsList;
    }

    public ModelsFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper helper = new DatabaseHelper(getActivity());

        mPostsList = new ArrayList<>();

       for (Post item : helper.getPosts()) {
           mPostsList.add(item);
       }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_models, container, false);

        mRecyclerView = mView.findViewById(R.id.models_recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mPostsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        return mView;
    }

    @Override
    public void onPostClick(int position) {
        Post clickedPost = mPostsList.get(position); //esse cara identifica qual card foi clicado
        PostDialog dialog = new PostDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "Post from " + clickedPost.getAuthor());
    }

}
