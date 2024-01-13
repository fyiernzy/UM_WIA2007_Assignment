package com.example.betterher.GetSupport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.betterher.MainActivity;
import com.example.betterher.R;


public class GetSupportIntroFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_get_support_intro, container, false);
        final Button btnGetSupport = view.findViewById(R.id.btn_get_support);
        btnGetSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.replaceFragment(new GetSupportFragment());
            }
        });

        return view;
    }
}