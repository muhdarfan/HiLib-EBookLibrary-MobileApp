package com.eaglez.hilib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.components.material.MaterialCategory;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class MaterialCategoryAdapter extends FirestoreAdapter<MaterialCategoryAdapter.CategoryViewHolder> {
    public interface OnMaterialCategorySelectedListener {
        void OnMaterialCategorySelected(DocumentSnapshot material);
    }

    private OnMaterialCategorySelectedListener mListener;

    public MaterialCategoryAdapter(Query query, OnMaterialCategorySelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new CategoryViewHolder(inflater.inflate(R.layout.card_library_category_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.card_library_file_tv_title);
            tvDesc = itemView.findViewById(R.id.card_library_file_tv_description);
        }

        public void bind(final DocumentSnapshot snapshot, final OnMaterialCategorySelectedListener listener) {
            MaterialCategory category = snapshot.toObject(MaterialCategory.class);

            tvTitle.setText(category.getTitle());
            tvDesc.setText(category.getDescription());
            this.itemView.setOnClickListener(v -> listener.OnMaterialCategorySelected(snapshot));
        }
    }
}
