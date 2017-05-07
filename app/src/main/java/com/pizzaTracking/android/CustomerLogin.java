package com.pizzaTracking.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pizzaTracking.android.Common.DataRetrieved;
import com.pizzaTracking.android.Common.FirebaseSearch;
import com.pizzaTracking.android.Customer.CustomerInfo;

public class CustomerLogin extends AppCompatActivity{



    private String TAG = "customlog";
    private FirebaseAuth mAuth;
    Button b1;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    SignInButton googleSignInButton;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        InitiateFirebaseAuthentication();
        InitiateFirebaseDB();
        InitiateGoogleSign();
    }
    private void InitiateGoogleSign(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleLoginClientListener googleLoginClientListener = new GoogleLoginClientListener();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , googleLoginClientListener )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleSignInButton = (SignInButton)findViewById(R.id.google_sign_in_button);
        googleSignInButton.setOnClickListener(googleLoginClientListener);
    }
    private void InitiateFirebaseAuthentication(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String msg = "onAuthStateChanged:signed_in:";
                    msg += user.getUid();
                    // User is signed in
                    int d = Log.d(TAG,msg);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };

    }
    private void InitiateFirebaseDB(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            Toast.makeText(CustomerLogin.this, R.string.auth_succeeded  + ",Email:" + acct.getEmail() + ",Display Name:" + acct.getDisplayName(),
                    Toast.LENGTH_SHORT).show();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            Toast.makeText(CustomerLogin.this, R.string.auth_failed,
                    Toast.LENGTH_SHORT).show();
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
    private void SaveAndGoToItems(){
        SharedPreferences.Editor preferences = getSharedPreferences(getString(R.string.SharedPreferenceDBName), MODE_PRIVATE).edit();

    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final String email = acct.getEmail();
        //final String token = acct.getIdToken();
        final String name = acct.getDisplayName();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser userAuth = mAuth.getCurrentUser();
                            final String userId = userAuth.getUid();
                            final CustomerInfo customerInfo = new CustomerInfo(userId);
                            FirebaseSearch search = new FirebaseSearch("users");
                            search.SearchObjectById(myRef, userId, new DataRetrieved() {
                                @Override
                                public void OnDataRetrieved(Object data) {
                                    if(data == null){
                                        customerInfo.Id = userId;
                                        customerInfo.Email = email;
                                        customerInfo.Name =  name;
                                        customerInfo.SaveIntoFirebase(myRef);
                                    }
                                    else {
                                        customerInfo.FromDataSnapshot((DataSnapshot)data);
                                    }
                                }
                            });
                            SharedPreferences.Editor preferences = getSharedPreferences(getString(R.string.SharedPreferenceDBName), MODE_PRIVATE).edit();
                            preferences.putString("UserId", userId);
                            preferences.putString("Email",email);
                            preferences.putString("Name",name);
                            preferences.commit();
                            Intent orderIntent = new Intent(CustomerLogin.this, CustomerNewOrder.class);
                            startActivity(orderIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CustomerLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void GoToNextActivity(boolean isNew, String userId){
        SharedPreferences preferences = CustomerLogin.this.getSharedPreferences(getString( R.string.SharedPreferenceDBName) , Context.MODE_PRIVATE);
        preferences.edit().putString("userid",userId).apply();
        if( isNew){
            Intent intent = new Intent(getApplicationContext(), CustomerProfile.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(),CustomerNewOrder.class);
            startActivity(intent);
        }
    }

    private class GoogleLoginClientListener implements GoogleApiClient.OnConnectionFailedListener,
            View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.google_sign_in_button:
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    break;
                // ...
            }
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }
}
