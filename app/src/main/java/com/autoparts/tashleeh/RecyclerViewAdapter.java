package com.autoparts.tashleeh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.autoparts.tashleeh.Model.DataModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<DataModel> dataList;
    private FragmentActivity activity;

    public RecyclerViewAdapter(FragmentActivity activity, List<DataModel> dataList) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data to the card here
        // For example: holder.bind(dataList.get(position));
        holder.cardTitle.setText(dataList.get(position).getTitle());
        Picasso.get().load(dataList.get(position).getImage()).into(holder.cardImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDetailFragment dataFragment = new ItemDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putString("title",holder.cardTitle.getText().toString());
                bundle.putString("image",dataList.get(position).getImage());
                dataFragment.setArguments(bundle);

                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, dataFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle;
        ImageView cardImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.card_title);
            cardImage = itemView.findViewById(R.id.card_image);

            // Initialize views in the card layout here
        }

        public void bind(DataModel data) {
            // Bind data to views here
        }
    }

}
