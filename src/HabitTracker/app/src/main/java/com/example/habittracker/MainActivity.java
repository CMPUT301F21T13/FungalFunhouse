package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView profileGrid;
    ArrayList<UserProfile> profileList;
    ArrayAdapter<UserProfile> profileArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView profileGrid = findViewById(R.id.profile_grid);
        profileList = new ArrayList<>();


        profileArrayAdapter = new ProfileListAdapterGrid(this, profileList);



    }
}