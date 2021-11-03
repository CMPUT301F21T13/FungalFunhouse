package com.example.habittracker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
    private static final ArrayList<String> WEEKDAYS = new ArrayList<String>() {{
        add(SUNDAY);
        add(MONDAY);
        add(TUESDAY);
        add(WEDNESDAY);
        add(THURSDAY);
        add(FRIDAY);
        add(SATURDAY);
    }};

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

    /**
     * Default constructor, initiates every day of the week to be false
     */
    public WeeklySchedule(ArrayList<String> weekdays) {
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

    public boolean checkSunday() {
        return schedule.get(SUNDAY);
    }

    public boolean checkMonday() {
        return schedule.get(MONDAY);
    }

    public boolean checkTuesday() {
        return schedule.get(TUESDAY);
    }

    public boolean checkWednesday() {
        return schedule.get(WEDNESDAY);
    }

    public boolean checkThursday() {
        return schedule.get(THURSDAY);
    }

    public boolean checkFriday() {
        return schedule.get(FRIDAY);
    }

    public boolean checkSaturday() {
        return schedule.get(SATURDAY);
    }

    /**
     * Checks if all the days of the week are set to false
     * @return true if all false, false if at least one is true;
     */
    public boolean checkAllFalse() {
        for(String key : schedule.keySet()){
            if(schedule.get(key)){
                return false;
            }
        }
        return true;
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

    private void fillWithArray(ArrayList<String> weekdays) {
        for (String day:weekdays) {
            switch(day) {
                case SUNDAY:
                    addSunday();
                    break;
                case MONDAY:
                    addMonday();
                    break;
                case TUESDAY:
                    addTuesday();
                    break;
                case WEDNESDAY:
                    addWednesday();
                    break;
                case THURSDAY:
                    addThursday();
                    break;
                case FRIDAY:
                    addFriday();
                    break;
                case SATURDAY:
                    addSaturday();
                    break;
            }
        }
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





















