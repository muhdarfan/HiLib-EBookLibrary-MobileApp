package com.eaglez.hilib.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eaglez.hilib.MainActivity;
import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.Users;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etMatric, etFullName, etPhone, etUsername, etPass;
    private Button btnRegister;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.register_et_email);
        etMatric = findViewById(R.id.register_et_matric);
        etFullName = findViewById(R.id.register_et_full_name);
        etPhone = findViewById(R.id.register_et_phone);
        etUsername = findViewById(R.id.register_et_username);
        etPass = findViewById(R.id.register_et_password);

        btnRegister = findViewById(R.id.register_btn_register);
        btnLogin = findViewById(R.id.register_tv_login);

        btnLogin.setOnClickListener(v -> onBackPressed());
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, pass, email, matric, name, phone;

                username = etUsername.getText().toString().trim();
                pass = etPass.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                matric = etMatric.getText().toString().trim();
                name = etFullName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();

                if(ValidateForm(username, pass, email, matric, name, phone)) {
                    RegisterAccount(username, pass, email, matric, name, phone);
                }
            }
        });
    }

    private void RegisterAccount(String username, String pass, String email, String matric, String name, String phone) {
        Core.getAuth().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Core.getDatabase().getReference("users").child(Core.getUser().getUid()).setValue(new Users(username, email, matric, name, phone));

                startActivity(new Intent(RegisterActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean ValidateForm(String username, String pass, String email, String matric, String name, String phone) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("");
            return false;
        }

        if (TextUtils.isEmpty(pass)) {
            etPass.setError("");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("");
            return false;
        }

        if (TextUtils.isEmpty(matric)) {
            etMatric.setError("");
            return false;
        }

        if (TextUtils.isEmpty(name)) {
            etFullName.setError("");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("");
            return false;
        }

        return true;
    }
}