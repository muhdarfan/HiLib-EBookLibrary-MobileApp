package com.eaglez.hilib.ui.library;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.adapter.MaterialAdapter;
import com.eaglez.hilib.adapter.MaterialCategoryAdapter;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.material.MyMaterial;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LibraryFragment extends Fragment implements MaterialCategoryAdapter.OnMaterialCategorySelectedListener {
    private RecyclerView rvMaterial;
    private Query mQuery;

    private MaterialCategoryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMaterial = view.findViewById(R.id.library_rv_main);

        mQuery = Core.getStore().collection("materials").orderBy("title", Query.Direction.ASCENDING);
        loadMaterialCategory();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        return root;
    }

    private void loadMaterialCategory() {
        mAdapter = new MaterialCategoryAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();

                if(getItemCount() == 0) {
                    rvMaterial.setVisibility(View.GONE);
                } else {
                    rvMaterial.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Snackbar.make(getView(), "An error has been occurred. Contact administrator.", Snackbar.LENGTH_LONG).show();
            }
        };

        rvMaterial.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMaterial.setAdapter(mAdapter);
    }

    @Override
    public void OnMaterialCategorySelected(DocumentSnapshot material) {
        Bundle bundle = new Bundle();
        bundle.putString("id", material.getId());
        bundle.putString("title", material.getString("title"));

        NavHostFragment.findNavController(this).navigate(R.id.navigation_library_material, bundle);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}