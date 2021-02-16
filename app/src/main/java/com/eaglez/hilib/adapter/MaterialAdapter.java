package com.eaglez.hilib.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Core;
import com.eaglez.hilib.components.material.MaterialFile;
import com.eaglez.hilib.components.material.MyMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MaterialAdapter extends FirestoreAdapter<MaterialAdapter.MaterialViewHolder> {
    public interface OnMaterialSelectedListener {
        void OnMaterialSelected(DocumentSnapshot material, MyMaterial mFile);
    }

    private OnMaterialSelectedListener mListener;

    public MaterialAdapter(Query query, OnMaterialSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MaterialViewHolder(inflater.inflate(R.layout.card_library_file_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class MaterialViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDesc, tvViewBtn;
        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.card_library_tv_file_name);
            tvDesc = itemView.findViewById(R.id.card_library_tv_file_desc);
            tvViewBtn = itemView.findViewById(R.id.card_library_tv_view_btn);
        }

        public void bind(final DocumentSnapshot snapshot, final OnMaterialSelectedListener listener) {
            MaterialFile file = snapshot.toObject(MaterialFile.class);
            MyMaterial mFile = Core.getSubscribedMaterial(snapshot.getId());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM YYYY");
            tvName.setText(file.getName());
            tvViewBtn.setEnabled(true);

            if (mFile != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(mFile.getBorrowDate());
                c.add(Calendar.DAY_OF_MONTH, 7);

                if (new Date().after(c.getTime())) {
                    tvDesc.setText(Html.fromHtml("Your trial has ended.<br/><font color=red>Please subscribe premium plan to borrow this material.</font>"));
                    tvViewBtn.setEnabled(false);
                } else {
                    tvDesc.setText("This material will be accessible until " + sdf.format(c.getTime()) + ".");
                    tvViewBtn.setText("View PDF");

                }
            } else {
                tvDesc.setText(R.string.material_file_desc);
                tvViewBtn.setText(R.string.material_file_subscribe_btn);
            }

            this.tvViewBtn.setOnClickListener(v -> listener.OnMaterialSelected(snapshot, mFile));
        }
    }
}
