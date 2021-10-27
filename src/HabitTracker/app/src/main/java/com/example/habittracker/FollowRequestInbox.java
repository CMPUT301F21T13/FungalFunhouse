package com.example.habittracker;

import java.util.ArrayList;

public class FollowRequestInbox {

    private ArrayList<FollowRequest> requests;
    private UserProfile owner;

    public void addRequest(FollowRequest request) {
        requests.add(request);
    }

    public void removeRequest(FollowRequest request) {
        requests.remove(request);
    }

    public void acceptRequest(FollowRequest request) {
        addRequest(request);
    }

    public void denyRequest(FollowRequest request) {
        removeRequest(request);
    }

    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile profile) {
        this.owner = profile;
    }
}
