package com.eaglez.hilib.ui.library;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eaglez.hilib.MainActivity;
import com.eaglez.hilib.R;
import com.eaglez.hilib.adapter.MaterialAdapter;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.material.MaterialFile;
import com.eaglez.hilib.components.material.MyMaterial;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.rajat.pdfviewer.PdfViewerActivity;

import java.util.ArrayList;

public class MaterialFileListFragment extends Fragment implements MaterialAdapter.OnMaterialSelectedListener {
    private RecyclerView rvFileList;
    private Query query;

    private MaterialAdapter mAdapter;
    private DocumentReference materialRef;

    private MaterialFileListModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String mID = getArguments().getString("id");
            materialRef = Core.getStore().collection("materials").document(mID);
            if (materialRef != null) {
                return;
            }
        }

        getActivity().onBackPressed();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(MaterialFileListModel.class);
        return inflater.inflate(R.layout.material_file_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (materialRef == null)
            return;

        rvFileList = view.findViewById(R.id.rv_material_file_list);

        query = materialRef.collection("files");

        LoadMaterialFiles();
    }

    private void LoadMaterialFiles() {
        mAdapter = new MaterialAdapter(query, this);

        rvFileList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFileList.setAdapter(mAdapter);
    }

    @Override
    public void OnMaterialSelected(DocumentSnapshot material, MyMaterial mFile) {
        MaterialFile file = material.toObject(MaterialFile.class);

        if (mFile != null) {
            startActivity(PdfViewerActivity.Companion.launchPdfFromUrl(
                    getContext(),
                    file.getUrl(),
                    file.getName(),
                    "",
                    false
            ));
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Confirmation")
                    .setMessage("Are you sure want to subscribe '" + file.getName() + "'? This material will be expired after 7 days of subscribed date.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Add to calendar
                            Core.getStore().collection("issued").add(new MyMaterial(Core.getUser().getUid(), material.getString("name"), material.getReference()));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (this.mAdapter != null) {
            mAdapter.startListening();
            mViewModel.getmMyFile().observe(getViewLifecycleOwner(), myMaterials -> mAdapter.setQuery(query));
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (this.mAdapter != null) {
            mAdapter.stopListening();
            mViewModel.getmMyFile().removeObservers(getViewLifecycleOwner());
        }
    }
}