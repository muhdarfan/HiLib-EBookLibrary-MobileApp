package com.eaglez.hilib.ui.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.ui.AboutActivity;
import com.eaglez.hilib.ui.account.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private Button btnLogout;
    private ImageView ivQR;
    private ProfileViewModel profileViewModel;
    private TextView name;
    private NavigationView nvProfile;
    private ImageView ivProfile;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_REQUEST = 22;

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

        // NAVIGATION
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

        ivProfile.setOnClickListener(v -> {
            ivProfile.setEnabled(false);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext(), R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(view.getContext()).inflate(R.layout.bottom_sheet_profile, view.findViewById(R.id.profileBottomSheetContainer));

            bottomSheetView.findViewById(R.id.bottom_sheet_profile_btn_take_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), "Camera is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            bottomSheetView.findViewById(R.id.bottom_sheet_profile_btn_choose_photo).setOnClickListener(v12 -> {
                bottomSheetDialog.dismiss();

                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image from here..."), PICK_IMAGE_REQUEST);
            });

            bottomSheetView.findViewById(R.id.bottom_sheet_profile_btn_cancel).setOnClickListener(v1 -> bottomSheetDialog.dismiss());

            bottomSheetDialog.setOnDismissListener(dialog -> ivProfile.setEnabled(true));
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivProfile.setImageBitmap(bitmap);

            // upload
            uploadImage();
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ivProfile.setImageBitmap(bitmap);

                // Upload
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        final StorageReference ref = Core.getStorage().child("profile/" + Core.getUser().getUid() + ".jpeg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ivProfile.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ref.putBytes(baos.toByteArray()).addOnSuccessListener(taskSnapshot -> {
            progressDialog.dismiss();

            ref.getDownloadUrl().addOnCompleteListener(task -> {
                Core.getUserData().setImageURL(task.getResult().toString());
            });
            Toast.makeText(getContext(), "Profile picture has been updated.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setMessage("Uploaded " + (int) progress + "%");
        });
    }
}