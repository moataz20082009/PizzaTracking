package com.pizzaTracking.android.Orders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseDataItem;
import com.pizzaTracking.android.Restaurant.FoodItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz.Moustafa on 5/1/2017.
 */

public class Order extends FirebaseDataItem {
    public String UserId="";
    public ArrayList<String> Items=new ArrayList<String>();
    public Order() {
        super();
    }
    public Order(String id) {
        super(id);
    }

    @Override
    protected String GetRootNodeName() {
        return "Orders";
    }

    @Override
    public void FromDataSnapshot(DataSnapshot dataSnapshot) {
        Id = dataSnapshot.getKey();
        UserId = (String)dataSnapshot.child("UserId").getValue();
        Items = new ArrayList<String>();
        for (DataSnapshot interest: dataSnapshot.child("Interests").getChildren()){
            Items.add((String)interest.getValue());
        }
    }

    @Override
    public void SaveIntoFirebase(DatabaseReference database) {
        if(Id.length() < 1) Id = GenerateKey(database);
        HashMap<String,Object> mainInfo = new HashMap<String,Object>();
        mainInfo.put("UserId",UserId);
        this.ElementNode(database).updateChildren(mainInfo);
        HashMap<String,Object> foodInfo = new HashMap<String,Object>();
        for (int i=0 ; i < Items.size() ; i++) {
            foodInfo.put(String.valueOf(i+1), Items.get(i));
        }
        this.ElementNode(database).child("FoodItems").updateChildren(foodInfo);
    }

    @Override
    public void ReadFromFirebase(DatabaseReference database,final DataRetrieved listener) {
        this.ElementNode(database).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FromDataSnapshot(dataSnapshot);
                if(listener != null){
                    listener.OnDataRetrieved(Order.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
