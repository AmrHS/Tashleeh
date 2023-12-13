package com.autoparts.tashleeh;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.autoparts.tashleeh.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("click_action")) {
            String clickAction = getIntent().getExtras().getString("click_action");

            if ("OPEN_ACTIVITY".equals(clickAction)) {
                replaceFragment(new SearchFragment(),"search");
            }
        }

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new GalleryFragment(),"gallery");
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.user) {
                replaceFragment(new UserFragment(),"user");
            } else if (itemId == R.id.search) {
                replaceFragment(new SearchFragment(),"search");
            } else if (itemId == R.id.logout) {
                Toast.makeText(getApplicationContext(), "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
                Paper.book().destroy();
                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(intent);
                finish();
            }
            return true;
        });

        FloatingActionButton fab = findViewById(R.id.fabutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new GalleryFragment(),"gallery");
            }
        });



    }

    private void replaceFragment(Fragment fragment,String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment,tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void clearSearch(){
        FragmentManager fm = getSupportFragmentManager();
        SearchFragment fragment = (SearchFragment) fm.findFragmentByTag("search");
        fragment.clearSearch();
    }

    public void applyFilter(String company,String category,String year){
        FragmentManager fm = getSupportFragmentManager();
        SearchFragment fragment = (SearchFragment) fm.findFragmentByTag("search");
        fragment.applyFilter(company,category,year);

    }
}