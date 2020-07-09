package com.pedromihael.desmascarandoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapDialogFragment extends DialogFragment
        implements OnMapReadyCallback {

    private Double lat;
    private Double longi;
    private String time;

    GoogleMap mMap;

    private TextView descrip;

    public MapDialogFragment(Post post) {
        this.lat = post.getLatitude();
        this.longi = post.getLongitude();
        this.time = post.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_dialog, container, false);

        return rootView;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        SupportMapFragment mapFragment = (SupportMapFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng location = new LatLng(this.lat,this.longi);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));



        googleMap.addMarker(new MarkerOptions().position(location).title("Pessoa sem másca - " + this.time));
        mMap.setMyLocationEnabled(true);


    }



}