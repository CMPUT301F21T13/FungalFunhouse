package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile extends Profile implements Parcelable {
    private FollowerList following;
    private FollowerList followers;
    public HabitList habitList;

    public UserProfile(String username) {
        this.username = username;
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
        followers.addProfile(profile);
    }

    public void removerFollower(UserProfile profile) {
        followers.removeProfile(profile);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}

