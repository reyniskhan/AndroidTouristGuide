package com.example.androidtouristguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewPlaceActivity extends AppCompatActivity {

    private String categoryName, description, locationLatitude, locationLongitude,  name;

    private Button addNewPlaceButton;
    private EditText inputPlaceName, inputPlaceDescription, inputPlaceLocationLatitude, inputPlaceLocationLongitude;
    private ImageView inputPlaceImage;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private ProgressDialog loadingBar;
    private String  downloadImageUrl;
    private DatabaseReference placeRef;
    private StorageReference placeImagesRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_place);

        categoryName = getIntent().getExtras().get("category").toString();
        placeImagesRef = FirebaseStorage.getInstance().getReference().child("Place Images");
        placeRef = FirebaseDatabase.getInstance().getReference().child("Places");
        loadingBar = new ProgressDialog(AdminAddNewPlaceActivity.this);


        Toast.makeText(AdminAddNewPlaceActivity.this,categoryName,Toast.LENGTH_SHORT).show();

        addNewPlaceButton = findViewById(R.id.add_new_place);
        inputPlaceName = findViewById(R.id.place_name);
        inputPlaceDescription = findViewById(R.id.place_description);
        inputPlaceLocationLatitude = findViewById(R.id.place_location_latitude);
        inputPlaceLocationLongitude = findViewById(R.id.place_location_longitude);
        inputPlaceImage = findViewById(R.id.select_place_image);
        loadingBar = new ProgressDialog(this);


        inputPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addNewPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePlaceData();
            }
        });


    }

    private void openGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            inputPlaceImage.setImageURI(imageUri);
        }

    }

    private void validatePlaceData(){

        description = inputPlaceDescription.getText().toString();
        locationLatitude = inputPlaceLocationLatitude.getText().toString();
        locationLongitude = inputPlaceLocationLongitude.getText().toString();
        name = inputPlaceName.getText().toString();

        if (imageUri == null){
            Toast.makeText(AdminAddNewPlaceActivity.this, "Product image is mandatory",Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(description)){
            Toast.makeText(AdminAddNewPlaceActivity.this, "Product description is mandatory",Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(name)){
            Toast.makeText(AdminAddNewPlaceActivity.this, "Product name is mandatory",Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(locationLatitude)){
            Toast.makeText(AdminAddNewPlaceActivity.this, "Product price is mandatory",Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(locationLongitude)){
            Toast.makeText(AdminAddNewPlaceActivity.this, "Product price is mandatory",Toast.LENGTH_SHORT).show();

        }else{

            StorePlaceInformation();

        }


    }

    private void StorePlaceInformation() {

        loadingBar.setTitle("Add new Place");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        final StorageReference filePath = placeImagesRef.child(imageUri.getLastPathSegment() + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Toast.makeText(AdminAddNewPlaceActivity.this,message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewPlaceActivity.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){

                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewPlaceActivity.this,"Getting Place Image successfull!!!",Toast.LENGTH_SHORT).show();

                            savePlaceInfoToDatabase();
                        }

                    }
                });

            }
        });


    }

    private void savePlaceInfoToDatabase(){

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("description",description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("name",name);
        productMap.put("locationLatitude",locationLatitude);
        productMap.put("locationLongitude",locationLongitude);

        placeRef.child(name).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(AdminAddNewPlaceActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewPlaceActivity.this,"Place is added successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewPlaceActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

}
