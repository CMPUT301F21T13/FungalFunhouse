package com.example.habittracker;

import java.util.ArrayList;

public class Social {
    private FollowerList followers;
    private FollowerList following;
    private FollowRequestInbox inbox;


    public Social(UserProfile owner){
        this.following = new FollowerList();
        this.followers = new FollowerList();
        this.inbox = new FollowRequestInbox(owner);
    }

    public FollowRequestInbox getRequestInbox() {
        return this.inbox;
    }

    public void followUser(UserProfile profile) {
        following.addProfile(profile);
    }

    public void unfollowUser(UserProfile profile) {
        following.removeProfile(profile);
    }

    public void addFollower(UserProfile profile) {
        followers.addProfile(profile);
    }

    public void removeFollower(UserProfile profile) {
        followers.removeProfile(profile);
    }

    public ArrayList<Profile> getFollowing(){return following.getList();}

    public ArrayList<Profile> getFollowers(){ return followers.getList();  }
}
