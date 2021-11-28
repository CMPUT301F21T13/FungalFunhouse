package com.example.habittracker;

import static android.service.autofill.Validators.not;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;
import android.util.Log;

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

public class AddHabitActivityTest {
    //Sets the intent to a pre-existing user
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), AddHabitActivity.class);
        intent.putExtra("user", "mockUser");
    }

    @Rule
    public ActivityScenarioRule<AddHabitActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    /**
     * Tests functionality of all the parameters needed to create a new habit
     */
    @Test
    public void fillAllParametersTest() {
        //Fill in title of new habit
        Espresso.onView(withId(R.id.habit_title_edittext)).perform(typeText("Gym"));
        //Fill in reason for new habit
        Espresso.onView(withId(R.id.habit_reason_edittext)).perform(typeText("Get Exercise"));
        //Close soft keyboard
        Espresso.closeSoftKeyboard();
        //Do not test "Add Start Date" button as that is a separate test
        //Start date will default to current date
        //Select each weekday
        Espresso.onView(withId(R.id.habit_sunday_chip)).perform(click());
        Espresso.onView(withId(R.id.habit_monday_chip)).perform(click());
        Espresso.onView(withId(R.id.habit_tuesday_chip)).perform(click());
        Espresso.onView(withId(R.id.habit_wednesday_chip)).perform(click());
        Espresso.onView(withId(R.id.habit_thursday_chip)).perform(click());
        Espresso.onView(withId(R.id.habit_friday_chip)).perform(click());
        Espresso.onView(withId(R.id.habit_saturday_chip)).perform(click());
        //Change to non-public habit
        Espresso.onView(withId(R.id.habit_publicVisibility_switch)).perform(click());

        //No errors popping up from these actions shows that functionality is working properly
    }

    /**
     * Tests to make sure errors pop up when leaving a parameter empty when creating a new habit
     */
    @Test
    public void haveEmptyParametersTest() {
        //Click Finish to prompt errors
        Espresso.onView(withId(R.id.habit_finish_button)).perform(click());
        //Check for title error
        Espresso.onView(withId(R.id.habit_title_edittext)).check(matches(hasErrorText("Empty Title")));
        //Check for reason error
        Espresso.onView(withId(R.id.habit_reason_edittext)).check(matches(hasErrorText("Empty Reason")));
    }

    /**
     * Tests functionality of the calendar fragment
     */
    @Test
    public void testCalendarFragment() {
        //Click on button to open calendar fragment
        Espresso.onView(withId(R.id.habit_addstartdate_button)).perform(click());
        //Click Confirm
        Espresso.onView(withText("CONFIRM")).perform(click());

        //Although not exhaustive, at least checks that the fragment is pulled up by
        //being able to click the "CONFIRM" button
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
