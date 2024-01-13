package com.example.betterher.GetSupport;

import static com.example.betterher.GetSupport.GetSupportCenterModel.VIEW_TYPE_CARD_VIEW;
import static com.example.betterher.GetSupport.GetSupportCenterModel.VIEW_TYPE_HEADER;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.betterher.R;

import java.util.ArrayList;

public class GetSupportAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<GetSupportCenterModel> getSupportCenterModels;

    public GetSupportAdapter(Context context, ArrayList<GetSupportCenterModel> getSupportCenterModels) {
        this.context = context;
        this.getSupportCenterModels = getSupportCenterModels;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_CARD_VIEW;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvCenterName, tvCenterPhoneNumber, tvCenterAddress;
        Button btnContactNow;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCenterName = itemView.findViewById(R.id.tv_center_name);
            tvCenterPhoneNumber = itemView.findViewById(R.id.tv_center_phone_number);
            tvCenterAddress = itemView.findViewById(R.id.tv_center_address);
            btnContactNow = itemView.findViewById(R.id.btn_contact_now);
        }

        public void setViews(String centerName, String centerPhoneNumber, String centerAddress) {
            tvCenterName.setText(centerName);
            tvCenterPhoneNumber.setText(centerPhoneNumber);
            tvCenterAddress.setText(centerAddress);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_get_support, parent, false);
                return new HeaderViewHolder(headerView);

            case VIEW_TYPE_CARD_VIEW:
                View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview_get_support, parent, false);
                return new CardViewHolder(cardView);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_CARD_VIEW:
                String centerName = getSupportCenterModels.get(position - 1).getCenterName();
                String centerPhoneNumber = "Phone Number: " + getSupportCenterModels.get(position - 1).getCenterPhoneNumber();
                String centerAddress = getSupportCenterModels.get(position - 1).getCenterAddress();
                ((CardViewHolder) holder).setViews(centerName, centerPhoneNumber, centerAddress);

                ((CardViewHolder) holder).btnContactNow.setOnClickListener(args -> {
                    Intent phone_intent = new Intent(Intent.ACTION_DIAL);
                    phone_intent.setData(Uri.parse("tel:" + getSupportCenterModels.get(position - 1).getCenterPhoneNumber()));
                    context.startActivity(phone_intent);
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return getSupportCenterModels.size() + 1;
    }

}
