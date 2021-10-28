package com.example.habittracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Entity class representing a habit.
 */
public class Habit {

    //private variables
    private String title;
    private String reason;
    private UUID hid;
    private String dateToStart;

    //public variables
    public WeeklySchedule weeklySchedule;
    public CompletionSchedule completionSchedule;

    public Habit(String title, String reason, String dateToStart) {
        this.title = title;
        this.reason = reason;
        this.hid = UUID.randomUUID();
        this.dateToStart = dateToStart;
        this.weeklySchedule = new WeeklySchedule();
        this.completionSchedule = new CompletionSchedule();
    }

    public Habit() {
        this.title = "No title entered";
        this.reason = "No reason entered";
        this.hid = UUID.randomUUID();
        this.dateToStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //Today's date
        this.weeklySchedule = new WeeklySchedule();
        this.completionSchedule = new CompletionSchedule();
    }

    public String getTitle() {
        return title;
    }

    public String getReason() {
        return reason;
    }

    public UUID getHid() {
        return hid;
    }

    public String getDateToStart() {
        return dateToStart;
    }

    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }

    public CompletionSchedule getCompletionSchedule() {
        return completionSchedule;
    }

    public boolean setTitle(String title) {
        if(titleConstraint(title)){
            this.title = title;
            return true;
        }
        return false;
    }

    public boolean setReason(String reason) {
        if(reasonConstraint(reason)){
            this.reason = reason;
            return true;
        }
        return false;
    }

    /**
     * Sets the date to be started
     * @param dateToStart Must be of format yyyy-MM-dd
     */
    public void setDateToStart(String dateToStart) {
        this.dateToStart = dateToStart;
    }

    /**
     * Checks whether the given string "title" is longer than 20 characters
     * @param title Prospective title to be checked
     * @return true if title is less than 20 char, false otherwise
     */
    private boolean titleConstraint(String title) {
        return title.length() <= 20;
    }

    /**
     * Checks whether the given string "reason" is longer than 30 characters
     * @param reason Prospective reason to be checked
     * @return true if reason is less than 30 char, false otherwise
     */
    private boolean reasonConstraint(String reason) {
        return reason.length() <= 30;
    }

}//Habit
