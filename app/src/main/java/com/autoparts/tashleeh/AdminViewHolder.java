package com.autoparts.tashleeh;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.autoparts.tashleeh.R;


public class AdminViewHolder extends RecyclerView.ViewHolder {
    TextView cardTitle;
    ImageView cardImage;
    Button removeItem;


    public AdminViewHolder(View itemView) {
        super(itemView);
        cardTitle = itemView.findViewById(R.id.admin_card_title);
        cardImage = itemView.findViewById(R.id.admin_card_image);
        removeItem = itemView.findViewById(R.id.admin_remove_btn);

    }

}
