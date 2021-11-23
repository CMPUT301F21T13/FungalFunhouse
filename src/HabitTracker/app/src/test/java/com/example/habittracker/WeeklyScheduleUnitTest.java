package com.example.habittracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class WeeklyScheduleUnitTest {

    public WeeklySchedule mockWeeklySchedule() {
        WeeklySchedule weeklySchedule = new WeeklySchedule();
        weeklySchedule.addMonday();
        weeklySchedule.addWednesday();
        weeklySchedule.addFriday();

        return weeklySchedule;
    }

    @Test
    public void testWeeklyScheduleConstructors() {
        WeeklySchedule defaultConstructor = new WeeklySchedule();
        assertTrue(defaultConstructor.checkAllFalse());

        ArrayList<String> weekdays = new ArrayList<>();
        weekdays.add("Monday");
        weekdays.add("Wednesday");

        WeeklySchedule normalConstructor = new WeeklySchedule(weekdays);
        assertTrue(normalConstructor.checkMonday());
        assertTrue(normalConstructor.checkWednesday());
    }

    @Test
    public void testWeeklyScheduleAdders() {
        //create schedule and clear it
        WeeklySchedule weekdays = mockWeeklySchedule();
        weekdays.clearSchedule();
        assertTrue(weekdays.checkAllFalse());

        //check all the adders work
        weekdays.addSunday();
        assertTrue(weekdays.checkSunday());

        weekdays.addMonday();
        assertTrue(weekdays.checkMonday());

        weekdays.addTuesday();
        assertTrue(weekdays.checkTuesday());

        weekdays.addWednesday();
        assertTrue(weekdays.checkWednesday());

        weekdays.addThursday();
        assertTrue(weekdays.checkThursday());

        weekdays.addFriday();
        assertTrue(weekdays.checkFriday());

        weekdays.addSaturday();
        assertTrue(weekdays.checkSaturday());

        //double check all of the weekdays are false
        assertFalse(weekdays.checkAllFalse());

    }

    @Test
    public void testWeeklyScheduleRemovers() {
        //create schedule and fill it
        WeeklySchedule weekdays = mockWeeklySchedule();
        weekdays.fillSchedule();
        assertFalse(weekdays.checkAllFalse());

        //check all removers work
        weekdays.removeSunday();
        assertFalse(weekdays.checkSunday());

        weekdays.removeMonday();
        assertFalse(weekdays.checkMonday());

        weekdays.removeTuesday();
        assertFalse(weekdays.checkTuesday());

        weekdays.removeWednesday();
        assertFalse(weekdays.checkWednesday());

        weekdays.removeThursday();
        assertFalse(weekdays.checkThursday());

        weekdays.removeFriday();
        assertFalse(weekdays.checkFriday());

        weekdays.removeSaturday();
        assertFalse(weekdays.checkSaturday());

        assertTrue(weekdays.checkAllFalse());

    }

}
