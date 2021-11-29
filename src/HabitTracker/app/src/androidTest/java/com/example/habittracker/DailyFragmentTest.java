package com.example.habittracker;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class DailyFragmentTest {

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
     * checks to make sure clicking on a list item calls
     * AddEventActivity.java correctly
     *
     * Must have a habit with the title 'Cry'
     * @throws AssertionFailedError
     */
    @Test
    public void testHabitSelection() throws AssertionFailedError {
        //click on daily fragment button
        Espresso.onView(withId(R.id.daily_button)).perform(click());
        //Click on habit in list of habits with title Run
        Espresso.onView(withText("Cry")).perform(click());
        //Check to make sure the addEventActivity is opened
        intended(hasComponent(AddEventActivity.class.getName()));
    }

    
    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
