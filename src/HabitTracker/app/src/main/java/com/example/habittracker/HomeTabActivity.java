package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is an Activity for the Home Tab of the Habit Tracker
 * It contains four separate tabs (HABITS, DAIlY, EVENTS, and FRIENDS)
 * And manages/switches between the fragments for each tab
 */
public class HomeTabActivity extends AppCompatActivity {

    Button habitButton;
    Button dailyButton;
    Button eventsButton;
    Button followButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);


        // The Fragment Manager for the four tabs
        // Initializes the Home Tab to show the HABITS section
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.tabbed_section, new HabitsFragment());
        ft.commit();

        habitButton = findViewById(R.id.habit_button);
        habitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the contents of the container with the new fragment
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.tabbed_section, new HabitsFragment());
                ft.commit();
            }
        });

        dailyButton = findViewById(R.id.daily_button);
        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the contents of the container with the new fragment
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.tabbed_section, new DailyFragment());
                ft.commit();
            }
        });

        eventsButton = findViewById(R.id.event_button);
        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the contents of the container with the new fragment
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.tabbed_section, new EventsFragment());
                ft.commit();
            }
        });

        followButton = findViewById(R.id.follow_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.tabbed_section, new FriendsFragment());
                ft.commit();
            }
        });
    }
}