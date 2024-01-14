package com.example.betterher.ReportCases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.betterher.MainActivity;
import com.example.betterher.R;
import com.example.betterher.TrackCases.TrackCasesFragment;


public class ReportIntroFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_intro, container, false);
        final Button btnReportNow = view.findViewById(R.id.btn_report_now);
        btnReportNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment reportFragment = new ReportFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, reportFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        });
        return view;
    }
}