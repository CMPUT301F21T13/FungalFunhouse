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
                "2021-11-22", true, 0, weekDays.getSchedule());
        return habit;
    }

    /**
     * Test all constructors for Habit class
     */
    @Test
    public void testHabitConstructors() {
        WeeklySchedule weekDays = new WeeklySchedule();
        weekDays.addMonday();
        weekDays.addWednesday();
        weekDays.addFriday();

        //Default constructor sets habit parameters to default values
        Habit defaultConstructor = new Habit();
        assertEquals("No title entered", defaultConstructor.getTitle());
        assertEquals(Habit.class, defaultConstructor.getClass());

        //Editing constructor is primarily used to edit habits, takes in a hid
        Habit editingConstructor = new Habit("Gym", "Get gains", "404",
                "2021-11-22", true, 0, weekDays.getSchedule());
        assertEquals("Gym", editingConstructor.getTitle());
        assertEquals(Habit.class, editingConstructor.getClass());

        //Normal constructor is primarily used to create new habits, does not take an hid
        Habit normalConstructor = new Habit("Feed Fish", "They don't die",
                "2021-11-22", true, weekDays.getSchedule());
        assertEquals("Feed Fish", normalConstructor.getTitle());
        assertEquals(Habit.class, normalConstructor.getClass());

    }

    /**
     * Make sure all getters work for Habit class
     */
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

    /**
     * Make sure all setters work for Habit class
     */
    @Test
    public void testHabitSetters() {
        WeeklySchedule weekDays = new WeeklySchedule();
        weekDays.addMonday();

        Habit habit = mockHabit();

        habit.setTitle("Piano");
        assertEquals("Piano", habit.getTitle());

        habit.setReason("Practice");
        assertEquals("Practice", habit.getReason());

        //hid is normally a random UUID.toString()
        habit.setHid("500");
        assertEquals("500", habit.getHid());

        //Date format is in yyyy-MM-dd
        habit.setDateToStart("2020-01-12");
        assertEquals("2020-01-12", habit.getDateToStart());

        habit.setPublicVisibility(false);
        assertFalse(habit.getPublicVisibility());

        habit.setWeeklySchedule(weekDays);
        assertEquals(weekDays.getSchedule(), habit.getWeeklySchedule().getSchedule());

    }

}
