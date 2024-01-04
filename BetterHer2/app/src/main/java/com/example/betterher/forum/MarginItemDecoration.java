package com.example.swipablecardtest.forum;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private final int margin;

    public MarginItemDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = margin;
        outRect.top = margin;
        outRect.left = margin;
        outRect.right = margin;
    }
}

