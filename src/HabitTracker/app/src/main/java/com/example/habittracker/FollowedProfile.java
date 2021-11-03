package com.example.habittracker;

import java.util.ArrayList;

public class FollowedProfile extends Profile{
    private HabitList habitList;
    private String follwer;


    public String getFollwer(){
        return follwer;
    }
    public void setFollower(String username){
        follwer = username;
    }

    public ArrayList<Habit> getHabits(){
        return habitList.getList();
    }

    public void setHabits(HabitList habits){
        habitList = habits;
    }

}
