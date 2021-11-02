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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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
        });
    }

}
