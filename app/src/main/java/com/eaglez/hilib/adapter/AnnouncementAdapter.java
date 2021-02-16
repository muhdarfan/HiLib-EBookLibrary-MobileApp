package com.eaglez.hilib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eaglez.hilib.R;
import com.eaglez.hilib.components.Announcement;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;

public class AnnouncementAdapter extends FirestoreAdapter<AnnouncementAdapter.ViewHolder> {
    public interface OnNewsSelectedListener {
        void OnNewsSelected(DocumentSnapshot news);
    }

    private OnNewsSelectedListener mListener;

    public AnnouncementAdapter(Query query, OnNewsSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.card_announcement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.rv_news_tv_title);
            tvDate = itemView.findViewById(R.id.rv_news_tv_date);

            itemView.setOnClickListener(v -> {

            });
        }

        public void bind(final DocumentSnapshot snapshot, final OnNewsSelectedListener listener) {
            Announcement announcement = snapshot.toObject(Announcement.class);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            tvTitle.setText(announcement.getTitle());
            tvDate.setText("Posted on " + sdf.format(announcement.getDate_created()));
        }
    }
}
