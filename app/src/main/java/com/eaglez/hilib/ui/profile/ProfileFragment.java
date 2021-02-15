package com.eaglez.hilib.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eaglez.hilib.MainActivity;
import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.Users;
import com.eaglez.hilib.ui.AboutActivity;
import com.eaglez.hilib.ui.account.LoginActivity;
import com.google.android.material.navigation.NavigationView;

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    private ImageView ivProfile, ivQR;
    private ProfileViewModel profileViewModel;
    private TextView name;
    private NavigationView nvProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.profile_tv_name);
        ivQR = view.findViewById(R.id.profile_iv_qr);
        ivProfile = view.findViewById(R.id.profile_iv_image);
        btnLogout = view.findViewById(R.id.profile_btn_logout);
        nvProfile = view.findViewById(R.id.profile_navigation);

        nvProfile.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile_menu_borrow_history:
                    NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.navigation_profile_borrow_history);
                    break;

                case R.id.profile_menu_user_info:
                    NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.navigation_profile_edit);
                    break;

                case R.id.profile_menu_about:
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    break;
            }

            return true;
        });


        Glide.with(view).load("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + Core.getUser().getUid()).into(ivQR);

        profileViewModel.getmUser().observe(getViewLifecycleOwner(), users -> {
            Glide.with(view).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24).error(R.drawable.ic_baseline_person_24)).load(users.getImageURL()).into(ivProfile);
            name.setText("Hello, " + users.getUsername());
        });

        btnLogout.setOnClickListener(v -> new AlertDialog.Builder(getContext()).setTitle("Log out")
                .setCancelable(true)
                .setMessage("Are you sure want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Core.getAuth().signOut();

                    Toast.makeText(getContext(), "You have been successfully logged out.", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                })
                .setNegativeButton("No", (dialog, which) -> dialog.cancel()).show());
    }


}