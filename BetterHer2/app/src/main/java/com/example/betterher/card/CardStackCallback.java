package com.example.swipablecardtest;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {
    private List<Card> oldCardList;
    private List<Card> newCardList;

    public CardStackCallback(List<Card> oldCardList, List<Card> newCardList) {
        this.oldCardList = oldCardList;
        this.newCardList = newCardList;
    }

    @Override
    public int getOldListSize() {
        return oldCardList.size();
    }

    @Override
    public int getNewListSize() {
        return newCardList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        int oldImageID = oldCardList.get(oldItemPosition).getImageResourceId();
        int newImageID = newCardList.get(newItemPosition).getImageResourceId();
        return oldImageID == newImageID;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Card oldCard = oldCardList.get(oldItemPosition);
        Card newCard = newCardList.get(newItemPosition);
        return oldCard == newCard;
    }
}
