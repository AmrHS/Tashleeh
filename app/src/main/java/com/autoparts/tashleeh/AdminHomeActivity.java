package com.autoparts.tashleeh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.autoparts.tashleeh.databinding.ActivityHomeAdminBinding;
import com.autoparts.tashleeh.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ActivityHomeAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new AdminItemsFragment());
        binding.bottomNavigationViewAdmin.setBackground(null);
        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.user) {
                replaceFragment(new AdminUserFragment());
            }  else if (itemId == R.id.add) {
                replaceFragment(new AddItemFragment());
            } else if (itemId == R.id.logout) {
                Toast.makeText(getApplicationContext(), "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                Paper.book().destroy();
                Intent intent=new Intent(AdminHomeActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
            }
            return true;
        });

        FloatingActionButton fab = findViewById(R.id.fabuttonAdmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AdminItemsFragment());
            }
        });



    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_admin, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}