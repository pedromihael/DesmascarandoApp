package com.pedromihael.desmascarandoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pedromihael.desmascarandoapp.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

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
            String mPassword = passwordEditText.getText().toString();

            if (isFormValid(mName, mUsername, mPassword)) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                this.finish();
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
            }

            /* if (mName.isEmpty()) {
                Toast.makeText(this, "Insira seu nome!", Toast.LENGTH_LONG);
            }
            if (usernameEditText.getText().toString() == null) {
                if (!Patterns.EMAIL_ADDRESS.matcher(usernameEditText.getText().toString()).matches()) {
                    Toast.makeText(this, "Insira um email válido!", Toast.LENGTH_LONG);
                }
                else Toast.makeText(this, "Insira seu email!", Toast.LENGTH_LONG);
            }
            if (passwordEditText.getText().toString() == null) {
                if (passwordEditText.getText().toString().length() < 5) {
                    Toast.makeText(this, "Sua senha deve ter ao menos 5 dígitos", Toast.LENGTH_LONG);
                }
                else Toast.makeText(this, "Insira uma senha!", Toast.LENGTH_LONG);
            } */

            // adicionar ao banco.

        });

    }

    private boolean isFormValid(String name, String username, String password) {
        // fazer validação campo a campo depois
        return (name.length() != 0 && username.length() != 0 && password.length() != 0);
    }
}