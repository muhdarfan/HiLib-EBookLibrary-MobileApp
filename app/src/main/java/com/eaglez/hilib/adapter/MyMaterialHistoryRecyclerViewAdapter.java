package com.eaglez.hilib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.components.material.MyMaterial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyMaterialHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MyMaterialHistoryRecyclerViewAdapter.ViewHolder> {
    private Context context;
    public List<MyMaterial> materialList;

    public MyMaterialHistoryRecyclerViewAdapter(Context context, ArrayList<MyMaterial> userMaterial) {
        this.context = context;
        this.materialList = userMaterial;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_material_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(materialList.get(position));
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDate;

        public ViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.card_material_history_tv_title);
            tvDate = view.findViewById(R.id.card_material_history_tv_date);
        }

        public void bind(final MyMaterial file) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
            tvTitle.setText(file.getTitle() == null ? "Error" : file.getTitle());
            tvDate.setText(sdf.format(file.getBorrowDate()));
        }
    }
}