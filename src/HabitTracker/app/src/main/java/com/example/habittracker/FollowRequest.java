package com.example.habittracker;


/**
 * This is a class of FollowRequests to be used as an request [object]
 * It allows for the creation, and editing of a specific request
 */
public class FollowRequest {
    private String sender;
    private String target;

    /**
     * The constructor for FollowRequest
     * @param sender UserProfile: the user the request is sent from
     * @param target UserProfile: the user the request is sent to
     */
    public FollowRequest(String sender, String target){
        this.sender = sender;
        this.target = target;
    }

    /**
     * Returns the sender of this request
     * @return UserProfile: returns
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the Sender of this request
     * @param sender UserProfile: the target to be sent from
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Returns the target of this request
     * @return UserProfile: the target of this request
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the target of this Request
     * @param target UserProfile: the target to be sent to
     */
    public void setTarget(String target) {
        this.target = target;
    }
}
