package com.example.habittracker;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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

public class CreateUserTest {

    @Rule
    public ActivityScenarioRule<CreateUser> activityRule = new ActivityScenarioRule<>(CreateUser.class);

    private String takenUsername = "mockUser";
    private String freeUsername = "newUser";
    private String password = "password1";


    @Before
    public void setUp() throws Exception {
        Intents.init();
    }


    @Test
    public void testMissingUsernameScenario() {
        // click sign up button
        Espresso.onView(withId(R.id.signupbutton)).perform(click());
        // check if correct error message is displayed
        Espresso.onView(withId(R.id.setusername)).check(matches(hasErrorText("Username required")));
    }

    @Test
    public void testMissingPasswordScenario() {
        // input some username in username field
        Espresso.onView(withId(R.id.setusername)).perform(typeText(takenUsername));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // click sign up button
        Espresso.onView(withId(R.id.signupbutton)).perform(click());
        // check if correct error message is displayed
        Espresso.onView(withId(R.id.setpassword)).check(matches(hasErrorText("Password required")));
    }

    @Test
    public void testInvalidUsernameScenario() throws InterruptedException {
        // input taken username in username field
        Espresso.onView(withId(R.id.setusername)).perform(typeText(takenUsername));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // input some password in password field
        Espresso.onView(withId(R.id.setpassword)).perform(typeText(password));
        // click sign up button
        Espresso.onView(withId(R.id.signupbutton)).perform(click());
        // check if correct error message is displayed
        Thread.sleep(4000);
        Espresso.onView(withId(R.id.setusername)).check(matches(hasErrorText("Username Taken")));
    }

    @Test
    public void testValidUsernameScenario() throws InterruptedException {
        // input taken username in username field
        Espresso.onView(withId(R.id.setusername)).perform(typeText(freeUsername));
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        // input some password in password field
        Espresso.onView(withId(R.id.setpassword)).perform(typeText(password));
        // click sign up button
        Espresso.onView(withId(R.id.signupbutton)).perform(click());
        // checking if next activity is started due to log in
        
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
