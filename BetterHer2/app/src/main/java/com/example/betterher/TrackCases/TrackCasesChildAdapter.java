package com.example.betterher.TrackCases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterher.R;

import java.util.List;

public class TrackCasesChildAdapter extends RecyclerView.Adapter<TrackCasesChildAdapter.ChildViewHolder> {
    private List<TrackCasesChildItem> trackCasesChildItemList;

    public TrackCasesChildAdapter(List<TrackCasesChildItem> trackCasesChildItemList) {
        this.trackCasesChildItemList = trackCasesChildItemList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_case_progress, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int position) {
        TrackCasesChildItem childItem = trackCasesChildItemList.get(position);

        int textColor = R.color.dark_gray;
        int iconResource = R.drawable.ic_done;

        if (position == trackCasesChildItemList.size() - 1) {
            textColor = childItem.getCaseStatus().equals("Completed") ? R.color.dark_gray : R.color.peach;
            iconResource = childItem.getCaseStatus().equals("Completed") ? R.drawable.ic_done : R.drawable.ic_pending;
        }

        childViewHolder.ivProgressIcon.setImageResource(iconResource);
        childViewHolder.tvProgressDate.setText(childItem.getProgressDate());
        childViewHolder.tvProgressTime.setText(childItem.getProgressTime());
        childViewHolder.tvProgressTitle.setText(childItem.getProgressTitle());
        childViewHolder.tvProgressDesc.setText(childItem.getProgressDesc());

        childViewHolder.tvProgressDate.setTextColor(ContextCompat.getColor(childViewHolder.itemView.getContext(), textColor));
        childViewHolder.tvProgressTime.setTextColor(ContextCompat.getColor(childViewHolder.itemView.getContext(), textColor));
        childViewHolder.tvProgressTitle.setTextColor(ContextCompat.getColor(childViewHolder.itemView.getContext(), textColor));
        childViewHolder.tvProgressDesc.setTextColor(ContextCompat.getColor(childViewHolder.itemView.getContext(), textColor));
    }

    @Override
    public int getItemCount() {
        return trackCasesChildItemList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProgressIcon;
        private TextView tvProgressDate, tvProgressTime, tvProgressTitle, tvProgressDesc;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProgressIcon = itemView.findViewById(R.id.iv_progress_icon);
            tvProgressDate = itemView.findViewById(R.id.tv_progress_date);
            tvProgressTime = itemView.findViewById(R.id.tv_progress_time);
            tvProgressTitle = itemView.findViewById(R.id.tv_progress_title);
            tvProgressDesc = itemView.findViewById(R.id.tv_progress_desc);
        }
    }
}
