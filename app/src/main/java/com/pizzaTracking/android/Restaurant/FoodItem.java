package com.pizzaTracking.android.Restaurant;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseDataItem;
import com.pizzaTracking.android.Customer.CustomerInfo;

import java.util.HashMap;

/**
 * Created by Moataz.Moustafa on 5/1/2017.
 */

public class FoodItem extends FirebaseDataItem {
    public double Price  = 0;
    public String Name = "";
    public String Id = "";
    public String RestaurantId="";
    public String RestaurantName="";

    @Override
    protected String GetRootNodeName() {
        return "menu";
    }

    @Override
    public void FromDataSnapshot(DataSnapshot dataSnapshot) {
        Id = dataSnapshot.getKey();
        Name = (String)dataSnapshot.child("Name").getValue();
        RestaurantId = (String)dataSnapshot.child("RestaurantId").getValue();
        RestaurantName = (String)dataSnapshot.child("RestaurantName").getValue();
        try {
            Price = Double.parseDouble(String.valueOf(dataSnapshot.child("Price").getValue()));
        }catch (Exception ex){

        }
    }

    @Override
    public void SaveIntoFirebase(DatabaseReference database) {
        if(Id.length() < 1) Id = GenerateKey(database);
        HashMap<String,Object> mainInfo = new HashMap<String,Object>();
        mainInfo.put("Price",Price);
        mainInfo.put("Name",Name);
        mainInfo.put("Id",Id);
        mainInfo.put("RestaurantId",RestaurantId);
        mainInfo.put("RestaurantName",RestaurantName);
        this.ElementNode(database).updateChildren(mainInfo);
    }

    @Override
    public void ReadFromFirebase(DatabaseReference database, final DataRetrieved listener) {
        this.ElementNode(database).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FromDataSnapshot(dataSnapshot);
                if(listener != null){
                    try {
                        listener.SetDataAndRun(FoodItem.this);
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
