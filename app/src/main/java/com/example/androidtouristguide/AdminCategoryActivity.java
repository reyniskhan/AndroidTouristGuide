package com.example.androidtouristguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminCategoryActivity extends AppCompatActivity {

    private Button btnAddReligiousPlaces, btnAddHotels, btnAddRestaurants, btnRemoveUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        btnAddHotels = findViewById(R.id.btn_add_hotels);
        btnAddReligiousPlaces = findViewById(R.id.btn_add_religiousplaces);
        btnAddRestaurants = findViewById(R.id.btn_add_restaurants);
        btnRemoveUsers = findViewById(R.id.btn_remove_users);

        btnAddHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(AdminCategoryActivity.this,AdminAddNewPlaceActivity.class);
                intent.putExtra("category","Hotels");
                startActivity(intent);
            }
        });

        btnAddRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPlaceActivity.class);
                intent.putExtra("category","Restaurants");
                startActivity(intent);
            }
        });

        btnAddReligiousPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewPlaceActivity.class);
                intent.putExtra("category","Religious Places");
                startActivity(intent);
            }
        });
    }
}
