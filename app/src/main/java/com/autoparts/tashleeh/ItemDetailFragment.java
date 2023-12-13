package com.autoparts.tashleeh;

import android.net.MailTo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autoparts.tashleeh.Model.Products;
import com.autoparts.tashleeh.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemDetailFragment extends Fragment {

    private TextView itemTitle;
    private TextView itemData;
    private TextView itemAdmin;
    private TextView itemLoc;
    private TextView itemNum;
    private ImageView itemImage;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemDetailFragment newInstance(String param1, String param2) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                replaceFragment(new SearchFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        itemImage = view.findViewById(R.id.itemImage);
        itemTitle = view.findViewById(R.id.itemTitle);
        itemData = view.findViewById(R.id.itemData);
        itemAdmin = view.findViewById(R.id.itemAdmin);
        itemLoc  = view.findViewById(R.id.itemLocation);
        itemNum  = view.findViewById(R.id.itemNumber);


        Bundle bundle = getArguments();
        if (bundle != null) {
            // Now 'receivedData' contains the data sent from Fragment A
            String receivedData = bundle.getString("title");
            String receivedImage = bundle.getString("image");
            String receivedBrand = bundle.getString("brand");
            String receivedYear = bundle.getString("year");
            String receivedModel = bundle.getString("model");
            String receivedPrice = bundle.getString("price");
            String receivedDesc = bundle.getString("desc");
            String receivedOwner = bundle.getString("owner");
            itemTitle.setText(receivedData);
            itemData.setText( "الشركة: "+ receivedBrand +"\n"+ "الموديل: " + receivedModel +" - "+receivedYear + "\n"+ "السعر: " + receivedPrice +"\n"+ "التفاصيل: " + receivedDesc);
            getAdminDetails(receivedOwner);
            Picasso.get().load(receivedImage).into(itemImage);
            //String productID = bundle.getString("pid");
            //getProductDetails(productID);

        }

        return view;
    }

    private void getAdminDetails(String AdminID) {
        DatabaseReference AdminsRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        AdminsRef.child(AdminID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Users user = dataSnapshot.getValue(Users.class);
                    itemAdmin.setText(" اسم التشليح:  " + user.getName() );
                    itemNum.setText(" الرقم:  " + user.getPhone());
                    itemLoc.setText(" الموقع:  " + "\n"+ Html.fromHtml(user.getAddress()));
                    itemLoc.setMovementMethod(LinkMovementMethod.getInstance());
                    //productPrice.setText(products.getPrice());
                    //productDescription.setText(products.getDescription());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}