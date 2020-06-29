package com.pedromihael.desmascarandoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PostDialog extends AppCompatDialogFragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Activity mainActivity;
    private EditText mEditTextModel;
    private TextView mEditTextBrand;
    private Button mCancelButton;
    private Button mConfirmButton;
    private DialogListener listener;
    private Uri uri;
    private ImageView img;
    private DatabaseHelper dbHelper;
    private Context context;
    private ArrayList<Double> location = new ArrayList<>();
    private User author;
    private Bitmap bitmap;

    public PostDialog(Bitmap bitmap, Context context, ArrayList<Double> location, User user) {
        this.bitmap = bitmap; //endereco da foto
        this.context = context;
        this.mainActivity = (Activity) context;
        this.location = location;
        this.author = user;
    }

    public PostDialog(Uri uri, Context context, ArrayList<Double> location, User user) {
        this.uri = uri; //endereco da foto
        this.context = context;
        this.mainActivity = (Activity) context;
        this.location = location;
        this.author = user;
    }

    public PostDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_post_dialog, null); // trocar para dialog padrao

        builder.setView(view);

        mCancelButton = view.findViewById(R.id.cancel_button);
        mConfirmButton = view.findViewById(R.id.confirm_button);

        /* Ele seta o horario do post e a imagem selecionada da galeria */
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM, yyyy - HH:mm");
        Date date = new Date();
        String dateFormatted = dateFormat.format(date);
        mEditTextBrand = view.findViewById(R.id.edit_brand);
        mEditTextBrand.setText(dateFormatted);

        Toast.makeText(getActivity(), mEditTextBrand.getText(), Toast.LENGTH_LONG).show();
        img = view.findViewById(R.id.imagem_da_galeria);
        img.setImageBitmap(resize(bitmap, 960,1280));
        String path = saveImage(bitmap);
//        Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
//        try {
//            getPhotoDetails(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

        /**/

        mCancelButton.setOnClickListener((v) -> this.dismiss());

        mConfirmButton.setOnClickListener((v) -> {
            //adicionar ao banco
            /*
            * time:
            * DateFormat dateFormat = new SimpleDateFormat("yyyyMMdHHmmss");
              Date date = new Date();
              String timestamp = dateFormat.format(date);
            * latitude: location.get(0)
            * longitude: location.get(1)
            * post_id: this.author.getUser_id() + @ + timestamp
            * */
            Cellphone cellphone = null;
            String brand = mEditTextBrand.getText().toString();
            String model = mEditTextModel.getText().toString();
            CellPhoneOpenHelper helper = new CellPhoneOpenHelper(getContext());

            /* if (hasNotNullAssurance(model, brand)) {
                if (isBrand(model, brand)) {
                    cellphone = new Cellphone(brand);
                } else if (isDevice(model)) {
                    cellphone = new Cellphone(model, brand);
                }

                listener.persistNewCellphoneData(cellphone, helper);
                this.dismiss();

            } else {
                Toast.makeText(view.getContext(),
            "Para cadastrar um dispositivo, insira todos os campos. Para cadastrar uma marca, insira apenas a marca",
                Toast.LENGTH_SHORT).show();
            } */
            getDialog().dismiss();
        });

        mEditTextBrand = view.findViewById(R.id.edit_brand);

        return builder.create();
    }

    private Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap output = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) newWidth / bitmap.getWidth(), (float) newHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());
        return output;
    }

    private void getPhotoDetails(String filepath) throws IOException {
        ExifInterface exif = new ExifInterface(filepath);
        String teste = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
//        String teste2 = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//        String teste3 = exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);

        Toast.makeText(getActivity(), teste, Toast.LENGTH_LONG).show();
    }

    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File directory = new File(
                Environment.getExternalStorageDirectory() + "/Desmascarados_images");
        // Criando o diretório caso ele não exista!
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            File fileImage = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            fileImage.createNewFile();
            FileOutputStream fo = new FileOutputStream(fileImage);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{fileImage.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved:->" + fileImage.getAbsolutePath());
            return fileImage.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener {
    }
}

