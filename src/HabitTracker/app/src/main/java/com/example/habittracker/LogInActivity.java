package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    ArrayList<UserProfile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        TextView newAccount = (TextView) findViewById(R.id.newuser);
        TextView forgotPassword = (TextView) findViewById(R.id.forgotpassword);
        MaterialButton loginButton = (MaterialButton) findViewById(R.id.loginbutton);

        profileList = new ArrayList<>();
        UserProfile user1 = new UserProfile("user1");
        UserProfile user2 = new UserProfile("user2");
        profileList.add(user1);
        profileList.add(user2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                for (UserProfile i : profileList) {
                    UserProfile user = i;
//                    Log.i("value of username: ", aUsername); // test to check value of string aUsername using Log class
                    if (user.getUsername().equals(username.getText().toString())) {
                        // user exists
                        flag = 1;
                        //                    profileList.get()
                        //                    profileList.indexOf();
                        Toast.makeText(LogInActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogInActivity.this, HomeTabActivity.class);
                        intent.putExtra("user", (Parcelable) user);
                        startActivity(intent);
                    }
                }
                if (flag == 0) {
                    //user doesn't exist / can't be found
                    Toast.makeText(LogInActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, CreateUser.class);
                startActivity(intent);
            }
        });

    }

}
