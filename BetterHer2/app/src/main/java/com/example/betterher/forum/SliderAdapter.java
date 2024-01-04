package com.example.swipablecardtest.forum;

import com.example.swipablecardtest.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {
    private final Context context;
    private final List<SliderItem> itemList;

    public SliderAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
    }

    public void addItem(SliderItem item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        SliderItem item = itemList.get(position);
        Glide.with(context).load(item.getImageUrl()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    static class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSliderItem); // Replace with your ImageView ID
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}

