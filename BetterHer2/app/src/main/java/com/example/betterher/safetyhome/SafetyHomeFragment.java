package com.example.betterher.safetyhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.betterher.GetSupport.GetSupportIntroFragment;
import com.example.betterher.R;
import com.example.betterher.ReportCases.ReportIntroFragment;
import com.example.betterher.TrackCases.TrackCasesFragment;
import com.example.betterher.UrgentHelp.UrgentHelpFragment;

public class SafetyHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safety_home, container, false);

        //Urgent Help
        Button btnUrgentHelp =view.findViewById(R.id.btn_urgent_help);
        View.OnClickListener oclUrgentHelp = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment urgentHelpFragment = new UrgentHelpFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, urgentHelpFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        };
        btnUrgentHelp.setOnClickListener(oclUrgentHelp);

        //Report Inequality
        Button btnReport =view.findViewById(R.id.btn_report);
        View.OnClickListener oclReport = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment reportIntroFragment = new ReportIntroFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, reportIntroFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        };
        btnReport.setOnClickListener(oclReport);

        //Track Cases
        Button btnTrackCases =view.findViewById(R.id.btn_track_cases);
        View.OnClickListener oclTrackCases = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment trackCasesFragment = new TrackCasesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, trackCasesFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        };
        btnTrackCases.setOnClickListener(oclTrackCases);

        //Get Support
        Button btnGetSupport =view.findViewById(R.id.btn_get_support);
        View.OnClickListener oclGetSupport = new View.OnClickListener() {
            public void onClick(View view) {
                Fragment getSupportIntroFragment = new GetSupportIntroFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, getSupportIntroFragment); // R.id.fragment_container is the id of the layout container where the fragment will be placed
                transaction.addToBackStack(null); // Optional: add transaction to the back stack
                transaction.commit();
            }
        };
        btnGetSupport.setOnClickListener(oclGetSupport);

        return view;
    }
}