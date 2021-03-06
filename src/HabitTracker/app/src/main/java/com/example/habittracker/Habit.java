package com.example.habittracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Entity class representing a habit.
 */
public class Habit {

    //private variables
    private String title;
    private String reason;
    private String hid;
    private String dateToStart;
    private Boolean publicVisibility;
    private ArrayList<HabitEvent> habitEventList;
    private int eventsCompleted;
    //private long listPosition;


    //public variables
    public WeeklySchedule weeklySchedule;

    /**
     * Constructor used for creating a habit as you do not need to supply the hid
     * @param title Habits title
     * @param reason Habits reason
     * @param dateToStart format "yyyy-MM-dd"
     * @param publicVisibility Determines whether followers see this habit, true to show, false otherwise
     * @param weekdays ArrayList of weekdays the habit is to be performed on
     */
    public Habit(String title, String reason, String dateToStart, boolean publicVisibility, ArrayList<String> weekdays) {
        this.title = title;
        this.reason = reason;
        this.hid = UUID.randomUUID().toString();
        this.dateToStart = dateToStart;
        this.publicVisibility = publicVisibility;
        this.weeklySchedule = new WeeklySchedule(weekdays);
    }

    /**
     * Constructor used for editing habits as the hid and listPosition needs to remain the same when editing
     * @param title Habits title
     * @param reason Habits reason
     * @param hid unique Identifier pulled from the database DO NOT MANUALLY SET THIS
     * @param dateToStart format "yyyy-MM-dd"
     * @param publicVisibility Determines whether followers see this habit, true to show, false otherwise
     * @param weekdays ArrayList of weekdays the habit is to be performed on
     */
    public Habit(String title, String reason, String hid, String dateToStart, boolean publicVisibility, ArrayList<String> weekdays, int eventsCompleted) {
        this.title = title;
        this.reason = reason;
        this.hid = hid;
        this.dateToStart = dateToStart;
        this.publicVisibility = publicVisibility;
        this.weeklySchedule = new WeeklySchedule(weekdays);
        this.eventsCompleted = eventsCompleted;
    }

    /**
     * Default Habit constructor
     */
    public Habit() {
        this.title = "No title entered";
        this.reason = "No reason entered";
        this.hid = UUID.randomUUID().toString();
        this.dateToStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //Today's date
        this.publicVisibility = true;
        this.weeklySchedule = new WeeklySchedule();
    }

    public String getTitle() {
        return title;
    }

    public String getReason() {
        return reason;
    }

    public String getHid() {
        return hid;
    }

    public String getDateToStart() {
        return dateToStart;
    }

    public Boolean getPublicVisibility() {
        return publicVisibility;
    }

//    public long getListPosition() {
//        return listPosition;
//    }

    public WeeklySchedule getWeeklySchedule() {
        return weeklySchedule;
    }

    public ArrayList<HabitEvent> getHabitEventList(){return habitEventList; }

    public int getEventsCompleted() {
        return eventsCompleted;
    }

    public void setTitle(String title) {
            this.title = title;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Only call when editing a habit so the hid is always the same
     * @param hid The string value of the hid grabbed from the habit in the database
     */
    public void setHid(String hid) {
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

//    public void setListPosition(int position){
//        this.listPosition = position;
//    }

    public void setWeeklySchedule(WeeklySchedule weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }

    public void setHabitEventList(ArrayList<HabitEvent> habitEventList){this.habitEventList = habitEventList; }

    public void setEventsCompleted(int eventsCompleted) {
        this.eventsCompleted = eventsCompleted;
    }

    /**
     * Grab the eventsDone to events that could have been done ratio
     * @return event completion ratio
     */
    public double getCompletionRatio() {
        double ratio = 0.0;

        ratio = eventsCompleted / getPastEventDays();

        return ratio;
    }

    /**
     * Calculates the number of days in which the habit could have been done
     * based on the weekly schedule and dateToStart
     * @return Number of days the event could have been completed on
     */
    public double getPastEventDays() {
        String[] dateArray = dateToStart.split("-");

        //set start date to the habits dateToStart
        Calendar start = Calendar.getInstance();
        start.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[2]) - 1);
        //set end date to current date
        Calendar end = Calendar.getInstance();

        double numberOfDays = 0;
        //Calculate the number of days the event should have occurred on based on the
        //current weeklySchedule and eventsCompleted
        while (start.before(end)) {
            if (weeklySchedule.contains(start.get(Calendar.DAY_OF_WEEK))) {
                numberOfDays++;
            }
            start.add(Calendar.DATE, 1);

        }

        return numberOfDays;
    }

}//Habit
