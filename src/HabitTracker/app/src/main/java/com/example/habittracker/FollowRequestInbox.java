package com.example.habittracker;

import java.util.ArrayList;

/**
 * This is a class controlling all requests sent to a particular user It allows
 * the use of adding, removing, accepting and rejecting requests As well as
 * setting or getting the owner of a particular inbox
 */
public class FollowRequestInbox {

    private ArrayList<FollowRequest> requests;
    private UserProfile owner;

    /**
     * The constructor method for FollowRequestInbox
     * 
     * @param owner UserProfile: the owner of the inbox
     */
    public FollowRequestInbox(UserProfile owner) {
        requests = new ArrayList<>();
        this.owner = owner;
    }

    /**
     * Adds a request to this inbox
     * 
     * @param request FollowRequest: the added request
     */
    public void addRequest(FollowRequest request) {
        requests.add(request);
    }

    /**
     * Removes a request from this inbox
     * 
     * @param request FollowRequest: the removed request
     */
    public void removeRequest(FollowRequest request) {
        requests.remove(request);
    }

    /**
     * Accepts a request from this inbox
     * 
     * @param request FollowRequest: the accepted request (leads to addRequest)
     */
    public void acceptRequest(FollowRequest request) {
        FollowedProfile f = new FollowedProfile();
        f.setUsername(request.getSender());
        owner.addFollower(f);


        //TODO Implement Database updates

        //request.getSender().followUser(owner);
        removeRequest(request);
    }

    /**
     * Denies a request from this inbox
     * 
     * @param request FollowRequest: the denied request (leads to RemoveRequest)
     */
    public void denyRequest(FollowRequest request) {
        removeRequest(request);
    }

    /**
     * Returns the owner of this inbox
     * 
     * @return UserProfile: owner of the inbox
     */
    public UserProfile getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this inbox
     * 
     * @param profile UserProfile: new to be set owner of the inbox
     */
    public void setOwner(UserProfile profile) {
        this.owner = profile;
    }

    public ArrayList<FollowRequest> getRequests() {
        return requests;
    }
}

