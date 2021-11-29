package com.example.habittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the activity used for account creation. The user is prompted
 * to enter a unique username and a password and then must press the
 * signup button to create their account and be sent back to the login page (loginactivity)
 */
public class CreateUser extends AppCompatActivity {

    private static final String TAG = "CreateUser";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    
    String usernameStr;
    String passwordStr;
    TextView username;
    TextView password;
    Button signupButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_activity);

        username = (TextView) findViewById(R.id.setusername);
        password = (TextView) findViewById(R.id.setpassword);
        signupButton = (Button) findViewById(R.id.signupbutton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();

                if (usernameStr.isEmpty()) {
                    username.setError("Username required");
                    username.requestFocus();
                } else if (passwordStr.isEmpty()) {
                    password.setError("Password required");
                    password.requestFocus();
                } else {
                    db.collection("users").document(usernameStr).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        username.setError("Username Taken");
                                        username.requestFocus();
                                    } else {
                                        // addUser method is called hear to create new user and add them to db
                                        addUser(usernameStr, passwordStr);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateUser.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });
                }
            }
        });
    }


    /**
     * addUser method used to modularize the functionality for user account creation for readability
     * @param usernameStr username
     * @param passwordStr password
     */
    public void addUser(String usernameStr, String passwordStr) {
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_USERNAME, usernameStr);
        user.put(KEY_PASSWORD, passwordStr);

        db.collection("users").document(usernameStr).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateUser.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateUser.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString()); //will pass exception so we can see error
                    }
                });
        Intent intent = new Intent(CreateUser.this, LogInActivity.class);;
        startActivity(intent);
        finish();
    }


}
