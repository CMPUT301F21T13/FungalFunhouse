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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests the basic functinality of the EventsFragment that
 * lives within the HomeTabActivity
 */

@RunWith(AndroidJUnit4.class)
@LargeTest

public class EventsFragmentTest {

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
     * Tests if selecting an item correctly sends you to showEventsForHabitActivity
     * @throws InterruptedException
     */
    @Test
    public void testHabitEventsSelection() throws InterruptedException {
        //click on events fragment button
        Espresso.onView(withId(R.id.event_button)).perform(click());
        Thread.sleep(1000);
        //click on habit in list with title Run
        Espresso.onView(withText("Run")).perform(click());
        //check to make sure the ShowEventsForHabitActivity is opened
        intended(hasComponent(ShowEventsForHabitActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
