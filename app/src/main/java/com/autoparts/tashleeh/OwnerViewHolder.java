package com.autoparts.tashleeh;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class OwnerViewHolder extends RecyclerView.ViewHolder {
    TextView ownerTitle;
    ImageView cardImage;
    Button acceptBtn, rejectBtn;


    public OwnerViewHolder(View itemView) {
        super(itemView);
        ownerTitle = itemView.findViewById(R.id.owner_card_title);
        cardImage = itemView.findViewById(R.id.owner_card_image);
        acceptBtn = itemView.findViewById(R.id.admin_accept_btn);
        rejectBtn = itemView.findViewById(R.id.admin_reject_btn);


    }

}
