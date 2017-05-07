package com.pizzaTracking.android.Common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Moataz.Moustafa on 5/6/2017.
 */

public class FirebaseDBConnection {
    public FirebaseDatabase database;
    public DatabaseReference reference;
    public static FirebaseDBConnection Create(){
        FirebaseDBConnection connection = new FirebaseDBConnection();
        connection.database = FirebaseDatabase.getInstance();
        connection.reference = connection.database.getReference();
        return connection;
    }
}
