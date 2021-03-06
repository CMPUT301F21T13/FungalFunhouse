package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is an Activity for the Home Tab of the Habit Tracker It contains four
 * separate tabs (HABITS, DAIlY, EVENTS, and FRIENDS) And manages/switches
 * between the fragments for each tab
 */
public class HomeTabActivity extends AppCompatActivity {

    private Button habitButton;
    private Button dailyButton;
    private Button eventsButton;
    private Button followButton;
    private ConstraintLayout buttonPanel;
    private Button backButton;
    private UserProfile currentUser;
    FirebaseFirestore db;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HomeTheme);
        setContentView(R.layout.activity_home_tab);

        // Initialize Variables
        try {
            currentUsername = getIntent().getStringExtra("user");
            currentUser = new UserProfile(currentUsername);
        } catch (NullPointerException e) {
            Log.e("HabitsFragment: ", "Could not get 'user' from bundle" + e);
        }

        buttonPanel = findViewById(R.id.button_panel);
        db = FirebaseFirestore.getInstance();
        habitButton = findViewById(R.id.habit_button);
        dailyButton = findViewById(R.id.daily_button);
        eventsButton = findViewById(R.id.event_button);
        followButton = findViewById(R.id.follow_button);
        backButton = findViewById(R.id.back_button);

        if (savedInstanceState == null) {
            OpenHabitsFragment(false, currentUser);
        }

        habitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenHabitsFragment(false, currentUser);
            }
        });

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDailyFragment(currentUser);
            }
        });

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenEventsFragment(currentUser);
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFriendsFragment(currentUser);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPanel.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                OpenFriendsFragment(currentUser);
            }
        });
    }

    /**
     * This method opens a Habit Tab within the home page It displays and handles
     * all habits for a user Including their addition, editing, and deletion of a
     * habit
     * 
     * @param following   boolean: A flag for if this is a User's habits or
     *                    FollowedUser's habits
     * @param userToPrint UserProfile: the current User who's habits we are
     *                    displaying
     */
    public void OpenHabitsFragment(boolean following, UserProfile userToPrint) {
        if (following) {
            buttonPanel.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }

        HabitsFragment fragment = new HabitsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userToPrint);
        bundle.putBoolean("following", following);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

    }

    /**
     * This method opens a Daily tab within the home page It will display all of a
     * User's daily habits to complete
     */
    public void OpenDailyFragment(UserProfile userToPrint) {
        DailyFragment fragment = new DailyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userToPrint);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    /**
     * This method opens an Event tab within the home page It will display all of a
     * User's habit events to complete And will allow for the creation, edition, and
     * deletion of said events
     */
    public void OpenEventsFragment(UserProfile userToPrint) {
        EventsFragment fragment = new EventsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userToPrint);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    /**
     * This method opens a Friends Tab within the home page It will display all
     * users the currentUser is following
     * 
     * @param userToPrint UserProfile: the current User we are reading
     */
    public void OpenFriendsFragment(UserProfile userToPrint) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userToPrint);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}