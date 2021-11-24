package com.example.habittracker;

import static android.service.autofill.Validators.not;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;

import android.content.Intent;
import android.service.autofill.Validator;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This a test method for Request Activity
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class RequestActivityTest {

    private View decorView;
    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), RequestActivity.class);
        intent.putExtra("user", "testUser1");
    }

    @Rule
    public ActivityScenarioRule<RequestActivity> activityRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setUp() throws Exception {
        Intents.init();
        activityRule.getScenario().onActivity(
                activity -> decorView = activity.getWindow().getDecorView()
        );
    }


    /**
     * Checks that users are being displayed
     * @throws Exception
     */
    @Test
    public void testUserList() throws Exception{
        Thread.sleep(2000);
        Espresso.onView(withText("testUser2")).check(matches(isDisplayed()));
    }


    /**
     * Checks if the search_input correctly
     * Filters and displays individuals
     * @throws Exception
     */
    @Test
    public void testFilterUsers() throws Exception{
        //Enter the filter "testUser2"
        Espresso.onView(withId(R.id.username_input)).perform(clearText()).perform(typeText("testUser2"));
        Espresso.onView(withId(R.id.search_button)).perform(click());
        Thread.sleep(1000);

        //check if the list has filtered
        onData(anything()).inAdapterView(withId(R.id.user_search_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid))
                .check(matches(withText("testUser2")));
        Espresso.onView(withText("mockUser")).check(doesNotExist());
    }


    /**
     * Check that sending a request works
     * @throws Exception
     */
    @Test
    public void testSendRequest() throws Exception{
        Thread.sleep(1000);

        //Filter and select User "testUser2"
        Espresso.onView(withId(R.id.username_input)).perform(clearText()).perform(typeText("testUser2"));
        Espresso.onView(withId(R.id.search_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.user_search_list)).atPosition(0)
                .onChildView(withId(R.id.username_text_grid))
                .perform(click());
        Espresso.onView(withId(R.id.enter_button)).perform(click());

    }


    @After
    public void tearDown() throws Exception{
        Intents.release();
    }

}
