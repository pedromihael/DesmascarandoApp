package com.pedromihael.desmascarandoapp;

/**
 * NAO APAGAR!! VAMOS USAR PARA MOSTRAR INFORMACOES QUANDO CLICAR NUM PIN DO MAPA
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    User author;

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
        View view = inflater.inflate(R.layout.new_cellphone_dialog, null); // trocar para dialog padrao

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
        img.setImageURI(uri);
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
        });

        mEditTextBrand = view.findViewById(R.id.edit_brand);
        mEditTextModel = view.findViewById(R.id.edit_model);

        return builder.create();
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

