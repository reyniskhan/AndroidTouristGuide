package com.example.androidtouristguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainPlacesActivity extends AppCompatActivity {

    private Button applyChangesBtn, deleteBtn;
    private EditText name, description, locationLatitude, locationLongitude;
    private ImageView imageView;

    private String pName = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_places);

        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.place_name_maintain);
        locationLatitude = findViewById(R.id.place_location_latitude_maintain);
        locationLongitude = findViewById(R.id.place_location_longitude_maintain);
        description = findViewById(R.id.place_description_maintain);
        imageView = findViewById(R.id.place_image_maintain);
        deleteBtn = findViewById(R.id.delete_product_btn);

        pName = getIntent().getStringExtra("name");
        productRef = FirebaseDatabase.getInstance().getReference().child("Places").child(pName);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteThisProduct();

            }
        });



    }

    private void deleteThisProduct() {

        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainPlacesActivity.this,"Selected Product is deleted successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMaintainPlacesActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
    }

    private void displaySpecificProductInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    String pName = dataSnapshot.child("name").getValue().toString();
                    String pLocationLatitude = dataSnapshot.child("locationLatitude").getValue().toString();
                    String pLocationLongitude = dataSnapshot.child("locationLongitude").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    locationLatitude.setText(pLocationLatitude);
                    locationLongitude.setText(pLocationLongitude);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void applyChanges(){

        String pName = name.getText().toString();
        String pLocationLatitude = locationLatitude.getText().toString();
        String pLocationLongitude = locationLongitude.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals("")){
            Toast.makeText(AdminMaintainPlacesActivity.this,"Write name",Toast.LENGTH_SHORT).show();
        } else if (pDescription.equals("")){
            Toast.makeText(AdminMaintainPlacesActivity.this,"Write description",Toast.LENGTH_SHORT).show();
        } else if (pLocationLongitude.equals("") || pLocationLatitude.equals("")){
            Toast.makeText(AdminMaintainPlacesActivity.this,"Write location",Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("description",pDescription);
            productMap.put("locationLatitude",pLocationLatitude);
            productMap.put("locationLongitude",pLocationLongitude);
            productMap.put("name",pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainPlacesActivity.this,"Changes Applied Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainPlacesActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }
}
