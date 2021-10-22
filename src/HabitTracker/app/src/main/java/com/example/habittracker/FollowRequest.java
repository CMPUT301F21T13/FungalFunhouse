package com.example.habittracker;

public class FollowRequest {
    private UserProfile sender;
    private UserProfile target;

    public UserProfile getSender() {
        return sender;
    }

    public void setSender(UserProfile sender) {
        this.sender = sender;
    }

    public UserProfile getTarget() {
        return target;
    }

    public void setTarget(UserProfile target) {
        this.target = target;
    }
}
