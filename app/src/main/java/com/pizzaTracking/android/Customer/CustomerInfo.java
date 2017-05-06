package com.pizzaTracking.android.Customer;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseDataItem;
import com.pizzaTracking.android.Common.Interests;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz.Moustafa on 5/1/2017.
 */

public class CustomerInfo extends FirebaseDataItem {
    public String Name="";
    public String Phone="";
    public String Email="";
    public String Address="";
    public int Age = 20;
    public ArrayList<String> Interests = new ArrayList<String>();
    public CustomerInfo() {
        super();
    }
    public CustomerInfo(String id) {
        super(id);
    }

    @Override
    protected String GetRootNodeName() {
        return "users";
    }

    @Override
    public void FromDataSnapshot(DataSnapshot dataSnapshot) {
        Id = dataSnapshot.getKey();
        Address = (String)dataSnapshot.child("Address").getValue();
        try {
            Age = Integer.parseInt(String.valueOf(dataSnapshot.child("Age").getValue()));
        }catch (Exception ex){

        }
        Email = (String)dataSnapshot.child("Email").getValue();
        Phone = (String)dataSnapshot.child("Phone").getValue();
        Name = (String)dataSnapshot.child("Name").getValue();
        Interests = new ArrayList<String>();
        for (DataSnapshot interest: dataSnapshot.child("Interests").getChildren()){
            Interests.add((String)interest.getValue());
        }
    }


    public void SaveIntoFirebase(final DatabaseReference database){
        if(Id.length() < 1) Id = GenerateKey(database);
        HashMap<String,Object> mainInfo = new HashMap<String,Object>();
        mainInfo.put("Address",Address);
        mainInfo.put("Age",Age);
        mainInfo.put("Email",Email);
        mainInfo.put("Name",Name);
        mainInfo.put("Phone",Phone);
        this.ElementNode(database).updateChildren(mainInfo);
        HashMap<String,Object> InterestsInfo = new HashMap<String,Object>();
        for (int i=0 ; i < Interests.size() ; i++) {
            InterestsInfo.put(String.valueOf(i+1), Interests.get(i));
        }
        this.ElementNode(database).child("Interests").updateChildren(InterestsInfo);
    }
    public void ReadFromFirebase(final DatabaseReference database, final DataRetrieved listener){
        this.ElementNode(database).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FromDataSnapshot(dataSnapshot);
                if(listener != null){
                    try {
                        listener.SetDataAndRun(CustomerInfo.this);
                    }catch (Exception ex){
                        Log.d("error",ex.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
