package com.example.habittracker;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Serialization {

	static FirebaseFirestore db = FirebaseFirestore.getInstance();

	/* Class will contain the methods for 
	 * I/O with the firestore
	 * Mainly Profile construction and deconstruction
	 */

	public static void serializeUserProfile(UserProfile user){
		ArrayList<Profile> followers = user.getFollowers();
		ArrayList<Profile> following = user.getFollowing();
		ArrayList<FollowRequest> requestInbox = user.getRequests();
		ArrayList<Habit> habits = user.getHabits();

		String username = user.getUsername();
		ArrayList<String> followerNames = getProfileNames(followers);
		ArrayList<String> followingNames = getProfileNames(following);
		ArrayList<String> followRequestSenders = getProfileNames(getRequestSenders(requestInbox));


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
