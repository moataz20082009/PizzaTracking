package com.pizzaTracking.android.Common;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//import com.google.firebase.database.ValueEventListener;

/**
 * Created by Moataz.Moustafa on 5/3/2017.
 */

public class FirebaseItemsList<T extends FirebaseDataItem> {
    protected String RootNodeName = "";
    private Class<T> genericType;
    public FirebaseItemsList(String rootNodeName,Class<T> type){
        genericType = type;
        RootNodeName = rootNodeName;
    }
    protected T CreateInstance(Class type) throws IllegalAccessException, InstantiationException {
        return (T) type.newInstance();
    }
    public void GetAll(final DatabaseReference database, final DataRetrieved listener){
        database.child(RootNodeName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> result = new ArrayList<T>();
                for (DataSnapshot item: dataSnapshot.getChildren()){
                    try {
                        T entity = (T) CreateInstance(genericType);
                        entity.FromDataSnapshot(item);
                        result.add(entity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                if(listener != null){
                    try {
                        listener.SetDataAndRun(result);
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
    public void SearchByKeyValue(final DatabaseReference database, final String key, final String value, final DataRetrieved listener){
        database.child(RootNodeName).orderByChild(key).equalTo(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> result = new ArrayList<T>();
                for (DataSnapshot item: dataSnapshot.getChildren()){
                    try {
                        T entity = (T) CreateInstance(genericType);
                        entity.FromDataSnapshot(item);
                        result.add(entity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }
                if(listener != null){
                    try {
                        listener.SetDataAndRun(result);
                    }catch (Exception ex){
                        Log.d("error",ex.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        database.child(RootNodeName).orderByChild(key).equals(value).addListenerForSingleValueEvent(new ValueEventListener() {
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
    */
    }
}
