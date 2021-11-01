package com.example.habittracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the activity used for adding new habits to the UserProfile habitlist
 * to be displayed in the Habits tab of HomeTabActivity
 */
public class AddHabitActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit_activity);
    }

}
