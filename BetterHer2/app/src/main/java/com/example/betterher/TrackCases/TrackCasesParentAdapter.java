package com.example.betterher.TrackCases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterher.R;

import java.util.List;

public class TrackCasesParentAdapter extends RecyclerView.Adapter<TrackCasesParentAdapter.ParentViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<TrackCasesParentItem> parentItemList;

    TrackCasesParentAdapter(List<TrackCasesParentItem> parentItemList) {
        this.parentItemList = parentItemList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_track_cases, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder parentViewHolder, int position) {
        TrackCasesParentItem parentItem = parentItemList.get(position);

        parentViewHolder.tvCaseRefCode.setText(parentItem.getCaseID());
        parentViewHolder.tvIncidentType.setText(parentItem.getIncidentType());
        parentViewHolder.tvIncidentDate.setText(parentItem.getIncidentDate());
        parentViewHolder.tvIncidentTime.setText(parentItem.getIncidentTime());
        parentViewHolder.tvIncidentLocation.setText(parentItem.getIncidentLocation());
        parentViewHolder.tvCaseStatus.setText(parentItem.getCaseStatus());

        LinearLayoutManager layoutManager = new LinearLayoutManager(parentViewHolder.ChildRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(parentItem.getTrackCasesChildItems().size());

        TrackCasesChildAdapter trackCasesChildAdapter = new TrackCasesChildAdapter(parentItem.getTrackCasesChildItems());
        parentViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        parentViewHolder.ChildRecyclerView.setAdapter(trackCasesChildAdapter);
        parentViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return parentItemList.size();
    }

    class ParentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCaseRefCode, tvIncidentType, tvIncidentDate, tvIncidentTime, tvIncidentLocation, tvCaseStatus;
        private RecyclerView ChildRecyclerView;

        ParentViewHolder(final View itemView) {
            super(itemView);
            tvCaseRefCode = itemView.findViewById(R.id.tv_case_ref_code);
            tvIncidentType = itemView.findViewById(R.id.tv_incident_type);
            tvIncidentDate = itemView.findViewById(R.id.tv_incident_date);
            tvIncidentTime = itemView.findViewById(R.id.tv_incident_time);
            tvIncidentLocation = itemView.findViewById(R.id.tv_incident_location);
            tvCaseStatus = itemView.findViewById(R.id.tv_case_status);
            ChildRecyclerView = itemView.findViewById(R.id.rv_case);
        }
    }
}