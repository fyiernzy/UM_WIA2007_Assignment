package com.example.betterher.GetSupport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.betterher.GetSupport.GetSupportAdapter;
import com.example.betterher.GetSupport.GetSupportCenterModel;
import com.example.betterher.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetSupportFragment extends Fragment {
    ArrayList<GetSupportCenterModel> centerModels = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_support, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("1X6GNlmU3m1O-uTBE1i1_LS3ZrnQP8FXLRo9KoKlO0cE").child("CounsellingCenters");

        GetSupportAdapter adapter = new GetSupportAdapter(requireContext(), centerModels);
        recyclerView = view.findViewById(R.id.rv_centers);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GetSupportCenterModel getSupportCenterModel = dataSnapshot.getValue(GetSupportCenterModel.class);
                    centerModels.add(getSupportCenterModel);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}