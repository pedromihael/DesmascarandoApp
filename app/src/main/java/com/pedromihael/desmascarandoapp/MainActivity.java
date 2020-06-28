package com.pedromihael.desmascarandoapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PostDialog.DialogListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private ViewPager mViewPager;

    //Photo Variables
    private final int CAPTURE_PHOTO = 100;
    private Uri uri;
    private String imgPath;
    //End Photos
    Intent thisIntent = new Intent(this, MainActivity.class);
    Bundle bundleFromLogin = thisIntent.getExtras();

    ArrayList<Double> location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = findViewById(R.id.tabs);
        mAppBarLayout = findViewById(R.id.app_bar);
        mViewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ModelsFragment(), "Desmascarados");
        viewPagerAdapter.addFragment(new BrandsFragment(), "Mapa");

        mViewPager.setAdapter(viewPagerAdapter); // sets up the adapter to the view pager
        mTabLayout.setupWithViewPager(mViewPager); // sets up the view pager (with adapter) to the corresponding tab

        /* Funcionalidade de postar foto */
        /* BOTAO FLUTUANTE */
//        img = findViewById(R.id.photo);

        /* get location */
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            this.location = getCurrentLocation();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, CAPTURE_PHOTO);
        });

    }

    @SuppressLint("MissingPermission")
    public ArrayList<Double> getCurrentLocation() {
        ArrayList<Double> location = new ArrayList<>();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            location.add(latitude);
                            location.add(longitude);
                        }
                    }
                }, Looper.getMainLooper());

        return location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            } else {
                getCurrentLocation();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_PHOTO) {
            uri = data.getData();
            openNewCellphoneDialog(uri);
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPhotoDetails(String filepath) throws IOException {
        ExifInterface exif = new ExifInterface(filepath);
        String teste = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//        String teste2 = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//        String teste3 = exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);

        Toast.makeText(this, teste, Toast.LENGTH_SHORT).show();
    }

    private void openNewCellphoneDialog(Uri uri) {

        User author = new User("0");
        // trocar userID acima pelo passado no bundle (está vindo nulo)
        PostDialog dialog = new PostDialog(uri, this, location, author);
        dialog.show(getSupportFragmentManager(), "New Post");

    }

}