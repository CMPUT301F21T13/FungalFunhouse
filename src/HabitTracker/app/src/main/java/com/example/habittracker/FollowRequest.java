package com.example.habittracker;


/**
 * This is a class of FollowRequests to be used as an request [object]
 * It allows for the creation, and editing of a specific request
 */
public class FollowRequest {
    private UserProfile sender;
    private UserProfile target;

    /**
     * The constructor for FollowRequest
     * @param sender UserProfile: the user the request is sent from
     * @param target UserProfile: the user the request is sent to
     */
    public FollowRequest(UserProfile sender, UserProfile target){
        this.sender = sender;
        this.target = target;
    }

    /**
     * Returns the sender of this request
     * @return UserProfile: returns
     */
    public UserProfile getSender() {
        return sender;
    }

    /**
     * Sets the Sender of this request
     * @param sender UserProfile: the target to be sent from
     */
    public void setSender(UserProfile sender) {
        this.sender = sender;
    }

    /**
     * Returns the target of this request
     * @return UserProfile: the target of this request
     */
    public UserProfile getTarget() {
        return target;
    }

    /**
     * Sets the target of this Request
     * @param target UserProfile: the target to be sent to
     */
    public void setTarget(UserProfile target) {
        this.target = target;
    }
}
