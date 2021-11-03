package com.example.habittracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Boolean publicVisibility;

    //public variables
    public WeeklySchedule weeklySchedule;
    //public CompletionSchedule completionSchedule; // This may or may not be redundant when habit events are created

    public Habit(String title, String reason, String dateToStart, boolean publicVisibility, ArrayList<String> weekdays) {
        this.title = title;
        this.reason = reason;
        this.hid = UUID.randomUUID();
        this.dateToStart = dateToStart;
        this.publicVisibility = publicVisibility;
        this.weeklySchedule = new WeeklySchedule(weekdays);
        //this.completionSchedule = new CompletionSchedule();
    }

    public Habit(String title, String reason, UUID hid, String dateToStart, boolean publicVisibility, ArrayList<String> weekdays) {
        this.title = title;
        this.reason = reason;
        this.hid = hid;
        this.dateToStart = dateToStart;
        this.publicVisibility = publicVisibility;
        this.weeklySchedule = new WeeklySchedule(weekdays);
        //this.completionSchedule = new CompletionSchedule();
    }

    public Habit() {
        this.title = "No title entered";
        this.reason = "No reason entered";
        this.hid = UUID.randomUUID();
        this.dateToStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //Today's date
        this.publicVisibility = true;
        this.weeklySchedule = new WeeklySchedule();
        //this.completionSchedule = new CompletionSchedule();
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

    public Boolean getPublicVisibility() {
        return publicVisibility;
    }

    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }

    /*
    public CompletionSchedule getCompletionSchedule() {
        return completionSchedule;
    }
     */

    public void setTitle(String title) {
            this.title = title;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setHid(UUID hid) {
        this.hid = hid;
    }

    /**
     * Sets the date to be started
     * @param dateToStart Must be of format yyyy-MM-dd
     */
    public void setDateToStart(String dateToStart) {
        this.dateToStart = dateToStart;
    }

    /**
     * Set the visibility of the habit for followers. If the Boolean value is true then it is public
     * if the value is false then it is private.
     * @param visibility Boolean, true for public, false for private.
     */
    public void setPublicVisibility(Boolean visibility) {
        this.publicVisibility = visibility;
    }

    public void setWeeklySchedule(WeeklySchedule weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }

}//Habit
