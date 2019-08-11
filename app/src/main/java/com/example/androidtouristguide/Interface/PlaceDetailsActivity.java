package com.example.androidtouristguide.Interface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtouristguide.Haversine;
import com.example.androidtouristguide.Model.Places;
import com.example.androidtouristguide.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView pName, pDescription, pDistance;

    private String placeName = "";

    private final String TAG = "PlaceDetailsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private LatLng pLatLng;

    private String Name;

    private double Distance;

    private double wayLatitude = 0.0, wayLongitude = 0.0;


    public double pLocationLatitude, pLocationLongitude;

    private boolean isCurrentLocationAvailable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeName = getIntent().getStringExtra("name");
        Toast.makeText(PlaceDetailsActivity.this, placeName, Toast.LENGTH_SHORT).show();

        pName = findViewById(R.id.place_name_details);
        pDescription = findViewById(R.id.place_description_details);
        pDistance = findViewById(R.id.place_distance_details);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        pName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCamera(pLatLng,DEFAULT_ZOOM,Name);
            }
        });

        getPlaceDetails(placeName);

        getLocationPermission();


    }



    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(true){

                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(PlaceDetailsActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null){
                            String locat = location.toString();
                            String lat = locat.substring(15,24);
                            String lon = locat.substring(25,34);
                            wayLatitude = Double.parseDouble(lat);
                            wayLongitude = Double.parseDouble(lon);

                             Distance = Haversine.distance(wayLatitude,wayLongitude,pLocationLatitude,pLocationLongitude);
                             pDistance.setText(Distance + " KM");

                        }else {
                        }
                    }
                });

            }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }




    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");

                    //initialize our map
                    initMap();
                }
            }
        }

    }



    private void initMap() {

        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(PlaceDetailsActivity.this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onMapReady: Map is ready");

        mMap = googleMap;

        moveCamera(pLatLng, DEFAULT_ZOOM, Name);


        getDeviceLocation();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }





    private void moveCamera(LatLng latLng, float zoom, String title){


        try {

            Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            mMap.addMarker(options);
        }catch(NullPointerException e){
            Log.i("Error","Error while moving camera " + e.getMessage());
        }

    }

    private void getPlaceDetails(final String placeName) {


        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("Places");

        placeRef.child(placeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    Places places = dataSnapshot.getValue(Places.class);

                    pName.setText(places.getName());
                    pDescription.setText(places.getDescription());


                    pLocationLatitude = Double.parseDouble(places.getLocationLatitude());
                    pLocationLongitude = Double.parseDouble(places.getLocationLongitude());


                    pLatLng = new LatLng(pLocationLatitude, pLocationLongitude);

//                    Distance = Haversine.distance(wayLatitude, wayLongitude, pLocationLatitude, pLocationLongitude);


//                    pDistance.setText(Distance + "\tKM");
                    // pDistance.setText("");

                    Name = places.getName();



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



//    @Override
//    public void onLocationChanged(Location location) {
//
//        Log.d("location", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
//
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }
}
