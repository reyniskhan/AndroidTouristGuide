package com.example.androidtouristguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtouristguide.Interface.PlaceDetailsActivity;
import com.example.androidtouristguide.Model.Places;
import com.example.androidtouristguide.Prevalent.Prevalent;
import com.example.androidtouristguide.ViewHolder.PlaceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String type = "";

    private DatabaseReference placeRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        placeRef = FirebaseDatabase.getInstance().getReference().child("Places");
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);

        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if (!type.equals("Admin")){
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Places> options=
                new FirebaseRecyclerOptions.Builder<Places>()
                        .setQuery(placeRef, Places.class)
                        .build();


        FirebaseRecyclerAdapter<Places, PlaceViewHolder> adapter =
                new FirebaseRecyclerAdapter<Places, PlaceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PlaceViewHolder holder, int i, @NonNull final Places model) {



                        holder.txtPlaceName.setText(model.getName());
                        holder.txtPlaceDescription.setText(model.getDescription());
                        holder.txtPlaceDistance.setText("Distance = " + "");
                        Picasso.get().load(model.getImage()).into(holder.imageView);




                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (type.equals("Admin")){

                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainPlacesActivity.class);
                                    intent.putExtra("name",model.getName());
                                    startActivity(intent);

                                }else{

                                    Intent intent = new Intent(HomeActivity.this, PlaceDetailsActivity.class);
                                    intent.putExtra("name",model.getName());
                                    startActivity(intent);

                                }



                            }
                        });


                    }

                    @NonNull
                    @Override
                    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_layout, parent, false);
                        PlaceViewHolder holder = new PlaceViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {

                if (type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, SearchPlacesActivity.class);
                    intent.putExtra("Admin","Admin");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(HomeActivity.this, SearchPlacesActivity.class);
                    startActivity(intent);
                }


        } else if (id == R.id.nav_settings) {

            if (!type.equals("Admin")) {

                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else{
                Toast.makeText(HomeActivity.this,"Only for End-Users. Can't be accessed by Admins!!!",Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.nav_logout) {


                Paper.book().destroy();
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
