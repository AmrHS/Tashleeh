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
import com.autoparts.tashleeh.Model.Products;
import com.autoparts.tashleeh.Model.SearchDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Products> dataList;
    private FragmentActivity activity;

    public SearchAdapter(FragmentActivity activity) {
        this.activity = activity;
    }
    public void setItems(List<Products> items) {
        this.dataList = items;
        notifyDataSetChanged();
    }

    public void clearItems() {
        if(dataList != null){
        this.dataList.clear();
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {

        holder.cardTitle.setText(dataList.get(position).getPname());
        holder.cardBrand.setText(dataList.get(position).getCompany());
        Picasso.get().load(dataList.get(position).getImage()).into(holder.cardImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDetailFragment dataFragment = new ItemDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putString("title",holder.cardTitle.getText().toString());
                bundle.putString("brand",holder.cardBrand.getText().toString());
                bundle.putString("model",dataList.get(position).getModel());
                bundle.putString("price",dataList.get(position).getPrice());
                bundle.putString("desc",dataList.get(position).getDescription());
                bundle.putString("year",dataList.get(position).getYear());
                bundle.putString("image",dataList.get(position).getImage());
                bundle.putString("owner",dataList.get(position).getOwner());


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
        if(dataList != null)
        return dataList.size();
        else
            return 0;
    }


    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle;
        ImageView cardImage;
        TextView cardBrand;


        public SearchViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.search_card_title);
            cardImage = itemView.findViewById(R.id.search_card_image);
            cardBrand = itemView.findViewById(R.id.search_card_brand);

        }

    }

}
