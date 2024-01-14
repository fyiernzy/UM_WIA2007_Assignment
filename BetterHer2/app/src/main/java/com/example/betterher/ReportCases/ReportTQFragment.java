package com.example.betterher.ReportCases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.betterher.R;
import com.example.betterher.TrackCases.TrackCasesFragment;


public class ReportTQFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_t_q, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String caseID = bundle.getString("caseID");
            if (caseID != null) {
                TextView textViewCaseID = view.findViewById(R.id.tv_report_tq_case_id);
                textViewCaseID.setText(caseID);
            }
        }

        final Button btnTrackCases = view.findViewById(R.id.btn_track_cases);
        btnTrackCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment trackCasesFragment = new TrackCasesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, trackCasesFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        });
        return view;
    }
}