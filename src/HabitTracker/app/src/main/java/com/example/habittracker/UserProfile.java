package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is for UserProfiles
 * It handles instances of habits, followers, followinbox and usernames
 * For this user
 */
public class UserProfile extends Profile implements Parcelable {
    private ArrayList<String> followers;
    private ArrayList<String>following;
    private ArrayList<Habit> habitList;
    private FollowRequestInbox inbox;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserProfile(String username) {
        this.username = username;
        this.followers = new ArrayList<String>();
        this.following = new ArrayList<String>();
        this.habitList = new ArrayList<Habit>();
        this.inbox = new FollowRequestInbox(this);
    }

    protected UserProfile(Parcel in) {
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    /**
     * This method takes in a username and sets the current User
     * to follow the profile user
     * @param profile String: username of the user to be followed
     */
    public void followUser(String profile) {
        following.add(profile);
        HashMap<String, String> data = new HashMap<>();
        data.put("username", profile);
        db.collection("users").document(username).collection("following")
                .document(profile)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Followings", "Following sucessfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Following", "Following request failed" + e.toString());
                    }
                });
    }

    /**
     * Takes in a profile to unfollow
     * @param profile String: the user to unfollow
     */
    public void unfollowUser(String profile) {
        following.remove(profile);
    }

    /**
     * This method takes in a username and sets the current user
     * To have profile User as a follower
     * @param profile String: username of the new follower to be added
     */
    public void addFollower(String profile) {
        followers.add(profile);
        HashMap<String, String> data = new HashMap<>();
        data.put("username", profile);
        db.collection("users").document(username).collection("followers")
                .document(profile)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Followers", "Follower sucessfully added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Following", "Follower request failed" + e.toString());
                    }
                });

    }

    /**
     * Takes in a profile to remove
     * @param profile String: the follower to be removed
     */
    public void removeFollower(String profile) {
        followers.remove(profile);
    }

    /**
     * Returns a list of usernames this user is following
     * @return ArrayList<String>: The list of following usernames
     */
    public ArrayList<String> getFollowing() {
        return this.following;
    }

    /**
     * Returns a list of usernames this user is being followed by
     * @return ArrayList<String>: The list of followers usernames
     */
    public ArrayList<String> getFollowers() {
        return this.followers;
    }

    /**
     * Returns an instance of this inbox
     * @return FollowRequestInbox: The current inbox
     */
    public FollowRequestInbox getInbox() {
        return this.inbox;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    // Habit list functions
    public Habit getHabit(int position) {
        return habitList.get(position);
    }

    public void addHabit(Habit habit) {
        habitList.add(habit);
    }

    public void removeHabit(Habit habit) {
        habitList.remove(habit);
    }

    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }

    public void clearList() {
        habitList.clear();
    }

    public ArrayList<Habit> getHabitList() {
        return habitList;
    }

}
