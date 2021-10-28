package com.example.habittracker;

import java.util.Hashtable;

/**
 * To be used in Habit class
 *
 * A custom Hashtable used to keep track of the dates when the habit
 * was completed and not completed. The Keys are Strings of date format yyyy-MM-dd
 * and the Values are boolean representing whether or not the habit was completed
 * on that date.
 */
public class CompletionSchedule {
    private Hashtable<String, Boolean> schedule;

    /**
     * Default constructor, hashtable is empty
     */
    public CompletionSchedule() {
        schedule = new Hashtable<String, Boolean>();
    }

    /**
     * Adds to the Hashtable the Key: date and Value: true
     * to represent that the habit was completed that day
     * @param date String of format yyyy-MM-dd
     */
    public void dateCompleted(String date) {
        schedule.put(date, true);
    }

    /**
     * Adds to the Hashtable the Key: date and Value: false
     * to represent that the habit was completed that day
     * @param date String of format yyyy-MM-dd
     */
    public void dateNotCompleted(String date) {
        schedule.put(date, false);
    }

    /**
     * Totals the number of true values in the hashtable which represents the days
     * the habit was completed
     * @return -1 if x < 33%, 0 if 33% < x < 66%, and 1 if x > 66%
     */
    public int getCompletionConsistency() {
        int count = 0;

        for(String key : schedule.keySet()){
            if(schedule.get(key)){
                count++;
            }
        }

        double ratio = count / (double) schedule.size();

        if(ratio < 0.33){
            return -1;
        }
        else if(ratio < 0.66 && ratio > 0.33){
            return 0;
        }
        else{
            return 1;
        }

    }

}//CompletionSchedule
