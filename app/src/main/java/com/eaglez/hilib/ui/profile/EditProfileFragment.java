package com.eaglez.hilib.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Core;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class EditProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private EditText etEmail, etName, etUsername, etPass, etPhone;
    private Button btnSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.profile_edit_et_email);
        etName = view.findViewById(R.id.profile_edit_et_full_name);
        etUsername = view.findViewById(R.id.profile_edit_et_username);
        etPass = view.findViewById(R.id.profile_edit_et_password);
        etPhone = view.findViewById(R.id.profile_edit_et_phone);
        btnSave = view.findViewById(R.id.profile_edit_btn_save);

        profileViewModel.getmUser().observe(getViewLifecycleOwner(), users -> {
            etEmail.setText(users.getEmail());
            etName.setText(users.getFullname());
            etUsername.setText(users.getUsername());
            etPhone.setText(users.getPhone());
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (Validate(name, email, username, pass, phone)) {
                if (!Core.getUserData().getEmail().equals(email)) {
                    Core.getUser().updateEmail(email).addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            Core.getUserData().setEmail(email);
                    });
                }

                if (!TextUtils.isEmpty(pass)) {
                    //Core.getUser().updatePassword(pass);
                }

                Core.getUserData().setFullname(name);
                Core.getUserData().setUsername(username);
                Core.getUserData().setPhone(phone);

                Core.getDatabase().getReference("users").child(Core.getUser().getUid()).setValue(Core.getUserData()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            profileViewModel.update();
                            Snackbar.make(view, "Your profile has been successfully updated.", Snackbar.LENGTH_LONG).show();
                        } else
                            Snackbar.make(view, "An error has been occurred while saving user data.", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean Validate(String name, String email, String username, String pass, String phone) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("");
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("");
            return false;
        }

        if (!TextUtils.isEmpty(pass) && pass.length() < 6) {
            etPass.setError("");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("");
            return false;
        }

        return true;
    }


}