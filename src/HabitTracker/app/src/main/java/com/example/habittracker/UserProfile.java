package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;


public class UserProfile extends Profile implements Parcelable {
    private FollowerList following;
    private FollowerList followers;
    public HabitList habitList;

    public UserProfile(String username) {
        this.username = username;
        this.following = new FollowerList();
        this.followers = new FollowerList();
    }


    protected UserProfile(Parcel in) {
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };



    public void followUser(UserProfile profile) {
        following.addProfile(profile);
    }

    public void unfollowUser(UserProfile profile) {
        following.removeProfile(profile);
    }

    public void addFollower(UserProfile profile) {
        System.out.println("this is the user profile: "+ profile);
        followers.addProfile(profile);
    }

    public void removeFollower(UserProfile profile) {
        followers.removeProfile(profile);
    }

    public ArrayList<Profile> getFollowing(){return following.getList();}

    public ArrayList<Profile> getFollowers(){ return followers.getList();  }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}

