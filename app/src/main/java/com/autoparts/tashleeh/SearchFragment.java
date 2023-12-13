package com.autoparts.tashleeh;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.autoparts.tashleeh.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class SearchFragment extends Fragment {

   SearchView searchView;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    private ImageView filters;
    boolean flag;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                replaceFragment(new GalleryFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_search, container, false);
       searchView = view.findViewById(R.id.search_view);
       filters    = view.findViewById(R.id.filters);


       filters.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ItemListDialogFragment.newInstance(30).show(getActivity().getSupportFragmentManager(), "dialog");


           }
       });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);

            }
        });
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new SearchAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        SearchView searchView = view.findViewById(R.id.search_view);

        searchView.setQueryHint("البحث بالاسم");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.clearItems();
                } else {
                    searchItems(newText);
                }
                return true;
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String receivedData = bundle.getString("title");
            String receivedImage = bundle.getString("image");
            applyFilter("الشركة", receivedData,  "السنة");

        }else{
            applyFilter("الشركة", "الفئة",  "السنة");
        }


        return view;
    }

    public void clearSearch(){

        searchView.onActionViewCollapsed();

    }

    public void applyFilter(String company,String category, String year){
        List<Products> products = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Products product = postSnapshot.getValue(Products.class);
                    flag = true;

                    if(!company.equals("الشركة") && !product.getCompany().equals(company))
                    {flag = false;}
                    if(!category.equals("الفئة") && !product.getCategory().equals(category))
                    {flag = false;}
                    if(!year.equals("السنة") && !product.getYear().equals(year))
                    {flag = false;}

                    if(flag == true)
                    {products.add(product);}

                }


                adapter.setItems(products);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void searchItems(String query) {
        databaseReference.orderByChild("pname").startAt(query).endAt(query + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Products> products = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Products product = dataSnapshot.getValue(Products.class);
                            products.add(product);
                        }
                        adapter.setItems(products);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




}

