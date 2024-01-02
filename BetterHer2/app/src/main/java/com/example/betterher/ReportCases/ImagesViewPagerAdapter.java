package com.example.betterher.ReportCases;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.betterher.R;

import java.util.ArrayList;

public class ImagesViewPagerAdapter extends PagerAdapter {
    private Context context;
    ArrayList<Uri> imageUrls;
    LayoutInflater layoutInflater;

    public ImagesViewPagerAdapter(Context context, ArrayList<Uri> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.layout_show_images, container, false);
        ImageView imageView = view.findViewById(R.id.iv_upload_image);
        imageView.setImageURI(imageUrls.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((RelativeLayout)object).removeView(container);
    }
}
