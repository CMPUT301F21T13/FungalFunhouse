package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserProfile extends Profile implements Parcelable {
    private ArrayList<String> followers;
    private ArrayList<String>following;
    private ArrayList<Habit> habitList;
    private FollowRequestInbox inbox;

    public UserProfile(String username) {
        this.username = username;
        this.followers = new ArrayList<String>();
        this.following = new ArrayList<String>();
        this.habitList = new ArrayList<Habit>();
        this.inbox = new FollowRequestInbox(this);
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

    public void followUser(String profile) {
        following.add(profile);
    }

    public void unfollowUser(String profile) {
        following.remove(profile);
    }

    public void addFollower(String profile) {
        followers.add(profile);
    }

    public void removeFollower(String profile) {
        followers.remove(profile);
    }

    public ArrayList<String> getFollowing() {
        return this.following;
    }

    public ArrayList<String> getFollowers() {
        return this.followers;
    }

    public FollowRequestInbox getInbox() {
        return this.inbox;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    // Habit list functions
    public Habit getHabit(int position) {
        return habitList.get(position);
    }

    public void addHabit(Habit habit) {
        habitList.add(habit);
    }

    public void removeHabit(Habit habit) {
        habitList.remove(habit);
    }

    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }

    public void clearList() {
        habitList.clear();
    }

    public ArrayList<Habit> getHabitList() {
        return habitList;
    }

}
