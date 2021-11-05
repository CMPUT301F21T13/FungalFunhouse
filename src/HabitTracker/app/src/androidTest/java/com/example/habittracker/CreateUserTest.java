package com.example.habittracker;

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

public class CreateUserTest {

    @Rule
    public ActivityScenarioRule<CreateUser> activityRule = new ActivityScenarioRule<>(CreateUser.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }


    @Test
    public void testMissingUsernameScenario() {

    }

    @Test
    public void testMissingPasswordScenario() {
        
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
