package com.example.habittracker;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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

@RunWith(AndroidJUnit4.class)
@LargeTest
/**
 * This is a test method for the HomeTabActivity
 */
public class HomeTabActivityTest {


    //Sets the intent to a pre-existing user
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeTabActivity.class);
        intent.putExtra("user", "mockUser");
    }

    @Rule
    public ActivityScenarioRule<HomeTabActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }


    /**
     * Tests if the activity shows the Habit Fragment
     * When Habits button pressed
     */
    @Test
    public void showHabitsFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.habit_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withId(R.id.habit_fragment_title)).check(matches(withText("Habits")));
    }

    /**
     * Tests if the activity shows the Daily Fragment
     * When Daily button pressed
     */
    @Test
    public void showDailyFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.daily_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withId(R.id.daily_title)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the activity shows the Events Fragment
     * When Events button pressed
     */
    @Test
    public void showEventsFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.event_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withId(R.id.events_fragment_title)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the activity shows the Friends Fragment
     * When Friends button pressed
     */
    @Test
    public void showFriendsFragment(){
        //click the follow button
        Espresso.onView(withId(R.id.follow_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withId(R.id.friends_title)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
