package com.example.habittracker;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.hasToString;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
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

@RunWith(AndroidJUnit4.class)
@LargeTest

/**
 * This is a test method for InboxActivity
 */
public class InboxActivityTest {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), InboxActivity.class);
        intent.putExtra("user", "testUser1");
    }

    @Rule
    public ActivityScenarioRule<InboxActivity> activityRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();

    }

    /**
     * Tests if the basic display of the inbox list
     * Is showing the correct output/format
     * @throws Exception
     */
    @Test
    public void testRequestsReceived() throws Exception{
        addRequest();
        Thread.sleep(2000);
        onData(anything()).inAdapterView(withId(R.id.request_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid))
                .check(matches(withText("Follow request from: testUser2")));

    }

    /**
     * This Test is for the functionality of clicking
     * A single request in the list and having
     * The accept and reject buttons pop up
     * @throws Exception
     */
    @Test
    public void clickRequestTest() throws Exception{
        addRequest();
        Thread.sleep(2000);

        //make sure the buttons are initially hidden
        Espresso.onView(withId(R.id.accept_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        Espresso.onView(withId(R.id.deny_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        //select a ListView item
        onData(anything()).inAdapterView(withId(R.id.request_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid))
                .perform(click());

        //The buttons should now show up
        Espresso.onView(withId(R.id.accept_button)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.deny_button)).check(matches(isDisplayed()));

        deleteRequest();
    }

    /**
     * This test is for the functionality of
     * accepting a request
     * @throws Exception
     */
    @Test
    public void acceptRequestTest() throws Exception{
        addRequest();
        Thread.sleep(2000);
        //select and accept a listview item
        onData(anything()).inAdapterView(withId(R.id.request_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid))
                .perform(click());
        Espresso.onView(withId(R.id.accept_button)).perform(click());

        //The listview item should now be gone
        Espresso.onView(withText("testUser2")).check(doesNotExist());

    }

    /**
     * This test is for the functionality of
     * rejecting a request
     * @throws Exception
     */
    @Test
    public void rejectRequestTest() throws Exception{
        addRequest();
        Thread.sleep(2000);
        //select and accept a listview item
        onData(anything()).inAdapterView(withId(R.id.request_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid))
                .perform(click());
        Espresso.onView(withId(R.id.deny_button)).perform(click());

        //The listview item should now be gone
        Espresso.onView(withText("testUser2")).check(doesNotExist());

    }
    @After
    public void tearDown() throws Exception {
        Intents.release();

    }


    /**
     * Adds a test request from TestUser2 to testUser1
     * Used for mimicking the functionalities of accepting
     * And rejecting requests
     */
    public void addRequest(){
        FollowRequest testRequest = new FollowRequest("testUser2", "testUser1");
        Serialization.addRequest(testRequest);
    }


    /**
     * Deletes a test request form testUser2 to testUser1
     * Used for mickicking functionalities of accepting
     * and rejecting requests
     */
    public void deleteRequest(){
        FollowRequest testRequest = new FollowRequest("testUser2", "testUser1");
        Serialization.removeRequest(testRequest);
    }
}


