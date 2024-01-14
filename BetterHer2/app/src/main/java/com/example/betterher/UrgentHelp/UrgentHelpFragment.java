package com.example.betterher.UrgentHelp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.betterher.R;

public class UrgentHelpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_urgent_help, container, false);

        Button btnCall999 = view.findViewById(R.id.btn_call_999);
        btnCall999.setOnClickListener(args -> {
            String phoneNum = "999";
            Intent phone_intent = new Intent(Intent.ACTION_DIAL);
            phone_intent.setData(Uri.parse("tel:" + phoneNum));
            startActivity(phone_intent);

        });
        return view;
    }
}