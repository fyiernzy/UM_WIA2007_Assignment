package com.example.betterher.ReportCases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.betterher.R;

import java.util.ArrayList;

public class RoleAdapter extends ArrayAdapter<SpinnerItem> {
    public RoleAdapter(Context context, ArrayList<SpinnerItem> spinnerItemArrayList) {
        super(context, 0, spinnerItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_role_item, parent, false);
        }

        TextView tvRole = convertView.findViewById(R.id.tv_item);
        SpinnerItem currentItem = getItem(position);

        if (currentItem != null) {
            tvRole.setText(currentItem.getItemName());
        }
        return convertView;
    }

}
