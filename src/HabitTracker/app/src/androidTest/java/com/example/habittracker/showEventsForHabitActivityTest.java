package com.example.habittracker;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
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

/**
 * Test class for showEventsForHabitActivity
 */
public class showEventsForHabitActivityTest {

    //Sets the intent to a pre-existing user
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ShowEventsForHabitActivity.class);
        intent.putExtra("user", "mockUser");
        intent.putExtra("habit id", "87839ef6-d77c-485b-9999-22ac00e8d4d6");
        intent.putExtra("habit title", "Run");
    }

    @Rule
    public ActivityScenarioRule<ShowEventsForHabitActivity> activityRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    /**
     * Tests if the Events Logs pull up the correct instances
     * @throws Exception
     */
    @Test
    public void checkEventLogs()throws Exception{
        Thread.sleep(1000);
        Espresso.onView(withText("2021-11-29")).perform(scrollTo());
        Espresso.onView(withText("2021-11-29")).perform(click());
    }

    /**
     * Ensures that all single click buttons work as intended
     * Add: sends to AddEventActivity
     * Edit/Delete: Do nothing when nothing is selected
     * @throws Exception
     */
    @Test
    public void checkLogButtons()throws Exception{
        Thread.sleep(1000);

        //Edit stays in the activity when not selected
        Espresso.onView(withId(R.id.show_events_edit_floating_button)).perform(click());
        Espresso.onView(withId(R.id.show_events_edit_floating_button)).check(matches(isDisplayed()));

        //Delete stays in the activity when not selected
        Espresso.onView(withId(R.id.show_events_delete_floating_button)).perform(click());
        Espresso.onView(withId(R.id.show_events_delete_floating_button)).check(matches(isDisplayed()));

        //Add pulls up the calendar fragment then sends to addEventActivity
        Espresso.onView(withId(R.id.show_events_add_floating_button)).perform(click());
        Espresso.onView(withText("Pick Date To Start")).check(matches(isDisplayed()));
    }

    /**
     * Checks that the add button pulls up a calendar fragment
     * which can then send to addEventActivity
     * @throws Exception
     */
    @Test
    public void checkAddEvent() throws Exception{


        Espresso.onView(withId(R.id.show_events_add_floating_button)).perform(click());
        Espresso.onView(withText("Pick Date To Start")).check(matches(isDisplayed()));
        Espresso.onView(withText("CONFIRM")).perform(click());
        intended(hasComponent(AddEventActivity.class.getName()));
    }
    /**
     * Tests if edit Button sends to Add Event Activity
     * @throws Exception
     */
    @Test
    public void checkEditEvent() throws Exception{
        Thread.sleep(1000);
        Espresso.onView(withText("2021-11-29")).perform(scrollTo());
        Espresso.onView(withText("2021-11-29")).perform(click());

        Espresso.onView(withId(R.id.show_events_edit_floating_button)).perform(click());
        intended(hasComponent(AddEventActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
