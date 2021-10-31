package com.example.habittracker;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * To be used in Habit class.
 *
 * A custom Hashtable which represents a reoccurring weekly schedule using a hashtable.
 * The keys are strings of the week day with the values being true or false depending
 * on whether or not the habit is scheduled for that day.
 */
public class WeeklySchedule {

    private LinkedHashMap<String, Boolean> schedule;

    //Constants
    private static final String SUNDAY = "Sunday";
    private static final String MONDAY = "Monday";
    private static final String TUESDAY = "Tuesday";
    private static final String WEDNESDAY = "Wednesday";
    private static final String THURSDAY = "Thursday";
    private static final String FRIDAY = "Friday";
    private static final String SATURDAY = "Saturday";

    /**
     * Default constructor, initiates every day of the week to be false
     */
    public WeeklySchedule() {
        schedule = new LinkedHashMap<>();
        schedule.put(SUNDAY, false);
        schedule.put(MONDAY, false);
        schedule.put(TUESDAY, false);
        schedule.put(WEDNESDAY, false);
        schedule.put(THURSDAY, false);
        schedule.put(FRIDAY, false);
        schedule.put(SATURDAY, false);
    }

    public void addSunday() {
        schedule.put(SUNDAY, true);
    }

    public void addMonday() {
        schedule.put(MONDAY, true);
    }

    public void addTuesday() {
        schedule.put(TUESDAY, true);
    }

    public void addWednesday() {
        schedule.put(WEDNESDAY, true);
    }

    public void addThursday() {
        schedule.put(THURSDAY, true);
    }

    public void addFriday() {
        schedule.put(FRIDAY, true);
    }

    public void addSaturday() {
        schedule.put(SATURDAY, true);
    }

    public void removeSunday() {
        schedule.put(SUNDAY, false);
    }

    public void removeMonday() {
        schedule.put(MONDAY, false);
    }

    public void removeTuesday() {
        schedule.put(TUESDAY, false);
    }

    public void removeWednesday() {
        schedule.put(WEDNESDAY, false);
    }

    public void removeThursday() {
        schedule.put(THURSDAY, false);
    }

    public void removeFriday() {
        schedule.put(FRIDAY, false);
    }

    public void removeSaturday() {
        schedule.put(SATURDAY, false);
    }

    /**
     * Sets every weekday to false
     */
    public void clearSchedule() {
        removeSunday();
        removeMonday();
        removeTuesday();
        removeWednesday();
        removeThursday();
        removeFriday();
        removeSaturday();
    }

    /**
     * sets every weekday to true
     */
    public void fillSchedule() {
        addSunday();
        addMonday();
        addTuesday();
        addWednesday();
        addThursday();
        addFriday();
        addSaturday();
    }

    /**
     * Provides an ArrayList with the Strings of each day the habit is to be completed on
     * @return ArrayList of type string
     */
    public ArrayList<String> getSchedule() {
        ArrayList<String> days = new ArrayList<>();

        for(String key : schedule.keySet()){
            if(schedule.get(key)){
                days.add(key);
            }
        }

        return days;
    }

}//WeeklySchedule





















