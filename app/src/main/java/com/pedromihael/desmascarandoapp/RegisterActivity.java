package com.pedromihael.desmascarandoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pedromihael.desmascarandoapp.ui.login.LoginActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    private final DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText nameEditText = findViewById(R.id.register_name);
        final EditText usernameEditText = findViewById(R.id.register_username);
        final EditText passwordEditText = findViewById(R.id.register_password);
        final Button loginButton = findViewById(R.id.register_login_button);
        final Button registerButton = findViewById(R.id.register_register_button);
        final ProgressBar loadingProgressBar = findViewById(R.id.register_loading);

        loginButton.setOnClickListener(v -> {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            this.startActivity(loginActivity);
        });

        registerButton.setOnClickListener(v -> {
            String mName = nameEditText.getText().toString();
            String mUsername = usernameEditText.getText().toString();
            String mPassword = encrypt(passwordEditText.getText().toString());

            if (isFormValid(mName, mUsername, mPassword)) {
                User user = new User(mName, mUsername, mPassword);
                loadingProgressBar.setVisibility(View.VISIBLE);

                if (!dbHelper.isUserRegistered(mUsername)) {
                    if (dbHelper.addUser(user)) {
                        Toast.makeText(this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent homeIntent = new Intent(this, LoginActivity.class);
                        this.startActivity(homeIntent);
                        this.finish();
                    } else {
                        Toast.makeText(this, "Falha ao cadastrar! Tente novamente.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Usuário já cadastrado.", Toast.LENGTH_LONG).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }

            } else {
                Toast.makeText(this, "Preencha todos os campos direito", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isFormValid(String name, String username, String password) {
        if (name.length() == 0) { return false; }
        if (!username.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(username).matches()) { return false; }
        if (password.length() == 0 || password.trim().length() <= 5) { return false; }

        return true;
    }

    private String encrypt(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest != null ? digest.digest(password.getBytes(StandardCharsets.UTF_8)) : new byte[0];

        return Arrays.toString(hash);
    }
}