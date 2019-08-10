package com.example.androidtouristguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidtouristguide.Interface.PlaceDetailsActivity;
import com.example.androidtouristguide.Model.Places;
import com.example.androidtouristguide.ViewHolder.PlaceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchPlacesActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            type = getIntent().getExtras().get("Admin").toString();
        }

        inputText = findViewById(R.id.search_product_name);
        searchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchPlacesActivity.this));


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchInput = inputText.getText().toString();
                onStart();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Places");

        FirebaseRecyclerOptions<Places> options = new FirebaseRecyclerOptions.Builder<Places>()
                .setQuery(reference.orderByChild("name").startAt(searchInput),Places.class).build();


        FirebaseRecyclerAdapter<Places, PlaceViewHolder> adapter =
                new FirebaseRecyclerAdapter<Places, PlaceViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PlaceViewHolder holder, int i, @NonNull final Places model) {
                        holder.txtPlaceName.setText(model.getName());
                        holder.txtPlaceDescription.setText(model.getDescription());
                        holder.txtPlaceDistance.setText("Distance = ");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (type.equals("Admin")){

                                    Intent intent = new Intent(SearchPlacesActivity.this, AdminMaintainPlacesActivity.class);
                                    intent.putExtra("name",model.getName());
                                    startActivity(intent);

                                }else{

                                    Intent intent = new Intent(SearchPlacesActivity.this, PlaceDetailsActivity.class);
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

        searchList.setAdapter(adapter);
        adapter.startListening();



    }
}
