package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Serialization {

	static FirebaseFirestore db = FirebaseFirestore.getInstance();
	static final String TAG = "Sanple";

	/* Class will contain the methods for 
	 * I/O with the firestore
	 * Mainly Profile construction and deconstruction
	 */

	public static <OnFailureListener> void serializeUserProfile(UserProfile user){
		ArrayList<Profile> followers = user.getFollowers();
		ArrayList<Profile> following = user.getFollowing();
		ArrayList<FollowRequest> requestInbox = user.getRequests();
		ArrayList<Habit> habits = user.getHabits();

		String username = user.getUsername();
		ArrayList<String> followerNames = getProfileNames(followers);
		ArrayList<String> followingNames = getProfileNames(following);
		ArrayList<String> followRequestSenders = getProfileNames(getRequestSenders(requestInbox));

		Map<String, Object> profile = new HashMap<>();
		profile.put("username", user.getUsername());
		profile.put("followers", followerNames);
		profile.put("following", followingNames);
		profile.put("follow-requests", followRequestSenders);

		db.collection("UserProfiles").document(username)
				.set(profile)
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						Log.d(TAG, "Profile successfully written!");
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.w(TAG, "Profile writing document", e);
					}
				});

	}

	private static ArrayList<Profile> getRequestSenders(ArrayList<FollowRequest> requests){
		ArrayList<Profile> senders = new ArrayList<>();
		for (FollowRequest request : requests) {
			senders.add(request.getTarget());
		}
		return senders;
	}
 	private static ArrayList<String> getProfileNames(ArrayList<Profile> users) {
		ArrayList<String> names = new ArrayList<>();

		for (Profile user : users) {
			names.add(user.getUsername());
		}

		return names;
	}
}
