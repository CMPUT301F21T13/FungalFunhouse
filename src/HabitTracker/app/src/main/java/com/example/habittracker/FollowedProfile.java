package com.example.habittracker;


public class FollowedProfile extends Profile{
    private String follwer;


    public String getFollower(){
        return follwer;
    }
    public void setFollower(String username){
        follwer = username;
    }

}
