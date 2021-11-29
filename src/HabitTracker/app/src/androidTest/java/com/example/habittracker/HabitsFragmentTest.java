package com.example.habittracker;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
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
public class HabitsFragmentTest {

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
     * Tests functionality of the add habit floating button, checks to make sure
     * the AddHabitActivity class is pulled up
     * @throws AssertionFailedError
     */
    @Test
    public void testAddHabitButton() throws AssertionFailedError {
        //Click on add habit floating button, Opens
        Espresso.onView(withId(R.id.add_habbit_floating_button)).perform(click());
        //Check to make sure the add habit activity class was pulled up
        intended(hasComponent(AddHabitActivity.class.getName()));
    }

    /**
     * Tests functionality of the edit habit floating button, checks to make sure
     * the AddHabitActivity class is pulled up
     *
     * Must have a habit with the title Run
     * @throws AssertionFailedError
     */
    @Test
    public void testEditHabitButton() throws AssertionFailedError {
        //Click on habit with title Run
        Espresso.onView(withText("Run")).perform(click());
        //Click on add habit floating button, Opens
        Espresso.onView(withId(R.id.edit_habit_floating_button)).perform(click());
        //Check to make sure the add habit activity class was pulled up
        intended(hasComponent(AddHabitActivity.class.getName()));
    }

    /**
     * Does not test functionality of the delete button. It simply checks to make
     * sure that the delete button shows up when a habit is selected
     *
     * Must have a habit with the title Run
     */
    @Test
    public void testDeleteHabitButton() {
        //Click on habit with title Run
        Espresso.onView(withText("Run")).perform(click());
        //Click on add habit floating button, Opens
        Espresso.onView(withId(R.id.delete_habit_floating_button)).check(matches(isDisplayed()));
    }

    /**
     * Tests to make sure that the data's information is displayed when the habit
     * is clicked on.
     *
     * When the habit is clicked on it should display:
     * reason
     * dateToStart
     * weekdays
     */
    @Test
    public void testExpandingView() {
        //Click on habit with title Run
        Espresso.onView(withText("Run")).perform(click());
        //Check to make sure the details of the habit is shown
        Espresso.onView(withText("get more exercise")).check(matches(isDisplayed()));
        Espresso.onView(withText("2021-10-31")).check(matches(isDisplayed()));
        Espresso.onView(withText("Monday, Thursday, Friday")).check(matches(isDisplayed()));

        //To test the visual indicator I would have to rely on the database even more
        //So im simply not testing that
    }

    //Espresso is unable to test manual reordering as it cannot longClick() and
    //swipe to drop at the same time

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
