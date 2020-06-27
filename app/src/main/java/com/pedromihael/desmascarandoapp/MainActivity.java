package com.pedromihael.desmascarandoapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NewCellphoneDialog.DialogListener {

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

//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));

            startActivityForResult(intent, CAPTURE_PHOTO);
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_PHOTO) {

            uri = data.getData();
            String[] filepath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, filepath, null,
                    null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filepath[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            //Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();

            try {
                getPhotoDetails(filePath);
            } catch (IOException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
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