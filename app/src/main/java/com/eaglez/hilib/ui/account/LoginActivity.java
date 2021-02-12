package com.eaglez.hilib.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eaglez.hilib.*;
import com.eaglez.hilib.components.Core;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private EditText etUser, etPass;
    private Button btnLogin;
    private TextView btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.login_et_email);
        etPass = findViewById(R.id.login_et_password);
        btnLogin = findViewById(R.id.login_btn_login);
        btnRegister = findViewById(R.id.login_tv_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etUser.getText().toString().trim(), password = etPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etUser.setError("");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etPass.setError("");
                    return;
                }

                Core.getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    }
                });
            }
        });

        btnRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}