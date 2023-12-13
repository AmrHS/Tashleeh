package com.autoparts.tashleeh;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.autoparts.tashleeh.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {

    ActivityResultLauncher<Intent> addItemActivityResultLauncher;
    private String CategoryName, Model, Company, Year, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice,InputProductModel;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddItemFragment() {
    }


    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
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

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        addItemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();

                            if (data!=null)
                            {
                                imageUri = data.getData();
                                InputProductImage.setImageURI(imageUri);
                            } else
                            {
                                Toast.makeText(getActivity(), "خطأ .. حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                                replaceFragment(new AddItemFragment());
                            }
                        }
                    }
                });

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
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);


        //CompanySpinner======================================
        Spinner spinner = view.findViewById(R.id.company_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.brands_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Company = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //==============================================

        //YearSpinner======================================
        Spinner year_spinner = view.findViewById(R.id.year_spinner);
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.years_array,
                android.R.layout.simple_spinner_item
        );
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Year = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //==============================================

        //CategorySpinner======================================
        Spinner Category_spinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> category_adapter = ArrayAdapter.createFromResource(
            getActivity(),
            R.array.categories_array,
            android.R.layout.simple_spinner_item
        );
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category_spinner.setAdapter(category_adapter);
        Category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                CategoryName = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //==============================================

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        AddNewProductButton = view.findViewById(R.id.add_item_btn);
        InputProductImage = view.findViewById(R.id.item_image);
        InputProductName = view.findViewById(R.id.item_name);
        InputProductDescription = view.findViewById(R.id.item_description);
        InputProductPrice = view.findViewById(R.id.item_price);
        InputProductModel = view.findViewById(R.id.item_model);
        loadingBar = new ProgressDialog(getActivity());

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
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

    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        addItemActivityResultLauncher.launch(galleryIntent);
    }

    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();
        Model = InputProductModel.getText().toString();

        if (imageUri == null)
        {
            Toast.makeText(getActivity(), "أضف صورة", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(getActivity(), "أضف وصف", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price))
        {
            Toast.makeText(getActivity(), "أضف سعر", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(getActivity(), "أضف اسم", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(CategoryName))
        {
            Toast.makeText(getActivity(), "أضف فئة", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Company))
        {
            Toast.makeText(getActivity(), "أضف شركة", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Model))
        {
            Toast.makeText(getActivity(), "أضف موديل", Toast.LENGTH_SHORT).show();
        }
        else
        {
             StoreProductInformation();
        }

    }

    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "تم رفع الصورة", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(getActivity(), "تم تحصيل لينك الصورة", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("owner", Prevalent.currentOnlineUser.getPhone());
        productMap.put("model", Model);
        productMap.put("year", Year);
        productMap.put("company", Company);


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            replaceFragment(new AdminItemsFragment());
                            loadingBar.dismiss();
                            Toast.makeText(getActivity(), "تم اضافة القطعة", Toast.LENGTH_SHORT).show();
                            sendFCMNotification();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendFCMNotification() {

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Tashleeh", "Sent successfully to all devices");
                    } else {
                        Log.e("Tashleeh", "Failed to send notification: " + task.getException());
                    }
                });

        new Thread(() -> {
            try {
                // Set FCM server key
                String serverKey = "AAAAmgCdJ2Y:APA91bF3P6hKZnlEISmFdG5nUW1IG_zpii9jR0q5lHkRr3bxaD9rqahv7ZiqD7XROs_-i2Zql86v9w40AT6a5YchNBpa4Be-IhGWs4uAEbH50kg1nHmp7P6BjxZJz6EiJ2Oo2FFpmKM2";

                // Set FCM endpoint
                String fcmEndpoint = "https://fcm.googleapis.com/fcm/send";

                // Create a connection to the FCM server
                URL url = new URL(fcmEndpoint);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=" + serverKey);
                connection.setDoOutput(true);

                // Create the JSON payload for the notification
                JSONObject jsonPayload = new JSONObject();
                jsonPayload.put("to", "/topics/allDevices"); // Send to all users

                JSONObject notification = new JSONObject();
                notification.put("title", "!! قطع جديدة");
                notification.put("body", "اضافات جديدة لتشليح شرورة");

                JSONObject data = new JSONObject();
                data.put("click_action", "OPEN_ACTIVITY");

                jsonPayload.put("notification", notification);
                jsonPayload.put("data", data);

                // Send the FCM notification
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(jsonPayload.toString().getBytes("UTF-8"));
                }

                // Get the response from the FCM server
                int responseCode = connection.getResponseCode();
                Log.d("Tashleeh", "FCM Notification Response Code: " + responseCode);

            } catch (Exception e) {
                Log.e("Tashleeh", "Error");
                e.printStackTrace();
            }
        }).start();
    }

}
