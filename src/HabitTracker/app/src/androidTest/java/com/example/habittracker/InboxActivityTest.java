package com.example.habittracker;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class InboxActivityTest {

    @Rule
    public ActivityScenarioRule<InboxActivity> activityRule = new ActivityScenarioRule<>(InboxActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test

}
