package com.example.habittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CreateUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_activity);

        TextView username = (TextView) findViewById(R.id.setusername);
        TextView password = (TextView) findViewById(R.id.setpassword); // no password functionality implemented yet
        Button signupButton = (Button) findViewById(R.id.signupbutton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserProfile user = new UserProfile(username.getText().toString());

                SharedPreferences sharedPreferences = getSharedPreferences("savedUserNames", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("newUser", username.getText().toString());
                editor.commit();

                Intent intent = new Intent(CreateUser.this, LogInActivity.class);
//                intent.putExtra("new_user", (Parcelable) user);
                startActivity(intent);
                finish();
            }
        });
    }
}
