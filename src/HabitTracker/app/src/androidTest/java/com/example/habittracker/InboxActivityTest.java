package com.example.habittracker;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
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

/**
 * This is a test method for InboxActivity
 */
public class InboxActivityTest {

    static Intent intent;
    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomeTabActivity.class);
        intent.putExtra("user", "testUser1");
    }
    @Rule
    public ActivityScenarioRule<InboxActivity> activityRule = new ActivityScenarioRule<>(InboxActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();
        
    }

}
