package com.example.betterher.ReportCases;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.betterher.GetSupport.GetSupportIntroFragment;
import com.example.betterher.MainActivity;
import com.example.betterher.R;


public class ReportIntroFragment extends Fragment {

    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_intro, container, false);

        final Button btnReportNow = view.findViewById(R.id.btn_report_now);
        mainActivity = (MainActivity) getActivity();
        btnReportNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(new ReportFragment());
            }
        });
        return view;
    }
}