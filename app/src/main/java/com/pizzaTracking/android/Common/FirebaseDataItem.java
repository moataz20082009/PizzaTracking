package com.pizzaTracking.android.Common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Moataz.Moustafa on 5/3/2017.
 */

public abstract class FirebaseDataItem {
    public String Id = "";
    protected String RootNodeName = "";
    public FirebaseDataItem(){
        RootNodeName = GetRootNodeName();
    }
    public FirebaseDataItem(String id){
        Id = id;
        RootNodeName = GetRootNodeName();
    }
    protected String GenerateKey(final DatabaseReference database){
        return database.child(RootNodeName).push().getKey();
    }
    protected abstract String GetRootNodeName();
    protected DatabaseReference ElementNode(final DatabaseReference database){
        return database.child(RootNodeName).child(Id);
    }
    public abstract void FromDataSnapshot(DataSnapshot snapshot);
    public abstract void SaveIntoFirebase(final DatabaseReference database);
    public abstract void ReadFromFirebase(final DatabaseReference database, final DataRetrieved listener);
}
