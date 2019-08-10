package com.example.androidtouristguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {

    private Button btnAddReligiousPlaces, btnAddHotels, btnAddRestaurants, btnMaintainPlaces, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Paper.init(this);

        btnMaintainPlaces = findViewById(R.id.btn_maintain_places);

        btnAddHotels = findViewById(R.id.btn_add_hotels);
        btnAddReligiousPlaces = findViewById(R.id.btn_add_religiousplaces);
        btnAddRestaurants = findViewById(R.id.btn_add_restaurants);
        btnLogout = findViewById(R.id.btn_logout_admin);

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

        btnMaintainPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }
}
