package com.example.androidtouristguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidtouristguide.Model.Users;
import com.example.androidtouristguide.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton =  findViewById(R.id.main_join_now_btn);
        loginButton = findViewById(R.id.main_login_btn);
        Paper.init(this);
        loadingBar = new ProgressDialog(MainActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhone = Paper.book().read(Prevalent.userPhoneKey);
        String UserPassword = Paper.book().read(Prevalent.userPasswordKey);

        if (UserPhone != "" && UserPassword != ""){

            if (!TextUtils.isEmpty(UserPassword) && !TextUtils.isEmpty(UserPhone)){
                allowAccess(UserPhone,UserPassword);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please wait while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void allowAccess(final String phone, final String password) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child(phone).exists()){

                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Login Successfullu;",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);


                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Incorrect Password",Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this,"Incorrect Phone",Toast.LENGTH_SHORT).show();

                    }


                }else{
                    Toast.makeText(MainActivity.this,"Account doesnt exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
