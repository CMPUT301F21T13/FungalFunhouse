package com.example.habittracker;

import android.media.Image;

import java.util.Calendar;

public class HabitEvent {
    private Calendar dateTime;
    private String title;
    private String comment;
    private Image photograph;
    private boolean unique;
    private boolean done;
    private Point location;

    public HabitEvent(){
        setComment("No Comment");
        setTitle("No Title");
        setLocation(new Point());
        setUnique(false);
        setDate(null);
    }
    public HabitEvent(Calendar date){
        setComment("No Comment");
        setTitle("No Title");
        setLocation(new Point());
        setUnique(false);
        setDate(date);
    }

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

    public Image getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Image image) {
        this.photograph = image;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public boolean getDone(){ return this.done; }
    public void setDone(boolean done){this.done = done; }
}
