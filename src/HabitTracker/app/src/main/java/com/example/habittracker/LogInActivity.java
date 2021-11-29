package com.example.habittracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is the activity used for users logging into the app.
 * This activity acts as the home activity and is the first
 * activity displayed to users upon opening the app.
 * This activity allows users to either login to an existing account
 * or select 'create new account' to take them to the account creation activity
 *
 * note: although this acts as the main activity, it is not. The main activity
 *       starts at app startup, opens this activity, and then mainactivity is killed
 */
public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    String usernameStr;
    String passwordStr;
    TextView username;
    TextView password;
    TextView newAccount;
    Button loginButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        newAccount = (TextView) findViewById(R.id.newuser);

        loginButton =  findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();

                if (usernameStr.isEmpty()) {
                    username.setError("Username required");
                    username.requestFocus();
                } else if (passwordStr.isEmpty()) {
                    password.setError("Password required");
                    password.requestFocus();
                } else {
                    logInUser(usernameStr, passwordStr);
                }
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, CreateUser.class));
//                finish();
            }
        });;

    }

    public void logInUser(String usernameStr, String passwordStr) {
        db.collection("users").document(usernameStr).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            //user account exists
                            String username_db = documentSnapshot.getString(KEY_USERNAME);
                            String password_db = documentSnapshot.getString(KEY_PASSWORD);
                            if (username_db.equals(usernameStr) && password_db.equals(passwordStr)) {
                                // user exists and password is correct for username
                                Toast.makeText(LogInActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogInActivity.this, HomeTabActivity.class);
                                intent.putExtra("user", usernameStr);
                                startActivity(intent);
                            } else if (username_db.equals(usernameStr) && !password_db.equals(passwordStr)){
                                //user exists but password is incorrect
                                password.setError("Incorrect password");
                                password.requestFocus();
                            }
                        } else {
                            //username cannot be found and so user account does no exist
                            username.setError("Account does not exist");
                            username.requestFocus();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogInActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString()); //will pass exception so we can see error
                    }
                });
    }
}
