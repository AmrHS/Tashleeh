package com.autoparts.tashleeh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autoparts.tashleeh.Model.DataModel;
import com.autoparts.tashleeh.Model.Products;
import com.autoparts.tashleeh.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.annotations.NonNull;

public class AdminItemsFragment extends Fragment {

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    List<String> dataList = new ArrayList<>();
    public AdminItemsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                replaceFragment(new AdminItemsFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_items, container, false);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = view.findViewById(R.id.adminRecyclerView);

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("owner").equalTo(Prevalent.currentOnlineUser.getPhone()), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, AdminViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products,AdminViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.cardTitle.setText(model.getPname());
                        Picasso.get().load(model.getImage()).into(holder.cardImage);
                        holder.removeItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ProductsRef.child(model.getPid())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getActivity(),"تم ازالة القطعة",Toast.LENGTH_SHORT).show();
                                                    replaceFragment(new AdminItemsFragment());
                                                }
                                            }
                                        });

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_card_layout, parent, false);
                        return new AdminViewHolder(view);
                    }
                };
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Products product = postSnapshot.getValue(Products.class);
                    dataList.add(product.getModel());
                }

                Set uniqueValues = new HashSet(dataList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });



        return view;
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_admin, fragment);
        fragmentTransaction.commit();
    }

}