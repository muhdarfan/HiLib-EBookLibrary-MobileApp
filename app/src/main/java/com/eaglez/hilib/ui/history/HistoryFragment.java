package com.eaglez.hilib.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.adapter.MyMaterialHistoryRecyclerViewAdapter;
import com.eaglez.hilib.components.Core;

/**
 * A fragment representing a list of Items.
 */
public class HistoryFragment extends Fragment {
    private RecyclerView rvHistoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        rvHistoryList = view.findViewById(R.id.list);

        rvHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistoryList.setAdapter(new MyMaterialHistoryRecyclerViewAdapter(getContext(), Core.getUserMaterial()));
        return view;
    }
}