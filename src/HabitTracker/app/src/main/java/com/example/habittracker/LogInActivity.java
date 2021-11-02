package com.example.habittracker;

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
//        TextView forgotPassword = (TextView) findViewById(R.id.forgotpassword); //no "forgot password?" functionality implemented yet

        loginButton =  findViewById(R.id.loginbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();
                if (usernameStr.isEmpty()) {
                    // add code later to display text to user: "Username Required!"
                    username.requestFocus();
                } else if (passwordStr.isEmpty()) {
                    // add code later to display text to user: "Password Required!"
                    password.requestFocus();
                } else {
                    db.collection("users").document(usernameStr).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String username_db = documentSnapshot.getString(KEY_USERNAME);
                                    String password_db = documentSnapshot.getString(KEY_PASSWORD);
                                    if (username_db.equals(usernameStr) && password_db.equals(passwordStr)) {
                                        // user exists
                                        Toast.makeText(LogInActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LogInActivity.this, HomeTabActivity.class);
                                        intent.putExtra("user", usernameStr);
                                        startActivity(intent);
                                    } else if (username_db.equals(usernameStr) && !password_db.equals(passwordStr)){
                                        Toast.makeText(LogInActivity.this, "password incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LogInActivity.this, "account doesn't exist", Toast.LENGTH_SHORT).show();
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
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, CreateUser.class));
                finish();
            }
        });;

    }

    @Override
    protected void onStart() {
        super.onStart();
        //to add real-time data retrieval from db
    }
}
