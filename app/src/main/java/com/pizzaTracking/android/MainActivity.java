package com.pizzaTracking.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseSearch;
import com.pizzaTracking.android.Common.Interests;
import com.pizzaTracking.android.Customer.CustomerInfo;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity{

    Button b1;
    Button customerLoginBtn;
    Button restautantLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button)findViewById(R.id.setUserButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ButtonClick();
            }
        });
        customerLoginBtn = (Button)findViewById(R.id.loginAsCustomerBtn);
        customerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CustomerLogin.class);
                startActivity(intent);
            }
        });
        restautantLoginBtn = (Button)findViewById(R.id.loginAsRestaurantBtn);
        restautantLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RestaurantLogin.class);
                startActivity(intent);
            }
        });
    }
    private void ButtonClick(){
        GetUserByEmail();
    }
    private void SetUser(){
        CustomerInfo customerInfo = new CustomerInfo("test1");
        customerInfo.Address =  "egypt";
        customerInfo.Email = "kjhaskjh@kjhjkds.com";
        customerInfo.Age = 22;
        customerInfo.Name = "me";
        customerInfo.Phone = "12345";
        customerInfo.Interests = new ArrayList<String>(Arrays.asList(Interests.Options.get(0),Interests.Options.get(1),Interests.Options.get(2)));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        customerInfo.SaveIntoFirebase(myRef);
    }
    private void GetUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        CustomerInfo customerInfo = new CustomerInfo("test1");
        customerInfo.ReadFromFirebase(myRef, new DataRetrieved() {
            @Override
            public void OnDataRetrieved(Object data) {
                CustomerInfo info  = (CustomerInfo)data;
                try {
                    Toast.makeText(MainActivity.this, "Email:" + info.Email, Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Log.d("error", "OnDataRetrieved: ");
                }
            }
        });
    }
    private void GetUserByEmail(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String email = "kjhaskjh@kjhjkds.com";
        FirebaseSearch searcher = new FirebaseSearch("users");
        searcher.SearchObjectByKeyValue(myRef, "Email", email, new DataRetrieved() {
            @Override
            public void OnDataRetrieved(Object data) {
                DataSnapshot snapshot = (DataSnapshot)data;
                CustomerInfo info = new CustomerInfo();
                info.FromDataSnapshot(snapshot);
                Toast.makeText(MainActivity.this, "Address:" + info.Address, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

