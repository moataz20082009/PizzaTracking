package com.pizzaTracking.android.Restaurant;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseDataItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz.Moustafa on 5/1/2017.
 */

public class RestaurantInfo extends FirebaseDataItem {
    public String Name="";
    public String Id = "";
    public String Phone="";
    public String Email="";
    public String Address = "";
    public ArrayList<String> Offers = new ArrayList<String>();
    public double Longitude=0;
    public double Latitude=0;
    public RestaurantInfo() {
        super();
    }
    public RestaurantInfo(String id) {
        super(id);
    }

    @Override
    protected String GetRootNodeName() {
        return "Restaurant";
    }

    @Override
    public void FromDataSnapshot(DataSnapshot dataSnapshot) {
        Id = dataSnapshot.getKey();
        Address = (String)dataSnapshot.child("Address").getValue();
        Email = (String)dataSnapshot.child("Email").getValue();
        Name = (String)dataSnapshot.child("Name").getValue();
        Offers = new ArrayList<String>();
        for (DataSnapshot interest: dataSnapshot.child("Interests").getChildren()){
            Offers.add((String)interest.getValue());
        }
    }

    @Override
    public void SaveIntoFirebase(DatabaseReference database) {
        if(Id.length() < 1) Id = GenerateKey(database);
        HashMap<String,Object> mainInfo = new HashMap<String,Object>();
        mainInfo.put("Address",Address);
        mainInfo.put("Email",Email);
        mainInfo.put("Name",Name);
        this.ElementNode(database).updateChildren(mainInfo);
        HashMap<String,Object> offersInfo = new HashMap<String,Object>();
        for (int i=0 ; i < Offers.size() ; i++) {
            offersInfo.put(String.valueOf(i+1), Offers.get(i));
        }
        this.ElementNode(database).child("Interests").updateChildren(offersInfo);
    }

    @Override
    public void ReadFromFirebase(DatabaseReference database,final DataRetrieved listener) {
        this.ElementNode(database).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FromDataSnapshot(dataSnapshot);
                if(listener != null){
                    listener.OnDataRetrieved(RestaurantInfo.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
