package com.pizzaTracking.android.Common;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pizzaTracking.android.Customer.CustomerInfo;

import java.util.ArrayList;

/**
 * Created by Moataz.Moustafa on 5/3/2017.
 */

public class FirebaseSearch {
    protected String RootNodeName = "";
    public FirebaseSearch(String rootNodeName){
        RootNodeName = rootNodeName;
    }
    public void SearchObjectById(final DatabaseReference database, final String key, final DataRetrieved listener) {
        database.child(RootNodeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    if (item.getKey().equals(key)) {
                        if (listener != null) {
                            try {
                                listener.SetDataAndRun(item);
                                found = true;
                                break;
                            } catch (Exception ex) {
                                Log.d("error", ex.getMessage());
                            }
                        }
                    }
                }
                if (listener != null && found == false) {
                    try {
                        listener.SetDataAndRun(null);
                    } catch (Exception ex) {
                        Log.d("error", ex.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void SearchObjectByKeyValue(final DatabaseReference database, final String key, final String value, final DataRetrieved listener){
        database.child(RootNodeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot item: dataSnapshot.getChildren()){
                    for (DataSnapshot detail: item.getChildren()){
                        if(detail.getKey().equals(key) && String.valueOf(detail.getValue()).equals(value)){
                            if(listener != null){
                                try {
                                    listener.SetDataAndRun(item);
                                    found = true;
                                    break;
                                }catch (Exception ex){
                                    Log.d("error",ex.getMessage());
                                }
                            }
                        }
                    }
                    if(found){
                        break;
                    }
                }
                if(listener != null && found == false){
                    try {
                        listener.SetDataAndRun(null);
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
