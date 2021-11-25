package com.example.habittracker;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
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

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
