package com.example.habittracker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HabitEventList {
    private Habit modelHabit;
    private ArrayList<HabitEvent> habitEventList;

    public void addEvent(HabitEvent habitEvent){
        habitEventList.add(habitEvent);
    }
    public void removeEvent(HabitEvent habitEvent){
        habitEventList.remove(habitEvent);
    }

    public ArrayList<HabitEvent> getList() {
        return habitEventList;
    }

    public void setList(ArrayList<HabitEvent> eventList) {
        this.habitEventList = eventList;
    }

    //week starts on sunday
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void generateWeekEvents(int year, int weekStart){
        WeeklySchedule week =  modelHabit.getWeeklySchedule();
        Calendar date = Calendar.getInstance();
        date.setLenient(true);
        if(week.checkSunday()){
            date.setWeekDate(year, weekStart, 1);
            this.addEvent(new HabitEvent(date));
        }
        if(week.checkMonday()){
            date.setWeekDate(year, weekStart, 2);
            this.addEvent(new HabitEvent(date));
        }
        if(week.checkTuesday()){
            date.setWeekDate(year, weekStart, 3);
            this.addEvent(new HabitEvent(date));
        }
        if(week.checkWednesday()){
            date.setWeekDate(year, weekStart, 4);
            this.addEvent(new HabitEvent(date));
        }
        if(week.checkThursday()){
            date.setWeekDate(year, weekStart, 5);
            this.addEvent(new HabitEvent(date));
        }
        if(week.checkFriday()){
            date.setWeekDate(year, weekStart, 6);
            this.addEvent(new HabitEvent(date));
        }
        if(week.checkSaturday()){
            date.setWeekDate(year, weekStart, 7);
            this.addEvent(new HabitEvent(date));
        }
    }
}
