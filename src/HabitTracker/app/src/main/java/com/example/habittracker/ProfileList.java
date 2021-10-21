package com.example.habittracker;

import java.util.ArrayList;

public abstract class ProfileList {
    protected ArrayList<Profile> profileList;

    public void addProfile(Profile profile){
        profileList.add(profile);
    }

    public void removeProfile(Profile profile){
        profileList.remove(profile);
    }

    public ArrayList<Profile> getList(){
        return profileList;
    }

    public void setList(ArrayList<Profile> profileList){
        this.profileList = profileList;
    }

    public void clearList(){
        this.profileList.clear();
    }
}
