package com.example.habittracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * used test all functionality for Habit class
 */
public class HabitUnitTest {

    public Habit mockHabit() {
        WeeklySchedule weekDays = new WeeklySchedule();
        weekDays.addMonday();
        weekDays.addFriday();
        Habit habit = new Habit("Feed Fish", "They don't die", "404",
                "2021-11-22", true, weekDays.getSchedule());
        return habit;
    }

    @Test
    public void testHabitConstructors() {
        WeeklySchedule weekDays = new WeeklySchedule();
        weekDays.addMonday();
        weekDays.addWednesday();
        weekDays.addFriday();

        Habit defaultConstructor = new Habit();
        assertEquals("No title entered", defaultConstructor.getTitle());
        assertEquals(Habit.class, defaultConstructor.getClass());

        Habit editingConstructor = new Habit("Gym", "Get gains", "404",
                "2021-11-22", true, weekDays.getSchedule());
        assertEquals("Gym", editingConstructor.getTitle());
        assertEquals(Habit.class, editingConstructor.getClass());

        Habit normalConstructor = new Habit("Feed Fish", "They don't die",
                "2021-11-22", true, weekDays.getSchedule());
        assertEquals("Feed Fish", normalConstructor.getTitle());
        assertEquals(Habit.class, normalConstructor.getClass());

    }

    @Test
    public void testHabitGetters() {
        WeeklySchedule weekDays = new WeeklySchedule();
        weekDays.addMonday();
        weekDays.addFriday();

        Habit habit = mockHabit();
        assertEquals("Feed Fish", habit.getTitle());
        assertEquals("They don't die", habit.getReason());
        assertEquals("404", habit.getHid());
        assertEquals("2021-11-22", habit.getDateToStart());
        assertTrue(habit.getPublicVisibility());
        assertEquals(weekDays.getSchedule(), habit.getWeeklySchedule().getSchedule());

    }

    @Test
    public void testHabitSetters() {
        WeeklySchedule weekDays = new WeeklySchedule();
        weekDays.addMonday();

        Habit habit = mockHabit();

        habit.setTitle("Piano");
        assertEquals("Piano", habit.getTitle());

        habit.setReason("Practice");
        assertEquals("Practice", habit.getReason());

        habit.setHid("500");
        assertEquals("500", habit.getHid());

        habit.setDateToStart("2020-01-12");
        assertEquals("2020-01-12", habit.getDateToStart());

        habit.setPublicVisibility(false);
        assertFalse(habit.getPublicVisibility());

        habit.setWeeklySchedule(weekDays);
        assertEquals(weekDays.getSchedule(), habit.getWeeklySchedule().getSchedule());

    }

}
