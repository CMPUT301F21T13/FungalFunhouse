package com.example.habittracker;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertTrue;
import static java.lang.Thread.sleep;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class tests the AddEventActivity when it is called to make a
 * new event (ie. the fields should be empty)
 *
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class AddEventActivityTest {

    //Sets the intent to a pre-existing user and 'Run' habit
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), AddEventActivity.class);
        intent.putExtra("user", "mockUser");
        // adds hid for the Run habit in mockUser
        intent.putExtra("habit id", "87839ef6-d77c-485b-9999-22ac00e8d4d6");
        // adds current date
        intent.putExtra("date", new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
    }

    @Rule
    public ActivityScenarioRule<AddEventActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    /**
     * this tests checks if the comment edittext is empty
     * @throws InterruptedException
     */
    @Test
    public void emptyComment() {
        // checks to see if comment edit text is empty
        Espresso.onView(withId(R.id.add_event_comment_edittext)).check(matches(withText("")));
    }

    /**
     * this tests checks if the ImageView is empty
     * @throws InterruptedException
     */
    @Test
    public void emptyImageView() throws InterruptedException {
        // checks to see if ImageView is empty
    }

    /**
     * this tests checks if the Map fragment is empty
     * @throws InterruptedException
     */
    @Test
    public void emptyMapFragment() throws InterruptedException {
        // checks to see if Maps is being displayed (it should not be)
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.add_event_placeholder_for_maps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    /**
     * Tests that the add image button sends you to the App's Camera
     * @throws Exception
     */
    @Test
    public void addImageButton() throws Exception{
        Espresso.onView(withId(R.id.add_event_photo_button)).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

    }

    /**
     * Tests that the add location button sends you the Maps Activity
     * @throws Exception
     */
    @Test
    public void addLocationButton() throws Exception{
        Espresso.onView(withId(R.id.add_event_maps_button)).perform(click());
        intended(hasComponent(EventMapsActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

}
