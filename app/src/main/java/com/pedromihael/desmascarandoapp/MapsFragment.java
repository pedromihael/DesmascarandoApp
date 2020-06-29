package com.pedromihael.desmascarandoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    ArrayList<Post> postsList = new ArrayList<>();
    DatabaseHelper dbHelper;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Campo Grande-MS, Brasil.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng cg = new LatLng(-20.4697, -54.6201);
            googleMap.addMarker(new MarkerOptions().position(cg).title("Campo Grande")); // adicionar vários desse
            getPosts();

            if (postsList.size() > 0) {
                for (Post post : postsList) {
                    LatLng location = new LatLng(post.getLatitude(), post.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(location).title("Pessoa sem másca - " + post.getTime()));
                }
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cg));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        this.dbHelper = new DatabaseHelper(getActivity());
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void getPosts() {
        this.postsList.addAll(dbHelper.getPosts());
    }
}