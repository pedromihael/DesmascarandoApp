package com.pedromihael.desmascarandoapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.pedromihael.desmascarandoapp.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PostDialog.DialogListener, AlterNameDialog.DialogListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private ViewPager mViewPager;
    private DatabaseHelper dbHelper;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private final int CAPTURE_PHOTO = 100;
    private Uri uri;
    private String imgPath;

    Bundle bundleFromLogin;

    private ArrayList<Double> location;
    private ArrayList<Post> postsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent thisIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.bundleFromLogin = thisIntent.getExtras();

        mTabLayout = findViewById(R.id.tabs);
        mAppBarLayout = findViewById(R.id.app_bar);
        mViewPager = findViewById(R.id.view_pager);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** verifica se já está logado usando shared preferences **/

        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);

        String username = sp.getString("username", null);
        String password = sp.getString("password", null);
        User user = new User(username, password);
        dbHelper = new DatabaseHelper(this);

        if (!dbHelper.getUser(user)) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        /** continua caso ja esteja logado **/

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new PostsFragment(), "Desmascarados");
        viewPagerAdapter.addFragment(new MapsFragment(), "Mapa");

        mViewPager.setAdapter(viewPagerAdapter); // sets up the adapter to the view pager
        mTabLayout.setupWithViewPager(mViewPager); // sets up the view pager (with adapter) to the corresponding tab

        dbHelper = new DatabaseHelper(this);
        this.postsList = dbHelper.getPosts();

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
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted",
                            Toast.LENGTH_SHORT).show();
                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=
                                PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("Tudo ok!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
            case REQUEST_CODE_LOCATION_PERMISSION :
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
                    } else {
                        getCurrentLocation();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_PHOTO) {

            RecyclerView recyclerView = findViewById(R.id.posts_recyclerview);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            linearLayout.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayout);

            final RecyclerViewAdapter adapter = new RecyclerViewAdapter(postsList);
            recyclerView.setAdapter(adapter);

            assert data != null;
            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            openNewCellphoneDialog(bitmap, adapter, postsList);

        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        }
    }

    private void openNewCellphoneDialog(Bitmap bitmap, RecyclerViewAdapter adapter, ArrayList<Post> posts) {
        User author = new User("0");
        // trocar userID acima pelo passado no bundle (está vindo nulo)
        PostDialog dialog = new PostDialog(bitmap, this, location, author, adapter, postsList);
        dialog.show(getSupportFragmentManager(), "New Post");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings_logout) {

            SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed = sp.edit();
            Ed.putString("username",null);
            Ed.putString("password",null);
            Ed.apply();

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        } else if (id == R.id.action_settings_alter_name) {
            AlterNameDialog dialog = new AlterNameDialog();
            dialog.show(getSupportFragmentManager(), "Alter name");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void persistNewName(String name, String pass, DatabaseHelper helper) {
//        helper.alterName(name, pass);
    }
}