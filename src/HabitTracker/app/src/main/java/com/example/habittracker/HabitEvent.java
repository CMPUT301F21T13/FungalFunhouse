package com.example.habittracker;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Class for Habit Events which exist for a specific day's instance of a habit
 */
public class HabitEvent {
    private Calendar dateTime;
    private String title;
    private String comment;
    private Bitmap photograph;
    private boolean unique;
    private boolean done;
    private LatLng location;

    //constructor without date
    public HabitEvent(){
        setComment("No Comment");
        setTitle("No Title");
        setLocation(new LatLng(0,0));
        setUnique(false);
        setDate(null);
    }

    //constructor with date
    public HabitEvent(Calendar date){
        setComment("No Comment");
        setTitle("No Title");
        setLocation(new LatLng(0,0));
        setUnique(false);
        setDate(date);
    }

    //Getter and Setter Methods
    public Calendar getDate() {
        return dateTime;
    }

    public void setDate(Calendar date) {
        this.dateTime = date;
    }

    public String getTitle(){ return this.title; }

    public void setTitle(String title){ this.title = title;}
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Bitmap image) {
        this.photograph = image;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public boolean getDone(){ return this.done; }
    public void setDone(boolean done){this.done = done; }
}
