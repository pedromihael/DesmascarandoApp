package com.pedromihael.desmascarandoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostsFragment extends Fragment implements RecyclerViewAdapter.OnPostListener{

    View mView;
    private RecyclerView mRecyclerView;
    private ArrayList<Post> mPostsList;

    public ArrayList<Post> getmPostsList() {
        return mPostsList;
    }

    public void setmPostsList(ArrayList<Post> mPostsList) {
        this.mPostsList = mPostsList;
    }

    public PostsFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        mPostsList = new ArrayList<>();
        mPostsList.add(new Post("author", 123.0, 321.0, "horario00", "postid"));
        mPostsList.add(new Post("author", 123.0, 321.0, "horario00", "postid"));
        mPostsList.add(new Post("author", 123.0, 321.0, "horario00", "postid"));
        mPostsList.add(new Post("author", 123.0, 321.0, "horario00", "postid"));
        mPostsList.add(new Post("author", 123.0, 321.0, "horario00", "postid"));

        //Toast.makeText(getContext(), helper.getPosts().get(0).getPost_id(), Toast.LENGTH_SHORT).show();

        mPostsList.addAll(helper.getPosts());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_posts, container, false);

        mRecyclerView = mView.findViewById(R.id.posts_recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mPostsList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        return mView;
    }

    @Override
    public void onPostClick(int position) {
        Post clickedPost = mPostsList.get(position);
    }

}
