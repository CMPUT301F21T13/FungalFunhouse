package com.example.habittracker;

import android.app.Activity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.intent.Intents.intended;

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

/**
 * This a test method for Request Activity
 * Credits for base use of robotium go to
 * The UofA CMPUT 301 TA team of Fall SEM 2021
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class RequestActivityTest {

    @Rule
    public ActivityScenarioRule<RequestActivity> activityRule = new ActivityScenarioRule<>(RequestActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }
    @Test
    public void testUserList(){
        Espresso.onView(withId(R.id.username_input)).perform(typeText("mockUser"));
        Espresso.onView(withId(R.id.search_button)).perform(click());
        Espresso.onView(withText("mockUser")).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
    }

}
