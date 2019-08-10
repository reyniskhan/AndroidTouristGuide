package com.example.androidtouristguide.Interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.androidtouristguide.Model.Places;
import com.example.androidtouristguide.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PlaceDetailsActivity extends AppCompatActivity {

    private ImageView pImage;
    private TextView  pName, pDescription, pDistance;

    private String placeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeName = getIntent().getStringExtra("name");

        pImage = findViewById(R.id.place_image_details);
        pName = findViewById(R.id.place_name_details);
        pDescription = findViewById(R.id.place_description_details);
        pDistance = findViewById(R.id.place_distance_details);

        getPlaceDetails(placeName);

    }

    private void getPlaceDetails(final String placeName){

        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("Places");

        placeRef.child(placeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Places places = dataSnapshot.getValue(Places.class);

                    pName.setText(places.getName());
                    pDescription.setText(places.getDescription());
                    pDistance.setText(places.getDistance());

                    Picasso.get().load(places.getImage()).into(pImage);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
