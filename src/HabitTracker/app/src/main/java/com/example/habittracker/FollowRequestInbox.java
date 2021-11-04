package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class controlling all requests sent to a particular user
 * It allows the use of adding, removing, accepting and rejecting requests
 * As well as setting or getting the owner of a particular inbox
 */
public class FollowRequestInbox {

    private ArrayList<FollowRequest> requests;
    private UserProfile owner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
     * @param request FollowRequest: the added request
     */
    public void addRequest(FollowRequest request) {
        requests.add(request);
        HashMap<String, String> data = new HashMap<>();
        data.put("sender", request.getSender());
        data.put("target", request.getTarget());
        db.collection("users")
                .document(owner.getUsername()).collection("followrequestinbox")
                .document(request.getSender())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Follow Request Inbox", "Follow Request successfully sent");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Follow Request Inbox", "Follow request failed to send" + e.toString());
                    }
                });
    }

    /**
     * Removes a request from this inbox
     * @param request FollowRequest: the removed request
     */
    public void removeRequest(FollowRequest request) {
        requests.remove(request);
    }

    /**
     * Accepts a request from this inbox
     * @param request FollowRequest: the accepted request (leads to addRequest)
     */
    public void acceptRequest(FollowRequest request) {
        //TODO: add sender username to followers, query sender, add following target to
        /*
        UserProfile sender = db.collection("users").getId(request.getSender());
        owner.addFollower(request.getSender());
        request.getSender().followUser(owner);
        removeRequest(request);

         */

    }

    /**
     * Denies a request from this inbox
     * @param request FollowRequest: the denied request (leads to RemoveRequest)
     */
    public void denyRequest(FollowRequest request) {
        removeRequest(request);
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

    public ArrayList<FollowRequest> getRequests(){return this.requests;}
}