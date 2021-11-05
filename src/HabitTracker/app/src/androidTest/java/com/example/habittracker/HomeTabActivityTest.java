package com.example.habittracker;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
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


    @Rule
    public ActivityScenarioRule<HomeTabActivity> activityRule = new ActivityScenarioRule<>(HomeTabActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void showHabitsFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.habit_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withText("Habits")).check(matches(isDisplayed()));
    }

    @Test
    public void showDailyFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.daily_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withText("Daily")).check(matches(isDisplayed()));
    }

    @Test
    public void showEventsFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.event_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withText("Events")).check(matches(isDisplayed()));
    }

    //This test fails do to an invalid intent sent
    @Test
    public void showFriendsFragment(){
        //click the habit button
        Espresso.onView(withId(R.id.follow_button)).perform(click());
        //check if its the habit tab
        Espresso.onView(withText("Friends")).check(matches(isDisplayed()));
    }

}
