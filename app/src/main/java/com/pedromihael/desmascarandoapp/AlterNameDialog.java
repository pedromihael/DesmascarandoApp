package com.pedromihael.desmascarandoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AlterNameDialog extends AppCompatDialogFragment {

    private EditText mEditTextModel;
    private EditText mEditTextBrand;
    private Button mCancelButton;
    private Button mConfirmButton;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alter_name_dialog, null);

        builder.setView(view);

        mCancelButton = view.findViewById(R.id.cancel_button);
        mConfirmButton = view.findViewById(R.id.confirm_button);

        mCancelButton.setOnClickListener((v) -> this.dismiss());
        mConfirmButton.setOnClickListener((v) -> {

            String email = mEditTextBrand.getText().toString();
            String name = mEditTextModel.getText().toString();
            DatabaseHelper helper = new DatabaseHelper(getContext());

            if (hasNotNullAssurance(name, email)) {
                Toast.makeText(getContext(), "Nome alterado para: " + name, Toast.LENGTH_SHORT).show();
                listener.persistNewName(name, email, helper);
                this.dismiss();
            } else {
                Toast.makeText(view.getContext(),
            "Para alterar, preencha todos os campos",
                Toast.LENGTH_SHORT).show();
            }

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

    private boolean isNameEmpty(String name) {
        if (name.isEmpty() || name.equals(null)) {
            return true;
        }

        return false;
    }

    private boolean isPassEmpty(String pass) {
        if (pass.isEmpty() || pass.equals(null)) {
            return true;
        }

        return false;
    }

    private boolean hasNotNullAssurance(String name, String pass) {
        if (isPassEmpty(pass) && isNameEmpty(name)) {
            return false;
        }

        if (isPassEmpty(pass) && !isNameEmpty(name)) {
            return false;
        }

        if (!isPassEmpty(pass) && isNameEmpty(name)) {
            return true;
        }

        return true;
    }

    public interface DialogListener {
        void persistNewName(String name, String pass, DatabaseHelper helper);
    }
}

