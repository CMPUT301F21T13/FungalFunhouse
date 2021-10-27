package com.example.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CreateUser extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_activity);

        TextView username = (TextView) findViewById(R.id.setusername);
        TextView password = (TextView) findViewById(R.id.setpassword);
        MaterialButton signupButton = (MaterialButton) findViewById(R.id.signupbutton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateUser.this, HomeTabActivity.class);
                String usernameStr = username.getText().toString();
                intent.putExtra("username", usernameStr);
                startActivity(intent);
            }
        });
    }
}
