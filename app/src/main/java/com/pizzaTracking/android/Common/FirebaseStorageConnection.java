package com.pizzaTracking.android.Common;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Moataz.Moustafa on 5/6/2017.
 */

public class FirebaseStorageConnection {
    public StorageReference RootReference;
    public static FirebaseStorageConnection Create(){
        FirebaseStorageConnection connection = new FirebaseStorageConnection();
        connection.RootReference = FirebaseStorage.getInstance().getReference();
        return connection;
    }
    public String GetUrl(String root, String id){
        StorageReference image = RootReference.child(root + "/" + id + ".png");
        //image.getDownloadUrl();
        //TO-DO: GET URL FROM FIREBASE
        return "";
    }
}
