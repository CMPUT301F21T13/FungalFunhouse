package com.example.habittracker;

import java.util.ArrayList;

/**
 * This is a class controlling all requests sent to a particular user
 * It allows the use of adding, removing, accepting and rejecting requests
 * As well as setting or getting the owner of a particular inbox
 */
public class FollowRequestInbox {

    private ArrayList<Profile> requests;
    private UserProfile owner;

    /**
     * The constructor method for FollowRequestInbox
     * @param owner UserProfile: the owner of the inbox
     */
    public FollowRequestInbox(UserProfile owner){
        requests = new ArrayList<>();
        this.owner = owner;
    }

    /**
     * Adds a request to this inbox
     * @param sender UserProfile: sender of the request
     */
    public void addRequest(UserProfile sender) {
        requests.add(sender);
    }

    /**
     * Removes a request from this inbox
     * @param sender UserProfile: sender of the request
     */
    public void removeRequest(UserProfile sender) { requests.remove(sender);
    }

    /**
     * Accepts a request from this inbox
     * @param sender UserProfile: sender of the request
     */
    public void acceptRequest(UserProfile sender) {
        owner.getSocials().addFollower(sender);
        sender.getSocials().followUser(owner);
        removeRequest(sender);
    }

    /**
     * Denies a request from this inbox
     * @param sender UserProfile: sender of the request
     */
    public void denyRequest(UserProfile sender) {
        removeRequest(sender);
    }

    /**
     * Returns the owner of this inbox
     * @return UserProfile: owner of the inbox
     */
    public UserProfile getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this inbox
     * @param profile UserProfile: new to be set owner of the inbox
     */
    public void setOwner(UserProfile profile) {
        this.owner = profile;
    }

    public ArrayList<Profile> getRequests(){ return this.requests; }
}
