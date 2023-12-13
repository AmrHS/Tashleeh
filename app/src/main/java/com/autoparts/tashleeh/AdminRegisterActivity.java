package com.autoparts.tashleeh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminRegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;
    private TextView backToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        CreateAccountButton =  findViewById(R.id.register_btn);
        InputName =  findViewById(R.id.register_username_input);
        InputPassword =  findViewById(R.id.register_password_input);
        InputPhoneNumber =  findViewById(R.id.register_phone_number_input);
        loadingBar = new ProgressDialog(this);

        backToLogin = findViewById(R.id.back_to_login);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminRegisterActivity.this, LoginActivity.class));
            }
        });
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAdminAccount();
            }
        });
    }
    private void CreateAdminAccount(){
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "اكتب الاسم", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "اكتب الرقم", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "اكتب الباسورد", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("فتح حساب");
            loadingBar.setMessage("برجاء الانتظار");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateAdminPhoneNumber(name, phone, password);
        }

    }

    private void ValidateAdminPhoneNumber(final String name, final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("RegisteredAdmin").child(phone).exists())){
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);
                    RootRef.child("RegisteredAdmin").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(AdminRegisterActivity.this, "مبروك .. تم فتح الحساب", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(AdminRegisterActivity.this, LoginActivity.class));
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(AdminRegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else {
                    Toast.makeText(AdminRegisterActivity.this, "هذا الحساب -  " + phone + " موجود بالفعل", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(AdminRegisterActivity.this, "برجاء تغيير الرقم", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminRegisterActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}