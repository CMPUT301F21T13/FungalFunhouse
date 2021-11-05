package com.example.habittracker;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
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

public class LogInActivityTest {

    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule = new ActivityScenarioRule<>(LogInActivity.class);

    private String username = "mockUser";
    private String password = "1234";

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void testUsernameInputScenario() {
        // input a username in username field
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        // input a password in password field
        Espresso.onView(withId(R.id.password)).perform(typeText(password));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // perform login button click
        Espresso.onView(withId(R.id.loginbutton)).perform(click());
        // checking if next activity is started due to log in
//        Espresso.onView(withId(R.id.home_tab)).check(matches(isDisplayed()));
        intended(hasComponent(HomeTabActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
