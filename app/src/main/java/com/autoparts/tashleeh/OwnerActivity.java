package com.autoparts.tashleeh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.autoparts.tashleeh.Model.Products;
import com.autoparts.tashleeh.Model.Users;
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
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class OwnerActivity extends AppCompatActivity {

    private DatabaseReference AdminsRef;
    private RecyclerView recyclerView;
    List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        AdminsRef = FirebaseDatabase.getInstance().getReference().child("RegisteredAdmin");
        recyclerView = findViewById(R.id.ownerRecyclerView);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(AdminsRef, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, OwnerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users,OwnerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OwnerViewHolder holder, int position, @NonNull final Users model)
                    {
                        holder.ownerTitle.setText(model.getName());
                        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AdminsRef.child(model.getPhone())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(OwnerActivity.this,"تم المسح",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });

                        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createAdmin(model.getName(),model.getPhone(), model.getPassword());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OwnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_card_layout, parent, false);
                        return new OwnerViewHolder(view);
                    }
                };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }
    private void createAdmin(final String name, final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Admins").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    RootRef.child("Admins").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(OwnerActivity.this, "مبروك تم اضافة الحساب", Toast.LENGTH_SHORT).show();
                                AdminsRef.child(phone)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(OwnerActivity.this,"تم المسح",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(OwnerActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else {
                    Toast.makeText(OwnerActivity.this,  phone + "  موجود بالفعل ", Toast.LENGTH_SHORT).show();
                    Toast.makeText(OwnerActivity.this, "برجاء اعادة المحاولة", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OwnerActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}