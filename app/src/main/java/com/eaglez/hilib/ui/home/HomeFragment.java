package com.eaglez.hilib.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.adapter.AnnouncementAdapter;
import com.eaglez.hilib.components.Core;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment implements AnnouncementAdapter.OnNewsSelectedListener {
    private RecyclerView mNewsRecycler;
    private Query mQuery;

    private HomeViewModel homeViewModel;
    private AnnouncementAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNewsRecycler = view.findViewById(R.id.home_rv_news);

        mQuery = Core.getStore().collection("news").orderBy("date_created", Query.Direction.DESCENDING).limit(50);
        loadNewsData();
    }

    private void loadNewsData() {
        mAdapter = new AnnouncementAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                if(getItemCount() == 0) {
                    mNewsRecycler.setVisibility(View.GONE);
                } else {
                    mNewsRecycler.setVisibility(View.VISIBLE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Snackbar.make(getView(), "Error: Contact administrator.", Snackbar.LENGTH_LONG).show();
            }
        };

        mNewsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewsRecycler.setAdapter(mAdapter);
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



    @Override
    public void OnNewsSelected(DocumentSnapshot news) {

    }
}