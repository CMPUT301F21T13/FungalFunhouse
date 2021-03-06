package com.example.habittracker;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

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
     * Adds a request to this inbox (including within the database)
     * @param request FollowRequest: the request to be added
     */
    public void addRequest(FollowRequest request) {
        requests.add(request);
    }

    /**
     * Removes a request from this inbox (including from the database)
     * @param request FollowRequest: the request to be removed
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
        // add follower for owner of request
        // add following for sender of the request
        owner.addFollower(request.getSender());
        UserProfile sender = new UserProfile(request.getSender());
        sender.followUser(owner.getUsername());
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

    /**
     * This methods returns the list of all FollowRequests
     * @return ArrayList<FollowRequest>: List of all requests within this inbox
     */
    public ArrayList<FollowRequest> getRequests(){return this.requests;}
}

