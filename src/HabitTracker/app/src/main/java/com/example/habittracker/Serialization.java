package com.example.habittracker;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Serialization {

	FirebaseFirestore db = FirebaseFirestore.getInstance();

	/* Class will contain the methods for 
	 * I/O with the firestore
	 * Mainly Profile construction and deconstruction
	 */

	public static void serializeUserProfile(UserProfile user){
		ArrayList<Profile> followers = user.getFollowers();
		ArrayList<Profile> following = user.getFollowing();
		ArrayList<FollowRequest> requestInbox = user.getRequests();
		ArrayList<Habit> habits = user.getHabits();



	}
}
