package com.pedromihael.desmascarandoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements NewCellphoneDialog.DialogListener {

    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private ViewPager mViewPager;

    //Photo Variables
    private final int CAPTURE_PHOTO = 100;
    Uri uri;
    ImageView img;
    //End Photos

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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "propic.png";
//                uri = Uri.parse(root);
            startActivityForResult(i, CAPTURE_PHOTO);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap capImage = (Bitmap) data.getExtras().get("data");
//            img.setImageBitmap(capImage);
        }
    }

    private void openNewCellphoneDialog() {
        NewCellphoneDialog dialog = new NewCellphoneDialog();
        dialog.show(getSupportFragmentManager(), "New Cellphone");
    }

    @Override
    public void persistNewCellphoneData(Cellphone cellphone, CellPhoneOpenHelper helper) {
        // Adding the values from the dialog listener to the database
        // usa o helper aqui pra fazer o add de uma postagem, chama o metodo .addPost()
        // ainda vou pensar nos parametros pra ele
    }
}