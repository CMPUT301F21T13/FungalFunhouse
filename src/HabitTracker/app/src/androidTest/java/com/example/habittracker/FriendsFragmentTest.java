package com.example.habittracker;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;

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

@RunWith(AndroidJUnit4.class)
@LargeTest

/**
 * This is a Test Activity for the Friends Fragment
 * of HomeTabActivity
 */
public class FriendsFragmentTest {

    //Sets the intent to a pre-existing user
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeTabActivity.class);
        intent.putExtra("user", "testUser1");
    }

    @Rule
    public ActivityScenarioRule<HomeTabActivity> activityScenarioRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    /**
     * Tests if the following list shows the correct output
     * @throws Exception
     */
    @Test
    public void testFollowingList() throws Exception{
        Espresso.onView(withId(R.id.follow_button)).perform(click());
        Thread.sleep(2000);
        onData(anything()).inAdapterView(withId(R.id.friends_fragment_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid)).check(matches(withText("testUser2")));

    }

    /**
     * Tests the clicking functionality of following list
     * @throws Exception
     */
    @Test
    public void testClickingFollowingList() throws Exception{
        Espresso.onView(withId(R.id.follow_button)).perform(click());
        Thread.sleep(2000);
        //clicks the item at position 0 in the listview
        onData(anything()).inAdapterView(withId(R.id.friends_fragment_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid)).perform(click());

        //checks if that sends the user to the friend's linked habits (Habit Fragment)
        Espresso.onView(withId(R.id.habit_fragment_title)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the Activity sends you to InboxActivity
     * Upon clicking the Mail Button
     * @throws Exception
     */
    @Test
    public void testMailButton() throws Exception{
        //click the mail Button in Friends Fragment
        Espresso.onView(withId(R.id.follow_button)).perform(click());
        Espresso.onView(withId(R.id.mail_inbox_activity_button)).perform(click());

        //check if the current activity is now InboxActivity
        Espresso.onView(withId(R.id.inbox_title)).check(matches(isDisplayed()));
    }

    /**
     * Tests if the Activity sends you to RequestActivity
     * Upon clicking the Search Button
     * @throws Exception
     */
    @Test
    public void testSearchButton() throws Exception{
        //click the mail Button in Friends Fragment
        Espresso.onView(withId(R.id.follow_button)).perform(click());
        Espresso.onView(withId(R.id.send_request_activity_button)).perform(click());

        //check if the current activity is now InboxActivity
        Espresso.onView(withId(R.id.request_title)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
