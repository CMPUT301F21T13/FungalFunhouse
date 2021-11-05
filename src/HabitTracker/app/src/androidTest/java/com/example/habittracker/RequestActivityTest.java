package com.example.habittracker;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This a test method for Request Activity
 * Credits for base use of robotium go to
 * The UofA CMPUT 301 TA team of Fall SEM 2021
 */
public class RequestActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<RequestActivity> rule =
            new ActivityTestRule<>(RequestActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkList(){
        solo.assertCurrentActivity("Wrong Activity", RequestActivity.class);

    }


}
