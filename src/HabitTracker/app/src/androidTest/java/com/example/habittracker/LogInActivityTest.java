package com.example.habittracker;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.ComponentName;
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
    private String someUsername = "wubba lubba dub dub";
    private String password = "1234";
    private String somePassword = "oneRingToRuleThemAll";

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void testMissingUsernameScenario() {
        // click log in button
        Espresso.onView(withId(R.id.loginbutton)).perform(click());
        // check if correct error message is displayed
        Espresso.onView(withId(R.id.username)).check(matches(hasErrorText("Username required")));
    }

    @Test
    public void testMissingPasswordScenario() {
        // input some username in username field
        Espresso.onView(withId(R.id.username)).perform(typeText(someUsername));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // click log in button
        Espresso.onView(withId(R.id.loginbutton)).perform(click());
        // check if correct error message is displayed
        Espresso.onView(withId(R.id.password)).check(matches(hasErrorText("Password required")));
    }

    @Test
    public void testInvalidUsernameScenario() throws InterruptedException {
        // input some username in username field
        Espresso.onView(withId(R.id.username)).perform(typeText(someUsername));
        // input some password in password field
        Espresso.onView(withId(R.id.password)).perform(typeText(somePassword));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // click log in button
        Espresso.onView(withId(R.id.loginbutton)).perform(click());
        // check if valid error is displayed
        Thread.sleep(4000);
        Espresso.onView(withId(R.id.username)).check(matches(hasErrorText("Account does not exist")));

    }

    @Test
    public void testValidUsernameAndInvalidPasswordScenario() throws InterruptedException {
        // input some username in username field
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        // input some password in password field
        Espresso.onView(withId(R.id.password)).perform(typeText(somePassword));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // click log in button
        Espresso.onView(withId(R.id.loginbutton)).perform(click());
        // check if valid error is displayed
        Thread.sleep(4000);
        Espresso.onView(withId(R.id.password)).check(matches(hasErrorText("Incorrect password")));
    }

    @Test
    public void testValidInputScenario() {
        // NOTE: test 'fails' sometimes for seemingly no reason. I have been able to get it to consistently pass on
        // my machine for several test runs but it previously didn't work for a reason I am not aware of. (-jmacdona)

        // input a username in username field
        Espresso.onView(withId(R.id.username)).perform(typeText(username));
        // input a password in password field
        Espresso.onView(withId(R.id.password)).perform(typeText(password));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // perform login button click
        Espresso.onView(withId(R.id.loginbutton)).perform(click());
        // checking if next activity is started due to log in
        intended(hasComponent(HomeTabActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
