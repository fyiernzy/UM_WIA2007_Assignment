package com.example.betterher.safetyhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.betterher.GetSupport.GetSupportIntroFragment;
import com.example.betterher.MainActivity;
import com.example.betterher.R;
import com.example.betterher.ReportCases.ReportIntroFragment;
import com.example.betterher.TrackCases.TrackCasesFragment;
import com.example.betterher.UrgentHelp.UrgentHelpFragment;

public class SafetyHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_safety_home, container, false);

        //Urgent Help
        Button btnUrgentHelp =view.findViewById(R.id.btn_urgent_help);
        View.OnClickListener oclUrgentHelp = new View.OnClickListener() {
            public void onClick(View view) {
                mainActivity.replaceFragment(new UrgentHelpFragment());
            }
        };
        btnUrgentHelp.setOnClickListener(oclUrgentHelp);

        //Report Inequality
        Button btnReport =view.findViewById(R.id.btn_report);
        View.OnClickListener oclReport = new View.OnClickListener() {
            public void onClick(View view) {
                mainActivity.replaceFragment(new ReportIntroFragment());
            }
        };
        btnReport.setOnClickListener(oclReport);

        //Track Cases
        Button btnTrackCases =view.findViewById(R.id.btn_track_cases);
        View.OnClickListener oclTrackCases = new View.OnClickListener() {
            public void onClick(View view) {
                mainActivity.replaceFragment(new TrackCasesFragment());
            }
        };
        btnTrackCases.setOnClickListener(oclTrackCases);

        //Get Support
        Button btnGetSupport =view.findViewById(R.id.btn_get_support);
        View.OnClickListener oclGetSupport = new View.OnClickListener() {
            public void onClick(View view) {
                mainActivity.replaceFragment(new GetSupportIntroFragment());
            }
        };
        btnGetSupport.setOnClickListener(oclGetSupport);

        return view;
    }
}