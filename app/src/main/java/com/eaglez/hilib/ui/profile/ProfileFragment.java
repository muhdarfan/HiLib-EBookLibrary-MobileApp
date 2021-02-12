package com.eaglez.hilib.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.ui.account.LoginActivity;

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogout = view.findViewById(R.id.profile_btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Log out")
                        .setCancelable(true)
                        .setMessage("Are you sure want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }
}