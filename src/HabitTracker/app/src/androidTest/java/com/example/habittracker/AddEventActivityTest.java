package com.example.habittracker;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static java.lang.Thread.sleep;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class AddEventActivityTest {

    //Sets the intent to a pre-existing user and 'Run' habit
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), AddEventActivity.class);
        intent.putExtra("user", "mockUser");
        // adds hid for the Run habit in mockUser
        intent.putExtra("habit id", "87839ef6-d77c-485b-9999-22ac00e8d4d6");
        // adds current date
        intent.putExtra("date", new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
    }

    @Rule
    public ActivityScenarioRule<AddEventActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    /**
     * this tests checks the behaviour when all fields are empty and finish button is pressed
     * @throws InterruptedException
     */
    @Test
    public void emptyHabitEvent() {
        // clicks on finish button
        Espresso.onView(withId(R.id.add_event_finish_button)).perform(click());
        // checks to see if comment edit text is empty
        Espresso.onView(withId(R.id.add_event_comment_edittext)).check(matches(withText("")));
    }

    @Test
    public void commentHabitEvent() throws InterruptedException {
        // clicks on finish button
        Espresso.onView(withId(R.id.add_event_finish_button)).perform(click());
        // checks to see if user is sent back to DailyFragment
        intended(hasComponent(HomeTabActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
