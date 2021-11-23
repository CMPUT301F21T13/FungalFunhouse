package com.example.habittracker;

import java.util.ArrayList;
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
    public void generateWeekEvents(Date weekStart){
        WeeklySchedule week =  modelHabit.getWeeklySchedule();

    }
}
