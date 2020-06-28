package com.pedromihael.desmascarandoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements PostDialog.DialogListener {

    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private ViewPager mViewPager;

    //Photo Variables
    private final int CAPTURE_PHOTO = 100;
    private Uri uri;
    private String imgPath;
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
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, CAPTURE_PHOTO);
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_PHOTO) {
            uri = data.getData();
            openNewCellphoneDialog(uri);

            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, projection, null, null,null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            String path = cursor.getString(column_index);

            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
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
        PostDialog dialog = new PostDialog(uri);
        dialog.show(getSupportFragmentManager(), "New Cellphone");
    }

    //@Override
    public void persistNewCellphoneData(Cellphone cellphone, CellPhoneOpenHelper helper) {
        // Adding the values from the dialog listener to the database
        // usa o helper aqui pra fazer o add de uma postagem, chama o metodo .addPost()
        // ainda vou pensar nos parametros pra ele
    }
}