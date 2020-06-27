package com.pedromihael.desmascarandoapp;

/**
 * NAO APAGAR!! VAMOS USAR PARA MOSTRAR INFORMACOES QUANDO CLICAR NUM PIN DO MAPA
 */

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

import androidx.appcompat.app.AppCompatDialogFragment;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostDialog extends AppCompatDialogFragment {

    private EditText mEditTextModel;
    private TextView mEditTextBrand;
    private Button mCancelButton;
    private Button mConfirmButton;
    private DialogListener listener;
    private Uri uri;
    private ImageView img;

    public PostDialog(Uri uri){
        this.uri = uri;
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
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String dateformatted = dateFormat.format(date);
        mEditTextBrand = view.findViewById(R.id.edit_brand);
        mEditTextBrand.setText(dateformatted);

        //Toast.makeText(getActivity(), mEditTextBrand.getText(), Toast.LENGTH_LONG).show();
        img = view.findViewById(R.id.imagem_da_galeria);
        img.setImageURI(uri);
        /**/

        mCancelButton.setOnClickListener((v) -> this.dismiss());
        mConfirmButton.setOnClickListener((v) -> {
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

        });

        mEditTextBrand = view.findViewById(R.id.edit_brand);
        mEditTextModel = view.findViewById(R.id.edit_model);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }

    }

    public interface DialogListener { }
}

