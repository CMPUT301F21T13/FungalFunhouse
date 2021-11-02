package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;


public class UserProfile extends Profile implements Parcelable {
    private Social socials;
    public HabitList habitList;

    public UserProfile(String username) {
        this.username = username;
        this.socials = new Social(this);
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
    public Social getSocials(){return this.socials;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}

