package com.example.habittracker;

public class UserProfile extends Profile {
    private FollowerList following;
    private FollowerList followers;
    public HabitList habitList;

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

}
