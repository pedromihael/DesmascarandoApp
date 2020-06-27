package com.pedromihael.desmascarandoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements NewCellphoneDialog.DialogListener {

    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private ViewPager mViewPager;
    private final int CAPTURE_PHOTO = 102;
    Uri uri;

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


        /* BOTAO FLUTUANTE */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
//                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "propic.png";
//                uri = Uri.parse(root);
            startActivityForResult(i, CAPTURE_PHOTO);
        });
    }

    private void openNewCellphoneDialog() {
        NewCellphoneDialog dialog = new NewCellphoneDialog();
        dialog.show(getSupportFragmentManager(), "New Cellphone");
    }

    @Override
    public void persistNewCellphoneData(Cellphone cellphone, CellPhoneOpenHelper helper) {
        // Adding the values from the dialog listener to the database
        helper.addModel(cellphone);
    }
}