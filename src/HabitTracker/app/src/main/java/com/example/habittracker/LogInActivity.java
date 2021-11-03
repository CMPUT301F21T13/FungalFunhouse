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
                    if (checkUserExists(usernameStr)) {
                        // user account exists in db
                        if (verifyPassword(usernameStr, passwordStr)) {
                            // password correct, log in user
                            Toast.makeText(LogInActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, HomeTabActivity.class);
                            intent.putExtra("user", usernameStr);
                            startActivity(intent);
                        } else {
                            // password incorrect
                            password.setError("Incorrect password");
                            password.requestFocus();
                        }
                    } else {
                        Toast.makeText(LogInActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                    }
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

    public Boolean checkUserExists(String username) { ;
        final Boolean[] returnVal = new Boolean[1];
        returnVal[0] = false;
        db.collection("users").document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Toast.makeText(LogInActivity.this, "check 1", Toast.LENGTH_SHORT).show();
                            returnVal[0] = true;
                        } else {
                            Toast.makeText(LogInActivity.this, "check 2", Toast.LENGTH_SHORT).show();
                            returnVal[0] = false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("checkUserExists", e.toString());
                    }
                });

        return returnVal[0];
    }



    public Boolean verifyPassword(String username, String password) {
        final Boolean[] returnVal = new Boolean[1];
        returnVal[0] = false;

        db.collection("users").document(username).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String dbPassword = documentSnapshot.getString(KEY_PASSWORD);
                        if (dbPassword.equals(password)) {
                            returnVal[0] = true;
                        } else {
                            returnVal[0] = false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("verifyPassword", e.toString());
                    }
                });
        return returnVal[0];
    }




}
